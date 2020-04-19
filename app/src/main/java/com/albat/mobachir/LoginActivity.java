package com.albat.mobachir;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.albat.mobachir.network.interfaces.UserLoggedIn;
import com.albat.mobachir.network.managers.UserManager;
import com.albat.mobachir.network.models.User;
import com.albat.mobachir.util.CLog;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.MyToasty;
import com.albat.mobachir.util.SharedPreferencesManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class LoginActivity extends BaseActivity implements UserLoggedIn {
    String TAG = "LoginActivity";
    private EditText emailInput, passwordInput;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private final int RC_SIGN_IN = 1001;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton googleSignInButton;

    private TwitterLoginButton twitterLoginButton;
    DialogHelper dialogHelper;
    SharedPreferencesManager sharedPreferencesManager;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        dialogHelper = DialogHelper.getInstance();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        app = App.getInstance();

        if (sharedPreferencesManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }


        generateHashkey();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//        }

        requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar

        setContentView(R.layout.activity_login);

        initializeViews();
    }

    private void initializeViews() {
        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (email.isEmpty()) {
                    emailInput.setError("مطلوب");
                    emailInput.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordInput.setError("مطلوب");
                    passwordInput.requestFocus();
                    return;
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                login(Config.LoginType.NORMAL.getValue(), email, password);
            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        initializeFacebook();
        initializeGoogleLogin();
        initializeTwitter();
    }

    private void initializeFacebook() {
        callbackManager = CallbackManager.Factory.create();

        facebookLoginButton = (LoginButton) findViewById(R.id.login_facebookButton);

        findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                facebookLoginButton.performClick();
            }
        });


        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile, email"));
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        CLog.e("Facebook", "Success");


                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        CLog.e(TAG, "Facebook: " + object.toString());
                                        String userId = "", email = "", name = "", picture = null;
                                        try {
                                            userId = object.getString("id");
                                            name = object.getString("name");
                                            email = object.getString("email");
                                            picture = null;
                                        } catch (JSONException e) {
                                            CLog.e(TAG, e.getMessage(), e);
                                        }
                                        socialLogin(Config.LoginType.FACEBOOK.getValue(), userId, name, email);
                                    }
                                }

                        );
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,email,name");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        CLog.e("Facebook", "Canceled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        CLog.e("Facebook", error.getMessage(), error);
                    }
                }

        );
    }

    private void initializeGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                MyToasty.error(LoginActivity.this, "Connection Error.", Toast.LENGTH_LONG);
            }
        } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).addApi(Plus.API)
                .build();

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }

        findViewById(R.id.googlePlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGooglePlusSignIn();
            }
        });
        googleSignInButton = (SignInButton) findViewById(R.id.signin_google);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGooglePlusSignIn();
            }
        });
    }

    private void startGooglePlusSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void initializeTwitter() {

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.loginTwitterButton);

        findViewById(R.id.twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (twitterLoginButton != null)
                    twitterLoginButton.performClick();
            }
        });

        twitterLoginButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                CLog.e("Twitter", "Success");

                // TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterSession session = result.data;

                CLog.e("Twitter", session.getUserName());
                CLog.e("Twitter", session.getUserId() + "");

                socialLogin(Config.LoginType.TWITTER.getValue(), session.getUserId() + "", session.getUserName(), "");
            }

            @Override
            public void failure(TwitterException e) {
                CLog.e("Twitter", e.getMessage(), e);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Required For Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Required For Twitter
        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);

        // Required For GooglePlus
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGooglePlusSignInResult(result);
        }
    }

    private void handleGooglePlusSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            socialLogin(Config.LoginType.GOOGLE_PLUS.getValue(), account.getId(), account.getDisplayName(), account.getEmail());

            CLog.e(TAG, account.getDisplayName() + " " + account.getEmail());
        } else {
            CLog.e(TAG, result.getStatus().toString());
        }
    }


    private void login(int loginType, String loginId, String password) {
        dialogHelper.showLoadingDialog(this, "تسجيل الدخول");
        UserManager userManager = new UserManager();
        userManager.login(loginType, loginId, password, this);
    }

    private void socialLogin(int loginType, String loginId, String name, String email) {
        dialogHelper.showLoadingDialog(this, "تسجيل الدخول");
        UserManager userManager = new UserManager();
        userManager.socialLogin(loginType, loginId, name, email, this);
    }

    @Override
    public void onUserLoggedIn(User user, boolean success, String error) {
        if (!success || user == null) {
            dialogHelper.hideLoadingDialogError(this, "فشل في تسجيل الدخول", error);
            return;
        }

        dialogHelper.hideLoadingDialog();

        app.setUser(user);

        finishAffinity();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    // generate the hash key
    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),

                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                CLog.e("Hash key", Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            CLog.e(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            CLog.e(TAG, e.getMessage(), e);
        }
    }
}

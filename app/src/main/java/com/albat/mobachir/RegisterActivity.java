package com.albat.mobachir;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.albat.mobachir.network.interfaces.UserLoggedIn;
import com.albat.mobachir.network.managers.UserManager;
import com.albat.mobachir.network.models.User;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.EmailValidator;
import com.albat.mobachir.util.SharedPreferencesManager;

public class RegisterActivity extends BaseActivity implements UserLoggedIn {
    String TAG = "RegisterActivity";
    EditText nameInput, passwordInput, emailInput, confirmPasswordInput;


    DialogHelper dialogHelper;
    SharedPreferencesManager sharedPreferencesManager;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogHelper = DialogHelper.getInstance();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        app = App.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        setContentView(R.layout.activity_register);

        initializeViews();
    }

    private void initializeViews() {
        nameInput = (EditText) findViewById(R.id.name);
        passwordInput = (EditText) findViewById(R.id.password);
        confirmPasswordInput = (EditText) findViewById(R.id.confirmPassword);
        emailInput = (EditText) findViewById(R.id.email);


        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString().trim();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();
                String email = emailInput.getText().toString().trim();


                if (name.isEmpty()) {
                    nameInput.setError("مطلوب");
                    nameInput.requestFocus();
                    return;
                }


                if (!new EmailValidator().validate(email)) {
                    emailInput.setError("ادخل ايميل صحيح");
                    emailInput.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordInput.setError("مطلوب");
                    passwordInput.requestFocus();
                    return;
                }

                if (password.length() < 5) {
                    passwordInput.setError("علي الأقل 5 حروف");
                    passwordInput.requestFocus();
                    return;
                }

                if (!confirmPassword.equals(password)) {
                    confirmPasswordInput.setError("كلمة المرور غير متطابقة");
                    confirmPasswordInput.requestFocus();
                    return;
                }

                hideKeyboard();

                register(name, email, password);
            }
        });
    }


    private void register(String name, String email, String password) {
        dialogHelper.showLoadingDialog(this, "تسجيل الدخول");
        UserManager userManager = new UserManager();
        userManager.register(name, email, password, this);
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
        startActivity(new Intent(this, MainActivity.class));
    }


    void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        /*
         * If no view is focused, an NPE will be thrown
         *
         * Maxim Dmitriev
         */
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}

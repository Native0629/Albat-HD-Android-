package com.albat.mobachir.providers.news.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.Config;
import com.albat.mobachir.FullScreenActivity;
import com.albat.mobachir.MainActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.VideoPlayerActivity;
import com.albat.mobachir.network.interfaces.CommentAdded;
import com.albat.mobachir.network.interfaces.NewLikeToggled;
import com.albat.mobachir.network.managers.NewsManager;
import com.albat.mobachir.network.models.Comment;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.network.models.News;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.SharedPreferencesManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

public class ViewNewsFragment extends BaseFragment implements CommentAdded, NewLikeToggled {
    String TAG = "ViewNewsFragment";
    //Views
    TextView title;
    WebView webView;
    TextView commentsCount;
    TextView commentsCount2;
    TextView source;
    TextView charCount;
    TextView time;
    TextView likesCount;
    EditText commentText;
    ImageView addComment;
    ImageView picture;
    ImageView video;
    ImageView like;
    FrameLayout videoLayout;

    private News item;

    DialogHelper dialogHelper;
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("item", item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_news, null);
        setHasOptionsMenu(true);

        dialogHelper = new DialogHelper();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());

        title = view.findViewById(R.id.title);
        commentsCount = view.findViewById(R.id.commentsCount);
        charCount = view.findViewById(R.id.charCount);
        commentText = view.findViewById(R.id.commentText);
        addComment = view.findViewById(R.id.addComment);
        picture = view.findViewById(R.id.picture);
        video = view.findViewById(R.id.video);
        videoLayout = view.findViewById(R.id.videoLayout);
        commentsCount2 = view.findViewById(R.id.commentsCount2);
        source = view.findViewById(R.id.source);
        time = view.findViewById(R.id.time);
        likesCount = view.findViewById(R.id.likesCount);
        like = view.findViewById(R.id.like);

        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //True if the host application wants to leave the current WebView and handle the url itself, otherwise return false.
                return true;
            }
        });
//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        if (savedInstanceState != null)
            item = (News) savedInstanceState.getSerializable("item");
        if (item == null)
            item = new News();

        title.setText(item.title);
        commentsCount.setText("(" + item.commentsCount + ")");
        commentsCount2.setText("(" + item.commentsCount + ")");
        source.setText(item.source);
        likesCount.setText(item.likes + "");
        if (item.likedByMe) {
            like.setImageResource(R.drawable.ic_liked);
        } else {
            like.setImageResource(R.drawable.ic_unliked);
        }
        if (sharedPreferencesManager.isLoggedIn()) {
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleLike();
                }
            });
        }


        String timeText = CalendarHelper.reformatDateString(item.pubDate, Config.API_DATETIME_FORMAT, Config.APP_TIME_FORMAT);
        Date date = CalendarHelper.stringToDate(item.pubDate, Config.API_DATETIME_FORMAT);
        if (CalendarHelper.isToday(date))
            timeText = "اليوم" + " " + timeText;
        else
            timeText = CalendarHelper.formatDate(date, "EEEE") + " " + timeText;

        time.setText(timeText);

        if (item.picture == null || item.picture.isEmpty())
            picture.setVisibility(View.GONE);
        else {
            try {
                Picasso.with(getActivity()).load(item.picture).error(R.drawable.news_placeholder).placeholder(R.drawable.news_placeholder).into(picture);
            } catch (Exception e) {
                picture.setImageResource(R.drawable.news_placeholder);
                picture.setVisibility(View.GONE);
            }
            if (item.video != null) {
                try {
                    Picasso.with(getActivity()).load(item.picture).error(R.drawable.news_placeholder).placeholder(R.drawable.news_placeholder).into(video);
                } catch (Exception e) {
                    video.setImageResource(R.drawable.news_placeholder);
                }
            }
        }

        //webView.loadDataWithBaseURL(null, item.content, "text/html", "utf-8", null);
        webView.loadDataWithBaseURL(null, "<html dir=\"rtl\" lang=\"\"><body>" + item.content + "</body></html>", "text/html", "UTF-8", null);


        view.findViewById(R.id.viewComments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComments();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));
            }
        });

        commentText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Config.MAX_COMMENT_CHAR)});
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int remainingChars = Config.MAX_COMMENT_CHAR - charSequence.length();
                charCount.setText(remainingChars + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });

        if (item.video != null) {
            videoLayout.setVisibility(View.VISIBLE);
            video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.video.endsWith("m3u8")) {
                        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                        intent.putExtra(VideoPlayerActivity.LINK, item.video);
                        startActivity(intent);
                        //GiraffePlayer.play(getContext(), new VideoInfo(Uri.parse(link.getLink())));
                    } else {
                        Intent intent = new Intent(getActivity(), FullScreenActivity.class);
                        intent.putExtra(FullScreenActivity.LINK, item.video);
                        startActivity(intent);
                    }
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));
                }
            });
        } else
            videoLayout.setVisibility(View.GONE);
        return view;
    }

    private void toggleLike() {
        NewsManager newsManager = new NewsManager();
        newsManager.toggleLike(item.id, this);
    }

    @Override
    public void onNewLikeToggled(boolean likedByMe, int likes, boolean success, String error) {
        if (!success)
            return;

        item.likedByMe = likedByMe;
        item.likes = likes;

        likesCount.setText(item.likes + "");
        if (item.likedByMe) {
            like.setImageResource(R.drawable.ic_liked);
        } else {
            like.setImageResource(R.drawable.ic_unliked);
        }
    }

    private void addComment() {
        String comment = commentText.getText().toString().trim();
        if (comment.isEmpty()) {
            commentText.setError("مطلوب");
            commentText.requestFocus();
        }

        dialogHelper.showLoadingDialog(getActivity(), "جاري الإصافة");
        NewsManager newsManager = new NewsManager();
        newsManager.addComment(item.id, comment, this);
    }

    @Override
    public void onCommentAdded(Comment comment, boolean success, String error) {
        if (!success || comment == null) {
            dialogHelper.hideLoadingDialogError(getActivity(), "فشل في اضافة تعليق", error);
            return;
        }
        dialogHelper.hideLoadingDialog();
        showComments();

        commentText.setText("");
        item.commentsCount++;
        commentsCount.setText("(" + item.commentsCount + ")");
        commentsCount2.setText("(" + item.commentsCount + ")");
    }

    private void showComments() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showInterstitial();
        }

        Fragment fragment = new CommentsFragment().setNews(item);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean popBackStack() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showInterstitial();
        }

        return super.popBackStack();
    }

    public ViewNewsFragment setItem(News item) {
        this.item = item;
        return this;
    }
}

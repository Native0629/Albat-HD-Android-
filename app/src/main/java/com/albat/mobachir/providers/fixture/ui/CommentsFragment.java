package com.albat.mobachir.providers.fixture.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.CommentAdded;
import com.albat.mobachir.network.interfaces.CommentsRetrieved;
import com.albat.mobachir.network.managers.FixturesManager;
import com.albat.mobachir.network.models.Comment;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.news.CommentsAdapter;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.albat.mobachir.util.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class CommentsFragment extends BaseFragment implements CommentAdded, CommentsRetrieved {
    String TAG = "CommentsFragment";
    //Views
    private RecyclerView recyclerView;
    private Fixture fixture;
    private ArrayList<Comment> items = new ArrayList<>();

    //List
    private CommentsAdapter adapter;
    private TextView noComments;

    boolean isInitialized = false;

    TextView charCount;
    EditText commentText;
    ImageView addComment;

    DialogHelper dialogHelper;
    SharedPreferencesManager sharedPreferencesManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_fixture_comments, null);
        setHasOptionsMenu(true);

        dialogHelper = new DialogHelper();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());

        recyclerView = view.findViewById(R.id.list);
        noComments = view.findViewById(R.id.noComments);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        items = new ArrayList<>();
        adapter = new CommentsAdapter(items, getContext());
        recyclerView.setAdapter(adapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

//        refreshFab = view.findViewById(R.id.refresh);
//        refreshFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (getActivity() instanceof MainActivity) {
//                    ((MainActivity) getActivity()).showInterstitial();
//                }
//                noComments.setVisibility(View.GONE);
//                refreshFab.setVisibility(View.GONE);
//                adapter.clear();
//                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
//                loadItems();
//            }
//        });

        charCount = view.findViewById(R.id.charCount);
        commentText = view.findViewById(R.id.commentText);
        addComment = view.findViewById(R.id.addComment);

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

        isInitialized = true;
        if (fixture != null)
            loadItems();

        return view;
    }

    public void loadItems() {
        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
        FixturesManager fixturesManager = new FixturesManager();
        fixturesManager.getFixturesComments(fixture.id, this);
    }

    @Override
    public void onCommentsRetrieved(ArrayList<Comment> comments, boolean success, String error) {
        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);

        if (!success || comments == null) {
            noComments.setVisibility(View.VISIBLE);
            return;
        } else {
            //Add all the new posts to the list and notify the adapter
            items.clear();
            items.addAll(comments);
            if (items.isEmpty()) {
                noComments.setVisibility(View.VISIBLE);
            } else {
                noComments.setVisibility(View.GONE);
                recyclerView.scrollToPosition(items.size() - 1);
            }
        }
    }

    private void addComment() {
        String comment = commentText.getText().toString().trim();
        if (comment.isEmpty()) {
            commentText.setError("مطلوب");
            commentText.requestFocus();
        }

        dialogHelper.showLoadingDialog(getActivity(), "جاري الإصافة");
        FixturesManager fixturesManager = new FixturesManager();
        fixturesManager.addComment(fixture.id, comment, this);
    }

    @Override
    public void onCommentAdded(Comment comment, boolean success, String error) {
        if (!success || comment == null) {
            dialogHelper.hideLoadingDialogError(getActivity(), "فشل في اضافة تعليق", error);
            return;
        }
        dialogHelper.hideLoadingDialog();

        commentText.setText("");
        items.add(comment);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(items.size() - 1);
        noComments.setVisibility(View.GONE);
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
        if (isInitialized)
            loadItems();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        switch (messageEvent.getCode()) {
            case MessageEvent.REFRESH:
                loadItems();
                break;
        }
    }
}

package cmpe.sjsu.socialawesome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.adapters.TimeLineAdapter;
import cmpe.sjsu.socialawesome.models.Post;
import cmpe.sjsu.socialawesome.models.User;

import static cmpe.sjsu.socialawesome.StartActivity.USERS_TABLE;

public class TimeLineFragment extends SocialFragment {
    private RecyclerView mTimelineListView;
    private FloatingActionButton mAddNewPostBtnView;
    private TimeLineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Post> postList;
    private DatabaseReference currentUserRef;
    private ProgressDialog progress;

    public static int CREATE_POST = 21;
    public static int RESULT_OK = 1;
    public static String POST_CONTENT_KEY = "postContentKey";
    public static String FIREBASE_POST_KEY = "posts";

    public TimeLineFragment() {
        mTitle = TimeLineFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_timeline, container, false);

        mTimelineListView = (RecyclerView) view.findViewById(R.id.timelineListView);
        mAddNewPostBtnView = (FloatingActionButton) view.findViewById(R.id.addNewPostBtn);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progress = new ProgressDialog(getContext());
        progress.show();
        DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
        currentUserRef = userTableRef.child(UserAuth.getInstance().getCurrentUser().id);
        mLayoutManager = new LinearLayoutManager(getContext());
        mTimelineListView.setLayoutManager(mLayoutManager);
        initPostListFromServer();

        mAddNewPostBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                startActivityForResult(intent, CREATE_POST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_POST && resultCode == RESULT_OK) {
            Post newPost = new Post(UserAuth.getInstance().getCurrentUser(), data.getStringExtra(POST_CONTENT_KEY), null);
            mAdapter.addNewPost(newPost);
            addPostToServer(newPost);
        }
    }

    private void addPostToServer(Post post) {
        String key = currentUserRef.child(FIREBASE_POST_KEY).push().getKey();
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + FIREBASE_POST_KEY + "/" + key, postValues);
        currentUserRef.updateChildren(childUpdates);
    }

    private void initPostListFromServer() {
        currentUserRef.child(FIREBASE_POST_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    HashMap postMap = (HashMap)postSnapshot.getValue();
                    Post post = new Post(postSnapshot.child("user").getValue(User.class),
                            (String)postMap.get("contentPost"), (String)postMap.get("contentPostURL"));
                    postList.add(post);
                }
                Collections.sort(postList);
                mAdapter = new TimeLineAdapter(postList);
                mTimelineListView.setAdapter(mAdapter);
                progress.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

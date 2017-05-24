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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
    private DatabaseReference userTableRef;
    private ProgressDialog progress;

    public static int CREATE_POST = 21;
    public static int RESULT_OK = 1;
    public static String POST_CONTENT_KEY = "postContentKey";
    public static String POST_CONTENT_URL_KEY = "postContentURLKey";
    public static String FIREBASE_POST_KEY = "posts";
    public static String FIREBASE_FRIENDS_KEY = "friends";

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
        progress.setCancelable(false);
        progress.show();
        userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
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
            String picURL = data.getStringExtra(POST_CONTENT_URL_KEY);
            Post newPost = new Post(UserAuth.getInstance().getCurrentUser(),
                    Calendar.getInstance().getTime().getTime(),
                    data.getStringExtra(POST_CONTENT_KEY), picURL);
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
        userTableRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();

                HashMap usersMap = (HashMap)dataSnapshot.getValue();
                HashMap currentUser = (HashMap)usersMap.get(UserAuth.getInstance().getCurrentUser().id);
                Iterator postIterator = ((HashMap)currentUser.get(FIREBASE_POST_KEY)).entrySet().iterator();
                while (postIterator.hasNext()) {
                    Map.Entry postEntry = (Map.Entry) postIterator.next();
                    HashMap postMap = (HashMap)postEntry.getValue();
                    Post post = new Post(UserAuth.getInstance().getCurrentUser(), (long)postMap.get("timestamp"),
                            (String)postMap.get("contentPost"), (String)postMap.get("contentPhotoURL"));
                    postList.add(post);
                }
                Iterator friendIterator = ((HashMap)currentUser.get(FIREBASE_FRIENDS_KEY)).entrySet().iterator();
                while (friendIterator.hasNext()) {
                    Map.Entry friendEntry = (Map.Entry) friendIterator.next();
                    HashMap friendMap = (HashMap)usersMap.get(friendEntry.getKey().toString());
                    User friendUser = new User();
                    friendUser.first_name = (String)friendMap.get("first_name");
                    friendUser.last_name = (String)friendMap.get("last_name");
                    friendUser.profilePhotoURL = (String)friendMap.get("profilePhotoURL");
                    postIterator = ((HashMap)friendMap.get(FIREBASE_POST_KEY)).entrySet().iterator();
                    while (postIterator.hasNext()) {
                        Map.Entry postEntry = (Map.Entry) postIterator.next();
                        HashMap postMap = (HashMap)postEntry.getValue();
                        Post post = new Post(friendUser, (long)postMap.get("timestamp"),
                                (String)postMap.get("contentPost"), (String)postMap.get("contentPhotoURL"));
                        postList.add(post);
                    }
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

        /*currentUserRef.child(FIREBASE_POST_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    HashMap postMap = (HashMap)postSnapshot.getValue();
                    User user = postSnapshot.child("user").getValue(User.class);
                    Post post = new Post(UserAuth.getInstance().getCurrentUser(), (long)postMap.get("timestamp"),
                            (String)postMap.get("contentPost"), (String)postMap.get("contentPhotoURL"));
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
        */
    }

    @Override
    public void onRefresh() {
        initPostListFromServer();
    }
}

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

import java.util.ArrayList;

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

    public static int CREATE_POST = 21;
    public static int RESULT_OK = 1;
    public static String POST_CONTENT_KEY = "postContentKey";
    public static String POST_KEY = "posts";

    private ProgressDialog pd;

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

        mLayoutManager = new LinearLayoutManager(getContext());
        mTimelineListView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        postList.add(new Post(UserAuth.getInstance().getCurrentUser(), "This is the very first post.", null));
        mAdapter = new TimeLineAdapter(postList);
        mTimelineListView.setAdapter(mAdapter);

        mAddNewPostBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                startActivityForResult(intent, CREATE_POST);
            }
        });

        populateInfo();
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
        final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
        final DatabaseReference currentUserRef = userTableRef.child(UserAuth.getInstance().getCurrentUser().id);

        //currentUserRef.setValue();
    }

    private void populateInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(StartActivity.USERS_TABLE).child(UserAuth.getInstance().getCurrentUser().id);
        pd = new ProgressDialog(getContext());
        pd.show();

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                final User user = mutableData.getValue(User.class);
                if (user != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //populateInfoIntoEditText(user);
                        }
                    });

                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                //Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                pd.hide();
            }
        });
    }
}

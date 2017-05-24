package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmpe.sjsu.socialawesome.Utils.FriendUtils;
import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserIDMap;
import cmpe.sjsu.socialawesome.models.UserSummary;

import static cmpe.sjsu.socialawesome.StartActivity.USERS_TABLE;
import static cmpe.sjsu.socialawesome.models.User.FRIEND_LIST;
import static cmpe.sjsu.socialawesome.models.User.PENDING_FRIEND_LIST;
import static cmpe.sjsu.socialawesome.models.User.WAITING_FRIEND_LIST;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendFragment extends SocialFragment {
    private static ArrayList<String> mFriendList = new ArrayList<>();
    private static ArrayList<String> mOutGoingFriendList = new ArrayList<>();
    private static ArrayList<String> mPendingFriendRequestList = new ArrayList<>();
    private RecyclerView recList;
    private FriendListAdapter mAdapter;

    public FriendFragment() {
        mTitle = FriendFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        mAdapter = new FriendListAdapter(mFriendList);
        recList.setAdapter(mAdapter);

        Button OutRequstBtn = (Button) view.findViewById(R.id.outgoing_friend_request_btn);
        OutRequstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFriendList = new ArrayList<>();
                getFriendList(WAITING_FRIEND_LIST);
//                mAdapter.updateList(mOutGoingFriendList);
            }
        });

        Button PendingFriendRequstBtn = (Button) view.findViewById(R.id.friend_request_btn);
        PendingFriendRequstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                acceptReqBtn.setVisibility(View.VISIBLE);
                mFriendList = new ArrayList<>();
                getFriendList(PENDING_FRIEND_LIST);
            }
        });

        Button FriendBtn = (Button) view.findViewById(R.id.friend_btn);
        FriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFriendList = new ArrayList<>();
                getFriendList(FRIEND_LIST);
            }
        });

        Button addFriendBtn = (Button) view.findViewById(R.id.add_friend_btn);
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                acceptReqBtn.setVisibility(View.VISIBLE);
//                acceptReqBtn.setText("Follow");
//                addSelFriendBtn.setVisibility(View.VISIBLE);
                mFriendList = new ArrayList<>();
                getPublicUser();
            }
        });


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        Button acceptReqBtn;
        Button addSelFriendBtn;
        MyViewHolder(View view){
            super(view);
            this.acceptReqBtn = (Button) view.findViewById(R.id.accept_request_button);
            this.addSelFriendBtn = (Button) view.findViewById(R.id.add_select_friend_button);

        }
    }

//    @Override
//    public void onBindViewHolder(MyViewHolder myViewHolder, int i){
//        myViewHolder.acceptReqBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                /// button click event
//            }
//        });
//        myViewHolder.addSelFriendBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                /// button click event
//            }
//        });
//    }

    @Override
    public void onStart() {
        super.onStart();
//        FriendUtils.addFriend(getActivity(), 0, "cBjf2Jmc4TbiZsPWyG4L0Fa2dKl2");
        getFriendList(FRIEND_LIST);
    }

    private void getFriendList(String node) {

        final DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE).child(UserAuth.getInstance().getCurrentUser().id).child(node);
        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFriendList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserIDMap friendIdMap = postSnapshot.getValue(UserIDMap.class);
                    mFriendList.add(friendIdMap.id);
                    mAdapter.notifyDataSetChanged();
                }
                mAdapter = new FriendListAdapter(mFriendList);
                recList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPublicUser() {

        final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
        Query query = userTableRef.orderByChild("status").equalTo("2");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFriendList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User publicUser = postSnapshot.getValue(User.class);
                    mFriendList.add(publicUser.id);
                    mAdapter.notifyDataSetChanged();
                }
                mAdapter = new FriendListAdapter(mFriendList);
                recList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

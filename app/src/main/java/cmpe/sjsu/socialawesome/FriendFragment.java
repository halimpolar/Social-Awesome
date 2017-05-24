package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserSummary;

import static cmpe.sjsu.socialawesome.StartActivity.USERS_TABLE;
import static cmpe.sjsu.socialawesome.models.User.FRIEND_LIST;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendFragment extends SocialFragment {
    private static ArrayList<UserSummary> mFriendList = new ArrayList<>();
    private RecyclerView recList;
    private FriendListAdapter mAdapter;

    public FriendFragment() {
        mTitle = FriendFragment.class.getSimpleName();
    }

    private static void addFriendList(UserSummary userSummary) {
        mFriendList.add(userSummary);
    }

    private static UserSummary getUserSummary(User user) {
        UserSummary userSummary = new UserSummary();
        userSummary.id = user.id;
        userSummary.email = user.email;
        userSummary.first_name = user.first_name;
        userSummary.last_name = user.last_name;
        userSummary.profilePhotoURL = user.profilePhotoURL;
        userSummary.status = user.status;
        return userSummary;
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
    }

    @Override
    public void onStart() {
        super.onStart();
        getFriendList(FRIEND_LIST);
    }

    private void getFriendList(String node) {
        final DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE).child(UserAuth.getInstance().getCurrentUser().id).child(node);
        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserSummary userSummary = postSnapshot.getValue(UserSummary.class);
//                UserSummary userSummary = getUserSummary(user);
                    mFriendList.add(userSummary);
                    mAdapter.notifyDataSetChanged();
//                    addFriendList(userSummary);

                }
                mAdapter = new FriendListAdapter(mFriendList);
                recList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        Query query = mSelfRef.orderByChild("last_name");
//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                User user = dataSnapshot.getValue(User.class);
//                UserSummary userSummary = getUserSummary(user);
//                addFriendList(userSummary);
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        mFriendList.add(UserAuth.getCurrentUserSummary());
    }

}

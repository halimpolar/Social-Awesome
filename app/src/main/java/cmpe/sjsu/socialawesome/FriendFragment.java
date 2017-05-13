package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private RecyclerView recList;
    private FriendListAdapter mAdapter;
    private static ArrayList<UserSummary> mFriendList = new ArrayList<>();

    public FriendFragment() {
        mTitle = FriendFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friend, container, false);
        recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        return inflater.inflate(R.layout.fragment_friend, container, false);
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
                User user = dataSnapshot.getValue(User.class);
                UserSummary userSummary = getUserSummary(user);
                addFriendList(userSummary);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        Query query = friendRef.orderByChild("last_name");
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
        mAdapter = new FriendListAdapter(mFriendList);
        recList.setAdapter(mAdapter);
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

}

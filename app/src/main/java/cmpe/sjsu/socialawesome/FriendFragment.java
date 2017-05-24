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

import cmpe.sjsu.socialawesome.Utils.FriendUtils;
import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserIDMap;
import cmpe.sjsu.socialawesome.models.UserSummary;

import static cmpe.sjsu.socialawesome.StartActivity.USERS_TABLE;
import static cmpe.sjsu.socialawesome.models.User.FRIEND_LIST;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendFragment extends SocialFragment {
    private static ArrayList<String> mFriendList = new ArrayList<>();
    private RecyclerView recList;
    private FriendListAdapter mAdapter;

    public FriendFragment() {
        mTitle = FriendFragment.class.getSimpleName();
    }

    private static void addFriendList(String id) {
        mFriendList.add(id);
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
//        FriendUtils.unFollowFriend(getActivity(), 0, "NavUTJHA91azs7Un6iA69VDf7JX2");
        getFriendList(FRIEND_LIST);
    }

    private void getFriendList(String node) {
        final DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE).child(UserAuth.getInstance().getCurrentUser().id).child(node);
        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

}

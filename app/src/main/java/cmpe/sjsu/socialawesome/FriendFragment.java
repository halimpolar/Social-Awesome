package cmpe.sjsu.socialawesome;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cmpe.sjsu.socialawesome.Utils.FriendUtils;
import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserIDMap;
import cmpe.sjsu.socialawesome.models.UserSummary;

import static cmpe.sjsu.socialawesome.StartActivity.USERS_TABLE;
import static cmpe.sjsu.socialawesome.models.User.FOLOWING_FRIEND_LIST;
import static cmpe.sjsu.socialawesome.models.User.FRIEND_LIST;
import static cmpe.sjsu.socialawesome.models.User.PENDING_FRIEND_LIST;
import static cmpe.sjsu.socialawesome.models.User.WAITING_FRIEND_LIST;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendFragment extends SocialFragment {
    private static List<String> mFriendList = new ArrayList<>();
    private RecyclerView recList;
    private FriendListAdapter mAdapter;
    private FriendListAdapter.OnInfoUpdateListener mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTitle = context.getString(R.string.friends);

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
        mListener = new FriendListAdapter.OnInfoUpdateListener() {
            @Override
            public void onInfoUpdate(boolean bool, String st) {
                ((MainActivity) getActivity()).switchFriendToProfileFrag(bool, st);
            }
        };

        recList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        mAdapter = new FriendListAdapter(mFriendList, mListener);
        recList.setAdapter(mAdapter);


        final View addFriendByEmailView = view.findViewById(R.id.add_email_friend_view);

        Button OutRequstBtn = (Button) view.findViewById(R.id.outgoing_friend_request_btn);
        OutRequstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendByEmailView.setVisibility(View.GONE);
                mFriendList = new ArrayList<>();
                getFriendList(WAITING_FRIEND_LIST);
            }
        });

        Button PendingFriendRequstBtn = (Button) view.findViewById(R.id.friend_request_btn);
        PendingFriendRequstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendByEmailView.setVisibility(View.GONE);
                mFriendList = new ArrayList<>();
                getFriendList(PENDING_FRIEND_LIST);
            }
        });

        Button FriendBtn = (Button) view.findViewById(R.id.friend_btn);
        FriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendByEmailView.setVisibility(View.GONE);
                mFriendList = new ArrayList<>();
                getFriendList(FRIEND_LIST);
            }
        });

        Button followingFriendBtn = (Button) view.findViewById(R.id.following_friend_btn);
        followingFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendByEmailView.setVisibility(View.GONE);
                mFriendList = new ArrayList<>();
                getFriendList(FOLOWING_FRIEND_LIST);
            }
        });

        Button addFriendBtn = (Button) view.findViewById(R.id.add_friend_btn);
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendByEmailView.setVisibility(View.VISIBLE);
                mFriendList = new ArrayList<>();
                getPublicUser();
            }
        });

        Button addFriendEmailBtn = (Button) view.findViewById(R.id.add_email_friend_btn);
        final EditText emailET = (EditText) view.findViewById(R.id.email_editText);
        addFriendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                if (email.isEmpty() || email.length() == 0 || email.equals("") || email == null) {
                    emailET.setError("Please Enter the email!");
                } else {
                    FriendUtils.addFriendByEmail(getActivity(), email);
                    emailET.setText("");
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
//        FriendUtils.addFriend(getActivity(), 0, "cBjf2Jmc4TbiZsPWyG4L0Fa2dKl2");
        getFriendList(FRIEND_LIST);
    }

    private void getFriendList(final String node) {

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
                mAdapter = new FriendListAdapter(mFriendList, mListener);

                int type = 0;
                switch (node) {
                    case FRIEND_LIST:
                        type = 0;
                        break;
                    case WAITING_FRIEND_LIST:
                        type = 1;
                        break;
                    case PENDING_FRIEND_LIST:
                        type = 2;
                        break;
                    case FOLOWING_FRIEND_LIST:
                        type = 4;
                        break;
                    default:
                }
                mAdapter.updateType(type);
                recList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPublicUser() {
        final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
        Query query = userTableRef.orderByChild("status").equalTo(2);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFriendList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User publicUser = postSnapshot.getValue(User.class);
                    if (!publicUser.id.equals(UserAuth.getInstance().getCurrentUser().id)) {
                        mFriendList.add(publicUser.id);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                mAdapter = new FriendListAdapter(mFriendList, mListener);
                mAdapter.updateType(3);
                recList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

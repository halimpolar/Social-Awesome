package cmpe.sjsu.socialawesome;

import android.content.Intent;
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
import java.util.List;

import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserSummary;

import static cmpe.sjsu.socialawesome.StartActivity.USERS_TABLE;

/**
 * Created by lam on 4/28/17.
 */
public class InMailListFragment extends SocialFragment {
    final DatabaseReference mSelfRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE)
            .child(UserAuth.getInstance().getCurrentUser().id).child(User.IN_MAIL);
    private RecyclerView mListView;
    private List<UserSummary> mSummaryList = new ArrayList<>();
    private MessageListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.private_message_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (RecyclerView) view.findViewById(R.id.message_list);
        mListView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(llm);

        mAdapter = new MessageListAdapter(mSummaryList, new MessageListAdapter.OnMessageChatClickListener() {
            @Override
            public void onClicked(UserSummary user) {
                Intent intent = new Intent(getActivity(), PrivateMessageActivity.class);
                intent.putExtra(PrivateMessageActivity.ACTION_EXTRA, PrivateMessageActivity.ACTION_DETAIL);
                intent.putExtra(PrivateMessageActivity.BUNDLE_OTHER_USER, user);
                startActivity(intent);
            }
        });
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadMessageList();
    }

    private void loadMessageList() {
        mSelfRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mSummaryList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    UserSummary summary = messageSnapshot.getValue(UserSummary.class);
                    mSummaryList.add(summary);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

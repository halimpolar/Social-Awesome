package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cmpe.sjsu.socialawesome.Utils.UserAuth;

/**
 * Created by lam on 4/28/17.
 */

public class PrivateMessageChatFragment extends SocialFragment {
    private final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child(StartActivity.USERS_TABLE).child(UserAuth.getInstance().getCurrentUser().id);
    private RecyclerView mListView;
    private EditText mEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.private_message_chat, container, false);
        mListView = (RecyclerView) v.findViewById(R.id.message_list);
        mEditText = (EditText) v.findViewById(R.id.message_et);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void addNewChat() {

    }

}

package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cmpe.sjsu.socialawesome.Utils.DbUtils;
import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.InMailMessage;
import cmpe.sjsu.socialawesome.models.User;

import static cmpe.sjsu.socialawesome.StartActivity.USERS_TABLE;

/**
 * Created by lam on 4/28/17.
 */

public class InMailDetailFragment extends SocialFragment {
    public static final String STRING_IN_MAIL_KEY = "string_inmail_key";
    final DatabaseReference mSelfRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE)
            .child(UserAuth.getInstance().getCurrentUser().id).child(User.IN_MAIL);
    private EditText mUserNameEt;
    private EditText mSubjectEt;
    private EditText mContentEt;
    private Button mSendButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.in_mail_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserNameEt = (EditText) view.findViewById(R.id.userName);
        mContentEt = (EditText) view.findViewById(R.id.content_et);
        mSubjectEt = (EditText) view.findViewById(R.id.subject_et);
        mSendButton = (Button) view.findViewById(R.id.send_btn);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mUserNameEt.getText().toString()) &&
                        !TextUtils.isEmpty(mSubjectEt.getText().toString()) &&
                        !TextUtils.isEmpty(mContentEt.getText().toString()))
                    addNewChat(mUserNameEt.getText().toString(), mSubjectEt.getText().toString(), mContentEt.getText().toString());
            }
        });

        if (getArguments() != null && getArguments().getString(STRING_IN_MAIL_KEY) != null) {
            String inMailKey = getArguments().getString(STRING_IN_MAIL_KEY);
            loadChat(inMailKey);
        }
    }

    private void loadChat(String inMailKey) {
        if (inMailKey == null) {
            return;
        }

        mSelfRef.child(inMailKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    InMailMessage message = dataSnapshot.getValue(InMailMessage.class);
                    populateData(message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void populateData(InMailMessage message) {
        mUserNameEt.setText(message.userId);
        mSubjectEt.setText(message.subject);
        mContentEt.setText(message.content);
    }

    private void addNewChat(final String email, final String subject, final String content) {
        DbUtils.executeByEmail(getContext(), email, new DbUtils.OnQueryDbListener() {
            @Override
            public void execute(User user) {
                if (user == null) {
                    Toast.makeText(getContext(), "Not a valid email, please try again", Toast.LENGTH_SHORT).show();
                    return;
                }
                InMailMessage message = new InMailMessage();
                message.userId = user.id;
                message.subject = subject;
                message.content = content;
                message.lastTimeStamp = Double.toString(System.currentTimeMillis() / 1000);

                String key = mSelfRef.push().getKey();
                mSelfRef.child(key).setValue(message);

                DatabaseReference otherRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE).child(user.id).child(User.IN_MAIL);
                key = otherRef.push().getKey();
                otherRef.child(key).setValue(message);

            }
        });
    }
}
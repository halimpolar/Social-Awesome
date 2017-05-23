package cmpe.sjsu.socialawesome;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.User;
import static cmpe.sjsu.socialawesome.StartActivity.USERS_TABLE;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileFragment extends SocialFragment {
    private static final String TAG = ProfileFragment.class.toString();
    ProgressDialog pd;
    private EditText mNicknameEt;
    private EditText mEmailEt;
    private EditText mLocationEt;
    private EditText mProfessionEt;
    private EditText mAboutEt;
    private EditText mInterestEt;
    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private Button mUpdateBtn;
    private Button mEditBtn;
    private Button mCancelBtn;
    private DatabaseReference mFirebaseDatabase;
    String userId;

    public ProfileFragment() {
        mTitle = ProfileFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        mNicknameEt = (EditText) view.findViewById(R.id.nickname);
        mFirstNameEt = (EditText) view.findViewById(R.id.first_name);
        mLastNameEt = (EditText) view.findViewById(R.id.last_name);
        mEmailEt = (EditText) view.findViewById(R.id.email);
        mLocationEt = (EditText) view.findViewById(R.id.location);
        mProfessionEt = (EditText) view.findViewById(R.id.profession);
        mAboutEt = (EditText) view.findViewById(R.id.about);
        mInterestEt = (EditText) view.findViewById(R.id.interests);
        mUpdateBtn = (Button) view.findViewById(R.id.update_btn);
        mCancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        mEditBtn = (Button) view.findViewById(R.id.edit_btn);
        //mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child(StartActivity.USERS_TABLE).child(UserAuth.getInstance().getCurrentUser().id);


        mFirebaseDatabase.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                final User user = mutableData.getValue(User.class);
                if (user != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateInfoIntoEditText(user);
                        }
                    });

                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_name = mFirstNameEt.getText().toString();
                String last_name = mLastNameEt.getText().toString();
                String email = mEmailEt.getText().toString();
                /*String nickname = mNicknameEt.getText().toString();
                String location = mLocationEt.getText().toString();
                String profession = mProfessionEt.getText().toString();
                String about_me = mAboutEt.getText().toString();
                String interest = mInterestEt.getText().toString();*/

                mFirebaseDatabase.child("first_name").setValue(first_name);
                mFirebaseDatabase.child("last_name").setValue(last_name);
                mFirebaseDatabase.child("email").setValue(email);
                /*mFirebaseDatabase.child("nickname").setValue(nickname);
                mFirebaseDatabase.child("location").setValue(location);
                mFirebaseDatabase.child("profession").setValue(profession);
                mFirebaseDatabase.child("about_me").setValue(about_me);
                mFirebaseDatabase.child("interest").setValue(interest);*/
            }
        });
        return view;
    }

 /*       mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseDatabase.child("first_name")
            }

        });
        return view;
    }
*/

    private void populateInfoIntoEditText(User user) {
        setEditText(mFirstNameEt, user.first_name);
        setEditText(mLastNameEt, user.last_name);
        setEditText(mEmailEt, user.email);
        /*setEditText(mLocationEt, user.location);
        setEditText(mNicknameEt, user.nickname);
        setEditText(mProfessionEt, user.profession);
        setEditText(mAboutEt, user.aboutMe);
        setEditText(mInterestEt, user.interests);*/
    }

    private void setEditText(EditText et, String st) {
        if (et != null && !TextUtils.isEmpty(st)) {
            et.setText(st);
        }
    }

    private void profileDisplay(User user) {

    }
}
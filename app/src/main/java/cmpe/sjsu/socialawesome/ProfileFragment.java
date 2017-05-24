package cmpe.sjsu.socialawesome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
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

    private String currentUserId;
    private MainActivity activity;

    public ProfileFragment()  {
        mTitle = ProfileFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        mEditBtn = (Button) view.findViewById(R.id.edit_btn);


        mNicknameEt = (EditText) view.findViewById(R.id.nickname);
        mFirstNameEt = (EditText) view.findViewById(R.id.first_name);
        mLastNameEt = (EditText) view.findViewById(R.id.last_name);
        mEmailEt = (EditText) view.findViewById(R.id.email);
        mLocationEt = (EditText) view.findViewById(R.id.location);
        mProfessionEt = (EditText) view.findViewById(R.id.profession);
        mAboutEt = (EditText) view.findViewById(R.id.about_me);
        mInterestEt = (EditText) view.findViewById(R.id.interests);
        mUpdateBtn = (Button) view.findViewById(R.id.update_btn);
        mCancelBtn = (Button) view.findViewById(R.id.cancel_btn);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEditBtn.setVisibility(View.VISIBLE);
        mFirstNameEt.setVisibility(View.GONE);
        mLastNameEt.setVisibility(View.GONE);
        mNicknameEt.setVisibility(View.GONE);
        mEmailEt.setVisibility(View.GONE);
        mLocationEt.setVisibility(View.GONE);
        mProfessionEt.setVisibility(View.GONE);
        mAboutEt.setVisibility(View.GONE);
        mInterestEt.setVisibility(View.GONE);
        mUpdateBtn.setVisibility(View.GONE);
        mCancelBtn.setVisibility(View.GONE);

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditBtn.setVisibility(View.GONE);
                mFirstNameEt.setVisibility(View.VISIBLE);
                mLastNameEt.setVisibility(View.VISIBLE);
                mNicknameEt.setVisibility(View.VISIBLE);
                mEmailEt.setVisibility(View.VISIBLE);
                mLocationEt.setVisibility(View.VISIBLE);
                mProfessionEt.setVisibility(View.VISIBLE);
                mAboutEt.setVisibility(View.VISIBLE);
                mInterestEt.setVisibility(View.VISIBLE);
                mUpdateBtn.setVisibility(View.VISIBLE);
                mCancelBtn.setVisibility(View.VISIBLE);

                mCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEditBtn.setVisibility(View.VISIBLE);
                        mFirstNameEt.setVisibility(View.GONE);
                        mLastNameEt.setVisibility(View.GONE);
                        mNicknameEt.setVisibility(View.GONE);
                        mEmailEt.setVisibility(View.GONE);
                        mLocationEt.setVisibility(View.GONE);
                        mProfessionEt.setVisibility(View.GONE);
                        mAboutEt.setVisibility(View.GONE);
                        mInterestEt.setVisibility(View.GONE);
                        mUpdateBtn.setVisibility(View.GONE);
                        mCancelBtn.setVisibility(View.GONE);
                    }
                });
            }
        });

        activity = (MainActivity) getActivity();
        if (activity.isOtherUser && activity.otherUserId != null) {
            currentUserId = activity.otherUserId;
        } else {
            currentUserId = UserAuth.getInstance().getCurrentUser().id;
        }

        //mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference()
                .child(StartActivity.USERS_TABLE).child(currentUserId);


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
                String location = mLocationEt.getText().toString();
                String nickname = mNicknameEt.getText().toString();
                String profession = mProfessionEt.getText().toString();
                String about_me = mAboutEt.getText().toString();
                String interest = mInterestEt.getText().toString();

                mFirebaseDatabase.child("first_name").setValue(first_name);
                mFirebaseDatabase.child("last_name").setValue(last_name);
                mFirebaseDatabase.child("email").setValue(email);
                mFirebaseDatabase.child("location").setValue(location);
                mFirebaseDatabase.child("nickname").setValue(nickname);
                mFirebaseDatabase.child("profession").setValue(profession);
                mFirebaseDatabase.child("about_me").setValue(about_me);
                mFirebaseDatabase.child("interest").setValue(interest);
                Toast.makeText(getActivity(), "Profile Has Been Updated",Toast.LENGTH_SHORT).show();

                mEditBtn.setVisibility(View.VISIBLE);
                mFirstNameEt.setVisibility(View.GONE);
                mLastNameEt.setVisibility(View.GONE);
                mNicknameEt.setVisibility(View.GONE);
                mEmailEt.setVisibility(View.GONE);
                mLocationEt.setVisibility(View.GONE);
                mProfessionEt.setVisibility(View.GONE);
                mAboutEt.setVisibility(View.GONE);
                mInterestEt.setVisibility(View.GONE);
                mUpdateBtn.setVisibility(View.GONE);
                mCancelBtn.setVisibility(View.GONE);
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //displayProfile();
            }

        });
    }

    private void populateInfoIntoEditText(User user) {
        setEditText(mFirstNameEt, user.first_name);
        setEditText(mLastNameEt, user.last_name);
        setEditText(mEmailEt, user.email);
        setEditText(mLocationEt, user.location);
        setEditText(mNicknameEt, user.nickname);
        setEditText(mInterestEt, user.interest);
        setEditText(mProfessionEt, user.profession);
        setEditText(mAboutEt, user.about_me);

    }

    private void setEditText(EditText et, String st) {
        if (et != null && !TextUtils.isEmpty(st)) {
            et.setText(st);
        }
    }

    //private static profileDisplay getProfile(User user) {
    //    UserAuth.getInstance().setCurrentUser(user);


    //}
}
package cmpe.sjsu.socialawesome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;

import cmpe.sjsu.socialawesome.Utils.TokenBroadcastReceiver;
import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.User;

public class StartActivity extends AppCompatActivity {
    public static final String USERS_TABLE = "users";
    private static final String TAG = StartActivity.class.toString();
    private EditText mEmailEt;
    private EditText mPasswordEt;
    private FirebaseAuth mAuth;
    private Button mSubmitBtn;
    private EditText mConfirmPasswordEt;
    private Button mCreateAccountTv;
    private Button mVerifyAccount;
    private Button mResendVerification;
    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private boolean mIsLogin = true;
    private String mCustomToken;
    private TokenBroadcastReceiver mTokenReceiver;


    private void launchMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        mAuth = FirebaseAuth.getInstance();

        //Buttons
        mSubmitBtn = (Button) findViewById(R.id.submit);
        mCreateAccountTv = (Button) findViewById(R.id.create_account);
        mVerifyAccount = (Button) findViewById(R.id.verify_account);
        mResendVerification = (Button) findViewById(R.id.resend_verification);
        //Fields
        mEmailEt = (EditText) findViewById(R.id.email);
        mPasswordEt = (EditText) findViewById(R.id.password);
        mConfirmPasswordEt = (EditText) findViewById(R.id.confirm_password);
        mFirstNameEt = (EditText) findViewById(R.id.first_name);
        mLastNameEt = (EditText) findViewById(R.id.last_name);

        mVerifyAccount.setVisibility(View.GONE);
        mResendVerification.setVisibility(View.GONE);

        mCreateAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupUI(!mIsLogin);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsLogin) {
                    createAccount(mEmailEt.getText().toString(), mPasswordEt.getText().toString());
                } else {
                    signIn(mEmailEt.getText().toString(), mPasswordEt.getText().toString());
                }
            }
        });

        setupUI(true);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validate()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //successLogin(mAuth.getCurrentUser());
                            sendEmailVerification();
                            updateUI(user);

                            mVerifyAccount.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendEmailVerification();
                                }

                            });

                            mResendVerification.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendEmailVerification();
                                }
                            });

                            mSubmitBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!mIsLogin) {
                                        createAccount(mEmailEt.getText().toString(), mPasswordEt.getText().toString());
                                    } else {
                                        signIn(mEmailEt.getText().toString(), mPasswordEt.getText().toString());
                                    }
                                }
                            });

                            setupUI(true);


                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            //TODO: show error message onscreen instead of toast
                            Toast.makeText(StartActivity.this, "You already have an account",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validate()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            successLogin(mAuth.getCurrentUser());
                            setupUI(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //TODO: Display failure message

                            Toast.makeText(StartActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validate() {
        //validate that the field is completed
        boolean valid = true;

        String email = mEmailEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailEt.setError("Required.");
            valid = false;
        } else {
            mEmailEt.setError(null);
        }

        String password = mPasswordEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordEt.setError("Required.");
            valid = false;
        } else {
            mPasswordEt.setError(null);
        }

        return valid;
    }

    private void successLogin(final FirebaseUser fbUser) {
        final String token = FirebaseInstanceId.getInstance().getToken();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
        if (fbUser.isEmailVerified()) {

            ref.child(fbUser.getUid()).runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    User user = mutableData.getValue(User.class);
                    if (user != null) {

                        if (token != null && !token.equals(user.token)) {
                            user.token = token;
                            ref.child(fbUser.getUid()).child("token").setValue(token);
                        }

                        UserAuth.getInstance().setCurrentUser(user);
                        launchMainActivity();
                    } else {
                        user = new User();
                        user.id = fbUser.getUid();
                        user.email = fbUser.getEmail();
                        user.first_name = mFirstNameEt.getText().toString();
                        user.last_name = mLastNameEt.getText().toString();
                        user.status = 1;
                        user.token = token;

                        Task task = ref.child(fbUser.getUid()).setValue(user);

                        if (!task.isSuccessful()) {
                            //TODO: show text fail to save user to db
                            Log.e(TAG, "Fail to save user to db");
                        } else {
                            UserAuth.getInstance().setCurrentUser(user);
                            launchMainActivity();
                            Log.d(TAG, "Successfully create user in db");
                        }
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });

        } else {
            Toast.makeText(StartActivity.this, "Account is not Verified",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // UI before email is verified
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mFirstNameEt.setVisibility(View.GONE);
            mLastNameEt.setVisibility(View.GONE);
            mConfirmPasswordEt.setVisibility(View.GONE);
            mCreateAccountTv.setVisibility(View.GONE);
            mSubmitBtn.setVisibility(View.VISIBLE);
            mVerifyAccount.setVisibility(View.VISIBLE);
            mResendVerification.setVisibility(View.VISIBLE);
            mSubmitBtn.setText(getString(R.string.login));

            //successLogin(mAuth.getCurrentUser());
        } else {
            successLogin(mAuth.getCurrentUser());
            //findViewById(R.id.verify_account).setVisibility(View.VISIBLE);

        }
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_account).setEnabled(false);

// Send verification email
// [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_account).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(StartActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(StartActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    // UI after email is verified
    private void setupUI(boolean isLogin) {
        mIsLogin = isLogin;
        if (isLogin) {
            mFirstNameEt.setVisibility(View.GONE);
            mLastNameEt.setVisibility(View.GONE);
            mConfirmPasswordEt.setVisibility(View.GONE);
            mSubmitBtn.setText(getString(R.string.login));
            mCreateAccountTv.setText(getString(R.string.create_account));
        } else {
            mFirstNameEt.setVisibility(View.VISIBLE);
            mLastNameEt.setVisibility(View.VISIBLE);
            mConfirmPasswordEt.setVisibility(View.VISIBLE);
            mSubmitBtn.setText(getString(R.string.signup));
            mCreateAccountTv.setText(R.string.signin_text);
        }
    }
}
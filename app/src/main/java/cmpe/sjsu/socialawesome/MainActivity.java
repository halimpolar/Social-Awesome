package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.User;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.toString();
    private static final String USERS_TABLE = "users";
    private EditText mEmailEt;
    private EditText mPasswordEt;
    private FirebaseAuth mAuth;
    private Button mSubmitBtn;
    private EditText mConfirmPasswordEt;
    private TextView mCreateAccountTv;
    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private boolean mIsLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mAuth = FirebaseAuth.getInstance();

        mSubmitBtn = (Button) findViewById(R.id.submit);
        mEmailEt = (EditText) findViewById(R.id.email);
        mPasswordEt = (EditText) findViewById(R.id.password);
        mConfirmPasswordEt = (EditText) findViewById(R.id.confirm_password);
        mFirstNameEt = (EditText) findViewById(R.id.first_name);
        mLastNameEt = (EditText) findViewById(R.id.last_name);
        mCreateAccountTv = (TextView) findViewById(R.id.create_account_text);

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
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //TODO: Display failure message

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validate()) {
            //TODO: show error message
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            successLogin(mAuth.getCurrentUser());

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            //TODO: show error message onscreen instead of toast
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validate() {
        //TODO: do validation on the input
        return true;
    }

    private void successLogin(FirebaseUser fbUser) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
        if (!mIsLogin) {
            User user = new User();
            user.email = fbUser.getEmail();
            user.first_name = mFirstNameEt.getText().toString();
            user.last_name = mLastNameEt.getText().toString();
//            user.nickname = "Nick";

            Task task = ref.child(fbUser.getUid()).setValue(user);

            if (task.isSuccessful()) {
                UserAuth.getInstance().setCurrentUser(user);
                Log.d(TAG, "Successfully create user in db");
            } else {
                //TODO: show text fail to save user to db
                Log.e(TAG, "Fail to save user to db");
            }
        } else {
            ref.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    User user = mutableData.getValue(User.class);
                    UserAuth.getInstance().setCurrentUser(user);

                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });
        }
    }

    private void setupUI(boolean isLogin) {
        mIsLogin = isLogin;
        if (isLogin) {
            mFirstNameEt.setVisibility(View.GONE);
            mLastNameEt.setVisibility(View.GONE);
            mConfirmPasswordEt.setVisibility(View.GONE);
            mSubmitBtn.setText(getString(R.string.login));
            mCreateAccountTv.setText(getString(R.string.create_account_text));
        } else {
            mFirstNameEt.setVisibility(View.VISIBLE);
            mLastNameEt.setVisibility(View.VISIBLE);
            mConfirmPasswordEt.setVisibility(View.VISIBLE);
            mSubmitBtn.setText(getString(R.string.signup));
            mCreateAccountTv.setText(R.string.signin_text);
        }
    }
}

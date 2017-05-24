package cmpe.sjsu.socialawesome;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserSummary;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingFragment extends SocialFragment {
    private RadioButton radio_private;
    private RadioButton radio_public;
    private RadioButton radio_friend;
    private RadioButton radio_yes;
    private RadioButton radio_no;
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private Button backBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference msubData;
    private DatabaseReference msuData;

    public SettingFragment() {
        mTitle = SettingFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_setting, container, false);
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child(StartActivity.USERS_TABLE).child(UserAuth.getInstance().getCurrentUser().id);
        radio_private = (RadioButton) view.findViewById(R.id.radio_private);
        radio_public = (RadioButton) view.findViewById(R.id.radio_public);
        radio_friend= (RadioButton) view.findViewById(R.id.radio_friend);
        radio_yes = (RadioButton) view.findViewById(R.id.radio_yes);
        radio_no = (RadioButton) view.findViewById(R.id.radio_no);
        radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) view.findViewById(R.id.radioGroup2);
        backBtn = (Button) view.findViewById(R.id.back_button);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mAuth = FirebaseAuth.getInstance();
                switch(checkedId) {
                    case R.id.radio_private:
                        mFirebaseDatabase.child("status").setValue(0);
                        Toast.makeText(getActivity(), "Profile Set to Private",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_public:
                        mFirebaseDatabase.child("status").setValue(1);
                        Toast.makeText(getActivity(), "Profile Set to Public",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_friend:
                        mFirebaseDatabase.child("status").setValue(2);
                        Toast.makeText(getActivity(), "Profile Set to Friend Only",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mAuth = FirebaseAuth.getInstance();
                switch(checkedId) {
                    case R.id.radio_yes:
                        mFirebaseDatabase.child("notification").setValue(true);
                        Toast.makeText(getActivity(), "Email Notification Set",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_no:
                        mFirebaseDatabase.child("notification").setValue(false);
                        Toast.makeText(getActivity(), "No Email Notification",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });




        return view;


    }


    /*public void onRadioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        mAuth = FirebaseAuth.getInstance();
        switch(view.getId()) {
            case R.id.radio_private:
                if(checked){
                    mFirebaseDatabase.child("status").setValue(0);
                }
                break;
            case R.id.radio_public:
                if(checked) {
                    mFirebaseDatabase.child("status").setValue(2);
                }
                break;
            case R.id.radio_friend:
                if(checked) {
                    mFirebaseDatabase.child("status").setValue(1);
                }
                break;
            case R.id.radio_yes:
                if(checked) {
                    mFirebaseDatabase.child("notification").setValue(true);
                }
                break;
            case R.id.radio_no:
                if(checked) {
                    mFirebaseDatabase.child("notification").setValue(false);
                }
                break;
        }
    }*/

}

 //   @Override
 //   public void onStart() {
 //       super.onStart();
//        List<String> tokens = Arrays.asList(UserAuth.getInstance().getCurrentUser().token);
//        HTTPUtil.sendPushNotification(getActivity(), tokens, "Test title", "Test Message", null);
//
//        tokens = Arrays.asList("lam.tran@sjsu.edu");
//        HTTPUtil.sendEmail(getActivity(), tokens, "Test title", "Test Message");

//        Intent intent = new Intent(getActivity(), PrivateMessageActivity.class);
//        intent.putExtra(PrivateMessageActivity.ACTION_EXTRA, PrivateMessageActivity.ACTION_DETAIL);
//        startActivity(intent);
//    }
//}

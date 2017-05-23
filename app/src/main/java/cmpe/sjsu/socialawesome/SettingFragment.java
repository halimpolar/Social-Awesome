package cmpe.sjsu.socialawesome;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

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
    private Button backBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;

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
        backBtn = (Button) view.findViewById(R.id.back_button);

        return view;


    }



    public void onRadioButtonClicked (View view) {
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
    }

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

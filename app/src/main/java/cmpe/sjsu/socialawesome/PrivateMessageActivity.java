package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

public class PrivateMessageActivity extends AppCompatActivity {
    public static final String ACTION_LIST = "open_list_message";
    public static final String ACTION_DETAIL = "open_new_message";
    public static final String ACTION_EXTRA = "open_new_message";
    private SocialFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra(ACTION_EXTRA))) {
            switch (getIntent().getStringExtra(ACTION_EXTRA)) {
                case ACTION_LIST:
                    mFragment = new PrivateMessageListFragment();
                    break;
                case ACTION_DETAIL:
                    mFragment = new PrivateMessageChatFragment();
                    break;
                default:
                    break;
            }

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.container, mFragment);
            transaction.commit();
        }
    }
}

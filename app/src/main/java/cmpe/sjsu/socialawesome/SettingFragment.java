package cmpe.sjsu.socialawesome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingFragment extends SocialFragment {

    public SettingFragment() {
        mTitle = SettingFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
//        List<String> tokens = Arrays.asList(UserAuth.getInstance().getCurrentUser().token);
//        HTTPUtil.sendPushNotification(getActivity(), tokens, "Test title", "Test Message", null);
//
//        tokens = Arrays.asList("lam.tran@sjsu.edu");
//        HTTPUtil.sendEmail(getActivity(), tokens, "Test title", "Test Message");

        Intent intent = new Intent(getActivity(), PrivateMessageActivity.class);
        intent.putExtra(PrivateMessageActivity.ACTION_EXTRA, PrivateMessageActivity.ACTION_DETAIL);
        startActivity(intent);
    }
}

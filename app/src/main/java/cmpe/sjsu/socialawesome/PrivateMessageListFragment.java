package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lam on 4/28/17.
 */

public class PrivateMessageListFragment extends SocialFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.private_message_list, container, false);
    }
}

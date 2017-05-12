package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cmpe.sjsu.socialawesome.Utils.FriendUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendFragment extends SocialFragment {

    public FriendFragment() {
        mTitle = FriendFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //add friend and follow on profile
//        UserSummary mySummary = UserAuth.getInstance().getCurrentUserSummary();
//        FriendUtils.addFriend(getActivity(), 1, mySummary);

        //add friend by email
//        FriendUtils.addFriendByEmail(getActivity(),"lam.tran@sjsu.edu");
        FriendUtils.addFriendByEmail(getActivity(),"sheilashi0112@gmail.com");
    }
}

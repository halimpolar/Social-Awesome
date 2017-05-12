package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import cmpe.sjsu.socialawesome.Utils.HTTPUtil;

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
        List<String> tokens = Arrays.asList("fIDrcwIl__E:APA91bEb4D7KqlqumXjPpblKv0FcToDfCOe_n0g7Wz6U_Hhzsuxsoz_gD4t2jD1BEi-Uk-pNJZxvD5F6qVs9jrqlb21F6G0oHU9svqqUlvGo3Hb39HJ6mrGPOyOy_E5Y_avlbtVV5onv", "dhLO55aORvI:APA91bEdqhZnduy-p1Adg-Dogey8J2cm6KDqZI_NgBF3WWRqKPyPZ47UaZHPAz175PrG4UuevhISK-FgjSatuVtKSuD1N5_Vc22dDDq9MP6IptVfkGpxo4JOxYsTJF4G3ATvRH3u5Ne5");
        HTTPUtil.sendPushNotification(getActivity(), tokens, "Test title", "Test Message", null);
    }
}

package cmpe.sjsu.socialawesome.Utils;

import com.google.firebase.auth.FirebaseAuth;

import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserSummary;

/**
 * Created by lam on 4/27/17.
 */

public class UserAuth {
    private static final UserAuth mInstance = new UserAuth();
    private static User mCurrentUser;
    private static UserSummary mCurrentUserSummary = new UserSummary();


    private UserAuth() {
    }

    public static UserAuth getInstance() {
        return mInstance;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public static UserSummary getCurrentUserSummary() {
        return mCurrentUserSummary;
    }

    public void setCurrentUser(User user) {
        mCurrentUser = user;
//        setCurrentUserSummary(user);
    }

//    public void setCurrentUserSummary(User user) {
//        mCurrentUserSummary.id = user.id;
//        mCurrentUserSummary.email = user.email;
//        mCurrentUserSummary.first_name = user.first_name;
//        mCurrentUserSummary.last_name = user.last_name;
//        mCurrentUserSummary.profilePhotoURL = user.profilePhotoURL;
//        mCurrentUserSummary.status = user.status;
//    }

    public void signOut() {
        mCurrentUser = null;
        FirebaseAuth.getInstance().signOut();
    }
}

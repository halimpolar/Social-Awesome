package cmpe.sjsu.socialawesome.Utils;

import com.google.firebase.auth.FirebaseAuth;

import cmpe.sjsu.socialawesome.models.User;

/**
 * Created by lam on 4/27/17.
 */

public class UserAuth {
    private static final UserAuth mInstance = new UserAuth();
    private User mCurrentUser;

    private UserAuth() {
    }

    public static UserAuth getInstance() {
        return mInstance;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(User user) {
        mCurrentUser = user;
    }

    public void signOut() {
        mCurrentUser = null;
        FirebaseAuth.getInstance().signOut();
    }
}

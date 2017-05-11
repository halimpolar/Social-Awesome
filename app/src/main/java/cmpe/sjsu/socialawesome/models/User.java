package cmpe.sjsu.socialawesome.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by lam on 4/27/17.
 */
@IgnoreExtraProperties
public class User {
    public static final String FRIEND_LIST = "friends";
    public static final String WAITING_FRIEND_LIST = "waitingFriends";
    public static final String FOLOWING_FRIEND_LIST = "followingFriends";
    public static final String FOLLOWER_LIST = "follower";
    public static final String PENDING_FRIEND_LIST = "pendingFriends";
    public String id;
    public String email;
    public String first_name;
    public String last_name;
    public String nickname;
    public String profilePhotoURL;
    public String location;
    public String profession;
    public String aboutMe;
    public String intestes;


    // 0 -- disable
    // 1 -- friends only view
    // 2 -- public
    public int status;

    public boolean notification;

    // List of friend already establish
    public List<UserSummary> friends;

    // List of friend this user sent invitation to
    public List<UserSummary> waitingFriends;

    // List of friend this user sent invitation to
    public List<UserSummary> followingFriends;

    // List of person who follow me
    public List<UserSummary> follower;

    // List of friends that sent invitation to this user
    public List<UserSummary> pendingFriends;

}

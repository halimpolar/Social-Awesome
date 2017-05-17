package cmpe.sjsu.socialawesome.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by lam on 4/27/17.
 */
@IgnoreExtraProperties
public class Post {
    public String authorName;
    public String profilePic;
    public String contentPost;
    public String contentPhotoURL;

}

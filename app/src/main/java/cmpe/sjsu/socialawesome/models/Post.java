package cmpe.sjsu.socialawesome.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lam on 4/27/17.
 */
@IgnoreExtraProperties
public class Post {
    public User user;
    public String contentPost;
    public String contentPhotoURL;
    public Date timestamp;

    public Post(User user, String contentPost, String contentPhotoURL) {
        this.user = user;
        this.contentPost = contentPost;
        this.contentPhotoURL = contentPhotoURL;
        this.timestamp = Calendar.getInstance().getTime();
    }

    public User getUser() {
        return user;
    }

    public String getAuthorName() {
        return user.first_name + " " + user.last_name;
    }

    public String getContentPost() {
        return contentPost;
    }

    public String getContentPhotoURL() {
        return contentPhotoURL;
    }
}

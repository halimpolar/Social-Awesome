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

    public Post(String authorName, String profilePic, String contentPost, String contentPhotoURL) {
        this.authorName = authorName;
        this.profilePic = profilePic;
        this.contentPost = contentPost;
        this.contentPhotoURL = contentPhotoURL;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getContentPost() {
        return contentPost;
    }

    public void setContentPost(String contentPost) {
        this.contentPost = contentPost;
    }

    public String getContentPhotoURL() {
        return contentPhotoURL;
    }

    public void setContentPhotoURL(String contentPhotoURL) {
        this.contentPhotoURL = contentPhotoURL;
    }
}

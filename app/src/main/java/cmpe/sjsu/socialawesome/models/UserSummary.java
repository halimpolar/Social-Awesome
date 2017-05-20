package cmpe.sjsu.socialawesome.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by lam on 4/27/17.
 */
@IgnoreExtraProperties
public class UserSummary implements Serializable{
    public String id;
    public String email;
    public String first_name;
    public String last_name;
    public String profilePhotoURL;
    public int status; //this
    public String nick_name;
    public String location;
    public String profession;

}

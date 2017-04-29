package cmpe.sjsu.socialawesome.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by lam on 4/27/17.
 */
@IgnoreExtraProperties
public class UserSummary {
    public String id;
    public String email;
    public String first_name;
    public String last_name;
}

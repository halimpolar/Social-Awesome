package cmpe.sjsu.socialawesome.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by lam on 4/27/17.
 */
@IgnoreExtraProperties
public class SingleMessage {
    public boolean isSelf;
    public String message;

    public SingleMessage(String message, boolean isSelf) {
        this.isSelf = isSelf;
        this.message = message;
    }
}

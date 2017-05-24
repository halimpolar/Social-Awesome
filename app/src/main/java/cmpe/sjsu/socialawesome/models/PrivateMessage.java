package cmpe.sjsu.socialawesome.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

/**
 * Created by lam on 4/27/17.
 */
@IgnoreExtraProperties
public class PrivateMessage {
    public static final String MESSAGES = "messages";
    public UserSummary user;
    public String lastTimeStamp;
    public String title;
    public Map<String, SingleMessage> messages;
}

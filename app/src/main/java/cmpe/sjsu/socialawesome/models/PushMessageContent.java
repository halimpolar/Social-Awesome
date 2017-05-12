package cmpe.sjsu.socialawesome.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lam on 5/12/17.
 */

public class PushMessageContent {
    public Map<String, String> data;

    public PushMessageContent(String title, String message, Map<String, String> data) {
        if (data == null) data = new HashMap<>();
        data.put("title", title);
        data.put("body", message);
        this.data = data;
    }
}

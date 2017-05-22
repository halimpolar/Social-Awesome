package cmpe.sjsu.socialawesome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.User;

public class CreatePostActivity extends AppCompatActivity {
    private EditText contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        contentText = (EditText) findViewById(R.id.post_content);
        User user = UserAuth.getInstance().getCurrentUser();
        if (user.profilePhotoURL == null) {
            user.profilePhotoURL = getString(R.string.default_profile_pic);
        }
        Picasso.with(getApplicationContext()).load(user.profilePhotoURL).into(((ImageView)findViewById(R.id.timeline_pic)));
        ((TextView)findViewById(R.id.timeline_name)).setText(user.first_name + " " + user.last_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.post) {
            Intent data = new Intent();
            data.putExtra(TimeLineFragment.POST_CONTENT_KEY, contentText.getText().toString());
            setResult(TimeLineFragment.RESULT_OK, data);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

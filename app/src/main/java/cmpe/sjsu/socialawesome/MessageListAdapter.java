package cmpe.sjsu.socialawesome;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cmpe.sjsu.socialawesome.models.UserSummary;

/**
 * Created by lam on 5/19/17.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    private List<UserSummary> mUsers;

    public MessageListAdapter(List<UserSummary> summaries) {
        mUsers = summaries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_list_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserSummary user = mUsers.get(position);
        holder.mUserName.setText(user.email);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mUserImage;
        public TextView mUserName;

        public ViewHolder(View view) {
            super(view);
            mUserImage = (ImageView) view.findViewById(R.id.userImage);
            mUserName = (TextView) view.findViewById(R.id.userName);
        }
    }
}

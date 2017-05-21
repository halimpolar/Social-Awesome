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
    private OnMessageChatClickListener mListener;

    public MessageListAdapter(List<UserSummary> summaries, OnMessageChatClickListener listener) {
        mUsers = summaries;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_list_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserSummary user = mUsers.get(position);
        holder.mUserName.setText(user.email);
        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClicked(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    interface OnMessageChatClickListener {
        void onClicked(UserSummary user);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mUserImage;
        public TextView mUserName;
        public View mRootView;

        public ViewHolder(View view) {
            super(view);
            mRootView = view;
            mUserImage = (ImageView) view.findViewById(R.id.userImage);
            mUserName = (TextView) view.findViewById(R.id.userName);
        }
    }
}

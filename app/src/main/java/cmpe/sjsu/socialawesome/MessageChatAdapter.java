package cmpe.sjsu.socialawesome;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.SingleMessage;
import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserSummary;

/**
 * Created by lam on 5/19/17.
 */

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.ViewHolder> {
    private List<SingleMessage> mMessages;
    private UserSummary mUser;
    private User mCurrentUser;

    public MessageChatAdapter(List<SingleMessage> messages, UserSummary userSummary) {
        mMessages = messages;
        mUser = userSummary;
        mCurrentUser = UserAuth.getInstance().getCurrentUser();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SingleMessage message = mMessages.get(position);
        holder.mTextContent.setText(message.message);
        holder.mUserName.setText(message.isSelf ? mCurrentUser.email : mUser.email);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mUserImage;
        public TextView mUserName;
        public TextView mTextContent;

        public ViewHolder(View view) {
            super(view);
            mUserImage = (ImageView) view.findViewById(R.id.userImage);
            mUserName = (TextView) view.findViewById(R.id.userName);
            mTextContent = (TextView) view.findViewById(R.id.message_content);
        }
    }
}

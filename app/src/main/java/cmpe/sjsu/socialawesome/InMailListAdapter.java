package cmpe.sjsu.socialawesome;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cmpe.sjsu.socialawesome.Utils.DbUtils;
import cmpe.sjsu.socialawesome.models.InMailMessage;
import cmpe.sjsu.socialawesome.models.User;

/**
 * Created by lam on 5/19/17.
 */

public class InMailListAdapter extends RecyclerView.Adapter<InMailListAdapter.ViewHolder> {
    private List<InMailMessage> mailMessages;
    private OnInMailMessageClickListener mListener;

    public InMailListAdapter(List<InMailMessage> messages, OnInMailMessageClickListener listener) {
        mailMessages = messages;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_list_in_mail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final InMailMessage message = mailMessages.get(position);
        DbUtils.executeById(holder.mRootView.getContext(), message.userId, new DbUtils.OnQueryDbListener() {
            @Override
            public void execute(User user) {
                holder.mUserNameEt.setText(user.email);
                holder.mSubjectEt.setText(message.subject);
                holder.mTimestampEt.setText(message.lastTimeStamp);
            }
        });

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClicked(message.id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mailMessages.size();
    }

    interface OnInMailMessageClickListener {
        void onClicked(String messageId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mUserImage;
        public TextView mUserNameEt;
        public TextView mSubjectEt;
        public TextView mTimestampEt;
        public View mRootView;

        public ViewHolder(View view) {
            super(view);
            mRootView = view;
            mUserImage = (ImageView) view.findViewById(R.id.userImage);
            mUserNameEt = (TextView) view.findViewById(R.id.userName_et);
            mSubjectEt = (TextView) view.findViewById(R.id.subject_et);
            mTimestampEt = (TextView) view.findViewById(R.id.timestamp_et);
        }
    }
}

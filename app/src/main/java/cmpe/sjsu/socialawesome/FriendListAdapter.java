package cmpe.sjsu.socialawesome;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import cmpe.sjsu.socialawesome.Utils.DbUtils;
import cmpe.sjsu.socialawesome.Utils.FriendUtils;
import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserSummary;

import static android.R.id.message;

/**
 * Created by bing on 5/13/17.
 */


public class FriendListAdapter extends RecyclerView.Adapter <FriendListAdapter.FriendViewHolder>{
    private List<String> entryList;
    private int mType = 0;

    public FriendListAdapter(List<String> entryList) {
        this.entryList = entryList;
//        Collections.sort(this.entryList, new DateComparator());
    }


    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);
        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FriendViewHolder holder, int position) {
        String entry = entryList.get(position);

        DbUtils.executeById(holder.vEmail.getContext(), entry, new DbUtils.OnQueryDbListener() {
            @Override
            public void execute(final User user) {
                holder.vName.setText(user.first_name + " " + user.last_name);
                holder.vEmail.setText("Email:            " + user.email);
                holder.vLocation.setText("Location:      " + user.location);

                switch (mType){
                    case 2:
                        holder.acceptReqBtn.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        holder.acceptReqBtn.setVisibility(View.VISIBLE);
                        holder.acceptReqBtn.setText("Follow");
                        holder.addSelFriendBtn.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        holder.acceptReqBtn.setVisibility(View.VISIBLE);
                        holder.acceptReqBtn.setText("Un Follow");
                    default:
                }

                holder.acceptReqBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        switch (mType){
                            case 2:
                                FriendUtils.unFollowFriend(holder.vEmail.getContext(), 0, user.id);
                                break;
                            case 3:
                                FriendUtils.addFriend(holder.vEmail.getContext(), 1, user.id);
                                holder.acceptReqBtn.setText("Following");
                                holder.acceptReqBtn.setClickable(false);
                                break;
                            case 4:
                                FriendUtils.unFollowFriend(holder.vEmail.getContext(), 1, user.id);
                                holder.acceptReqBtn.setText("Un-Followed");
                                holder.acceptReqBtn.setClickable(false);
                            default:
                        }
                    }
                });

                holder.addSelFriendBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        FriendUtils.addFriend(holder.vEmail.getContext(),0, user.id);
                    }
                });

                holder.mRootView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String otherUserId = user.id;
                        Boolean isOtherUser = false;
                        //TODO:
                    }
                });
            }
        });

        //TODO: setimageurl: http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vEmail;
        protected TextView vLocation;
        protected Button acceptReqBtn;
        protected Button addSelFriendBtn;
        protected View mRootView;

        public FriendViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.profileName);
            vEmail = (TextView) v.findViewById(R.id.profileEmail);
            vLocation = (TextView) v.findViewById(R.id.profileLocation);
            acceptReqBtn = (Button) v.findViewById(R.id.accept_request_button);
            addSelFriendBtn = (Button) v.findViewById(R.id.add_select_friend_button);
            mRootView = v;
        }
    }

    public void updateType(int type) {
        mType = type;
    }

}



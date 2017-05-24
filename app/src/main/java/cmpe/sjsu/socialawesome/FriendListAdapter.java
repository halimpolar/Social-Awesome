package cmpe.sjsu.socialawesome;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import cmpe.sjsu.socialawesome.Utils.DbUtils;
import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserSummary;

import static android.R.id.message;

/**
 * Created by bing on 5/13/17.
 */


public class FriendListAdapter extends RecyclerView.Adapter <FriendListAdapter.FriendViewHolder>{
    private List<String> entryList;

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
            public void execute(User user) {
                holder.vName.setText(user.first_name + " " + user.last_name);
                holder.vEmail.setText("Email:            " + user.email);
                holder.vLocation.setText("Location:      " + user.location);
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

        public FriendViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.profileName);
            vEmail = (TextView) v.findViewById(R.id.profileEmail);
            vLocation = (TextView) v.findViewById(R.id.profileLocation);

        }
    }

    public void updateList(List<String> newList) {
        entryList = newList;
        notifyDataSetChanged();
    }

//    public void addItem(CalendarEvent event) {
//        entryList.add(event);
//        Collections.sort(entryList, new DateComparator());
//        notifyDataSetChanged();
//    }

//    public class DateComparator implements Comparator<CalendarEvent> {
//        @Override
//        public int compare(CalendarEvent e1, CalendarEvent e2) {
//            return e1.startTime.compareTo(e2.startTime);
//        }
//    }
}


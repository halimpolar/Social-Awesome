package cmpe.sjsu.socialawesome;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import cmpe.sjsu.socialawesome.models.UserSummary;

/**
 * Created by bing on 5/13/17.
 */


public class FriendListAdapter extends RecyclerView.Adapter <FriendListAdapter.FriendViewHolder>{
    private List<UserSummary> entryList;

    public FriendListAdapter(List<UserSummary> entryList) {
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
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        UserSummary entry = entryList.get(position);
        holder.vName.setText(entry.first_name + " " + entry.last_name);
        holder.vNickName.setText("Nick Name:  " + entry.nick_name);
        holder.vEmail.setText("Email:            " + entry.email);
        holder.vLocation.setText("Location:      " + entry.location);
        holder.vProfession.setText("Profession:   " + entry.profession);
        //TODO: setimageurl: http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android

    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vEmail;
        protected TextView vNickName;
        protected TextView vLocation;
        protected TextView vProfession;

        public FriendViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.profileName);
            vEmail = (TextView) v.findViewById(R.id.profileEmail);
            vNickName = (TextView) v.findViewById(R.id.profileNickName);
            vLocation = (TextView) v.findViewById(R.id.profileLocation);
            vProfession = (TextView) v.findViewById(R.id.profileProfession);

        }
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


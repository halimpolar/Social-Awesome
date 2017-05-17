package cmpe.sjsu.socialawesome.adapters;

import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cmpe.sjsu.socialawesome.R;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {
    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView authorName;
        public ImageView profilePic;
        public TextView postContent;
        public ImageView postPic;
        public CardView card;

        public ViewHolder(View cardView) {
            super(cardView);
            card = (CardView)cardView.findViewById(R.id.card_view);
            authorName = (TextView)cardView.findViewById(R.id.timeline_name);
            profilePic = (ImageView)cardView.findViewById(R.id.timeline_pic);
            postContent = (TextView) cardView.findViewById(R.id.timeline_content);
            postPic = (ImageView)cardView.findViewById(R.id.timeline_attachment);
        }
    }

    public TimeLineAdapter(String[] dataset) {
        mDataset = dataset;
    }

    @Override
    public TimeLineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.authorName.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

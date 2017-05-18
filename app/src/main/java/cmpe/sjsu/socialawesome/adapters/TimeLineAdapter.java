package cmpe.sjsu.socialawesome.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cmpe.sjsu.socialawesome.R;
import cmpe.sjsu.socialawesome.models.Post;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {
    private ArrayList<Post> posts;

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

    public TimeLineAdapter(ArrayList<Post> posts) {
        this.posts = posts;
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
        holder.authorName.setText(posts.get(position).getAuthorName());
        holder.postContent.setText(posts.get(position).getAuthorName());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}

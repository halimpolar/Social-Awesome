package cmpe.sjsu.socialawesome;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cmpe.sjsu.socialawesome.adapters.TimeLineAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimeLineFragment extends SocialFragment {
    private RecyclerView mTimelineListView;
    private FloatingActionButton mAddNewPostBtnView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public TimeLineFragment() {
        mTitle = TimeLineFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_timeline, container, false);

        mTimelineListView = (RecyclerView) view.findViewById(R.id.timelineListView);
        mAddNewPostBtnView = (FloatingActionButton) view.findViewById(R.id.addNewPostBtn);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getContext());
        mTimelineListView.setLayoutManager(mLayoutManager);
        String[] test = new String[3];
        test[0] = "TEST 1";
        test[1] = "Test2";
        test[2] = "Sterling T";
        mAdapter = new TimeLineAdapter(test);
        mTimelineListView.setAdapter(mAdapter);
    }
}

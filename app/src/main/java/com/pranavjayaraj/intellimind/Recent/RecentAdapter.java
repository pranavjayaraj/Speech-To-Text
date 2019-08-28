package com.pranavjayaraj.intellimind.Recent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pranavjayaraj.intellimind.R;

import java.util.List;

/**
 * Created by kuttanz on 27/8/19.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.MyViewHolder> {

    private java.util.List<String> recentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button recent;

        public MyViewHolder(View view) {
            super(view);
            recent = (Button) view.findViewById(R.id.recent);

        }
    }


    public RecentAdapter(List<String> recentList) {
        this.recentList = recentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String recent = recentList.get(position);
        holder.recent.setText(recent);
    }

    @Override
    public int getItemCount() {
        return recentList.size();
    }
}

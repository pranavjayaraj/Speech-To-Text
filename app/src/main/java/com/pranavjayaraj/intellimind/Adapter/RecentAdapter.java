package com.pranavjayaraj.intellimind.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.pranavjayaraj.intellimind.R;
import com.pranavjayaraj.intellimind.UI.SearchActivity;

import java.util.List;

/**
 * Created by Pranav on 27/8/19.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.MyViewHolder> {

    private java.util.List<String> recentList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recent;

        public MyViewHolder(View view) {
            super(view);
            recent = (TextView) view.findViewById(R.id.recent);
            context =  view.getContext();
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
        final String recent = recentList.get(position);
        holder.recent.setText(recent);
        holder.recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchActivity = new Intent(context, SearchActivity.class);
                searchActivity.putExtra("query",recent);
                context.startActivity(searchActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentList.size();
    }
}

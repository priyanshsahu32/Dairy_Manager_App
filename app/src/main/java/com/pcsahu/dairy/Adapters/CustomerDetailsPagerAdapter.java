package com.pcsahu.dairy.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pcsahu.dairy.R;

import java.util.Date;
import java.util.List;

public class CustomerDetailsPagerAdapter extends RecyclerView.Adapter<CustomerDetailsPagerAdapter.ViewHolder> {

    private final Context context;
    private final List<String> noSupplyDates;
    private final List<String> extras;

    public CustomerDetailsPagerAdapter(Context context, List<String> noSupplyDates, List<String> extras) {
        this.context = context;
        this.noSupplyDates = noSupplyDates;
        this.extras = extras;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.page_customer_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.title.setText("No Supply Days");
            holder.listView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, noSupplyDates));
        } else {
            holder.title.setText("Extras");
            holder.listView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, extras));
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Two pages: No Supply Days and Extras
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ListView listView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvPageTitle);
            listView = itemView.findViewById(R.id.lvPageList);
        }
    }
}

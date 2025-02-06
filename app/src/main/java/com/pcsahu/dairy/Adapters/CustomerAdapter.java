package com.pcsahu.dairy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pcsahu.dairy.HomeActivity;
import com.pcsahu.dairy.Interfaces.RecycleViewClickListener;
import com.pcsahu.dairy.R;
import com.pcsahu.dairy.models.CustomerModel;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder>{



    final private RecycleViewClickListener clickListener;
    Context context;
    ArrayList<CustomerModel> arrayList;

    public CustomerAdapter(Context context, RecycleViewClickListener clickListener, ArrayList<CustomerModel> arrayList) {
        this.context = context;
        this.clickListener = clickListener;
        this.arrayList = arrayList;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from( context ).inflate( R.layout.customer_holder,parent,false );
        final MyViewHolder myViewHolder = new MyViewHolder( view );

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String customer_name = arrayList.get( position).getName();
        final String customer_pn = arrayList.get( position ).getPhNo();

        holder.customer_name.setText( customer_name );
        holder.customer_phNo.setText( customer_pn );
    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageButton editButton, deleteButton,downarrow;
        TextView customer_name, customer_phNo;
        LinearLayout ll;

        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
            downarrow = itemView.findViewById( R.id.dropdown_arrow );
            editButton = itemView.findViewById( R.id.edit_customer );
            deleteButton = itemView.findViewById( R.id.delete_customer );
            customer_name = itemView.findViewById( R.id.customer_name );
            customer_phNo = itemView.findViewById( R.id.customer_phNo );
            ll = itemView.findViewById( R.id.buttons_layout );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick( getAdapterPosition() );
                }
            } );

            itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    clickListener.onLongItemClick( getAdapterPosition() );
                    return true;
                }
            } );

            editButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onEditButtonClick( getAdapterPosition() );
                }
            } );

            deleteButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onDeleteButtonClick( getAdapterPosition() );
                }
            } );
            downarrow.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(ll.getVisibility()==View.VISIBLE){
                        ll.setVisibility( View.GONE );
                    }else{
                        ll.setVisibility( View.VISIBLE);
                    }

                }
            } );


        }
    }

}
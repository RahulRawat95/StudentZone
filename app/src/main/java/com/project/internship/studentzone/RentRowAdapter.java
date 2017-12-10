package com.project.internship.studentzone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rahul on 17-Sep-17.
 */

public class RentRowAdapter extends RecyclerView.Adapter<RentRowAdapter.MyViewHolder> {
    private List<String> mRent;
    private List<String> mDeposit;

    public RentRowAdapter(List<String> mRent, List<String> mDeposit) {
        this.mRent = mRent;
        this.mDeposit = mDeposit;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rent, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.sharingNoTextView.setText(position + 1 + " sharing");
        holder.rentTextView.setText(mRent.get(position));
        holder.depositTextView.setText(mDeposit.get(position));
    }

    @Override
    public int getItemCount() {
        return mRent.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_rent_sharing_no_textview)
        public TextView sharingNoTextView;

        @BindView(R.id.row_rent_rent_textview)
        public TextView rentTextView;

        @BindView(R.id.row_rent_deposit_textview)
        public TextView depositTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
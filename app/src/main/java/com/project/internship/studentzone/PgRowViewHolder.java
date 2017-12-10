package com.project.internship.studentzone;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Rahul on 17-Sep-17.
 */

public class PgRowViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView, addressTextview, sharingCountTextview, startRentTextview;
    public ImageView genderImageview, starImageview;
    public LinearLayout callLinearLayout, favouriteLinearLayout, viewMapLinearLayout;
    public CardView cardview;


    public PgRowViewHolder(View itemView) {
        super(itemView);
        cardview = itemView.findViewById(R.id.row_pg_cardview);
        nameTextView = itemView.findViewById(R.id.row_pg_name_textview);
        addressTextview = itemView.findViewById(R.id.row_pg_short_address_textview);
        sharingCountTextview = itemView.findViewById(R.id.row_pg_sharing_count_textview);
        startRentTextview = itemView.findViewById(R.id.row_pg_start_rent_textview);
        genderImageview = itemView.findViewById(R.id.row_pg_gender_imageview);
        starImageview = itemView.findViewById(R.id.row_pg_favourite_star_imageview);
        callLinearLayout = itemView.findViewById(R.id.row_pg_call_linear_layout);
        favouriteLinearLayout = itemView.findViewById(R.id.row_pg_favourite_linear_layout);
        viewMapLinearLayout = itemView.findViewById(R.id.row_pg_view_map_linear_layout);
    }
}

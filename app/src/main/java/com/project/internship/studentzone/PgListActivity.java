package com.project.internship.studentzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rahul on 17-Sep-17.
 */

public class PgListActivity extends AppCompatActivity {
    public static final String PG_STRING = "PgString";
    public static final String SHORT_ADDRESS = "PgShortAddress";

    private static final int CALL_PERMISSION = 1;
    private PgModel model;

    @BindView(R.id.activity_pg_list_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_pg_list_recycler_view)
    RecyclerView mRecyclerView;

    protected DatabaseReference mFirebaseDatabaseReference;
    protected FirebaseRecyclerAdapter<PgModel, PgRowViewHolder> mFirebaseAdapter;
    private String mPgString;
    private boolean mStarOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_list);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setSupportActionBar(mToolbar);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // TODO: 17-Sep-17 change to shared preference

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mPgString = prefs.getString(PG_STRING, null);


        mFirebaseAdapter = new FirebaseRecyclerAdapter<PgModel, PgRowViewHolder>(PgModel.class, R.layout.row_pg, PgRowViewHolder.class, mFirebaseDatabaseReference.child("PG").child(mPgString)) {

            @Override
            protected PgModel parseSnapshot(DataSnapshot snapshot) {
                PgModel pgModel = super.parseSnapshot(snapshot);
                pgModel.setKey(snapshot.getKey());
                return pgModel;
            }

            @Override
            protected void populateViewHolder(final PgRowViewHolder viewHolder, final PgModel model, int position) {
                viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PgListActivity.this, PgDetailsActivity.class);
                        intent.putExtra(PgDetailsActivity.PG_DETAILS, model);
                        startActivity(intent);
                    }
                });

                viewHolder.nameTextView.setText(model.getName());

                Glide.with(PgListActivity.this).load(model.getSex().equals("Male") ? R.drawable.male : R.drawable.female).into(viewHolder.genderImageview);

                viewHolder.addressTextview.setText(prefs.getString(SHORT_ADDRESS, null));

                StringBuilder br = new StringBuilder().append("Available Sharing: ");
                int i = Integer.parseInt(model.getRentalSharingCount());

                for (int j = 1; j <= i; ) {
                    br.append(j);
                    j++;
                    if (j <= i)
                        br.append(", ");
                }

                viewHolder.sharingCountTextview.setText(br.toString());

                viewHolder.startRentTextview.setText("Starting from Rs " + model.getRental().get(0));

                viewHolder.callLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkPermission(android.Manifest.permission.CALL_PHONE)) {
                            PgListActivity.this.model = model;
                            requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getContact()));
                            startActivity(intent);
                        }
                    }
                });

                viewHolder.favouriteLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: 17-Sep-17 save to favourite json here
                        //Store key and pgString here
                        //and check against json if this is marked favourite
                        //and remove favourite here too
                        //if(isFavoirite())
                        if (!mStarOn)
                            viewHolder.starImageview.setImageResource(R.drawable.ic_star_on_vector);
                        else
                            viewHolder.starImageview.setImageResource(R.drawable.ic_star_off_vector);
                        mStarOn = !mStarOn;
                    }
                });

                viewHolder.viewMapLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(model.getAddress()));
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                });

            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mFirebaseAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPermission(String permission) {
        return (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CALL_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPermission(android.Manifest.permission.CALL_PHONE)) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getContact()));
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please Give Permission...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                break;
        }
    }
}

package com.project.internship.studentzone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rahul on 17-Sep-17.
 */

public class PgDetailsActivity extends AppCompatActivity {

    public static final String PG_DETAILS = "detailsOfPg";

    private static final int CALL_PERMISSION = 1;

    private PgModel mModel;
    private RentRowAdapter mRentRowAdapter;

    @BindView(R.id.activity_pg_details_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_pg_details_pg_imageview)
    ImageView mPgImageview;

    @BindView(R.id.activity_pg_details_address_textview)
    TextView mAddressTextview;

    @BindView(R.id.activity_pg_details_call_imageview)
    ImageView mCallImageView;

    @BindView(R.id.activity_pg_details_contact_no_textview)
    TextView mContactNoTextview;

    @BindView(R.id.activity_pg_details_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_details);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setSupportActionBar(mToolbar);

        mModel = (PgModel) getIntent().getSerializableExtra(PG_DETAILS);

        mToolbar.setTitle(mModel.getName());

        Glide.with(PgDetailsActivity.this).load(mModel.getImageUrl()).into(mPgImageview);

        mAddressTextview.setText(mModel.getAddress());

        mCallImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkPermission(android.Manifest.permission.CALL_PHONE)) {
                    requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mModel.getContact()));
                    startActivity(intent);
                }
            }
        });

        mContactNoTextview.setText("Phone: " + mModel.getContact());

        mRentRowAdapter = new RentRowAdapter(mModel.getRental(), mModel.getDeposit());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRentRowAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_pg_details_activity, menu);
        menu.findItem(R.id.action_gender_image).setIcon(((PgModel) getIntent().getSerializableExtra(PG_DETAILS)).getSex().equals("Male") ? R.drawable.male : R.drawable.female);
        return true;
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
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mModel.getContact()));
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

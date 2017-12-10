package com.project.internship.studentzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREF = "MyPrefs";

    private DatabaseReference mFirebaseDatabaseReference;

    private Model mStateModel, mCityModel, mLocalityModel;

    private ArrayAdapter<Model> mAutoCompleteAdapter;

    @BindView(R.id.activity_main_locality_textview)
    TextView mCityTextView;

    @BindView(R.id.activity_main_state_textview)
    TextView mStateTextView;

    @BindView(R.id.activity_main_locality_imageview)
    ImageView mCityImageView;

    @BindView(R.id.activity_main_state_imageview)
    ImageView mStateImageView;

    @BindView(R.id.activity_main_locality_cardview)
    CardView mCityCardView;

    @BindView(R.id.activity_main_state_cardview)
    CardView mStateCardView;

    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;

    AutoCompleteTextView mAutoComplete;
    Boolean mCitySelected = false, mStateSelected = false;
    String mCityString = null, mLocalityString = null, mPgString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mAutoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        //Child the root before all the push() keys are found and add a ValueEventListener()

        mStateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCitySelected) {
                    mCitySelected = false;
                    mCityCardView.setVisibility(View.GONE);
                }
                mStateSelected = false;
                mStateCardView.setVisibility(View.GONE);
                mAutoComplete.setText("");
                setStateAdapter();
            }
        });

        mCityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCitySelected = false;
                mCityCardView.setVisibility(View.GONE);
                mAutoComplete.setText("");
                setCityAdapter(mStateModel.getKey());
            }
        });
    }

    private void addModelToAdapter(String child, String s) {
        mAutoCompleteAdapter.clear();

        DatabaseReference databaseReference = mFirebaseDatabaseReference.child(child);
        if (s != null)
            databaseReference = databaseReference.child(s);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                    //Get the suggestion by childing the key of the string you want to get.
                    Model model = new Model(suggestionSnapshot.child("key").getValue(String.class), suggestionSnapshot.child("name").getValue(String.class));
                    //Add the retrieved string to the list
                    mAutoCompleteAdapter.add(model);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAutoComplete.setAdapter(mAutoCompleteAdapter);
    }

    private void setStateAdapter() {

        addModelToAdapter("states", null);

        mAutoComplete.setHint("Enter State...");

        mAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Model model = mAutoCompleteAdapter.getItem(i);
                mAutoComplete.setText(model.toString());
                mAutoComplete.dismissDropDown();

                mStateModel = model;

                setCityAdapter(mStateModel.getKey());

                mStateSelected = true;

                mStateCardView.setVisibility(View.VISIBLE);
                mStateTextView.setText(model.getName());

                mAutoComplete.setText("");
            }
        });
    }

    private void setCityAdapter(int i) {
        mCityString = "s" + i;

        mAutoComplete.setHint("Enter City in " + mStateModel.getName());

        addModelToAdapter("cities", mCityString);

        mAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Model model = mAutoCompleteAdapter.getItem(i);
                mAutoComplete.setText(model.toString());
                mAutoComplete.dismissDropDown();

                mCityModel = model;

                setLocalityAdapter(mCityString, mCityModel.getKey());

                mCitySelected = true;

                mCityCardView.setVisibility(View.VISIBLE);
                mCityTextView.setText(model.getName());

                mAutoComplete.setText("");
            }
        });
    }

    private void setLocalityAdapter(String cityString, int i) {
        mLocalityString = cityString + "c" + i;

        mAutoComplete.setHint("Enter Locality in " + mCityModel.getName());

        addModelToAdapter("localities", mLocalityString);

        mAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Model model = mAutoCompleteAdapter.getItem(i);
                mLocalityModel = model;
                mAutoComplete.setText(mLocalityModel.toString());
                mAutoComplete.dismissDropDown();

                mPgString = mLocalityString + "l" + mLocalityModel.getKey();

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(PgListActivity.PG_STRING, mPgString);
                editor.putString(PgListActivity.SHORT_ADDRESS, mLocalityModel.getName() + ", " + mCityModel.getName());
                editor.commit();

                startActivity(new Intent(MainActivity.this, PgListActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                item.expandActionView();
                mAutoComplete = (AutoCompleteTextView) item.getActionView();
                mAutoComplete.setWidth(mToolbar.getWidth());
                if (!mStateSelected)
                    setStateAdapter();
                else if (!mCitySelected)
                    setCityAdapter(mStateModel.getKey());
                else
                    setLocalityAdapter(mCityString, mCityModel.getKey());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

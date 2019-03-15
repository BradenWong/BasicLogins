package com.example.basiclogins;

import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText editTextname;
    private EditText editTextcuisine;
    private EditText editTextaddress;
    private EditText editTextwebsite;
    private RatingBar ratingBat;
    private Button save;
    private Spinner spinner;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        wireWidgets();
        prefillFields();
        setOnClickListener();

    }

    private void prefillFields() {
        Intent restaurantIntent = getIntent();
        restaurant = restaurantIntent.getParcelableExtra(RestaurantListActivity.EXTRA_RESTAURANT);
        if(restaurant!= null){
            editTextname.setText(restaurant.getName());
            editTextcuisine.setText(restaurant.getCuisine());
            editTextaddress.setText(restaurant.getAddress());
            editTextwebsite.setText(restaurant.getWebsiteLink());
            ratingBat.setRating((float) restaurant.getRating());
            spinner.setSelection(restaurant.getPrice());

        }
   }

    private void setOnClickListener() {
            save.setOnClickListener(this);
        }


    private void wireWidgets() {
        editTextname = findViewById(R.id.editText_restaurantActivity_name);
        editTextcuisine = findViewById(R.id.editText_restaurantActivity_cuisine);
        editTextaddress = findViewById(R.id.editText_restaurantActivity_address);
        editTextwebsite = findViewById(R.id.editText_restarantActivity_website);
        ratingBat = findViewById(R.id.ratingBar_restaurantActivity_rating);
        save = findViewById(R.id.button_restaurantActivity_save);

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("$");
        categories.add("$$");
        categories.add("$$$");
        categories.add("$$$$");
        categories.add("$$$$$");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    private void SaveOnBackendless() {




        String name = editTextname.getText().toString();
        String address= editTextaddress.getText().toString();
        String cuisine =     editTextcuisine.getText().toString();
        String websiteLink = editTextwebsite.getText().toString();
        double rating = (float) ratingBat.getRating();
        int price  = spinner.getSelectedItemPosition();
            if (restaurant == null){
                restaurant = new Restaurant();
            }
        restaurant.setName(name);
        restaurant.setCuisine(cuisine);
        restaurant.setAddress(address);
        restaurant.setRating(rating);
        restaurant.setWebsiteLink(websiteLink);
        restaurant.setPrice(price);



        Backendless.Persistence.save(restaurant, new AsyncCallback<Restaurant>() {
            @Override
            public void handleResponse(Restaurant response) {
                Toast.makeText(RestaurantActivity.this, "I worked", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(RestaurantActivity.this, "U DID SOMETHING WRONG" + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button_restaurantActivity_save:
                        SaveOnBackendless();
//                        Intent closeRestaurantIntent = new Intent(RestaurantActivity.this,
//                                RestaurantListActivity.class);
//                        startActivity(closeRestaurantIntent);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
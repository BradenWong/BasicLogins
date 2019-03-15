package com.example.basiclogins;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {
    public static final String EXTRA_RESTAURANT = "";
    private ListView listViewRestaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        wireWidgets();
        //populateListView();
        //get a reference to the view for pressing
        //register if for context

        registerForContextMenu(listViewRestaurant);
        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click.
                Intent createRestaurantIntent = new Intent(RestaurantListActivity.this,
                        RestaurantActivity.class);
                startActivity(createRestaurantIntent);
                //finish();
            }
        });

    }

    private void populateListView() {
        String ownerId = Backendless.UserService.CurrentUser().getObjectId();
        String whereClause = "ownerId = '" + ownerId + "'"; //this line isn't complete
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause( whereClause );


        Backendless.Data.of( Restaurant.class).find(queryBuilder, new AsyncCallback<List<Restaurant>>(){
            @Override
            public void handleResponse(final List<Restaurant> restaurantList )
            {
                Log.d("RESTARAUNTS: ", "handleResponse: " + restaurantList.toString());
                // all Restaurant instances have been found
                RestaurantAdapter adapter = new RestaurantAdapter(
                        RestaurantListActivity.this,
                        R.layout.item_restaurantlist,
                        restaurantList);
                listViewRestaurant.setAdapter(adapter);
//                set the onItemClickListener to open the Restaurant Activity
                listViewRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent restaurantDetailIntent = new Intent(RestaurantListActivity.this, RestaurantActivity.class);
                        restaurantDetailIntent. putExtra(EXTRA_RESTAURANT, restaurantList.get(position));
                        startActivity(restaurantDetailIntent);
                       // finish();
                    }
                });
                //take the clicked object and include it in the Intent
                //in the RestaurantActivity's onCreate, check it there is a Parcelable extra
                //if there is, then get the Restaurant object and populate the fields

            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(RestaurantListActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();

                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.regmenu, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {
        //find out which menu_delete item was pressed
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.option1:
                Restaurant restaurant = (Restaurant) listViewRestaurant.getItemAtPosition(info.position);
                deleteRestaurant(restaurant);
                return true;
            default:
                return false;
        }
    }


    private void deleteRestaurant(Restaurant restaurant) {
        Backendless.Persistence.of(Restaurant.class ).remove(restaurant, new AsyncCallback<Long>()
        {
            public void handleResponse( Long response )
            {
                // Contact has been deleted. The response is the
                // time in milliseconds when the object was deleted
                populateListView();
            }
            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be
                // retrieved with fault.getCode()
                Toast.makeText(RestaurantListActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } );
    }

    //method to execute when option one is chosen
    private void doOptionOne() {
        Toast.makeText(this, "Option One Chosen...", Toast.LENGTH_LONG).show();
    }

    private void wireWidgets() {
        listViewRestaurant = findViewById(R.id.listview_restaurantlist);
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateListView();
    }
}

package com.example.tara.restaurant;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    List<String> foodPlatters = new ArrayList<>();
    JSONObject c;
    List<String> yourOrder = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Context context = MenuActivity.this;
                    Class destinationActivity = MainActivity.class;
                    startActivity(new Intent(context, destinationActivity));
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(MenuActivity.this, YourOrderActivity.class));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        final String course = intent.getStringExtra("received_text");

        // bottom navigation buttons
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // put info into strings
        final ArrayAdapter mAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, foodPlatters);

        ListView mListView = (ListView) findViewById(R.id.food_courses);

        mListView.setAdapter(mAdapter);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://resto.mprog.nl/menu";

        // request string response from url
        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray courses = response.getJSONArray("items");

                            //foodCourses = new String[courses.length()];
                            for (int i = 0; i < courses.length(); i++) {

                                c = courses.getJSONObject(i);
                                String sCategory = c.getString("category");

                                if (sCategory.equals(course)){

                                    String sName = c.getString("name");
                                    foodPlatters.add(sName);
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(MenuActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MenuActivity.this, "Catch", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MenuActivity.this, "Not connected to the internet", Toast.LENGTH_SHORT).show();
            }
        });

        // add request to RequestQueue
        queue.add(stringRequest);

        mListView.setOnItemClickListener(new
             AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                     String given_text = String.valueOf(adapterView.getItemAtPosition(position));
                     String foodCoursePicked = "You selected " + given_text;

                     Toast.makeText(MenuActivity.this, foodCoursePicked, Toast.LENGTH_SHORT).show();

                     Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
                     intent.putExtra("received_text", given_text);
                     intent.putExtra("button_text", "Submit");
                     startActivity(intent);
                 }
             });
    }
}

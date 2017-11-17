package com.example.tara.restaurant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity {

    List<String> yourOrder = new ArrayList<>();

    List<String> foodCourses = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Context context = MainActivity.this;
                    Class destinationActivity = MainActivity.class;
                    startActivity(new Intent(context, destinationActivity));
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(MainActivity.this, YourOrderActivity.class));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadSharedPrefs();

        setContentView(R.layout.activity_main2);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final ArrayAdapter mAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, foodCourses);

        ListView mListView = (ListView) findViewById(R.id.food_courses);

        mListView.setAdapter(mAdapter);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://resto.mprog.nl/categories";

        // request string response from url
        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray courses = response.getJSONArray("categories");

                            //foodCourses = new String[courses.length()];
                            for (int i = 0; i < courses.length(); i++) {

                                // JSONObject course = courses.getJSONObject(i);
                                String sCourse = courses.getString(i);

                                foodCourses.add(sCourse);
                                //System.out.println(foodCourses);
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        // add request to RequestQueue
        queue.add(stringRequest);

        mListView.setOnItemClickListener(new
           AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
               {
                   String given_text = String.valueOf(adapterView.getItemAtPosition(position));
                   String foodCoursePicked = "You selected " +
                           given_text;

                   Toast.makeText(MainActivity.this, foodCoursePicked, Toast.LENGTH_SHORT).show();

                   Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                   intent.putExtra("received_text", given_text);
                   startActivity(intent);
               }
           });
    }

    private void loadSharedPrefs() {
        SharedPreferences prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);

        int size = prefs.getInt("size", 0);
        for (int i = 0; i < size; i++) {
            String yourOrderStored = prefs.getString("yourOrder" + i, null);
            if (yourOrderStored != null) {
                yourOrder.add(yourOrderStored);
            }
        }
    }
}

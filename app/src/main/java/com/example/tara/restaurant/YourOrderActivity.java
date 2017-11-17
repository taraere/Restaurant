package com.example.tara.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class YourOrderActivity extends AppCompatActivity {

    String receivedName;
    List<String> yourOrder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        receivedName = intent.getStringExtra("received_text");

        loadSharedPrefs();

        final ArrayAdapter mAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, yourOrder);

        ListView mListView = (ListView) findViewById(R.id.food_courses);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new
                                                 AdapterView.OnItemClickListener() {
                                                     @Override
                                                     public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                 String given_text = String.valueOf(adapterView.getItemAtPosition(position));
                 String foodCoursePicked = "You selected " + given_text;

                 Toast.makeText(YourOrderActivity.this, foodCoursePicked, Toast.LENGTH_SHORT).show();

                 Intent intent = new Intent(YourOrderActivity.this, InfoActivity.class);
                 intent.putExtra("received_text", given_text);
                 intent.putExtra("button_text", "Delete");
                 startActivity(intent);
             }
         });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(YourOrderActivity.this, MainActivity.class));
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

    private void saveToSharedPrefs() {
        SharedPreferences prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int size = yourOrder.size();

        editor.putInt("size", size);
        for (int i = 0; i < size; i++) {
            editor.putString("yourOrder" + i, yourOrder.get(i));
        }

        editor.commit();
    }

    public void clickedOrder(View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://resto.mprog.nl/order";

        // request string response from url
        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String time = response.getString("preparation_time");
                            Toast.makeText(YourOrderActivity.this, time, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(YourOrderActivity.this, "Catch", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(YourOrderActivity.this, "Not connected to the internet", Toast.LENGTH_SHORT).show();
            }
        });

        // add request to RequestQueue
        queue.add(stringRequest);
    }
}

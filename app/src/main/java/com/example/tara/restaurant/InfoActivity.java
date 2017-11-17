package com.example.tara.restaurant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    List<String> yourOrder = new ArrayList<>();
    String sName;
    TextView output;
    String receivedName;
    Button button;
    RequestQueue queue;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Context context = InfoActivity.this;
                    Class destinationActivity = MainActivity.class;
                    startActivity(new Intent(context, destinationActivity));
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(InfoActivity.this, YourOrderActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        output = (TextView) findViewById(R.id.textView2);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        receivedName = intent.getStringExtra("received_text");

        final String buttonText = intent.getStringExtra("button_text");
        button = (Button) findViewById(R.id.button);
        button.setText(buttonText);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadSharedPrefs();
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);
        String url = "https://resto.mprog.nl/menu";

        // request string response from url
        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray courses = response.getJSONArray("items");
                            //yourOrder = new String[courses.length()];
                            for (int i = 0; i < courses.length(); i++) {
                                JSONObject c = courses.getJSONObject(i);
                                sName = c.getString("name");

                                if (sName.equals(receivedName)) {
                                    String sDescription = c.getString("description");
                                    String sPrice = c.getString("price");
                                    String image = c.getString("image_url");
                                    output.setText(sName + "\n\n" + sDescription + "\n\n $" + sPrice);
                                    imageGet(image, imageView);
                                }
                            }
                        } catch (JSONException e) {
                            output.setText("catch");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InfoActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        // add request to RequestQueue
        queue.add(stringRequest);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (button.getText().equals("Submit")) {
                    yourOrder.add(receivedName);
                    String foodCoursePicked = "You selected " + receivedName;
                    Toast.makeText(InfoActivity.this, foodCoursePicked, Toast.LENGTH_SHORT).show();
                }
                else if (button.getText().equals("Delete")){
                    yourOrder.remove(receivedName);
                    String foodCoursePicked = "You deleted " + receivedName;
                    Toast.makeText(InfoActivity.this, foodCoursePicked, Toast.LENGTH_SHORT).show();
                }
                saveToSharedPrefs();
                Intent intent = new Intent(InfoActivity.this, YourOrderActivity.class);
                intent.putExtra("received_text", sName);
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

    private void imageGet(String img, final ImageView imgV) {
        ImageRequest imageRequest = new ImageRequest(img, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imgV.setImageBitmap(bitmap);
            }

        }, 0, 0, null, Bitmap.Config.ALPHA_8,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InfoActivity.this, "Loading image failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(imageRequest);
    }
}

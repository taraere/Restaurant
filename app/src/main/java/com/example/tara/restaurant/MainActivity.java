package com.example.tara.restaurant;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> title_array = new ArrayList<>();
    ArrayList<String> notice_array = new ArrayList<>();

    ListView list;
//    BaseAdapter2 adapter;

    TextView mTextView;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_your_order);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // newRequestQueue
        mTextView = (TextView) findViewById(R.id.textView);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        new Task().execute();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://resto.mprog.nl/menu";

        // request string response from url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // display first five characters
                mTextView.setText("Response: " + response.substring(0,5));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That's not worked");
            }
        });

        // add request to RequestQueue

        queue.add(stringRequest);

        String[] foodCourses = {
                "Entrées", "Mains", "Deserts"};

        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                foodCourses);

        ListView theListView = (ListView) findViewById(R.id.food_courses);

        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new
                                                   AdapterView.OnItemClickListener() {
                                                       @Override
                                                       public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                                                           String foodCoursePicked = "You selected " +
                                                                   String.valueOf(adapterView.getItemAtPosition(position));

                                                           Toast.makeText(MainActivity.this, foodCoursePicked, Toast.LENGTH_SHORT).show();
                                                       }
                                                   });
    }
}


























//
//
//
//            class Task extends AsyncTask<Void, Void, String> {
//
//                @Override
//                protected String doInBackground(Void... params) {
//                    String str = null;
//                    try {
//                        HttpClient httpclient = new DefaultHttpClient();
//                        HttpPost httppost = new HttpPost(
//                                "http://10.0.2.2/BSDI/show.php");
//                        HttpResponse response = httpclient.execute(httppost);
//                        str = EntityUtils.toString(response.getEntity());
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                    return str;
//        }
//    }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            String response = result.toString();
//            try {
//
//
//                JSONArray new_array = new JSONArray(response);
//
//                for (int i = 0, count = new_array.length(); i < count; i++) {
//                    try {
//                        JSONObject jsonObject = new_array.getJSONObject(i);
//                        title_array.add(jsonObject.getString("title").toString());
//                        notice_array.add(jsonObject.getString("notice").toString());
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                adapter = new BaseAdapter2(MainActivity.this, title_array, notice_array);
//                list.setAdapter(adapter);
//
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                // tv.setText("error2");
//            }
//
//        }
//    }
//
//public static class BaseAdapter2 extends BaseAdapter {
//
//        private Activity activity;
//        // private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
//        private static ArrayList title, notice;
//        private static LayoutInflater inflater = null;
//
//        public BaseAdapter2(Activity a, ArrayList b, ArrayList bod) {
//            activity = a;
//            this.title = b;
//            this.notice = bod;
//
//            inflater = (LayoutInflater) activity
//                    .getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        }
//
//        public int getCount() {
//            return title.size();
//        }
//
//        public Object getItem(int position) {
//            return position;
//        }
//
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View vi = convertView;
//            if (convertView == null)
//                vi = inflater.inflate(R.layout.list_view, null);
//
//            TextView title2 = vi.findViewById(R.id.atitle); // title
//            String song = title.get(position).toString();
//            title2.setText(song);
//
//
//            TextView title22 = vi.findViewById(R.id.anotice); // notice
//            String song2 = notice.get(position).toString();
//            title22.setText(song2);
//
//            return vi;
//
//        }
//    }
//

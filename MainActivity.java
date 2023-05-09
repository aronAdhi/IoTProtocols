package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    TextView data;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String currentTime = getCurrentTimeAsString();


        Context context = this;
        data = findViewById(R.id.textView);
        url = "http://172.20.10.10:8085/requests";
        String[] jsonString = new String[1];
        jsonString[0] = "[{\"Id\":26,\"Time\":\"2023-05-05T10:54:11.000Z\",\"Destination\":1,\"Num_of_people\":2}]";

        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a JSON response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the JSON string in a TextView.
//                        TextView textView = findViewById(R.id.textView);
//                        textView.setText(response.toString());
                        data.setText("data fetched at " + currentTime);
                        jsonString[0] = response.toString();


                        try {
                            //jsonString[0] = "[{\"Id\":26,\"Time\":\"2023-05-05T10:54:11.000Z\",\"Destination\":1,\"Num_of_people\":2},{\"Id\":25,\"Time\":\"2023-05-05T10:54:00.000Z\",\"Destination\":1,\"Num_of_people\":3},{\"Id\":24,\"Time\":\"2023-05-05T10:53:42.000Z\",\"Destination\":2,\"Num_of_people\":31},{\"Id\":23,\"Time\":\"2023-05-05T10:53:14.000Z\",\"Destination\":2,\"Num_of_people\":7},{\"Id\":22,\"Time\":\"2023-05-05T10:53:06.000Z\",\"Destination\":2,\"Num_of_people\":12},{\"Id\":21,\"Time\":\"2023-05-05T10:52:54.000Z\",\"Destination\":2,\"Num_of_people\":21},{\"Id\":20,\"Time\":\"2023-05-05T10:52:42.000Z\",\"Destination\":1,\"Num_of_people\":10},{\"Id\":19,\"Time\":\"2023-05-05T10:52:21.000Z\",\"Destination\":2,\"Num_of_people\":23}]";

                            JSONArray jsonArray = new JSONArray(jsonString[0]);

                            // Create a list of objects that represent each item in the JSON array
                            List<JSONObject> jsonObjects = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                jsonObjects.add(jsonObject);
                            }

                            // Now you can use this list to create a CardView for each item
                            LinearLayout linearLayout = findViewById(R.id.parent_layout);
                            for (JSONObject jsonObject : jsonObjects) {
                                CardView cardView = new CardView(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(10, 20, 10, 0);
                                cardView.setLayoutParams(params);

                                // Set the card elevation (shadow)
                                cardView.setCardElevation(8);
                                // Set the card corner radius
                                cardView.setRadius(16);

                                String timeStr = jsonObject.getString("Time");
                                String formattedDate = timeStr;

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                                try {
                                    Date date = dateFormat.parse(timeStr);
                                    // convert the date to a formatted string using another date format
                                    SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    formattedDate = displayFormat.format(date);
                                    System.out.println("Formatted date: " + formattedDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String dest = null;
                                if(jsonObject.getInt("Destination") == 1)
                                    dest = "Lanka";
                                else
                                    dest = "Hyderabad Gate";


                                TextView textView = new TextView(context);
                                textView.setTextColor(Color.BLACK);
                                textView.setText("Id: " + jsonObject.getInt("Id") +
                                        "\nTime: " + formattedDate +
                                        "\nDestination: " + dest +
                                        "\nNumber of people: " + jsonObject.getInt("Num_of_people"));
                                textView.setPadding(20, 20, 20, 20);
                                textView.setBackgroundColor(Color.parseColor("#add8e6")); // Set the background color to green

                                cardView.addView(textView);
                                linearLayout.addView(cardView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("VOLLEY_ERROR", error.toString());
                data.setText(error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);

    }

    private String getCurrentTimeAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return dateFormat.format(now);
    }

}
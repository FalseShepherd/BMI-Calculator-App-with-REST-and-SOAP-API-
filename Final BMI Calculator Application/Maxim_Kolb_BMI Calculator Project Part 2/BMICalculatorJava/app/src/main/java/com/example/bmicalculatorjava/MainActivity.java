package com.example.bmicalculatorjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView bmiR;
    TextView healthR;
    TextView HelpSites;
    Button buttonCalc;
    Button buttonInfo;
    EditText weight;
    EditText height;
    String [] urls= {"https://www.cdc.gov/healthyweight/assessing/bmi/index.html","https://www.nhlbi.nih.gov/health/educational/lose_wt/index.htm","https://www.ucsfhealth.org/education/body_mass_index_tool/"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bmiR = findViewById(R.id.BMIResults);
        healthR = findViewById(R.id.HealthResults);
        buttonCalc= findViewById(R.id.CalculateButton);
        buttonInfo = findViewById(R.id.MoreInfoButton);
        weight = (EditText) findViewById(R.id.WeightTextBox);
        height = (EditText) findViewById(R.id.HeightTextBox);

        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view);
            }
        });

        buttonCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse(Integer.parseInt(weight.getText().toString()), Integer.parseInt(height.getText().toString()));
            }
        });
    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        TextView h = (TextView) popupView.findViewById(R.id.HelpSites);

        String urlString = "";
        for (int i = 0; i < urls.length; i++){
            urlString+= urls[i]+"\n";
        }

        h.setText(urlString);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }


    private void jsonParse(int h, int w){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://localhost:44314/api/BMICalculator/" + "MyHealth?height="+ h + "&weight=" + w;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject bmi = response.getJSONObject("bmi");
                            JSONObject risk = response.getJSONObject("risk");
                            JSONArray more = response.getJSONArray("more");
                            for(int i = 0; i < more.length(); i++){
                                urls[i] = (String)more.get(i);
                            }
                            bmiR.setText(String.valueOf(bmi));
                            healthR.setText(String.valueOf(risk));

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO: Handle error
                        error.printStackTrace();
                    }

                });

        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
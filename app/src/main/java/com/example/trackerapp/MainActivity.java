package com.example.trackerapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    Button getlocationBtn;
    TextView showLocationTxt;

    LocationManager locationManager;
    String latitude, longitude;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Adding permission
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        showLocationTxt = findViewById(R.id.result);
        getlocationBtn = findViewById(R.id.buttonStart);

        getlocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                //check if gps is anable or not
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //enable gps here

                    OnGPS();
                } else {
                    // GPS is ON
                    getLocation();
                    setDataToSrever();
                }
            }
        });
    }

    private void setDataToSrever() {
        // new PostDataAsyncTask().execute();

        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String res = jsonObject.getString("res");
                    Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();


                }
            } , new Response.ErrorListener

        }*/
        String url="192.168.3.169/script.php";
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Database_Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String > map=new HashMap<>();
                map.put("name","Abdullah");
                return map;
            }

        };
        queue.add(request);

    }




private void sendAndRequestResponse(){

        //RequestQueue initialized
        mRequestQueue=Volley.newRequestQueue(this);
        String url="192.168.3.169";

        //String Request initialized
        mStringRequest=new StringRequest(Request.Method.GET,url,new Response.Listener<String>(){
@Override
public void onResponse(String response){

        Toast.makeText(getApplicationContext(),"Response :"+response.toString(),Toast.LENGTH_LONG).show();//display the response on screen

        }
        },new Response.ErrorListener(){
@Override
public void onErrorResponse(VolleyError error){

        Log.i("","Error :"+error.toString());
        }
        });

        mRequestQueue.add(mStringRequest);
        }

private void getLocation(){

        //Check permission again
        if(ActivityCompat.checkSelfPermission(MainActivity.this,ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(MainActivity.this,
        Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this,new String[]
        {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else{
        Location LocationGps=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if(LocationGps!=null){
        double lat=LocationGps.getLatitude();
        double longi=LocationGps.getLongitude();

        latitude=String.valueOf(lat);
        longitude=String.valueOf(longi);

        showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+
        latitude+"\n"+"Longitude= "+longitude);

        }else if(LocationNetwork!=null){
        double lat=LocationNetwork.getLatitude();
        double longi=LocationNetwork.getLongitude();

        latitude=String.valueOf(lat);
        longitude=String.valueOf(longi);

        showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+
        latitude+"\n"+"Longitude= "+longitude);

        }else if(LocationPassive!=null){
        double lat=LocationPassive.getLatitude();
        double longi=LocationPassive.getLongitude();

        latitude=String.valueOf(lat);
        longitude=String.valueOf(longi);

        showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+
        latitude+"\n"+"Longitude= "+longitude);

        }else{
        Toast.makeText(this,"Unable to get your location",Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
        }
        }
        }

private void OnGPS(){
final AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES",new DialogInterface.OnClickListener(){
@Override
public void onClick(DialogInterface dialog,int which){
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        }).setNegativeButton("NO",new DialogInterface.OnClickListener(){
@Override
public void onClick(DialogInterface dialog,int which){
        dialog.cancel();
        }
        });
final AlertDialog alertDialog=builder.create();
        alertDialog.show();
        }

//HTTP connection to the URL


public class PostDataAsyncTask extends AsyncTask<String, String, String> {

    protected void onPreExecute() {
        super.onPreExecute();
        // do stuff before posting data
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            postText();
                /*// 1 = post text data, 2 = post file
                int actionChoice = 1;

                // post a text data
                if(actionChoice==1){

                }

                // post a file
                else{

                }*/

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String lenghtOfFile) {
        // do stuff after posting data
    }

}

    // this will post our text data
    private void postText() {
        try {
            // url where the data will be posted
            String postReceiverUrl = "http://192.168.3.169/script.php";
            Log.v("", "postURL: " + postReceiverUrl);

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);

            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("firstname", "Mike"));
            nameValuePairs.add(new BasicNameValuePair("lastname", "Dalisay"));
            nameValuePairs.add(new BasicNameValuePair("email", "mike@testmail.com"));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v("", "Response: " + responseStr);

                // you can add an if statement here and do other actions based on the response
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* // will post our text file
    private void postFile(){
        try{
            // the file to be posted
            String textFile = Environment.getExternalStorageDirectory() + "/sample.txt";
            Log.v("", "textFile: " + textFile);
            // the URL where the file will be posted
            String postReceiverUrl = "";
            Log.v("", "postURL: " + postReceiverUrl);
            // new HttpClient
            HttpClient httpClient = new DefaultHttpClient();
            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);
            File file = new File(textFile);
            FileBody fileBody = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("file", fileBody);
            httpPost.setEntity(reqEntity);
            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {

                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v("", "Response: " +  responseStr);

                // you can add an if statement here and do other actions based on the response
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}

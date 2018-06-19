package com.example.shanijoffe.hungry_monkey;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;


import javax.net.ssl.HttpsURLConnection;

import static com.loopj.android.http.AsyncHttpClient.log;
import static java.lang.Thread.sleep;

public class ResturantDetails extends AppCompatActivity {

//restaurant details activity .

    ListView lv;
    String val;
    double lon_dest,lat_dest;
    String line="";
    JSONObject res_obj,loc_jsn;
    String Idpath="";
    int resId;
    ToggleButton toggleButton;
    Intent i = getIntent();
    TextView dish_price_txtv,dish_name_txtv,rest_name_txtv,Kosher_txtv,rest_address_txtv,dish_description_txtv;
    ArrayAdapter<String> adapter;
    protected void onCreate(Bundle savedInstanceState) {
        String[] teams = {"Man Utd", "Man City", "Chelsea", "Arsenal", "Liverpool", "Totenham", "Man Utd", "Man City", "Chelsea", "Arsenal", "Liverpool", "Totenham"};
        super.onCreate( savedInstanceState );
        setContentView( R.layout.resturant_det );
        //connecting the UI
        dish_price_txtv = findViewById( R.id.dish_price_txtv );
        dish_name_txtv = findViewById( R.id.dish_name_txtv );
        rest_name_txtv = findViewById( R.id.rest_name_txtv );
        Kosher_txtv = findViewById( R.id.Kosher_txtv );
        rest_address_txtv = findViewById( R.id.rest_address_txtv );
        dish_description_txtv = findViewById( R.id.dish_description_txtv );
        FloatingActionButton toggleButton = (FloatingActionButton) findViewById( R.id.myToggleButton );
        FloatingActionButton show_res_menu_tbutton = (FloatingActionButton) findViewById( R.id.show_res_menu_tbutton );
        res_obj=new JSONObject(  );

        Intent i = getIntent();
        String rest_address2 = i.getStringExtra( "rest_address2" );
        String rest_location2 = i.getStringExtra( "rest_location2" );

        String rest_name2 = i.getStringExtra( "rest_name2" );
        String Kosher2 = i.getStringExtra( "Kosher2" );
        String dish_name2 = i.getStringExtra( "dish_name2" );
        String dish_price2 = i.getStringExtra( "dish_price2" );
        String dish_description2 = i.getStringExtra( "dish_description2" );
        String dish_id_inRest = i.getStringExtra( "dish_id_inRest" );
        //getting the restaurant's id.
        Log.e("my is in res ",dish_id_inRest);
        resId=  (int)Double.parseDouble(dish_id_inRest);

        Log.e("res id after casting ","@@@@@@@@@@@@@@@@@"+resId);

        dish_price_txtv.setText( dish_price2 + "ש''ח " + " " );
        dish_name_txtv.setText( dish_name2 );
        rest_name_txtv.setText( rest_name2 );
        if (Kosher2.equals( "true" )) {

            Kosher_txtv.setText( "כשרות מהדרין" );
            System.out.println( "כשר  " + Kosher2 + "\n" );
        } else {
            Kosher_txtv.setText( "אין כשרות" );
            System.out.println( "לא כשר  " + Kosher2 + "\n" );
        }
        dish_description_txtv.setText( dish_description2 + "." );
        rest_address_txtv.setText( rest_address2  );

        //build our json object
        try {
            res_obj.put( "rest_name", rest_name2);
            res_obj.put( "rest_address2", rest_name2);
            res_obj.put( "Kosher2", rest_name2);
            res_obj.put( "dish_name2", rest_name2);
            res_obj.put( "dish_price2", rest_name2);
            res_obj.put( "dish_description2", rest_name2);
            res_obj.put( "rest_location2", rest_location2);
            Log.e("r666","in res loc" + res_obj.get("rest_location2"));
        } catch (JSONException e) {
            e.printStackTrace();
        }




        //get the restaurant location


        try {
            loc_jsn=new JSONObject( res_obj.get("rest_location2").toString() );

            lon_dest= (double) loc_jsn.get("lon");
            lat_dest= (double) loc_jsn.get("lat");
            log.e("adrr:","lon res:"+lon_dest +"lat adrr"+lat_dest );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        toggleButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("loc","lat "+lat_dest + "lon " +lon_dest);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + lat_dest +"," +  lon_dest));
                startActivity(intent);

            }
        } );
         Idpath=String.valueOf(resId) ;
        show_res_menu_tbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //show menu from url
                goToUrl ( "http://newapp-env.eiymf2wfdn.eu-central-1.elasticbeanstalk.com/api/v1/viewMenu/" +Idpath); //show menu on web page.


            }
        });
    }

    public void goToSu(View view) {

        goToUrl ( "http://newapp-env.eiymf2wfdn.eu-central-1.elasticbeanstalk.com/api/v1/viewMenu/" +Idpath); //show menu on web page.
    }

    private void goToUrl (String url) {
        Log.i("b4 web","web&&&&&&&&&&&&&&&&&&");
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        Log.i("after web","web&&&&&&&&&&&&&&&&");
        startActivity(launchBrowser);
    }


    class myAsyncTask extends AsyncTask<String, Void, String> {

        JSONArray dataJsonArr = null;
        ProgressDialog pd;


        myAsyncTask(String dish_name,double lat ,double lon)
        {
            // init  all the parameters .

        }
        @Override
        protected void onPreExecute()
        {
            //diolog window

            super.onPreExecute();

            pd = new ProgressDialog( ResturantDetails.this);
            pd.setCancelable( false );
            pd.setMessage( "Searching ..." );
            pd.getWindow().setGravity( Gravity.CENTER );
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i( "in myAsyncTask  ", "in do" );
            String returnResult = getDishList_basic( "api" );//call the function that will send the data to server and present our resturants response

            return returnResult;
        }

        protected void onPostExecute(String result) {

//            Looper.prepare();

            try {
                sleep( 50 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pd.dismiss();

        }
    }

    @SuppressLint({"MissingPermission", "LongLogTag"})
    public String getDishList_basic(String url) {

//            String matchFound = "N";
        BufferedReader reader = null;
        String text = "";
        String nameDish;
        HttpURLConnection conn = null;

        nameDish = null;

        ////
        try {
            //eatblishing our connection to our basic search api

            log.i( "in post basic  ", "1" );
            URL url2 = new URL( "http://hungrymonkey-env.vivacrpttt.ec2-18-196-119-82.eu-central-1.compute.amazonaws.com/api/v1/basicSearch" ); // here is your URL path
            //  URL url2 = new URL( "https://www.facebook.com/" ); // here is your URL path
            //buidling our json object


            //POST
            conn = (HttpURLConnection) url2.openConnection();
            conn.setReadTimeout( 50000 /* milliseconds */ );
            conn.setConnectTimeout( 50000 /* milliseconds */ );
            conn.setRequestMethod( "POST" );
            conn.setDoInput( true );
            conn.setDoOutput( true );

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter( os, "UTF-8" ) );
            writer.write( getPostDataString( res_obj ) );
            writer.flush();
            writer.close();
            os.close();
            //check connection status
            int responseCode = conn.getResponseCode();
            Log.i( "responseCode@:", String.valueOf( responseCode ) );

            //getting our restaurants  from server
            if (responseCode >= 400 && responseCode <= 499) {
                Log.e("basic_search", "HTTPx Response: " + responseCode + " - " + conn.getResponseMessage());
                BufferedInputStream in = new BufferedInputStream( conn.getErrorStream() );
            }
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                reader = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream() ) );
                StringBuffer sb = new StringBuffer( "" );

                //parsing the response ...
                while ((line = reader.readLine()) != null) {

                    Log.i( "res is:",line +"\n");
                    sb.append( line );
                    break;
                }
                // Toast.makeText( getActivity(), sb.toString(), Toast.LENGTH_SHORT ).show();

                reader.close();

                return sb.toString();
            } else {
                return new String( "false : " + responseCode );
            }
        } catch (Exception e) {
            return new String( "Exception: " + e.getMessage() );
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append( URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}

package com.example.shanijoffe.hungry_monkey;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class showRecActivity extends AppCompatActivity {
    ListView list_fav;
    custom_adapter ad;
    HashMap<String, String> hashMap_;
    Vector<HashMap<String,String>> vector2=null;
    View view;
    JSONObject jsonObject_id;
    String my_fav_list=null;
    JSONObject location_res_jsn,_source,inner_hits,menuJ,hits_1;
    String[] teams={""};
    String name_res="",name_dish="",kosher="",rest_address="",dish_desc="",location_res_str="",price_dish="",dish_id_inRest="";
    JSONObject rest_location = new JSONObject();
    JSONObject menu = new JSONObject();
    String token=null;
    String url = new String(); //for basic search

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    ListView rec_dish_list;
    custom_adapter aa;
    Vector<HashMap<String,String>> vector=null;

    JSONArray menu_hits_jsonArray=null, jsonarray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_show_rec );

        rec_dish_list =  findViewById( R.id.list_rec );
        location_res_jsn = null;
        hashMap_ = null;
        vector = new Vector<>();
        hashMap_ = new HashMap<>();
        //getting our token
        settings =getSharedPreferences( "myPrefsFile",MODE_PRIVATE );
        token=settings.getString( "user_token" ,"null" );
        Log.i("token  in getRec is",token);
        //getting our favorites dishes.
        my_fav_list=getFavlist();
        Log.i("after parsing", String.valueOf( vector ) );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                        .setAction( "Action", null ).show();
            }
        } );
    }
    public  Vector<HashMap<String,String>> parseMyList (String list)
    {
        my_fav_list=list;
        try {
            if (my_fav_list != null) {
                jsonarray = new JSONArray( my_fav_list );
                Log.i( "f", " \n\n" );
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject js3 = jsonarray.getJSONObject( i );
                    // System.out.println( "json array at place " + i + js3.toString() + "\n" );
                }
            } else {
                Toast.makeText( showRecActivity.this, " אין ערכים", Toast.LENGTH_SHORT ).show();
            }
            int count = -1;

            List<Map<String, String>> myMap = new ArrayList<Map<String, String>>();
            for (int i = 0; i < jsonarray.length(); i++) {
                Log.i( "index", String.valueOf( i ) );
                //HashMap<String,String> hashMap_ = new HashMap<>();
                JSONObject obj = jsonarray.getJSONObject( i );
                _source = new JSONObject();
                _source = obj.getJSONObject( "_source" );
                rest_address = _source.getString( "rest_address" );
                name_res = _source.getString( "rest_name" );
                location_res_str = _source.getString( "rest_location" );
                location_res_jsn = new JSONObject( location_res_str );
                ///kosher
                kosher = _source.getString( "Kosher" );
                inner_hits = new JSONObject();
                inner_hits = obj.getJSONObject( "inner_hits" );
                menu = new JSONObject();
                menu = inner_hits.getJSONObject( "menu" );
                hits_1 = new JSONObject();
                hits_1 = menu.getJSONObject( "hits" );
                String hits_arr = hits_1.getString( "hits" );
                menu_hits_jsonArray = new JSONArray( hits_arr );
                for (int j = 0; j < menu_hits_jsonArray.length(); j++)
                {
                    //   count++;
                    hashMap_ = new HashMap<>();

                    //getting restaurant details.
                    hashMap_.put( "index", String.valueOf( count ) );
                    hashMap_.put( "rest_address", rest_address );
                    hashMap_.put( "rest_name", name_res );
                    hashMap_.put( "rest_location", String.valueOf( location_res_jsn ) );
                    hashMap_.put( "Kosher", kosher );
                    JSONObject obj2 = menu_hits_jsonArray.getJSONObject( j );
                    JSONObject _source2 = new JSONObject();
                    _source2 = obj2.getJSONObject( "_source" );
                    dish_desc = _source2.getString( "dish_description" );
                    hashMap_.put( "dish_description", dish_desc );
                    name_dish = _source2.getString( "dish_name" );
                    hashMap_.put( "dish_name", name_dish );
                    price_dish = _source2.getString( "dish_price" );
                    hashMap_.put( "dish_price", price_dish );
                    hashMap_.put( "dish_id_inRest", dish_id_inRest );
                    vector.add( hashMap_ );

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vector;
    }

    public String getFavlist()
    {
        Log.i("heyo","im in getFAV ");
        myAsyncTask m= (myAsyncTask) new myAsyncTask(token).execute(  );
        Log.i("heyo","im in geFAV 3333333333");
        return m.list;
    }
    class myAsyncTask extends AsyncTask<String, Void, String> {
        JSONParser jParser;
        JSONArray productList;
        JSONArray dataJsonArr = null;
        ProgressDialog pd;
        String id,list=null;

        myAsyncTask(String id)
        {
            // init  all the parameters .

            this.id=id;

        }
        @Override
        protected void onPreExecute()
        {
            //diolog window
            Log.i(" fav onPreExecute","getfav");
            super.onPreExecute();
            productList = new JSONArray();
            jParser = new JSONParser();
            pd = new ProgressDialog( showRecActivity.this );
            pd.setCancelable( false );
            pd.setMessage( "getting your fav dishes  ..." );
            pd.getWindow().setGravity( Gravity.CENTER );
            pd.show();
            this.list="fake list";
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i( "in myAsyncTask  ", "in do" );
            String returnResult = getDishList_basic( url );//call the function that will send the data to server and present our resturants response

            return returnResult;
        }

        protected void onPostExecute(String result) {

//            Looper.prepare();

            my_fav_list=result;
            if(my_fav_list!=null)
            {
                this.list=my_fav_list;

                vector= parseMyList(my_fav_list);
                Log.i("my fav dishes  vector", String.valueOf( vector ) );

                //connecting to our  list view
                aa = new custom_adapter( showRecActivity.this, R.layout.single_dish_item, vector,0 );
                //  Collections.sort(vector, new custom_adapter( getContext(), R.layout.single_dish_item, vector,flag_btn));
                rec_dish_list.setAdapter( aa );
            }
            try {
                sleep( 2000 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            Log.i("my fav dishes  getfav",result);


            pd.dismiss();


        }

        @SuppressLint({"MissingPermission", "LongLogTag"})
        public String getDishList_basic(String url) {

//            String matchFound = "N";
            BufferedReader reader = null;
            String text = "";
            StringBuffer response=null;
            String nameDish;
            HttpURLConnection conn = null;
            final String USER_AGENT = "Mozilla/5.0";
            nameDish = null;
            jsonObject_id= new JSONObject();
            Log.i("hi","&&&&&&&&&&");
            ////
            try {
                //eatblishing our connection to our basic search api

                URL url2 = new URL( "http://newapp-env.eiymf2wfdn.eu-central-1.elasticbeanstalk.com/api/v1/recsForUser" ); // here is your URL path

                //building the request

                conn = (HttpURLConnection) url2.openConnection();
                // conn.setReadTimeout( 50000 /* milliseconds */ );
                // conn.setConnectTimeout( 50000 /* milliseconds */ );
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "JWT "
                        + token);
                conn.setRequestMethod( "GET" );

                int httpStatus = conn.getResponseCode();
                Log.v("response rec man ", "httpStatus " + httpStatus);

                if (httpStatus == 200) {
                    Log.v("response success ", "httpStatus " + httpStatus);

                }

                //jwt

                if (httpStatus >= 400 && httpStatus <= 499) {
                    Log.e("getfavlist", "HTTPx Response: " + httpStatus + " - " + conn.getResponseMessage().toString());
                    BufferedInputStream in = new BufferedInputStream( conn.getErrorStream() );
                }
                //getting our restaurants  from server

                if (httpStatus == HttpURLConnection.HTTP_OK)
                { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);

                    }
                    System.out.println("response is"+response.toString());

                    in.close();

                    // print result
                    System.out.println(response.toString());
                    return response.toString();
                }
                else
                {
                    System.out.println("GET request not worked");
                }


            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }



    }

}

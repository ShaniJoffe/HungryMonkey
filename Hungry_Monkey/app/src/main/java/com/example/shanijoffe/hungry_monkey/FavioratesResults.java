package com.example.shanijoffe.hungry_monkey;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

import static com.example.shanijoffe.hungry_monkey.R.id.filter_by_price;
import static com.loopj.android.http.AsyncHttpClient.log;
import static java.lang.Thread.sleep;

public class FavioratesResults extends AppCompatActivity {

    int flag_btn=0;
    ListView list_fav;
    custom_adapter ad;
    HashMap<String, String> hashMap_;
    Vector<HashMap<String,String>> vector2=null;
    View view;
    JSONObject jsonObject_id;
    String my_fav_list=null;
    JSONObject location_res_jsn,_source,inner_hits,menuJ,hits_1;
    String[] teams={""};
    String name_res="",name_dish="",kosher="",rest_address="",dish_desc="",location_res_str="",price_dish="";
    JSONObject rest_location = new JSONObject();
    JSONObject menu = new JSONObject();
    JSONObject postDataParams_adv;
    Button firstButton;
    TextView nres;
    Button det;
    String myValue,line=null;
    String url = new String(); //for basic search
    FloatingActionButton fav;
    Button show_det_for_dish;
    Vector<HashMap<String,String>> vector=null;
    TextView  flag_price,flag_loc;

    JSONArray menu_hits_jsonArray=null, jsonarray;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_faviorates_results);
       // flag_price=(TextView)view.findViewById( R.id.txtv_flag_price );
      //  flag_loc=(TextView)view.findViewById( R.id.txtv_flag_loc );
      //  flag_price.setText( "false" );
//        list_fav = findViewById(R.id.list_fav);
//     // fav =(FloatingActionButton)view.findViewById( R.id.addToFav_btn ) ;
//
//        //jsons
//        postDataParams_adv = new JSONObject(); //  advanced  search json object.
//        jsonObject_id = new JSONObject();//basic search json object.
//        hashMap_ = null;
//       vector = new Vector<>();
//
//    ////getting the dish list from server
//
//
//        JSONObject rloc = new JSONObject();
//        try {
//            rloc.put("lon", "John");
//
//        rloc.put("lat", "Reese");
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//        hashMap_ = new HashMap<>();
//        //getting res detailes
//        hashMap_.put( "index", String.valueOf( "1" ) );
//        hashMap_.put( "rest_address", "ggg" );
//        hashMap_.put( "rest_name", "ggg" );
//        hashMap_.put( "rest_location", String.valueOf( rloc ) );
//        hashMap_.put( "Kosher",  "ggg"  );
//        hashMap_.put( "Kosher",  "ggg"  );
//        //getting dish detailes
//      //  JSONObject obj2 = menu_hits_jsonArray.getJSONObject(  "ggg"  );
//        JSONObject _source2 = new JSONObject();
//           // _source2 = obj2.getJSONObject( "_source" );
//
//      //  dish_desc = _source2.getString( "dish_description" );
//        hashMap_.put( "dish_description",  "ggg" );
//      //  name_dish = _source2.getString( "dish_name" );
//        hashMap_.put( "dish_name",  "ggg" );
//        //price_dish = _source2.getString( "dish_price" );
//        hashMap_.put( "dish_price",  "ggg" );
//
//        vector.add( hashMap_ );
//    //attach the adapter to list view
//
//Log.i("vector is",vector.toString());
//        ad = new custom_adapter( FavioratesResults.this, R.layout.single_dish_item, vector,0 );
//        Collections.sort(vector, new custom_adapter( FavioratesResults.this, R.layout.single_dish_item, vector,0));
//     list_fav.setAdapter( ad );
//
//        my_fav_list=getFavlist();
    }


   public String getFavlist()
   {
       Log.i("heyo","im in getFAV ");
       myAsyncTask m= (myAsyncTask) new myAsyncTask( "1").execute(  );
       return null;
   }
    class myAsyncTask extends AsyncTask<String, Void, String> {
        JSONParser jParser;
        JSONArray productList;
        JSONArray dataJsonArr = null;
        ProgressDialog pd;
        String id;

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
            pd = new ProgressDialog( FavioratesResults.this );
            pd.setCancelable( false );
            pd.setMessage( "getting your fav dishes  ..." );
            pd.getWindow().setGravity( Gravity.CENTER );
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i( "in myAsyncTask  ", "in do" );
            String returnResult = getDishList_basic( url );//call the function that will send the data to server and present our resturants response

            return returnResult;
        }

        protected void onPostExecute(String result) {

//            Looper.prepare();
            Log.i("dish_result getfav",result);
            my_fav_list=result;
            try {
                sleep( 50 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pd.dismiss();

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
                //eatblishing our connection to our basic search api

                log.i( "in getfav getdish   ", "1" );
                URL url2 = new URL( "http://hmfproject-env-2.dcnrhkkgqs.eu-central-1.elasticbeanstalk.com/api/v1/getFavs" ); // here is your URL path
                //buidling our json object
                jsonObject_id.put( "id", this.id ); //dish name
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
                writer.write( getPostDataString( jsonObject_id ) );
                writer.flush();
                writer.close();
                os.close();
                //check connection status
                int responseCode = conn.getResponseCode();
                Log.i( "responseCode@:", String.valueOf( responseCode ) );

                //getting our restaurants  from server
                if (responseCode >= 400 && responseCode <= 499) {
                    Log.e("getfavlist", "HTTPx Response: " + responseCode + " - " + conn.getResponseMessage());
                    BufferedInputStream in = new BufferedInputStream( conn.getErrorStream() );
                }
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.i("inresponseCode ","responseCode is ok");
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
                    Log.i("sb.toString ",sb.toString());

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

}

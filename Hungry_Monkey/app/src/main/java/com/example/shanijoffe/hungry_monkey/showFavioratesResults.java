package com.example.shanijoffe.hungry_monkey;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

import static com.loopj.android.http.AsyncHttpClient.log;
import static java.lang.Thread.sleep;

public class showFavioratesResults extends AppCompatActivity {

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
    String name_res="",name_dish="",kosher="",rest_address="",dish_desc="",location_res_str="",price_dish="",dish_id_inRest="";


    JSONObject rest_location = new JSONObject();
    JSONObject menu = new JSONObject();
    JSONObject postDataParams_adv;
    Button firstButton;
    TextView nres;
    Button det;
    String myValue,line=null,token=null;
    String url = new String(); //for basic search
    FloatingActionButton fav;
    Button show_det_for_dish;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    ListView fav_dish_list;
    custom_adapter aa;
    Vector<HashMap<String,String>> vector=null;
    TextView  flag_price,flag_loc;

    JSONArray menu_hits_jsonArray=null, jsonarray;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faviorates_results);
       fav_dish_list =  findViewById( R.id.list_fav );
        Log.i("supp","dude");
        //getting our token
        settings =getSharedPreferences( "myPrefsFile",MODE_PRIVATE );
        token=settings.getString( "user_token" ,"null" );
        Log.i("token  in getFav is",token);
        //getting our favorites dishes.
      //  my_fav_list=getFavlist();


//        Log.i("my fake list ",my_fav_list);
        log.i( "sup   ", "444444444444444" );
        //the parsing
        location_res_jsn = null;
        hashMap_ = null;
        vector = new Vector<>();

//        try {
//            if (my_fav_list != null) {
//                jsonarray = new JSONArray( my_fav_list );
//                Log.i( "f", " \n\n" );
//                for (int i = 0; i < jsonarray.length(); i++) {
//                    JSONObject js3 = jsonarray.getJSONObject( i );
//                    // System.out.println( "json array at place " + i + js3.toString() + "\n" );
//                }
//            } else {
//                Toast.makeText( showFavioratesResults.this, " אין ערכים", Toast.LENGTH_SHORT ).show();
//            }
//            int count = -1;
//            // Log.i( "dish_list in if", String.valueOf( jsonarray ) );
//            //   Log.i( "f", " \n\n" );
//            List<Map<String, String>> myMap = new ArrayList<Map<String, String>>();
//            //  Log.i( "jsonArray length", String.valueOf( jsonarray.length() ) );
//
//
//            for (int i = 0; i < jsonarray.length(); i++) {
//                Log.i( "index", String.valueOf( i ) );
//                //HashMap<String,String> hashMap_ = new HashMap<>();
//                JSONObject obj = jsonarray.getJSONObject( i );
//                _source = new JSONObject();
//                _source = obj.getJSONObject( "_source" );
//                //   log.i( "_source", String.valueOf( _source ) );
//                //rest_address
//                rest_address = _source.getString( "rest_address" );
//                //     System.out.println( "rest_address" + rest_address );
//                // hashMap_.put("rest_address",rest_address);
//                ///name_res
//                name_res = _source.getString( "rest_name" );
//                //  System.out.println( "name_res" + name_res );
//                //  hashMap_.put("rest_name",name_res);
//                //location res
//                location_res_str = _source.getString( "rest_location" );
//                location_res_jsn = new JSONObject( location_res_str );
//                //  System.out.println( "location_res_jsn" + location_res_jsn.toString() );
//                // hashMap_.put("rest_location", String.valueOf( location_res_jsn ) );
//
//                ///kosher
//                kosher = _source.getString( "Kosher" );
//                //  System.out.println( "kosher" + kosher );
//                //  hashMap_.put("Kosher", kosher );
//                //
//                inner_hits = new JSONObject();
//                inner_hits = obj.getJSONObject( "inner_hits" );
//                //  log.i( "inner_hits", String.valueOf( inner_hits ) );
//                menu = new JSONObject();
//                menu = inner_hits.getJSONObject( "menu" );
//                //  log.i( "menu", String.valueOf( menu ) );
//                hits_1 = new JSONObject();
//                hits_1 = menu.getJSONObject( "hits" );
//                String hits_arr = hits_1.getString( "hits" );
//                //  System.out.println( "hits_arr" + hits_arr );
//                menu_hits_jsonArray = new JSONArray( hits_arr );
//                //    log.i("menu_hits_jsonArray", String.valueOf( menu_hits_jsonArray ));
//                //  Log.i( "name restuartant:", name_res );
//                for (int j = 0; j < menu_hits_jsonArray.length(); j++) {
//                    //   count++;
//                    hashMap_ = new HashMap<>();
//
//                    //getting restaurant details.
//                    hashMap_.put( "index", String.valueOf( count ) );
//                    hashMap_.put( "rest_address", rest_address );
//                    hashMap_.put( "rest_name", name_res );
//                    hashMap_.put( "rest_location", String.valueOf( location_res_jsn ) );
//                    hashMap_.put( "Kosher", kosher );
//
//                    //getting dish detailes
//
//                    JSONObject obj2 = menu_hits_jsonArray.getJSONObject( j );
//                    JSONObject _source2 = new JSONObject();
//                    _source2 = obj2.getJSONObject( "_source" );
//                    dish_desc = _source2.getString( "dish_description" );
//                    hashMap_.put( "dish_description", dish_desc );
//                    name_dish = _source2.getString( "dish_name" );
//                    hashMap_.put( "dish_name", name_dish );
//                    price_dish = _source2.getString( "dish_price" );
//                    hashMap_.put( "dish_price", price_dish );
//
//                    //getting the id of our dish in restaurant
//                    dish_id_inRest = _source2.getString( "dish_id_inRest" );
//                    hashMap_.put( "dish_id_inRest", dish_id_inRest );
//
//                    // System.out.println( "dish_desc" + dish_desc );
//                    //    System.out.println( "name_dish" + name_dish );
//                    //   System.out.println( "price_dish" + price_dish );
//                    vector.add( hashMap_ );
//
//                }
//            }
            //fake data

            hashMap_ = new HashMap<>();

            //getting restaurant details.
            hashMap_.put( "index", "1" );
            hashMap_.put( "rest_address", "neverland  " );
            hashMap_.put( "rest_name", "shani's restaurant" );
            hashMap_.put( "rest_location", "0,0" );
            hashMap_.put( "Kosher", "nope" );
            hashMap_.put( "dish_description", "pretty dam good" );
            hashMap_.put( "dish_name", "steak" );
            hashMap_.put( "dish_price", "99" );
            hashMap_.put( "dish_id_inRest", "1.1" );
            vector.add( hashMap_ );




            //end of fake data

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }





        //connecting to our  list view
        aa = new custom_adapter( showFavioratesResults.this, R.layout.single_dish_item, vector,0 );
       //  Collections.sort(vector, new custom_adapter( getContext(), R.layout.single_dish_item, vector,flag_btn));
        fav_dish_list.setAdapter( aa );
        //parsing our dishe
    }



   public String getFavlist()
   {
       Log.i("heyo","im in getFAV ");
       myAsyncTask m= (myAsyncTask) new myAsyncTask(token).execute(  );
       Log.i("heyo","im in getFAV 3333333333");
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
            pd = new ProgressDialog( showFavioratesResults.this );
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
            Log.i("my fav dishes  getfav",result);
            my_fav_list=result;
            if(my_fav_list!=null)
            {
                this.list=my_fav_list;
            }
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


                URL url2 = new URL( "http://hmfproject-env-2.dcnrhkkgqs.eu-central-1.elasticbeanstalk.com/api/v1/getFavs" ); // here is your URL path
                jsonObject_id= new JSONObject();
                log.i( "sup   ", "5555" );
                //POST
                conn = (HttpURLConnection) url2.openConnection();
                conn.setReadTimeout( 50000 /* milliseconds */ );
                conn.setConnectTimeout( 50000 /* milliseconds */ );
                conn.setRequestMethod( "POST" );
                conn.setDoInput( true );
                conn.setDoOutput( true );
                conn.setRequestProperty( "Authorization", token);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter( os, "UTF-8" ) );
                writer.write( getPostDataString( jsonObject_id ) );
                writer.flush();
                writer.close();
                os.close();
                //check connection status
                log.i( "sup   ", "666" );

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

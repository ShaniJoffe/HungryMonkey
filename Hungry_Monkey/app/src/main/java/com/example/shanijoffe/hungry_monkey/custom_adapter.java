package com.example.shanijoffe.hungry_monkey; /**
 * Created by Shani Joffe on 1/16/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

import static com.loopj.android.http.AsyncHttpClient.log;


public class custom_adapter extends ArrayAdapter<HashMap<String, String>> implements Comparator<HashMap<String, String>> {
    Button show_det_for_dish;

    private final String[] rest_address = new String[1];
    private final String[] rest_location = new String[1];
    private final String[] rest_name = new String[1];
    private final String[] Kosher = new String[1];
    private final String[] dish_description = new String[1];
    private final String[] dish_name = new String[1];
    private final String[] dish_price = new String[1];
    private final String[] num_dishes = new String[1];
    private final boolean[] dish_like = new boolean[1];
    String[] users_favs_dishes ;//for the id of the dishes the user would like to save as favorites.

    private final String[] dish_id_inRest1 = new String[1];
    int position;
    TextView flag_price;
    TextView flag_loc;
    CheckBox like_btn;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String token =null;

    private Vector<HashMap<String, String>> _vec;
    private Context context;
    private int resource;
    int flag_btn;
    String line = "";

    public custom_adapter(Context context, int resource, Vector<HashMap<String, String>> vec, int flag_btn) {
        super( context, resource, vec );
        this._vec = vec;
        this.context = context;
        this.resource = resource;
        this.flag_btn = flag_btn;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        this.position = position;
        View view = LayoutInflater.from( context ).inflate( this.resource, parent, false );

        final TextView name_res_txtv = (TextView) view.findViewById( R.id.nameRes );
        final TextView name_dish_txtv = (TextView) view.findViewById( R.id.Dish_name );
        final TextView PriceDish_txtv = (TextView) view.findViewById( R.id.PriceDish );
        flag_price = (TextView) view.findViewById( R.id.txtv_flag_price );
        flag_loc = (TextView) view.findViewById( R.id.txtv_flag_loc );
        //fav_btn=(FloatingActionButton)view.findViewById( R.id.addToFav_btn );
        like_btn = (CheckBox) view.findViewById( R.id.likeIcon );

        ///getting our token from SharedPreferences.

        settings=context.getSharedPreferences( "myPrefsFile",0  );
        editor=settings.edit();
        token=settings.getString( "user_token" ,"null" );

        Log.i("token  in costume is",token);

        /////parsing the data of our dish


       // Log.i( "position", String.valueOf( position ) );
        rest_address[0] = _vec.get( position ).get( "rest_address" );
        rest_location[0] = _vec.get( position ).get( "rest_location" );
        rest_name[0] = _vec.get( position ).get( "rest_name" );
        Kosher[0] = _vec.get( position ).get( "Kosher" );
        dish_description[0] = _vec.get( position ).get( "dish_description" );
        dish_name[0] = _vec.get( position ).get( "dish_name" );
        dish_price[0] = _vec.get( position ).get( "dish_price" );
        num_dishes[0] = String.valueOf( _vec.get( position ).size() );
        dish_id_inRest1[0] = _vec.get( position ).get( "dish_id_inRest" ) ;
       // Log.i("dish id in res", String.valueOf( dish_id_inRest1[0] ) );


        // dish_like[0] = Boolean.parseBoolean( _vec.get( position ).get( "dish_like" ) );//field from server
        // dish_like[0] = Boolean.parseBoolean("false" );//field from server
        ///


        final String rest_address2 = rest_address[0];
        final String rest_location2 = rest_location[0];
        final String rest_name2 = rest_name[0];
        final String Kosher2 = Kosher[0];
        final String dish_description2 = dish_description[0];
        final String dish_name2 = dish_name[0];
        final String dish_price2 = dish_price[0];
        final String num_dishes2 = num_dishes[0] ;
        final String dish_id_inRest = dish_id_inRest1[0];

        //  final boolean dish_like2 = dish_like[0];//dish_like2 will have true of is liked or false if not
        // final boolean dish_like2 = dish_like[0];//dish_like2 will have true of is liked or false if not

        name_res_txtv.setText( rest_name[0] );
        name_dish_txtv.setText( dish_name[0] );
        PriceDish_txtv.setText( dish_price[0] + "שח " );
        users_favs_dishes= new String[1];

        //getting our  array from
        int size = settings.getInt("array_size", 0);
        //  users_favs_dishes = new String[size];
       // Log.i("size i got is  ", String.valueOf( size ) );



        ////

        //checkbox listener -when user adds a dish to favorites.

        show_det_for_dish = (Button) view.findViewById( R.id.choose_Dish );
        show_det_for_dish.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.i( "btn choose", "chhose" );
                Intent i = new Intent( getContext(), ResturantDetails.class );
                i.putExtra( "rest_address2", rest_address2 );
                i.putExtra( "rest_location2", rest_location2 );
                i.putExtra( "Kosher2", Kosher2 );
                i.putExtra( "dish_description2", dish_description2 );
                i.putExtra( "dish_name2", dish_name2 );
                i.putExtra( "rest_name2", rest_name2 );
                i.putExtra( "dish_price2", dish_price2 );
                context.startActivity( i );
            }
        } );

        like_btn.setChecked( false );

        like_btn.setOnClickListener( new View.OnClickListener() { //heart btn clicked !!
            @Override
            public void onClick(View view)
            {
             //   Log.i("add to fav ","clicked");
                users_favs_dishes[0]=dish_id_inRest;
                 log.i("my dish id is ", users_favs_dishes[0]);

                //  editor.putString( "my_fav_list", Arrays.toString(users_favs_dishes) ).apply();

                // Log.i("my position", String.valueOf( position ) );
                // System.out.println( "my array "+ Arrays.toString(users_favs_dishes));
                new SendPostRequest().execute();//authentication to server .
                notifyDataSetChanged();
            }





        } );

        like_btn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                 //   log.i("hi there","im checked? "+ isChecked);
                    compoundButton.setBackgroundColor( Color.RED);
                } else {
                    compoundButton.setBackgroundColor(Color.BLUE);
                }
            }
        } );
        return view;
    }
    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
        this.setNotifyOnChange( true );
    }

    private void sortMyList() {
        notifyDataSetChanged();
    }

    @Override
    public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {

        if (flag_btn == 1) {
          //  Log.i( "button filer by price ", "in compare" );
            if (Integer.parseInt( lhs.get( "dish_price" ) ) > Integer.parseInt( rhs.get( "dish_price" ) )) {

                return 1;
            }
        } else if (flag_btn == -1) {

         //   Log.i( "button filer by price ", "in compare left is " + lhs.toString() + "right is" + rhs.toString() );
            JSONObject location_lhs = new JSONObject();
            JSONObject location_rhs = new JSONObject();
            double lon_lhs = 0, lat_lhs = 0, lat_rhs = 0, lon_rhs = 0;

            try {

                location_lhs = new JSONObject( lhs.get( "rest_location" ) );
                lon_lhs = Double.parseDouble( String.valueOf( location_lhs.get( "lon" ) ) );
                lat_lhs = Double.parseDouble( String.valueOf( location_lhs.get( "lat" ) ) );
               // Log.i( "button filer by price ", "in compare left is " + location_lhs.toString() );

                location_rhs = new JSONObject( lhs.get( "rest_location" ) );
               // Log.i( "button filer by price ", "right is" + location_rhs.toString() );

                lon_rhs = Double.parseDouble( String.valueOf( location_rhs.get( "lon" ) ) );
                lat_rhs = Double.parseDouble( String.valueOf( location_rhs.get( "lat" ) ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }

           // Log.i( "in compare", "loaction LHS is 1" + "lon1" + lon_lhs + "lat1" + lat_lhs + "loaction2 RHS is" + "lon2" + lon_lhs + "lat2 " + lat_rhs );
            if (distance( lon_lhs, lat_lhs, lat_rhs, lon_rhs ) > 0) { // if distance < 0.1 miles we take locations as equal
                //do what you want to do...
              //  Log.i( "loc 1 is bigger then2 ", "in compare" );
            }
        }
        return -1;
    }

    /**
     * calculates the distance between two locations in MILES
     */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians( lat2 - lat1 );
        double dLng = Math.toRadians( lng2 - lng1 );

        double sindLat = Math.sin( dLat / 2 );
        double sindLng = Math.sin( dLng / 2 );

        double a = Math.pow( sindLat, 2 ) + Math.pow( sindLng, 2 )
                * Math.cos( Math.toRadians( lat1 ) ) * Math.cos( Math.toRadians( lat2 ) );

        double c = 2 * Math.atan2( Math.sqrt( a ), Math.sqrt( 1 - a ) );

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {
            Log.i( "in custom", "SendPostRequest" );
            try {

                URL url = new URL( "http://hmfproject-env-2.dcnrhkkgqs.eu-central-1.elasticbeanstalk.com/api/v1/setFavs" ); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put( "favs",users_favs_dishes[0].toString());

                Log.i("json to setfavs", String.valueOf( postDataParams ) );
                //POST
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
              //  conn.setReadTimeout( 50000 /* milliseconds */ );
              //  conn.setConnectTimeout( 50000 /* milliseconds */ );
             //  conn.setRequestMethod( "POST" );

              //  conn.setDoOutput( true );
                //token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjAiLCJpYXQiOjE1MjU2MjI3NDF9.GW3-wcU5MizmoZGtmzQdyNLbFedCJAwfDnHqrnVglkg";
               // conn.setRequestProperty("Content-Type", "application/json");
               // conn.setRequestProperty("Authorization", "JWT "
                 //       + token);
                //conn.setRequestProperty( "Authorization", token);               // conn.setRequestProperty( "Authorization","Basic ",token );
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
             //   conn.setRequestProperty("User-Agent", "Mozilla/5.0");
               conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty ("Authorization", "JWT " +" " +token);
                Log.i("my token is ",token);
               // conn.setRequestProperty( "Content-type", "application/x-www-form-urlencoded");
                //conn.setRequestProperty( "Accept", "*/*" );
              //  conn.setRequestProperty ("Authorization", "Basic " + token);
                // For POST only - START



                int responseCode = conn.getResponseCode();
                System.out.println("POST Response Code :: " + responseCode);
            //    OutputStream os = conn.getOutputStream();
             //   os.write(POST_PARAMS.getBytes());
              //  os.flush();
              //  os.close();
                // For POST only - END


               // conn.connect();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter( os, "UTF-8" ) );
                writer.write( getPostDataString( postDataParams ) );//sending the json object to server after encoding .
                writer.flush();
                writer.close();
                os.close();

              //  int responseCode = conn.getResponseCode();
              //  Log.i( "responseCode", String.valueOf( responseCode ) );
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream() ) );
                    StringBuffer sb = new StringBuffer( "" );

                    while ((line = in.readLine()) != null) {
                        sb.append( line );
                        Log.i( "line", line );
                        break;
                    }
                    in.close();
                    Log.i( "sb", sb.toString() );
                    return sb.toString();
                } else {
                    return new String( "false : " + responseCode );
                }
            } catch (Exception e) {
                return new String( "Exception: " + e.getMessage() );
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i( "usrn", result );

        }

        public String getPostDataString(JSONObject params) throws Exception {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            Iterator<String> itr = params.keys();
            while (itr.hasNext()) {
                String key = itr.next();
                Object value = params.get( key );
                if (first)
                    first = false;
                else
                    result.append( "&" );
                result.append( URLEncoder.encode( key, "UTF-8" ) );
                result.append( "=" );
                result.append( URLEncoder.encode( value.toString(), "UTF-8" ) );
            }
            return result.toString();
        }

    }
}
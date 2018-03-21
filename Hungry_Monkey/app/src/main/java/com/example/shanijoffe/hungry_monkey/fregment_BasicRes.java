package com.example.shanijoffe.hungry_monkey;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static com.loopj.android.http.AsyncHttpClient.log;
import static java.lang.Thread.sleep;


/**
 * A simple {@link Fragment} subclass.
 */
public class fregment_BasicRes extends Fragment {

    OnHeadlineSelectedListener mCallback;

    boolean show=false;
    String line=""; //for response server.
    Spinner sp_kosher;
    boolean st = false;
    boolean search_finshed = false;
    String url = new String(); //for basic search
    String url_adv = new String(); //for advanced search
    String dish_result = "";//for response.
    JSONObject postDataParams_adv;
    public static TextView dis_val,price_val,sss,MaxPrice_val;
    String kosher="";
    SearchView search_adv=null;
    TextView flag_txtv,txtvMax_price,lbl_distance,lbl_price_range,lbl_kosher,tvMin,tvMax;
    double lon,lat;
  CrystalRangeSeekbar rangeSeekbar;
    TextView tvMin_Distance,tvMax_Distance;
    JSONObject dish_inp;
     CrystalSeekbar seekBar_distance;
    Button Advnced_btn;
    Bundle bundle;
   String[] str = {"כשר", "לא כשר"};
    public fregment_BasicRes() {
        // Required empty public constructor
    }
    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */

        public Bundle getBundle();
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)//before fragment created
    {
        return inflater.inflate( R.layout.fragment_fregment__basic_res, container, false );
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(View view, Bundle savedInstanceState)

    {//after fragment created
        super.onViewCreated(view, savedInstanceState);
        //connecting components

        sp_kosher = (Spinner) getActivity().findViewById(R.id.sp_kosher);

        flag_txtv = (TextView) getActivity().findViewById( R.id.sss );
        search_adv=(SearchView)getActivity().findViewById( R.id.DishSearch );


        lbl_distance=(TextView)getActivity().findViewById( R.id.lbl_distance );//lables
        lbl_price_range=(TextView)getActivity().findViewById( R.id.lbl_price_range );
        lbl_kosher=(TextView)getActivity().findViewById( R.id.lbl_kosher );
         Advnced_btn=(Button)getActivity().findViewById(  R.id.Advnced_btn);
        // get min and max text view
         tvMin_Distance = (TextView) getActivity().findViewById(R.id.textMin5);
         tvMax_Distance = (TextView) getActivity().findViewById(R.id.textMax5);

         //seek bars
        rangeSeekbar = (CrystalRangeSeekbar) getActivity().findViewById(R.id.rangeSeekbar1);//for price range
        seekBar_distance = (CrystalSeekbar) getActivity().findViewById(R.id.sk_dis);//for distance range

        // get min and max text view
         tvMin = (TextView) getActivity().findViewById(R.id.textMin1);
         tvMax = (TextView) getActivity().findViewById(R.id.textMax1);
        //creating distance seekbar
     //   seekBar_distance = new CrystalSeekbar(getActivity());//for distance

        //setting visabilty
        rangeSeekbar.setVisibility( View.INVISIBLE );
        seekBar_distance.setVisibility( View.INVISIBLE );

//location


        // Inflate the layout for this fragment




///

        //tvMax_Distance.setText( "100" );
        //creating list for spinner


        //connecting adapter for kosher spinner .
        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, str);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_kosher.setAdapter(adp2);

        //jsons
        postDataParams_adv = new JSONObject(); //  advanced  search json object.
         dish_inp = new JSONObject();//basic search json object.


        //listener button that will show the advanced details

        Advnced_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = mCallback.getBundle();//getting location from main activity.
                // We need to use a different list item layout for devices older than Honeycomb
                int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
                Toast.makeText(getActivity(), "text " + bundle , Toast.LENGTH_LONG).show();

                lon = Double.parseDouble(String.valueOf( bundle.get( "lon" ) ));
                lat = Double.parseDouble(String.valueOf( bundle.get( "lat" ) )  );
                flag_txtv.setText( "true" );//turn flag ==TRUE for main activity to know that now we are im advancd search mode .

                //show our components.
                LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.adv_leyout);
                LinearLayout linearLayout2 = (LinearLayout) getView().findViewById(R.id.adv_leyout2);
                LinearLayout linearLayout3 = (LinearLayout)getView().findViewById(R.id.adv_leyout3);

                if (show==false)//dosent show advanced options-flag for advanced button.
                {
                    show=true;

                    flag_txtv.setText( "true" ); //flag for main activity . true means in advanced search - will use to saprate advanced search and basic search.

                    //show comopnents.

                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.VISIBLE);

                    lbl_price_range.setVisibility( View.VISIBLE );
                    lbl_distance.setVisibility( View.VISIBLE );
                    lbl_kosher.setVisibility( View.VISIBLE );
                    seekBar_distance.setVisibility( View.VISIBLE );
                    tvMax_Distance.setVisibility( View.VISIBLE );
                    rangeSeekbar.setVisibility(View.VISIBLE);

                    // distance seekbar
                    // set listener
                    seekBar_distance.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
                        @Override
                        public void valueChanged(Number minValue) {
                            tvMin_Distance.setText(String.valueOf(minValue));
                        }
                    });
                    // set final value listener
                    seekBar_distance.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
                        @Override
                        public void finalValue(Number value) {
                            Log.d("CRS=>", String.valueOf(value));
                            try {
                                postDataParams_adv.put("distance",value);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    // set listener
                    rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                        @Override
                        public void valueChanged(Number minValue, Number maxValue) {
                            tvMin.setText(String.valueOf(minValue));
                            tvMax.setText(String.valueOf(maxValue));
                        }
                    });

                    // set final value listener
                    rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
                        @Override
                        public void finalValue(Number minValue, Number maxValue) {
                            try {
                                postDataParams_adv.put("minPrice",minValue);
                                postDataParams_adv.put("maxPrice",maxValue);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    kosher = sp_kosher.getSelectedItem().toString();//spinner
                    try {
                        postDataParams_adv.put("kosher",kosher);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    linearLayout.setVisibility(View.INVISIBLE);
                    linearLayout2.setVisibility(View.INVISIBLE);
                    linearLayout3.setVisibility(View.INVISIBLE);
                    show=false;
                    flag_txtv.setText( "false" );
                }
            }});

        search_adv.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onQueryTextSubmit(String s) {

                //flag_txtv is a flag that when its true- it means that the user clicked on advanced serach button and false - he stays on basic search .
                //case where the user chose to activate the advanced search...

                try {
                    Bundle bundle = mCallback.getBundle();
                    // We need to use a different list item layout for devices older than Honeycomb
                    int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                            android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
                    Log.i("bundle recived :", String.valueOf( bundle ) );
                    Toast.makeText(getActivity(), "text " + bundle , Toast.LENGTH_LONG).show();

                    lon = Double.parseDouble(String.valueOf( bundle.get( "lon" ) ));
                    lat = Double.parseDouble(String.valueOf( bundle.get( "lat" ) )  );

                    postDataParams_adv.put("lon",lon);
                    postDataParams_adv.put("lat",lat);

                } catch (JSONException e) {
                    Log.i("our json location :","location not working");

                    e.printStackTrace();
                }

                if (flag_txtv.getText().toString().equals( "true" ))//check if advanced or basic search . true =advanced and false=basic.
                {
                //advanced search.
                    if (search_adv.getQuery().length()> 0) //checking that there is input
                    {

                        Log.i("adv: dishname", s);
                        try {
                            postDataParams_adv.put("dishName", s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            Log.i("our json object for server ADVANCED MODE :", postDataParams_adv.toString());

                            myAsyncTask2 m = (myAsyncTask2) new myAsyncTask2(postDataParams_adv.getString("kosher").toString(), postDataParams_adv.getString("distance"), postDataParams_adv.getString("maxPrice"), postDataParams_adv.getString("minPrice"), Double.parseDouble(postDataParams_adv.getString("lon")), Double.parseDouble(postDataParams_adv.getString("lat")), postDataParams_adv.getString("dishName")).execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                   // //case where we stay on the basic search
                    //simple search =basic search -here we send to server only 1 string .

                    log.i("exec ","dish name:"+s +"lat"+lat+ "lon" +lon);
                   myAsyncTask m = (myAsyncTask) new myAsyncTask(s,lat,lon).execute(  );
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        } );
    }
  ////////////////////////////////////////////////////////////////////////////////////////////
    //asynctask class for Basic search - here we search based on location and dish name (!)
    ///////////////////////////////////////////////////////////////////////////////////

    class myAsyncTask extends AsyncTask<String, Void, String> {
        JSONParser jParser;
        JSONArray productList;
        JSONArray dataJsonArr = null;
        ProgressDialog pd;
        double lon,lat;

        myAsyncTask(String dish_name,double lat ,double lon)
        {
            // init  all the parameters .

            this.lon=lon;
            this.lat=lat;
        }
        @Override
        protected void onPreExecute()
        {
            //diolog window
            Log.i(" basic onPreExecute","lon" +lon + "lat" + lat);
            super.onPreExecute();
            productList = new JSONArray();
            jParser = new JSONParser();
            pd = new ProgressDialog( getActivity() );
            pd.setCancelable( false );
            pd.setMessage( "Searching ..." );
            pd.getWindow().setGravity( Gravity.CENTER );
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i( "in myAsyncTask  ", "in do" );
            String returnResult = getDishList_basic( url );//call the function that will send the data to server and present our resturants response
            search_finshed = true;
            return returnResult;
        }

        protected void onPostExecute(String result) {
            dish_result=result;
//            Looper.prepare();
            Log.i("dish_result",dish_result);
            try {
                sleep( 50 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pd.dismiss();
            Log.i( "caling:", "freg1" );
            try {
                callFreg1();//calling the fragment that will parse more and  present our data
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @SuppressLint({"MissingPermission", "LongLogTag"})
        public String getDishList_basic(String url) {
            Dish tempDish = new Dish();
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
               URL url2 = new URL( "http://hungrymonkey-env.vivacrpttt.eu-central-1.elasticbeanstalk.com/api/v1/basicSearch" ); // here is your URL path
              //  URL url2 = new URL( "https://www.facebook.com/" ); // here is your URL path


                //buidling our json object
                dish_inp.put( "dishName", search_adv.getQuery() ); //dish name

                dish_inp.put("lon",lon);//lon
                dish_inp.put("lat",lat);//lat

                Log.e( "my json Basic  ", dish_inp.toString() );

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
                writer.write( getPostDataString( dish_inp ) );
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


    public void OnClick(View view)//login button
    {
            Intent i = new Intent(getActivity(), LoginPage.class);
            startActivity(i);
    }

    public void OnClickSign(View view)
    {
        Intent i = new Intent(getActivity(), SignUpActivity.class);
        startActivity(i);
    }

    public void callFreg1() throws InterruptedException {



        //calling fragment first and sending all the dish results to it .
        //calling and changing to results fragment
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                FirstFragment first = new FirstFragment();
                //planting our restaurants results in fragment
                Bundle bundle2 = new Bundle();
                try {
                    sleep( 1000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bundle2.putString( "dish_list", dish_result );
                first.setArguments( bundle2 );
                ft.replace( R.id.fragment_container, first, "fregment2 tag" );
                ft.commit();
                st = false;
            }
        });
    }
    ////////////////////////////////////////////
    //This class activates the advanced search
    ///////////////////////////////////////////

    class myAsyncTask2 extends AsyncTask<String, Void, String> {

        JSONParser jParser;
        JSONArray productList;
        JSONArray dataJsonArr = null;
        ProgressDialog pd;
        String distance_adv="",price_val_adv="",kosher_adv="",Dishname_adv="";
        double lon,lat;


        myAsyncTask2(String kosher_adv,String  distance_adv,String  mini_price_val_adv,String  max_price_val_adv ,double lon,double lat,String Dishname_adv) throws JSONException {
            // init  all the parameters .
           this.distance_adv=distance_adv;
           this.price_val_adv=price_val_adv;
           this.kosher_adv=kosher_adv;
           this.Dishname_adv=Dishname_adv;
           this.lon=lon;
           this.lat=lat;
//
//            dish_inp.put( "lon", this.lon );
//            dish_inp.put( "lat", this.lat );
//            dish_inp.put( "distance_adv", this.distance_adv );
//            dish_inp.put( "price_val_adv", this.price_val_adv );
//            dish_inp.put( "kosher_adv", this.kosher_adv );
//            dish_inp.put( "Dishname_adv", this.Dishname_adv );


        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productList = new JSONArray();
            jParser = new JSONParser();
            pd = new ProgressDialog( getActivity() );
            pd.setCancelable( false );
            pd.setMessage( "Searching ..." );
            pd.getWindow().setGravity( Gravity.CENTER );
            pd.show();
        }


        protected String doInBackground(String... strings) {
            Log.i( "in myAsyncTask2 ", "in do" );
            String returnResult = null;
            try {
                returnResult = getDishList2( url_adv );//sending data to server function
            } catch (JSONException e) {
                e.printStackTrace();
            }
            search_finshed = true;
            return returnResult;
        }

        protected void onPostExecute(String result) {
            dish_result=result;
            try {
                sleep( 50 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pd.dismiss();
            Log.i( "caling:", "freg1" );
            try {
                callFreg1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @SuppressLint({"MissingPermission", "LongLogTag"})
        public String getDishList2(String url) throws JSONException {
//            Dish tempDish = new Dish();
//            String matchFound = "N";
            BufferedReader reader = null;
            String text = "";
            String nameDish;
            HttpURLConnection conn = null;

            nameDish = null;

            ////
            try {



                log.i( "in advanced  doInBackground ", "1" );
                //   dish_inp.put( "kosher_adv", this.kosher_adv );
//                dish_inp.put( "dishName", "שקשוקה ירוקה" );
//                dish_inp.put( "distance",100 );
//                dish_inp.put( "minPrice", 40);
//                dish_inp.put( "maxPrice", 100 );
//                dish_inp.put( "lat", 31.783026 );
//                dish_inp.put( "lon", 35.310381 );
                URL url2 = new URL( "http://hungrymonkey-env.vivacrpttt.eu-central-1.elasticbeanstalk.com/api/v1/advancedSearch" ); // here is your URL path

                Log.e( "sending to adv", postDataParams_adv.toString() ); //json object advanced

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
                writer.write( getPostDataString( postDataParams_adv ) ); //post the object
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                Log.i( "responseCode@:", String.valueOf( responseCode ) );
                if (responseCode == HttpsURLConnection.HTTP_OK)
                {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream() ) );
                    StringBuffer sb = new StringBuffer( "" );

                    while ((line = in.readLine()) != null) {
                       Log.i( "adv res is:",line +"\n");

                        sb.append( line );
                        break;
                    }
                    in.close();
                    return sb.toString();
                } else {
                    return new String( "false : " + responseCode );
                }
            } catch (Exception e) {
                return new String( "Exception: " + e.getMessage() );
            }
        }

        public String getPostDataString(JSONObject params) throws Exception //side cick function for some Encoding by UTF .
        {
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
    }}




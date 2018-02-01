package com.example.shanijoffe.hungry_monkey;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static com.loopj.android.http.AsyncHttpClient.log;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,View.OnClickListener, LocationListener,  GoogleApiClient.OnConnectionFailedListener {

    private static final int UNKNOW_CODE = 99;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private RecyclerView.Adapter mAdapter;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    String url = new String();
    Spinner spinner;
    ArrayAdapter<String> adapter2;
    ArrayAdapter<CharSequence> adapter;
    EditText user_name_edit;
    EditText password_edit;
    Button loginButton;
    String user = "";
    String dish_result = "";
    boolean st = false;
    String user_pass = "";
    final int CODE_REQ = 1;
    String line = "";
    final String TAG = "MyActivity";
    String user2;
    ArrayList<Dish> DishResults = new ArrayList<Dish>();
    ArrayList<Dish> filteredDishResults = new ArrayList<Dish>();
    SearchView search;
    ListView searchResults;
    View myFragmentView;
    int gsm;
    Location lastLocation;
    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    TelephonyManager tManager;
    boolean search_finshed = false;
    MyPhoneStateListener myPhoneStateListener;
    boolean show = false;
    Button btn_f;
    TextView tv;
    private ImageView iv;
    public static Activity _mainActivity;
    TextView mono;

    protected void onCreate(Bundle savedInstanceState) {

        String[] teams = {"Man Utd", "Man City", "Chelsea", "Arsenal", "Liverpool", "Totenham"};
        super.onCreate( savedInstanceState );
        _mainActivity = this;
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN );

        }



        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( R.layout.activity_main );
        mono = (TextView) findViewById( R.id.welcome_txt );


        ///  btn_f=findViewById( R.id.btn_f1 );
        Log.i( TAG, "in main activity" );
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();
            ///location
            myPhoneStateListener = new MyPhoneStateListener();
            tManager = (TelephonyManager) getSystemService( Context.TELEPHONY_SERVICE );
            tManager.listen( myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS );

        }
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                , MY_PERMISSIONS_REQUEST_LOCATION );
        loginButton = (Button) findViewById( R.id.btn_login );

        search = (SearchView) findViewById( R.id.DishSearch );
        search.setQueryHint( "הכנס שם מנה " );
        search.setOnQueryTextListener( new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String text) {
                Log.i( "MainActivity", "in onQueryTextSubmit" );

                // searchResults.setVisibility(myFragmentView.VISIBLE);
                myAsyncTask m = (myAsyncTask) new myAsyncTask().execute( text );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
//                adapter2.getFilter().filter(text);
                Log.i( TAG, "HAYOOO" );
                return false;
            }
        } );
        Log.i( "sup", "sup" );
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!st) {
            fregment_BasicRes f1 = new fregment_BasicRes();
            ft.replace( R.id.fragment_container, f1 );
            ft.commit();
//            btn_f.setText( "load f2" );
            st = true;
        }

        Bundle user_det = getIntent().getExtras();
        if (user_det != null) {
            String name_res = user_det.getString( "user_name" );
            Log.i( "the name i got:", name_res );
            mono.setText( "welcome " + name_res );
        } else if (user_det == null) {
           // Toast.makeText( this, "Bundle is null", Toast.LENGTH_SHORT ).show();
        }


    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        /* Get the Signal strength from the provider, each time there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged( signalStrength );
            if (null != signalStrength && signalStrength.getGsmSignalStrength() != UNKNOW_CODE) {
                int signalStrengthPercent = signalStrength.getGsmSignalStrength();
                // System.out.println(signalStrength);

                gsm = signalStrength.getGsmSignalStrength();
                //Log.i( "gsm comes here","in gsm changed");
            }
        }
    }

    public void onClickFreg1(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!st) {
            fregment_BasicRes f1 = new fregment_BasicRes();
            ft.replace( R.id.fragment_container, f1 );
            ft.commit();
            st = true;
        } else {
            fregment2 fr2 = new fregment2();
            ft.replace( R.id.fragment_container, fr2 );
            ft.commit();

            st = false;

        }
    }

    public void OnClickSign(View view) {
        log.i( "hi", "sup" );
        Log.e( "MainActivity", "fuka u " );
        Log.i( "line is:", line );
        if (line == line) {
            Intent i = new Intent( this, SignUpActivity.class );
            String s = "משתשמש חדש  ";
            i.putExtra( "myString", s );
            startActivityForResult( i, CODE_REQ );
        }
    }

    public void OnClickShowAdvancedSearch(View view) {
        LinearLayout linearLayout = (LinearLayout) findViewById( R.id.adv_leyout );
        LinearLayout linearLayout2 = (LinearLayout) findViewById( R.id.adv_leyout2 );
        LinearLayout linearLayout3 = (LinearLayout) findViewById( R.id.adv_leyout3 );
        if (show == false) {

            linearLayout.setVisibility( View.VISIBLE );
            linearLayout2.setVisibility( View.VISIBLE );
            linearLayout3.setVisibility( View.VISIBLE );
            show = true;

        } else {
            linearLayout.setVisibility( View.INVISIBLE );
            linearLayout2.setVisibility( View.INVISIBLE );
            linearLayout3.setVisibility( View.INVISIBLE );
            show = false;

        }
    }

    public void OnClick(View view)//login button
    {
        log.i( "hi", "sup" );
        //new SendPostRequest().execute();
        Log.i( "line is:", line );
        if (line == line) {
            Intent i = new Intent( this, LoginPage.class );
            String s = line;
            i.putExtra( "myString", s );
            startActivityForResult( i, CODE_REQ );
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i( "onLocationChanged", "onLocationChanged" );
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Log.e( "latitude", location.getLatitude() + "" );
        Log.e( "longitude", location.getLongitude() + "" );
        String MNO = "";

        SharedPreferences settings0 = getSharedPreferences( "MNO", Context.MODE_PRIVATE );
        MNO = settings0.getString( "MNO", "" );


        try {
            jsonObject.put( "MNO", MNO );
            jsonObject.put( "latitude", location.getLatitude() );
            jsonObject.put( "longitude", location.getLongitude() );
            jsonArray.put( jsonObject );
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.e( "request", jsonArray.toString() );
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i( "onConnected", " in onConnected" );
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient );
            createLocationRequest();
            // Toast.makeText( this, "Last location " + lastLocation.getAltitude() + ", " + lastLocation.getLongitude(), Toast.LENGTH_SHORT ).show();

        } else {
            Toast.makeText( this, "No permissions", Toast.LENGTH_SHORT ).show();
        }

    }

    @SuppressWarnings({"ResourceType"})
    protected void createLocationRequest() {

        Log.i( "createLocationRequest", " in createLocationRequest" );

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval( 60000 );
        //  mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        mLocationRequest.setSmallestDisplacement( (float) 50.00 );

        //    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this );
        Log.i( "createLocationRequest", " in createLocationRequest" );
        // LocationServices.FusedLocationApi.requestLocationUpdates( mGoogleApiClient, mLocationRequest, (LocationListener) this );
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w( "MainActivity", "Permissions was granteed" );

                } else {
                    Log.e( "MainActivity", "Permissions was denied" );

                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void callFreg1() throws InterruptedException {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FirstFragment first = new FirstFragment();
// set Fragmentclass Arguments

        Bundle bundle2 = new Bundle();
        sleep( 1000 );
        bundle2.putString( "dish_list", dish_result );
        Log.i( "sending to freg1 :", dish_result );
        first.setArguments( bundle2 );
        ft.replace( R.id.fragment_container, first, "fregment2 tag" );
        ft.commit();
        st = false;
    }

    class myAsyncTask extends AsyncTask<String, Void, String> {
        JSONParser jParser;
        JSONArray productList;
        JSONArray dataJsonArr = null;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productList = new JSONArray();
            jParser = new JSONParser();
            pd = new ProgressDialog( MainActivity.this );
            pd.setCancelable( false );
            pd.setMessage( "Searching ..." );
            pd.getWindow().setGravity( Gravity.CENTER );
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i( "in do ", "in do" );
            String returnResult = getDishList( url );
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
        public String getDishList(String url) {
            Dish tempDish = new Dish();
            String matchFound = "N";
            BufferedReader reader = null;
            String text = "";
            String nameDish;
            HttpURLConnection conn = null;

            nameDish = null;

            ////
            try {
                log.i( "in doInBackground ", "1" );
                URL url2 = new URL( "http://hungrymonkey-env.vivacrpttt.eu-central-1.elasticbeanstalk.com/api/v1/basicSearch" ); // here is your URL path
                JSONObject dish_inp = new JSONObject();
                dish_inp.put( "dishName", search.getQuery() );

                Log.e( "params", dish_inp.toString() );

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
                int responseCode = conn.getResponseCode();
                Log.i( "responseCode@:", String.valueOf( responseCode ) );

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream() ) );
                    StringBuffer sb = new StringBuffer( "" );

                    while ((line = in.readLine()) != null) {
//                        Log.i( "line:", line );
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
            /////
//            try {
//                Log.i( "123", "1" );
//                URL url2 = new URL(" https://www.facebook.com/" );//temp url
//
//                // Send POST data request
//                conn = (HttpURLConnection) url2.openConnection();
//                conn.setReadTimeout(50000 /* milliseconds */);
//                conn.setConnectTimeout(50000 /* milliseconds */);
//                conn.setRequestMethod("POST");
//                conn.setDoOutput( true );
//                reader = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//                Log.i( "123", "2" );
//                JSONObject res = new JSONObject();
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                JSONObject json=new JSONObject( );
//                json.put( "dishName","שקשוקה" );
//                writer.write(json.toString());
//                writer.flush();
//                writer.close();
//                os.close();
//
//                // Read Server Response
//                while ((line = reader.readLine()) != null) {
//                    // Append server response in string
//                    sb.append( line + "\n" );
//                    res.put( "name", line.toString() );
//                }
//                Log.i( "res is :", res.toString() );
//                text = sb.toString();
//
//                Log.i( "onPostExecute", " in onPostExecute  my json from server is :" + text );
//                Log.i( "jsontext:", text );
//
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            }
//
//            return text;
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





        public boolean isTablet() {
            int display_mode = _mainActivity.getResources().getConfiguration().orientation;
            if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
                return false;
            }
            return true;
        }
    }

package com.example.shanijoffe.hungry_monkey;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import static com.loopj.android.http.AsyncHttpClient.log;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,View.OnClickListener, LocationListener,  GoogleApiClient.OnConnectionFailedListener {

    private static final int UNKNOW_CODE = 99;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private RecyclerView.Adapter mAdapter;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    String url=new String();
    Spinner spinner;
    ArrayAdapter<String> adapter2;
    ArrayAdapter<CharSequence> adapter;
    EditText user_name_edit;
    EditText password_edit;
    Button loginButton;
    String user="";
    String   dish_result="";
    boolean st=false;
    String user_pass="";
    final int CODE_REQ=1;
    String line="";
    final String TAG = "MyActivity";
    String user2;
    ArrayList<Dish> DishResults =new ArrayList<Dish>();
    ArrayList<Dish> filteredDishResults =new ArrayList<Dish>();
    SearchView search;
    ListView searchResults;
    View myFragmentView;
    int gsm;
    Location  lastLocation;
    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    TelephonyManager tManager;
    boolean search_finshed =false;
    MyPhoneStateListener myPhoneStateListener;
    boolean show=false;
    Button btn_f;
    protected void onCreate(Bundle savedInstanceState)
    {

        String[] teams={"Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham"};
        super.onCreate( savedInstanceState );
        if (Build.VERSION.SDK_INT < 16)
        {
            getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN );

        }

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( R.layout.activity_main );
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
            tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            tManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                , MY_PERMISSIONS_REQUEST_LOCATION);
        //
//        spinner=(Spinner) findViewById( R.id.sp_kosher );
//        adapter=ArrayAdapter.createFromResource( this,R.array.planets_array ,android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
//        spinner.setAdapter( adapter );
//        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("MainActivity"," MainActivity ");
//                Toast.makeText( getBaseContext(),adapterView.getItemAtPosition(i)+"isSalected",Toast.LENGTH_SHORT ).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        } );
        loginButton = (Button) findViewById( R.id.btn_login );
        search=(SearchView)findViewById( R.id.DishSearch );
        search.setQueryHint("הכנס שם מנה ");
//        searchResults =(ListView)findViewById( R.id.listview_search );
//        adapter2=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,teams);
//        searchResults.setAdapter( adapter2 );
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String text)
            {
                // searchResults.setVisibility(myFragmentView.VISIBLE);
                myAsyncTask m= (myAsyncTask) new myAsyncTask().execute(text);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {
//                adapter2.getFilter().filter(text);
                Log.i(TAG,"HAYOOO");
                return false;
            }
        });
        Log.i("sup","sup");
        FragmentManager fm =getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(!st)
        {
            fregment_BasicRes f1 = new fregment_BasicRes();
            ft.replace( R.id.fragment_container, f1 );
            ft.commit();
//            btn_f.setText( "load f2" );
            st = true;
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
        public void onSignalStrengthsChanged(SignalStrength signalStrength)
        {
            super.onSignalStrengthsChanged(signalStrength);
            if (null != signalStrength && signalStrength.getGsmSignalStrength() != UNKNOW_CODE) {
                int signalStrengthPercent = signalStrength.getGsmSignalStrength();
                // System.out.println(signalStrength);


                gsm=signalStrength.getGsmSignalStrength();
                //Log.i( "gsm comes here","in gsm changed");
            }
        }
    }

    public void onClickFreg1(View view)
    {
        FragmentManager fm =getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(!st)
        {
            fregment_BasicRes f1=new fregment_BasicRes();
            ft.replace( R.id.fragment_container,f1 );
            ft.commit();
           // btn_f.setText( "load f2" );
            st=true;
        }
        else
        {
            fregment2 fr2=new fregment2();
            ft.replace( R.id.fragment_container,fr2 );
            ft.commit();
          //  btn_f.setText( "load f1" );
            st=false;

        }
    }
    public void OnClickSign(View view)
    {
        log.i( "hi", "sup" );
        Log.e("MainActivity","fuka u ");
        Log.i("line is:",line);
        if (line == line)
        {
            Intent i = new Intent( this, SignUpActivity.class );
            String s = "משתשמש חדש  ";
            i.putExtra( "myString", s );
            startActivityForResult( i, CODE_REQ );
        }
    }

    public void OnClickShowAdvancedSearch(View view)
    {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.adv_leyout);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.adv_leyout2);
        LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.adv_leyout3);
        if (show==false)
        {

            linearLayout.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
            linearLayout3.setVisibility(View.VISIBLE);
            show=true;

        }
        else
        {
            linearLayout.setVisibility(View.INVISIBLE);
            linearLayout2.setVisibility(View.INVISIBLE);
            linearLayout3.setVisibility(View.INVISIBLE);
            show=false;

        }
    }
    public void OnClick(View view)//login button
    {
        log.i( "hi", "sup" );
        //new SendPostRequest().execute();
        Log.i("line is:",line);
        if (line == line)
        {
            Intent i = new Intent( this, LoginPage.class );
            String s = line ;
            i.putExtra( "myString", s );
            startActivityForResult( i, CODE_REQ );
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.i("onLocationChanged","onLocationChanged");
        double lat  =location.getLatitude();
        double lon =location.getLongitude();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Log.e("latitude", location.getLatitude() + "");
        Log.e("longitude", location.getLongitude() + "");
        String MNO="";

        SharedPreferences settings0 = getSharedPreferences("MNO",Context.MODE_PRIVATE);
        MNO = settings0.getString("MNO", "");


        try
        {
            jsonObject.put( "MNO", MNO );
            jsonObject.put( "latitude", location.getLatitude() );
            jsonObject.put( "longitude", location.getLongitude() );
            jsonArray.put( jsonObject );
        }


        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.e("request", jsonArray.toString());
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
    public void onConnected(@Nullable Bundle bundle)
    {
        Log.i("onConnected"," in onConnected");
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

        Log.i("createLocationRequest"," in createLocationRequest");
        System.out.printf("gggg");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval( 60000 );
        //  mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        mLocationRequest.setSmallestDisplacement( (float) 50.00 );

        //    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this );
        Log.i("createLocationRequest"," in createLocationRequest");
        // LocationServices.FusedLocationApi.requestLocationUpdates( mGoogleApiClient, mLocationRequest, (LocationListener) this );
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w("MainActivity", "Permissions was granteed");

                } else {
                    Log.e("MainActivity", "Permissions was denied");

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

        FragmentManager fm =getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("edttext", "From Activity");
// set Fragmentclass Arguments
        Bundle bundle2 = new Bundle();
        bundle2.putString("message", dish_result );
        Log.i("i, sending to freg2 :",dish_result);
        fregment2 f2=new fregment2();
        f2.setArguments( bundle2 );
        ft.replace( R.id.fragment_container,f2,"fregment2 tag" );
        ft.commit();
        fregment2 tp = (fregment2) getFragmentManager().findFragmentByTag("fregment2 tag");
        sleep(1000);
//        tp.changeText();
//        btn_f.setText( "load f1" );
        st=false;


    }

    class myAsyncTask extends AsyncTask<String, Void, String>
    {
        JSONParser jParser;
        JSONArray productList;
        JSONArray dataJsonArr = null;
        ProgressDialog pd ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productList=new JSONArray();
            jParser = new JSONParser();
            pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Searching...");
            pd.getWindow().setGravity( Gravity.CENTER);
            pd.show();
        }
        @Override
        protected String doInBackground(String... strings)
        {

            Log.i("in do ","in do");
            String returnResult = getDishList(url);
            search_finshed=true;

            return returnResult;
        }
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pd.dismiss();
            Log.i("caling:","freg1");
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
            JSONObject json ;
            nameDish = null;
            try {
                Log.i("123","1");
                URL url2 = new URL( "http://echo.jsontest.com/key/value/one/two " );
                // Send POST data request
                URLConnection conn = url2.openConnection();
                conn.setDoOutput( true );
                reader = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
                StringBuilder sb = new StringBuilder();
                String line = null;
                Log.i("123","2");
                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append( line + "\n" );
                }
                text = sb.toString();

                Log.i("onPostExecute"," in onPostExecute  my json from server is :" + text);

//                lastLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient );
//                jsonObject.put( "latitude", lastLocation.getLatitude() );
//                jsonObject.put( "longitude", lastLocation.getLongitude() );
//                jsonObject.put( "dish",text.toString() );

                json=new JSONObject( text );
                Log.i("jsontext:",text);
                dish_result= (String) json.get("one");
                Log.i("jsontext2:",dish_result);

                // get the array of users
                dataJsonArr = (JSONArray) json.get( "Dishes" );
                // loop through all users
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = (JSONObject) dataJsonArr.get( i );

                    // Storing each json item in variable
                    nameDish = (String) c.get( "nameDish" );

                    // show the values in our logcat
                    Log.e( TAG, "nameDish: " + nameDish );
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return nameDish;
        }
    }
    class SearchResultsAdapter extends BaseAdapter
    {
        private LayoutInflater layoutInflater;

        private ArrayList<Dish> productDetails=new ArrayList<Dish>();
        int count;
        Typeface type;
        Context context;

        //constructor method
        public SearchResultsAdapter(Context context, ArrayList<Dish> product_details) {

            layoutInflater = LayoutInflater.from(context);

            this.productDetails=product_details;
            this.count= product_details.size();
            this.context = context;
            type= Typeface.createFromAsset(context.getAssets(),"fonts/book.TTF");

        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int arg0) {
            return productDetails.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            ViewHolder holder;
            Dish tempProduct = productDetails.get(position);

            if (convertView == null)
            {
                convertView = layoutInflater.inflate(R.layout.single_dish_item, null);
                holder = new ViewHolder();
                holder.Dish_name = (TextView) convertView.findViewById(R.id.Dish_name);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.Dish_name.setText("hhh");


            return convertView;
        }

        class ViewHolder
        {
            TextView Dish_name;
        }

    }

}

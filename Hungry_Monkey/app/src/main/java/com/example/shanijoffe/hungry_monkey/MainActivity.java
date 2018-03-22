package com.example.shanijoffe.hungry_monkey;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MainActivity extends AppCompatActivity   implements fregment_BasicRes.OnHeadlineSelectedListener, GoogleApiClient.ConnectionCallbacks, LocationListener,  GoogleApiClient.OnConnectionFailedListener {

    private static final int UNKNOW_CODE = 99;//for intents
    final int CODE_REQ = 1;//for intents
    GoogleApiClient mGoogleApiClient;
    LocationRequest  mLocationRequest;;
    private RecyclerView.Adapter mAdapter;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    //Spinner spinner;
    Button loginButton;
    boolean st = false;//status for fregments
    boolean connectedFlag=false;//location
    final String TAG = "MyActivity";//for logs
    SearchView search;
    Location lastLocation;
    double lat,lon;

    boolean show = false;//for the advanced search button flag

    public static Activity _mainActivity;
    TextView mono;// title for main activity
    public static TextView sss;//flag to seperate between advanced and basic search


    protected void onCreate(Bundle savedInstanceState) {

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

        //connecting  our components
        FloatingActionButton backToMain =(FloatingActionButton)findViewById(R.id.home_btn);
        mono = (TextView) findViewById( R.id.welcome_txt );
        sss =(TextView)findViewById( R.id.sss );
        loginButton = (Button) findViewById( R.id.btn_login );
        search = (SearchView) findViewById( R.id.DishSearch );
        /////////////////////////

        Log.i( TAG, "in main activity" );

        //establish google api connection ...

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( (GoogleApiClient.ConnectionCallbacks) this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();


        }
        //ask user permission
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                , MY_PERMISSIONS_REQUEST_LOCATION );

        search.setQueryHint( "הכנס שם מנה " );
        Log.i( "MainActivity", "in onQueryTextSubmit" );

        if(connectedFlag==true)// build the req in case we are connected
        {

            createSearch(); //building the search request.
        }

        //adjusting fragments to main activity
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //calling basic search fragment
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
            mono.setText( getString(R.string.welcome)+ " " + name_res );
        }

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent( getBaseContext(), MainActivity.class );
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public void OnClickSign(View view) {
       // Log.e( "OnClickSign", "here " );

        Intent i = new Intent( this, SignUpActivity.class );
        String s = getString(R.string.newUser);
        i.putExtra( "myString", s );
        startActivityForResult( i, CODE_REQ );

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

        Intent i = new Intent( this, LoginPage.class );
        startActivityForResult( i, CODE_REQ );

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i( "onLocationChanged", "onLocationChanged" );
        lat = location.getLatitude();
        lon = location.getLongitude();
        fregment_BasicRes search_fragent = new fregment_BasicRes();
        Bundle args = new Bundle();
        args.putString( "lon", String.valueOf( lon ) );
        args.putString( "lat", String.valueOf( lat ) );
        search_fragent.setArguments(args);

    }




    public void onClick(View view) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void onConnected(@Nullable Bundle bundle)
    {
        Log.i("onConnected"," in onConnected");

        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {

            lastLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient );
            createLocationRequest();
            if(lastLocation!=null)
            {
                Toast.makeText( this, "Last location " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude(), Toast.LENGTH_SHORT ).show();
                log.i("onConnected","search");
                connectedFlag=true;
                fregment_BasicRes search_fragent = new fregment_BasicRes();
                lon=lastLocation.getLongitude();
                lat=lastLocation.getLatitude();
                Bundle args = new Bundle();
                args.putString( "lon", String.valueOf( lon ) );
                args.putString( "lat", String.valueOf( lat ) );
                search_fragent.setArguments(args);

                log.i("in connected sending dish name to frag : ","lon"+ lon+"lat"+ lat);

            }
            else{
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                Log.i("no last location","no last location");
                alertDialog.setTitle(getString(R.string.locMsgTitle));
                alertDialog.setMessage(getString(R.string.locMsg));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);

                            }
                        });
                alertDialog.show();
            }

        } else {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , MY_PERMISSIONS_REQUEST_LOCATION );
        }

        Toast.makeText( this, "bulding search", Toast.LENGTH_SHORT ).show();

    }


    public void createSearch()
    {
        search.setOnQueryTextListener( new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String text) { //on click search
             //   Log.i("basic onQueryTextSubmit","im here66");

                //send the dish name and location to exec fragment- this fragment will receive those inputs and route it to either basic/advanced search depending on the users requests .
                try {

                    if (search.getQuery().length()> 0) { //check that the user entered dish name.

                        //sending the data to search fragment
                        fregment_BasicRes search_fragent = new fregment_BasicRes();
                        Bundle args = new Bundle();

                        args.putString( "lon", String.valueOf( lon ) );
                        args.putString( "lat", String.valueOf( lat ) );
                        search_fragent.setArguments(args);
                      //  log.i("sending dish name to frag : ",text+"lon"+ lon+"lat"+ lat);
                        ////
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String text) {
//                adapter2.getFilter().filter(text);
                return false;
            }
        } );
    }
    @SuppressWarnings({"ResourceType"})




    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(6000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.w( "MainActivity", "Permissions was granteed" );
                } else
                    {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle(getString(R.string.locMsgTitle));
                    alertDialog.setMessage(getString(R.string.locMsg));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("here","onRequestPermissionsResult");
                                    finish();
                                    System.exit(0);

                                }
                            });
                    alertDialog.show();
                }
            }
        }
    }

    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public Bundle getBundle() {
        fregment_BasicRes search_fragent = new fregment_BasicRes();
        Bundle args = new Bundle();
        if(lastLocation!=null)
        {

            Toast.makeText( this, "Last location " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude(), Toast.LENGTH_SHORT ).show();
            log.i("onConnected","search");
            connectedFlag=true;

            lon=lastLocation.getLongitude();
            lat=lastLocation.getLatitude();
            args.putString( "lon", String.valueOf(lon ) );
            args.putString( "lat", String.valueOf( lat ) );
            search_fragent.setArguments(args);
            //log.i("in connected sending dish name to frag : ","lon"+ lon+"lat"+ lat);
            return args;
        }
        else{

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle(getString(R.string.locMsgTitle));
            alertDialog.setMessage(getString(R.string.locMsg));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("here","getBundle");
                            finish();
                            System.exit(0);

                        }
                    });
            alertDialog.show();
            String  computerSelected = getString(R.string.locPrompt);
            boolean locationOn = false;

            args.putString( "lon", String.valueOf( lon ) );
            args.putString( "lat", String.valueOf( lat ) );
            search_fragent.setArguments(args);
            return null;

        }
    }
}
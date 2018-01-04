package com.example.shanijoffe.hungry_monkey;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity
{

    String url=new String();
    Spinner spinner;
    ArrayAdapter<String> adapter2;
    ArrayAdapter<CharSequence> adapter;
    EditText user_name_edit;
    EditText password_edit;
    Button loginButton;
    String user="";
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
    protected void onCreate(Bundle savedInstanceState)
    {

        String[] teams={"Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham"};
        super.onCreate( savedInstanceState );
        if (Build.VERSION.SDK_INT < 16)
        {
            getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN );

        }

        Log.i( TAG, "in main activity" );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( R.layout.activity_main );
        spinner=(Spinner) findViewById( R.id.sp_kosher );
        adapter=ArrayAdapter.createFromResource( this,R.array.planets_array ,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
        spinner.setAdapter( adapter );
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("MainActivity","fuka u ");
                Toast.makeText( getBaseContext(),adapterView.getItemAtPosition(i)+"isSalected",Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );

       loginButton = (Button) findViewById( R.id.btn_login );
       search=(SearchView)findViewById( R.id.DishSearch );
       searchResults =(ListView)findViewById( R.id.listview_search );
        adapter2=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,teams);
       searchResults.setAdapter( adapter2 );
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {
                adapter2.getFilter().filter(text);
                Log.i(TAG,"HAYOOO");
                return false;
            }
        });
        Log.i("sup","sup");

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
       View v1 = findViewById(R.id.txtv_dis);
        View v2 = findViewById(R.id.txtv_price);
        View v3 = findViewById(R.id.txtv_kosher);
        View v4 = findViewById(R.id.sk_dis);
        View v5 = findViewById(R.id.sk_price);
        View v6 = findViewById(R.id.sp_kosher);

        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.VISIBLE);
        v3.setVisibility(View.VISIBLE);
        v4.setVisibility(View.VISIBLE);
        v5.setVisibility(View.VISIBLE);
        v6.setVisibility(View.VISIBLE);

    }


    public void OnClick(View view)
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
    class myAsyncTask extends AsyncTask<String, Void, String>
    {
        JSONParser jParser;
        JSONArray productList;
        JSONArray dataJsonArr = null;

        String textSearch;
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
            return returnResult;
        }
        protected void onPostExecute(String result)
        {

            super.onPostExecute(result);


                //calling this method to filter the search results from productResults and move them to
                //filteredProductResults
              //  filterProductArray(textSearch);
                searchResults.setAdapter(new SearchResultsAdapter(MainActivity.this,DishResults ));
                pd.dismiss();

        }
        public String getDishList(String url) {
            Dish tempDish = new Dish();
            String matchFound = "N";
            BufferedReader reader = null;
            String text = "";
            String nameDish;
            JSONObject json ;
            nameDish = null;
            try {

                URL url2 = new URL( "http://echo.jsontest.com/key/value/one/two " );
                // Send POST data request

                URLConnection conn = url2.openConnection();
                conn.setDoOutput( true );

                reader = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append( line + "\n" );
                }


                text = sb.toString();
                Log.i("MainActivity ",text);
                // get json string from url
                json=new JSONObject( text );
                text= (String) json.get("one");

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

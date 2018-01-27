package com.example.shanijoffe.hungry_monkey;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.Vector;

import static java.lang.Thread.sleep;


public class FirstFragment extends Fragment {

    JSONObject fakedata;
    String[] teams={""};
    String name_res="",name_dish="",Kosher="",rest_address="",rest_desc="";
    JSONObject rest_location = new JSONObject();
    JSONObject menu = new JSONObject();
    View view;
    Button firstButton;
    TextView nres;
    Button det;
    String myValue;
    Button show_det_for_dish;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);

        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState){

        Bundle bundle = this.getArguments();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        myValue = bundle.getString("name_res");
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        new myAsyncTask();
//
//        //temp data to test my list view
//        Log.i("my fake name_res  is:",name_res);
      Vector<HashMap<String,String>> vector = new Vector<>();
//        HashMap<String,String> hashMap = new HashMap<>();
//        hashMap.put("name_res",myValue);
//        hashMap.put("Dish_name","sushi");
//        HashMap<String,String> hashMap2 = new HashMap<>();
//        hashMap2.put("name_res",myValue);
//        hashMap2.put("Dish_name","sushi");
//        vector.add(hashMap);
//        vector.add(hashMap2);
//        HashMap<String,String> hashMap3 = new HashMap<>();
//        hashMap3.put("name_res",myValue);
//        hashMap3.put("Dish_name","sushi");
//        vector.add(hashMap3);
//        HashMap<String,String> hashMap4 = new HashMap<>();
//        hashMap4.put("name_res",myValue);
//        hashMap4.put("Dish_name","sushi");
//        vector.add(hashMap4);
//        HashMap<String,String> hashMap5 = new HashMap<>();
//        hashMap5.put("name_res",myValue);
//        hashMap5.put("Dish_name","sushi");
//        vector.add(hashMap5);
//        HashMap<String,String> hashMap6 = new HashMap<>();
//        hashMap6.put("name_res",myValue);
//        hashMap6.put("Dish_name","sushi");
//        vector.add(hashMap6);
//        Log.i("vector:", String.valueOf( vector));
        custom_adapter aa=new custom_adapter(getContext(),R.layout.single_dish_item,vector);
        listView.setAdapter(aa);
        aa.notifyDataSetChanged();


    }
    class myAsyncTask extends AsyncTask<String, Void, String> {
        JSONParser jParser;
        JSONArray productList;
        JSONArray dataJsonArr = null;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i( "FirstFragment", "in onPreExecute" );
        }

        @Override
        protected String doInBackground(String... strings) {


            BufferedReader reader = null;
            Log.i( "in do ", "in do" );
            Log.i( "123", "1" );
            URL url2 = null;//temp url
            try {
                url2 = new URL( "http://hungrymonkey-env.vivacrpttt.eu-central-1.elasticbeanstalk.com/api/v1/basicSearch" );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // Send POST data request
            URLConnection conn = null;
            try {
                conn = url2.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.setDoOutput( true );
            try {
                reader = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            String line = null;
            JSONObject res = new JSONObject();

            // Read Server Response
            try {
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append( line + "\n" );
                    res.put( "name", line.toString() );
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i( "res is :", res.toString() );


            return line;
        }

    }

        protected void onPostExecute(String result) throws JSONException {
            JSONObject jObj = new JSONObject(result);
            Log.i("my res is :",result);

//            userName_res = jObj.getString("message");
//            token=jObj.getString("token");
//            Log.i("userName_res)",userName_res);
//
//            Log.i( "response from server :", "name is "+userName_res+"token is "+token );
//            Bundle user_det = new Bundle();
//            user_det.putString("user_name",userName_res);//here i shpuld insert user_name
//            Intent i = new Intent( getApplicationContext(), MainActivity.class );
//            i.putExtras(user_det);
//            startActivityForResult( i );

        }


    }



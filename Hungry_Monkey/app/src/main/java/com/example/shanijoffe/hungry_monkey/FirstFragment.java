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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.loopj.android.http.AsyncHttpClient.log;
import static java.lang.Thread.sleep;


public class FirstFragment extends Fragment {

    JSONObject location_res_jsn,_source,inner_hits,menuJ,hits_1;
    String[] teams={""};
    String name_res="",name_dish="",kosher="",rest_address="",dish_desc="",location_res_str="",price_dish="";
    JSONObject rest_location = new JSONObject();
    JSONObject menu = new JSONObject();
    View view;
    Button firstButton;
    TextView nres;
    Button det;
    String myValue;
    Button show_det_for_dish;
    JSONArray menu_hits_jsonArray=null, jsonarray;

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
        myValue = bundle.getString("dish_list");

        ListView listView = (ListView) view.findViewById(R.id.list_view);

        location_res_jsn=null;

        Vector<HashMap<String,String>> vector = new Vector<>();

        try {


            jsonarray = new JSONArray(myValue);
             Log.i("f"," \n\n");
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject js3 = jsonarray.getJSONObject(i);
                System.out.println("json array at place "+i+  js3.toString() +"\n");


            }
            Log.i("dish_list in if", String.valueOf( jsonarray ) );
            Log.i("f"," \n\n");
            List<Map<String , String>> myMap  = new ArrayList<Map<String,String>>();
            Log.i("jsonArray length", String.valueOf( jsonarray.length() ) );
            for(int i=0; i<jsonarray.length(); i++)
            {
                Log.i("index", String.valueOf( i ) );
                HashMap<String,String> hashMap_ = new HashMap<>();
                JSONObject obj = jsonarray.getJSONObject(i);
                _source= new JSONObject( );
                _source=obj.getJSONObject( "_source" );
                log.i("_source", String.valueOf( _source ));

                //rest_address
                rest_address = _source.getString("rest_address");
                System.out.println("rest_address"+ rest_address);
                hashMap_.put("rest_address",rest_address);
                ///name_res
                name_res = _source.getString("rest_name");
                System.out.println("name_res"+name_res);
                hashMap_.put("rest_name",name_res);
                //location res
                location_res_str=_source.getString( "rest_location");
                location_res_jsn= new JSONObject( location_res_str );
                System.out.println("location_res_jsn"+ location_res_jsn.toString());
                hashMap_.put("rest_location", String.valueOf( location_res_jsn ) );

                ///kosher
                kosher=_source.getString("Kosher");
                System.out.println("kosher" + kosher);
                hashMap_.put("Kosher", kosher );
                //
                inner_hits=new JSONObject( );
                inner_hits=obj.getJSONObject( "inner_hits" );
                log.i("inner_hits", String.valueOf( inner_hits ));
                menu=new JSONObject(  );
                menu=inner_hits.getJSONObject("menu");

                log.i("menu", String.valueOf( menu ));
                hits_1=new JSONObject(  );
                hits_1=menu.getJSONObject("hits"  );
                String hits_arr=hits_1.getString( "hits" );
                System.out.println("hits_arr" + hits_arr);
                menu_hits_jsonArray = new JSONArray(hits_arr);
            //    log.i("menu_hits_jsonArray", String.valueOf( menu_hits_jsonArray ));
                Log.i("name restuartant:",name_res);
                for(int j=0; j<menu_hits_jsonArray.length(); j++)
                {
                    JSONObject obj2 = menu_hits_jsonArray.getJSONObject(j);
                    JSONObject _source2= new JSONObject();
                    _source2=obj2.getJSONObject( "_source" );
                    dish_desc=_source2.getString("dish_description");
                    hashMap_.put("dish_description", dish_desc );
                    name_dish=_source2.getString("dish_name");
                    hashMap_.put("dish_name", name_dish );
                    price_dish=_source2.getString("dish_price");
                    hashMap_.put("dish_price", price_dish );
                    System.out.println("dish_desc" + dish_desc);
                    System.out.println("name_dish" + name_dish);
                    System.out.println("price_dish" + price_dish);


                }
                vector.add(hashMap_);
            }
            log.i("inner_hits", String.valueOf( inner_hits ));
            for (Map<String, String> map : myMap)
            {
                System.out.println(map.get("rest_address"));
                System.out.println(map.get("rest_location"));
                System.out.println(map.get("rest_name"));
                System.out.println(map.get("Kosher"));
                System.out.println(map.get("dish_description"));
                System.out.println(map.get("dish_name"));
                System.out.println(map.get("dish_price"));
            }
            System.out.println("The vector is: " + vector);
            System.out.println("The myMap is: " + myMap);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

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



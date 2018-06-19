package com.example.shanijoffe.hungry_monkey;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.example.shanijoffe.hungry_monkey.R.id.filter_by_loc;
import static com.example.shanijoffe.hungry_monkey.R.id.filter_by_price;
import static com.example.shanijoffe.hungry_monkey.R.id.return_toMain_btn;
import static com.loopj.android.http.AsyncHttpClient.log;

public class searchResultsParser extends Fragment implements Comparable  {


    //parsing fragment


    JSONObject location_res_jsn,_source,inner_hits,menuJ,hits_1;
    String[] teams={""};
    String name_res="" ,imgUrl="",name_dish="",kosher="",rest_address="",dish_desc="",location_res_str="",price_dish="",dish_id_inRest="";
    JSONObject rest_location = new JSONObject();
    JSONObject menu = new JSONObject();
    View view;
    Button firstButton;
    TextView nres;
    Button det;
    String myValue;
    int flag_btn=0;
    FloatingActionButton fav;
    Button show_det_for_dish;
    ListView listView;
    HashMap<String, String> hashMap_;
    custom_adapter aa;
    TextView  flag_price,flag_loc;
    Vector vector=null;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    JSONArray menu_hits_jsonArray=null, jsonarray;
    String[] users_favs_dishes ;//for the id of the dishes the user would like to save as favorites.


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

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        Bundle bundle = this.getArguments();
        flag_price=(TextView)view.findViewById( R.id.txtv_flag_price );
        flag_loc=(TextView)view.findViewById( R.id.txtv_flag_loc );
        flag_price.setText( "false" );
        listView = (ListView) view.findViewById( R.id.list_view );


        settings=this.getActivity().getSharedPreferences( "myPrefsFile",0  );
        editor=settings.edit();

        myValue = bundle.getString( "dish_list" );
        log.i(" in first fragment my list before parsing",myValue);

        location_res_jsn = null;
        hashMap_ = null;
        vector = new Vector<>();
        vector=parseMyList( myValue );
        aa = new custom_adapter( getContext(), R.layout.single_dish_item, vector,flag_btn );
        // Collections.sort(vector, new custom_adapter( getContext(), R.layout.single_dish_item, vector,flag_btn));
        listView.setAdapter( aa );
        // filter_by_price button

        FloatingActionButton return_to_main = (FloatingActionButton) getActivity().findViewById( return_toMain_btn );
        return_to_main.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), navigation_HM.class);
                 startActivity(i);
            }

        } );


        FloatingActionButton b = (FloatingActionButton) getActivity().findViewById( filter_by_price );
        b.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                log.i( "firstFragment", "onOptionsItemSelected" );
                flag_btn=1;
                flag_price.setText( "true" );
                Comparator<String> ALPHABETICAL_ORDER1 = new Comparator<String>() {
                    public int compare(String object1, String object2) {
                        int res = String.CASE_INSENSITIVE_ORDER.compare( object1.toString(), object2.toString() );
                        return res;
                    }
                };

                //  Arrays.sort(vector.toArray(), (Comparator<? super Object>) vector.get( Integer.parseInt( "price_dish" ) ) );

                aa = new custom_adapter( getContext(), R.layout.single_dish_item, vector,flag_btn );
                Collections.sort(vector, new custom_adapter( getContext(), R.layout.single_dish_item, vector,flag_btn));
                listView.setAdapter( aa );
            }

        } );
        // filter_by_loc button
        FloatingActionButton b2 = (FloatingActionButton) getActivity().findViewById( filter_by_loc );
        b2.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                log.i( "firstFragment", "onOptionsItemSelected2" );
                flag_btn=-1;

                Comparator<String> ALPHABETICAL_ORDER1 = new Comparator<String>() {
                    public int compare(String object1, String object2) {
                        int res = String.CASE_INSENSITIVE_ORDER.compare( object1.toString(), object2.toString() );
                        return res;
                    }
                };

                //  Arrays.sort(vector.toArray(), (Comparator<? super Object>) vector.get( Integer.parseInt( "price_dish" ) ) );
                aa = new custom_adapter( getContext(), R.layout.single_dish_item, vector,flag_btn );
                Collections.sort(vector, new custom_adapter( getContext(), R.layout.single_dish_item, vector,flag_btn));
                listView.setAdapter( aa );
            }

        } );


        class myAsyncTask extends AsyncTask<String, Void, String> {
            JSONParser jParser;
            JSONArray productList;
            JSONArray dataJsonArr = null;
            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               // Log.i( "searchResultsParser", "in onPreExecute" );
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader reader = null;
                Log.i( "in do ", "in do" );
                Log.i( "123", "1" );
                URL url2 = null;//temp url
                try {
                    url2 = new URL( "http://newapp-env.eiymf2wfdn.eu-central-1.elasticbeanstalk.com/api/v1/basicSearch" );
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
    }


    public  Vector<HashMap<String,String>> parseMyList (String list)
    {
        myValue=list;
        try {
            if (myValue != null) {
                jsonarray = new JSONArray( myValue );
                Log.i( "f", " \n\n" );
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject js3 = jsonarray.getJSONObject( i );
                    // System.out.println( "json array at place " + i + js3.toString() + "\n" );
                }
            } else {
                Toast.makeText( getContext(), " אין ערכים", Toast.LENGTH_SHORT ).show();
            }
            int count = -1;

            List<Map<String, String>> myMap = new ArrayList<Map<String, String>>();
            for (int i = 0; i < jsonarray.length(); i++) {
                Log.i( "index", String.valueOf( i ) );
                //HashMap<String,String> hashMap_ = new HashMap<>();
                JSONObject obj = jsonarray.getJSONObject( i );
                _source = new JSONObject();
                _source = obj.getJSONObject( "_source" );
                rest_address = _source.getString( "rest_address" );
                name_res = _source.getString( "rest_name" );
                location_res_str = _source.getString( "rest_location" );
                location_res_jsn = new JSONObject( location_res_str );
                ///kosher
                kosher = _source.getString( "Kosher" );
                inner_hits = new JSONObject();
                inner_hits = obj.getJSONObject( "inner_hits" );
                menu = new JSONObject();
                menu = inner_hits.getJSONObject( "menu" );
                hits_1 = new JSONObject();
                hits_1 = menu.getJSONObject( "hits" );
                String hits_arr = hits_1.getString( "hits" );
                menu_hits_jsonArray = new JSONArray( hits_arr );
                for (int j = 0; j < menu_hits_jsonArray.length(); j++)
                {
                    //   count++;
                    hashMap_ = new HashMap<>();

                    //getting restaurant details.
                    hashMap_.put( "index", String.valueOf( count ) );
                    hashMap_.put( "rest_address", rest_address );
                    hashMap_.put( "rest_name", name_res );
                    hashMap_.put( "rest_location", String.valueOf( location_res_jsn ) );
                    hashMap_.put( "Kosher", kosher );
                    JSONObject obj2 = menu_hits_jsonArray.getJSONObject( j );
                    JSONObject _source2 = new JSONObject();
                    _source2 = obj2.getJSONObject( "_source" );
                    dish_desc = _source2.getString( "dish_description" );
                    hashMap_.put( "dish_description", dish_desc );
                    name_dish = _source2.getString( "dish_name" );
                    hashMap_.put( "dish_name", name_dish );
                    price_dish = _source2.getString( "dish_price" );
                    hashMap_.put( "dish_price", price_dish );
                    dish_id_inRest= _source2.getString( "dish_id_inRest" );

                    hashMap_.put( "dish_id_inRest", dish_id_inRest );

                    //img

                    imgUrl= _source2.getString( "imgUrl" );
                    Log.e("hopfully not null ",imgUrl);
                    hashMap_.put( "imgUrl", imgUrl );
                    vector.add( hashMap_ );

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vector;
    }
    protected void onPostExecute(String result) throws JSONException {
        JSONObject jObj = new JSONObject( result );
        Log.i( "my res is :", result );

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


    @Override
    public int compareTo(@NonNull Object o) {
        return -1;
    }
}




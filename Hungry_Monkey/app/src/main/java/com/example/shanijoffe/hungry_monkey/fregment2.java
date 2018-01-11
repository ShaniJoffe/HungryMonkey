package com.example.shanijoffe.hungry_monkey;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import static com.loopj.android.http.AsyncHttpClient.log;


/**
 * A simple {@link Fragment} subclass.
 */
public class fregment2 extends Fragment {

    ListView lv;
    JSONObject job;
    String Jsonoutput;
    String val;
    TextView PriceDish;
    TextView Dish_name;
    TextView nameRes;
    ArrayAdapter<String> adapter;
    boolean show=false;
    public fregment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        String[] teams={"Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham","Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham"};
        View view = inflater.inflate(R.layout.fragment_fregment2, container, false);
        nameRes=view.findViewById( R.id.nameRestxtv );
        Log.i("sup","in basic");
        lv=(ListView)view.findViewById(R.id.listView1);
        //connecting  adapter
        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,teams);
        lv.setAdapter(adapter);
        if(getActivity().getIntent().hasExtra("JSON_OBJECT"))
        {
            Intent i =getActivity().getIntent();
            JsonObject mJsonObject;

            val = i.getStringExtra("JSON_OBJECT");
        }
//now parse this job to get your name and email.or anuy other data in jobj.
        log.i("my json :",val);
        // nameRes.setText( "heyooo" );
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_fregment2, container, false );
    }


}

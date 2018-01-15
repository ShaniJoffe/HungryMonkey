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
import org.w3c.dom.Text;

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
    TextView nres;
    ArrayAdapter<String> adapter;
    boolean show=false;
    View view;
    public fregment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        String[] teams={"Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham","Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham"};
        View view = inflater.inflate(R.layout.fragment_fregment2, container, false);
        Log.i("sup","in basic");
        lv=(ListView)view.findViewById(R.id.listView1);
        //connecting  adapter
        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,teams);
        lv.setAdapter(adapter);
        // Inflate the layout for this fragment
        view= inflater.inflate( R.layout.fragment_fregment2, container, false );
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        nres = (TextView) getActivity().findViewById(R.id.res_name22);
        Bundle bundle = this.getArguments();

        String myValue = bundle.getString("message");
        Log.i("freg2 ","i got :"+myValue);
        // Displaying the user details on the screen
        nres.setText(myValue);
    }

}

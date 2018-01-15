package com.example.shanijoffe.hungry_monkey;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    Button det;
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
        det=getView().findViewById( R.id.choose_Dish );
        det.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( getActivity(),basic_results.class );
                startActivity( i );
//                FragmentManager fm =getFragmentManager();
//                FragmentTransaction ft=fm.beginTransaction();
//
//
//                ItemFragment f1 = new ItemFragment();
//                ft.replace( R.id.fragment_container, f1 );
//                ft.commit();

            }
        } );

    }

}

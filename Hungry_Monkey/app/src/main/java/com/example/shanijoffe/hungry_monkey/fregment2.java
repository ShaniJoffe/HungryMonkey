package com.example.shanijoffe.hungry_monkey;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class fregment2 extends Fragment {
///restaurant fragment-shows the restaurant detail's.

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
        View view = inflater.inflate(R.layout.fragment_fregment2, container, false);
        //connecting  adapter
        // Inflate the layout for this fragment
        view= inflater.inflate( R.layout.fragment_fregment2, container, false );
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        nres = (TextView) getActivity().findViewById(R.id.res_name22);
        Bundle bundle = this.getArguments();
        String myValue = bundle.getString("name_res");
        // Displaying the user details on the screen
        nres.setText(myValue);
        det=getView().findViewById( R.id.choose_Dish );
        det.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( getActivity(),basic_results.class );
                startActivity( i );
            }
        } );

    }

}

package com.example.shanijoffe.hungry_monkey;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.loopj.android.http.AsyncHttpClient.log;


/**
 * A simple {@link Fragment} subclass.
 */
public class fregment_BasicRes extends Fragment {

    boolean show=false;
    String line="";
    public fregment_BasicRes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_fregment__basic_res, container, false );
    }
    public void OnClickShowAdvancedSearch(View view)
    {
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.adv_leyout);
        LinearLayout linearLayout2 = (LinearLayout) getView().findViewById(R.id.adv_leyout2);
        LinearLayout linearLayout3 = (LinearLayout)getView().findViewById(R.id.adv_leyout3);
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
            Intent i = new Intent(getActivity(), LoginPage.class);
            startActivity(i);
            String s = line ;
            i.putExtra( "myString", s );

        }
    }
    public void OnClickSign(View view)
    {
        log.i( "hi", "sup" );
        Log.e("MainActivity","fuka u ");
        Log.i("line is:",line);
        if (line == line)
        {
            Intent i = new Intent(getActivity(), SignUpActivity.class);
            startActivity(i);
            String s = line ;
            i.putExtra( "myString", s );
        }
    }

}

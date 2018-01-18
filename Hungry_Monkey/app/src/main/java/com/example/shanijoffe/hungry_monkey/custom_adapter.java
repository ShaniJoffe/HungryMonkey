package com.example.shanijoffe.hungry_monkey; /**
 * Created by Shani Joffe on 1/16/2018.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanijoffe.hungry_monkey.MainActivity;
import com.example.shanijoffe.hungry_monkey.R;
import com.example.shanijoffe.hungry_monkey.SecondFragment;

import java.util.HashMap;
import java.util.Vector;


public class custom_adapter extends ArrayAdapter<HashMap<String,String>>
{
    Button show_det_for_dish;
    private LayoutInflater inflater;
    private Activity _activity;
    private final String[] cname = new String[1];
    private final String[] cnumber = new String[1];
    private Vector<HashMap<String, String>> _vec;
    private Context context;
    private int resource;
    public custom_adapter(Context context, int resource, Vector<HashMap<String, String>> vec )
    {
        super(context, resource, vec);
        this._activity = MainActivity._mainActivity;
        this._vec = vec;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view =  LayoutInflater.from(context).inflate(this.resource,parent,false);;
        final TextView name_res = (TextView) view.findViewById( R.id.nameRes);
        final TextView name_dish = (TextView) view.findViewById(R.id.Dish_name);
        //Button open_Contact_Button = (Button)view.findViewById(R.id.choose_Dish);
        cname[0] = _vec.get(position).get("name_res");
        cnumber[0] = _vec.get(position).get("Dish_name");
        final String cname2 = cname[0];
        final String cnum2 = cnumber[0];
        name_res.setText(cname[0]);
        name_dish.setText(cnumber[0]);
        show_det_for_dish=(Button) view.findViewById( R.id.choose_Dish );
        show_det_for_dish.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("btn choose","chhose");
                Intent i=new Intent( getContext(),basic_results.class );
                context.startActivity(i);
            }
        } );


//        open_Contact_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                if(MainActivity.isTablet())
//                {
//                    FragmentManager fm = _activity.getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                    Fragment fragment = new SecondFragment();
//                    Bundle b = new Bundle();
//                    b.putString("name_res",cname2);
//                    b.putString("phone",cnum2);
//                    fragment.setArguments(b);
//                    fragmentTransaction.replace(R.id.fragment_container, fragment );
//                    fragmentTransaction.commit();
//                }
//                else
//                {
//                    FragmentManager fm = _activity.getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                    Fragment fragment = new SecondFragment();
//                    Bundle b = new Bundle();
//                    b.putString("name_res",cname2);
//                    b.putString("phone",cnum2);
//                    fragment.setArguments(b);
//                    fragmentTransaction.replace(R.id.right_frame, fragment );
//                    fragmentTransaction.commit();
//                }
//            }
//        });
        return view;
    }
}
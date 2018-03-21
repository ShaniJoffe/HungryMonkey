package com.example.shanijoffe.hungry_monkey; /**
 * Created by Shani Joffe on 1/16/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Vector;


public class custom_adapter extends ArrayAdapter<HashMap<String,String>>
{
    Button show_det_for_dish;
    private LayoutInflater inflater;
    private Activity _activity;
    private final String[] rest_address = new String[1];
    private final String[] rest_location = new String[1];
    private final String[] rest_name = new String[1];
    private final String[] Kosher = new String[1];
    private final String[] dish_description = new String[1];
    private final String[] dish_name = new String[1];
    private final String[] dish_price = new String[1];

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
        final TextView name_res_txtv = (TextView) view.findViewById( R.id.nameRes);
        final TextView name_dish_txtv = (TextView) view.findViewById(R.id.Dish_name);
        final TextView PriceDish_txtv = (TextView) view.findViewById(R.id.PriceDish);

        //Button open_Contact_Button = (Button)view.findViewById(R.id.choose_Dish);


        //
        //
        Log.i("position", String.valueOf( position ) );
        rest_address[0] = _vec.get(position).get("rest_address");
        rest_location[0] = _vec.get(position).get("rest_location");
        rest_name[0] = _vec.get(position).get("rest_name");
        Kosher[0] = _vec.get(position).get("Kosher");
        dish_description[0] = _vec.get(position).get("dish_description");
        dish_name[0] = _vec.get(position).get("dish_name");
        dish_price[0] = _vec.get(position).get("dish_price");
        ///
        final String rest_address2 = rest_address[0];
        final String rest_location2 = rest_location[0];
        final String rest_name2 = rest_name[0];
        final String Kosher2 = Kosher[0];
        final String dish_description2 = dish_description[0];
        final String dish_name2 = dish_name[0];
        final String dish_price2 = dish_price[0];

        Log.i("rest_name2",rest_name2);


        name_res_txtv.setText(rest_name[0]);
        name_dish_txtv.setText(dish_name[0]);
        PriceDish_txtv.setText(dish_price[0] +"שח ");

        show_det_for_dish=(Button) view.findViewById( R.id.choose_Dish );
        show_det_for_dish.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("btn choose","chhose");
                Intent i=new Intent( getContext(),basic_results.class );
                i.putExtra( "rest_address2", rest_address2 );
                i.putExtra( "rest_location2", rest_location2 );
                i.putExtra( "Kosher2", Kosher2 );
                i.putExtra( "dish_description2", dish_description2 );
                i.putExtra( "dish_name2", dish_name2 );
                i.putExtra( "rest_name2", rest_name2 );
                i.putExtra( "dish_price2", dish_price2 );
                context.startActivity(i);
            }
        } );
        return view;
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
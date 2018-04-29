package com.example.shanijoffe.hungry_monkey; /**
 * Created by Shani Joffe on 1/16/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;


public class custom_adapter extends ArrayAdapter<HashMap<String,String>>  implements Comparator<HashMap<String, String>>{
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
    int position;
    TextView flag_price;
    TextView flag_loc;

    private Vector<HashMap<String, String>> _vec;
    private Context context;
    private int resource;
    int flag_btn;

    public custom_adapter(Context context, int resource, Vector<HashMap<String, String>> vec,int flag_btn) {
        super( context, resource, vec );
        this._vec = vec;
        this.context = context;
        this.resource = resource;
        this.flag_btn=flag_btn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;
        View view = LayoutInflater.from( context ).inflate( this.resource, parent, false );
        ;
        final TextView name_res_txtv = (TextView) view.findViewById( R.id.nameRes );
        final TextView name_dish_txtv = (TextView) view.findViewById( R.id.Dish_name );
        final TextView PriceDish_txtv = (TextView) view.findViewById( R.id.PriceDish );
        flag_price = (TextView) view.findViewById( R.id.txtv_flag_price );
        flag_loc = (TextView) view.findViewById( R.id.txtv_flag_loc );
        //Button open_Contact_Button = (Button)view.findViewById(R.id.choose_Dish);


        //
        //
        Log.i( "position", String.valueOf( position ) );
        rest_address[0] = _vec.get( position ).get( "rest_address" );
        rest_location[0] = _vec.get( position ).get( "rest_location" );
        rest_name[0] = _vec.get( position ).get( "rest_name" );
        Kosher[0] = _vec.get( position ).get( "Kosher" );
        dish_description[0] = _vec.get( position ).get( "dish_description" );
        dish_name[0] = _vec.get( position ).get( "dish_name" );
        dish_price[0] = _vec.get( position ).get( "dish_price" );
        ///
        final String rest_address2 = rest_address[0];
        final String rest_location2 = rest_location[0];
        final String rest_name2 = rest_name[0];
        final String Kosher2 = Kosher[0];
        final String dish_description2 = dish_description[0];
        final String dish_name2 = dish_name[0];
        final String dish_price2 = dish_price[0];

        Log.i( "rest_name2", rest_name2 );


        name_res_txtv.setText( rest_name[0] );
        name_dish_txtv.setText( dish_name[0] );
        PriceDish_txtv.setText( dish_price[0] + "שח " );

        show_det_for_dish = (Button) view.findViewById( R.id.choose_Dish );
        show_det_for_dish.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i( "btn choose", "chhose" );
                Intent i = new Intent( getContext(), basic_results.class );
                i.putExtra( "rest_address2", rest_address2 );
                i.putExtra( "rest_location2", rest_location2 );
                i.putExtra( "Kosher2", Kosher2 );
                i.putExtra( "dish_description2", dish_description2 );
                i.putExtra( "dish_name2", dish_name2 );
                i.putExtra( "rest_name2", rest_name2 );
                i.putExtra( "dish_price2", dish_price2 );
                context.startActivity( i );
            }
        } );
        return view;
    }

    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();


        this.setNotifyOnChange( true );
    }

    private void sortMyList() {

        notifyDataSetChanged();
    }


    @Override
    public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs)
    {

            if (flag_btn==1){
                Log.i( "button filer by price ", "in compare" );
                if (Integer.parseInt( lhs.get( "dish_price" ) ) > Integer.parseInt( rhs.get( "dish_price" ) ))
                {

                    return 1;
                }
            }
            else if(flag_btn==-1)
            {

                Log.i( "button filer by price ", "in compare left is "+lhs.toString() +"right is"+ rhs.toString() );
                JSONObject location_lhs=new JSONObject(  );
                JSONObject location_rhs = new JSONObject(  );
                double lon_lhs = 0,lat_lhs = 0,lat_rhs=0,lon_rhs=0;

                try {

                    location_lhs = new JSONObject( lhs.get( "rest_location" ));
                     lon_lhs= Double.parseDouble( String.valueOf( location_lhs.get("lon") ) );
                     lat_lhs= Double.parseDouble( String.valueOf( location_lhs.get("lat") ) );
                    Log.i( "button filer by price ", "in compare left is "+location_lhs.toString() );

                    location_rhs = new JSONObject( lhs.get( "rest_location" ));
                    Log.i( "button filer by price ", "right is"+ location_rhs.toString() );

                    lon_rhs= Double.parseDouble(  String.valueOf( location_rhs.get("lon") ));
                    lat_rhs= Double.parseDouble( String.valueOf(  location_rhs.get("lat")) );

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i( "in compare", "loaction LHS is 1"+ "lon1"+ lon_lhs +"lat1"+lat_lhs +"loaction2 RHS is" +"lon2"+lon_lhs +"lat2 "+lat_rhs );
                if (distance(lon_lhs, lat_lhs, lat_rhs, lon_rhs) > 0)
                { // if distance < 0.1 miles we take locations as equal
                    //do what you want to do...
                    Log.i( "loc 1 is bigger then2 ", "in compare" );
                }
            }
return -1;
    }

    /** calculates the distance between two locations in MILES */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }


}
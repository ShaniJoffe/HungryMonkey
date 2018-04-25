import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shanijoffe.hungry_monkey.Dish;
import com.example.shanijoffe.hungry_monkey.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shani Joffe on 1/6/2018.
 */
class UsersAdapter extends ArrayAdapter<Dish> {
    public UsersAdapter(Context context, ArrayList<Dish> dishes) {
        super(context, 0, dishes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Dish d = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_dish_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.nameRestxtv);
       // TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        tvName.setText(d.getDishtName());
        //tvHome.setText(user.hometown);
        // Return the completed view to render on screen
        return convertView;
    }
}
package com.example.simon_000.buddy.customs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.simon_000.buddy.R;

import java.util.ArrayList;
/**
 * Created by simon_000 on 2014-10-23.
 */
public class NameAdapter extends ArrayAdapter<String> {

    private ArrayList<String> ArrayListNames;
    private int Resource;
    private Context context;
    private LayoutInflater vi;

    public NameAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);

        ArrayListNames = objects;
        Resource = resource;
        this.context = context;

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            convertView = vi.inflate(Resource, null);
            //recycling views using viewholder (items getting offscreen)
            holder = new ViewHolder();

            holder.memberNames = (TextView) convertView.findViewById(R.id.memberNames);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        //setting text in each item based on the position in the list
        holder.memberNames.setText(ArrayListNames.get(position));
        return convertView;
    }

    static class ViewHolder{
        public TextView memberNames;
    }
}

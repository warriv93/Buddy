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
 * Created by simon_000 on 2014-10-22.
 */
public class GroupAdapter extends ArrayAdapter<String> {

    private ArrayList<String> ArrayListGroups;
    private int Resource;
    private Context context;
    private LayoutInflater vi;

    public GroupAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);

        ArrayListGroups = objects;
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

            holder.groupName = (TextView) convertView.findViewById(R.id.groupName);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        //setting text in each item based on the position in the list
        holder.groupName.setText(ArrayListGroups.get(position));
        return convertView;
    }

    static class ViewHolder{
        public TextView groupName;
    }
    public void updateList(){
        notifyDataSetChanged();
    }
}

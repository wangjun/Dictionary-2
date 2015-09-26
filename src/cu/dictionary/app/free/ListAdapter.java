package cu.dictionary.app.free;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<String> {
	public TextView text;
	LayoutInflater inflater;
	public ListAdapter(Context context, String[] objects) {
		super(context, 0, objects);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item, null);
		}
		text = (TextView)convertView.findViewById(R.id.item_name);
		text.setText(getItem(position));
		return convertView;
	}

}

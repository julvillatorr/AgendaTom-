package com.tesis.agendatome.adapter;

import java.util.ArrayList;

import com.tesis.agendatome.R;
import com.tesis.agendatome.model.Categoria;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class CustomGridViewAdapterCategoria extends ArrayAdapter<Categoria> {
	Context context;
	int layoutResourceId;
	ArrayList<Categoria> data = new ArrayList<Categoria>();

	public CustomGridViewAdapterCategoria(Context context, int layoutResourceId, ArrayList<Categoria> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new RecordHolder();
			holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		
		Categoria item = data.get(position);
		holder.imageItem.setImageBitmap(item.getImage());
		
		return row;
	}

	static class RecordHolder {
		ImageView imageItem;
	}
}
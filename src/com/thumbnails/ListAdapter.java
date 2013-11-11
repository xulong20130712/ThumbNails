package com.thumbnails;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thumbnails.R;
import com.thumbnails.ImageLoader.ImageCallBack;

public class ListAdapter extends BaseAdapter {

	private File[] lists;
	private LayoutInflater inflater;
	private Context context;
	private int COUNT= 8;
	private ThumbNailsThread[] threads= new ThumbNailsThread[8];
	private ImageLoader ImageLoader = new ImageLoader();
	 
	public ListAdapter(File[] lists, Context context) {
		
		this.lists= lists;
		this.context= context;
		inflater= LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		
		return lists.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		ContentHolder holder= null;
		if(convertView!= null) {
			
			holder= (ContentHolder) convertView.getTag();
		}else {
			
			convertView= inflater.inflate(R.layout.item, null);
			holder= new ContentHolder();
			holder.icon= (ImageView) convertView.findViewById(R.id.thumbnail);
			holder.name= (TextView) convertView.findViewById(R.id.item_name);
			holder.size= (TextView) convertView.findViewById(R.id.item_size);
			convertView.setTag(holder);
		}
		setImage(holder,position);
//		setBitmap(holder,position);
		holder.name.setText(lists[position].getName());
		holder.size.setText((lists[position].length()/ 1000/ 1000.0)+ "M");
		return convertView;
	}
	
	private void setImage(final ContentHolder holder, int position) {
		
		ImageLoader.loadDrawable(lists[position].getAbsolutePath(), 40, 40, new ImageCallBack() {
			
			@Override
			public void imageLoaded(Bitmap drawable) {

				holder.icon.setImageBitmap(drawable);
			}
		}, new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bitmap bitmap= (Bitmap) msg.obj;
				holder.icon.setImageBitmap(bitmap);
			}
		});
	}
	
	private void setBitmap(final ContentHolder holder, int position) {
		
		Handler handler= new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bitmap bitmap= (Bitmap) msg.obj;
				holder.icon.setImageBitmap(bitmap);
			}
		};
//		if(position< 8) {
			
			ThumbNailsThread thread= new ThumbNailsThread(handler, lists[position].getAbsolutePath(), 40, 40);
			threads[0]= thread;
			thread.start();
//		}else {
//			
//			int index= position% 8;
//			threads[index]
//		}
	}

	class ContentHolder {
		
		TextView name;
		TextView size;
		ImageView icon;
	}
}

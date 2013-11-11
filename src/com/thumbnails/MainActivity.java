package com.thumbnails;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;

import com.example.thumbnails.R;

public class MainActivity extends Activity {

	private ListView list;
	private String path;
	private File[] a_file;
	private File[] images;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list= (ListView) findViewById(R.id._list);
		path= Environment.getExternalStorageDirectory().getPath()+ "/DCIM/camera/";
		getFiles(path);
		System.out.println("imagesimages"+ a_file.length);
		list.setAdapter(new ListAdapter(a_file, this));
	}
	
	public void getFiles(String path) {
		
		File file= new File(path);
		if(file.exists()) {
			
			File[] a_file= file.listFiles();
			File[] jpg_images= file.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String filename) {

					
					return (dir.getName().lastIndexOf(filename)>= 0);
				}
			});
			this.a_file= a_file;
			images= jpg_images;
		}
	}
}

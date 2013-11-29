package com.thumbnails;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thumbnails.R;

public class MainActivity extends Activity {

	private ListView list;
	private String path;
	private File[] a_file;
	private File[] images;
	private long temp= 0l;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list= (ListView) findViewById(R.id._list);
//		path= Environment.getExternalStorageDirectory().getPath()+ "/DCIM/Camera/";
		path= Environment.getExternalStorageDirectory().getPath()+ "/DCIM/camera/";
		getFiles(path);
		list.setAdapter(new ListAdapter(images, this));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode== KeyEvent.KEYCODE_BACK) {
			
			exit();
		}
		return false;
	}
	
	public void exit() {
		
		if(System.currentTimeMillis()- temp> 2000){
			
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			temp= System.currentTimeMillis();
		}else {
			
			finish();
		}
	}
	
	public void getFiles(String path) {
		
		File file= new File(path);
		if(file.exists()) {
			
			File[] a_file= file.listFiles();
			File[] jpg_images= file.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String filename) {

					
					return (filename.lastIndexOf("jpg")>= 0);
				}
			});
			System.out.println("jpg_images---->>>>"+ jpg_images.length);
			this.a_file= a_file;
			images= jpg_images;
		}
	}
}

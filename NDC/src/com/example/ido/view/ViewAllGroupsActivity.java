package com.example.ido.view;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.ido.R;
import com.example.ido.R.id;
import com.example.ido.R.layout;
import com.example.ido.R.menu;
import com.example.ido.controller.ApplicationNavigationHandler;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAllGroupsActivity extends GeneralActivity {
	
	// VARIABLES DEFINED HERE
	int count = 0;
	// The cursor for query all groups from database
	private Cursor allGroupsCursor;
	
	// Adapter for All Groups List View
	private SimpleCursorAdapter allGroupsListViewAdapter;
	
	// All Groups List View
	private ListView allGroupsListView;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_groups);
		setupMessageButton();

		};
		

		
		private void setupMessageButton(){
			
			Button messageButton = (Button) findViewById(R.id.button1);
			
			messageButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					count++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {
						
						@Override
						public void run() {
							if (count == 1)
							{
								xmlFormater(1);
								Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
								count = 0;
							}
						}
					};
					if(count == 1){
						handler.postDelayed(r, 250);
						
						
					}else if (count == 2){
						xmlFormater(2);
						Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
						count = 0;
					}

				}	
			});
		}
		
		/* Checks if external storage is available for read and write */
		public boolean isExternalStorageWritable() {
		    String state = Environment.getExternalStorageState();
		    if (Environment.MEDIA_MOUNTED.equals(state)) {
		        return true;
		    }
		    return false;
		}
		
		void fileWriter(String instance){
			//Log.d("Elias", "entered filewriter");
		
				
				SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
				Date date = new Date();
				String dateFormatted = DATE_FORMAT.format(date);
				String root = Environment.getExternalStorageDirectory().toString();
				File fileDir = new File(root + "/odk/instances");
				fileDir.mkdirs();
				String fileName = "form" + dateFormatted + ".xml";
				File file = new File(fileDir,fileName);
				
				if(file.exists())
				{
					file.delete();
				}
				try
				{
					FileOutputStream out = new FileOutputStream(file);
					out.write(instance.getBytes());
					//Log.d("Miguel","Succes: " + getFilesDir());
					out.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

		}
		

		public void xmlFormater(int input){
			

			//xml instance template;
			//Log.d("Elias",Environment.getExternalStorageDirectory().getAbsolutePath());

			String xmlTest="<?xml version='1.0' ?><testform1 id='testform1'><formhub><uuid>1d01462f8b4446caa6431cdcbd7849ba</uuid></formhub><correct_behavior>%s</correct_behavior><end>2014-03-15T10:00:14.671-07</end><meta><instanceID>uuid:02e0e664-5198-437e-a231-88fb300dc62a</instanceID></meta></testform1>";
			
			String something;
			if(input == 1){
				 something = String.format(xmlTest, "1");
			}
			else{
				//this is for a "no" response, will insert 2 in the form.
				 something = String.format(xmlTest, "2");
			}
			fileWriter(something);
			
		}


}

package org.odk.collect.android.activities;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.odk.collect.android.R;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.listeners.InstanceUploaderListener;
import org.odk.collect.android.logic.PropertyManager;
import org.odk.collect.android.preferences.PreferencesActivity;
import org.odk.collect.android.provider.InstanceProviderAPI;
import org.odk.collect.android.provider.InstanceProviderAPI.InstanceColumns;
import org.odk.collect.android.utilities.WebUtils;
import org.opendatakit.httpclientandroidlib.Header;
import org.opendatakit.httpclientandroidlib.HttpResponse;
import org.opendatakit.httpclientandroidlib.HttpStatus;
import org.opendatakit.httpclientandroidlib.client.ClientProtocolException;
import org.opendatakit.httpclientandroidlib.client.HttpClient;
import org.opendatakit.httpclientandroidlib.client.methods.HttpHead;
import org.opendatakit.httpclientandroidlib.client.methods.HttpPost;
import org.opendatakit.httpclientandroidlib.conn.ConnectTimeoutException;
import org.opendatakit.httpclientandroidlib.conn.HttpHostConnectException;
import org.opendatakit.httpclientandroidlib.entity.mime.MultipartEntity;
import org.opendatakit.httpclientandroidlib.entity.mime.content.FileBody;
import org.opendatakit.httpclientandroidlib.entity.mime.content.StringBody;
import org.opendatakit.httpclientandroidlib.protocol.HttpContext;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.odk.collect.android.R;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.listeners.FormSavedListener;
import org.odk.collect.android.provider.FormsProviderAPI.FormsColumns;
import org.odk.collect.android.tasks.ClickerUploaderTask;
import org.odk.collect.android.tasks.InstanceUploaderTask;
import org.odk.collect.android.tasks.SaveToDiskTask;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class StartClicksCapture extends Activity { // implements FormSavedListener {
	
	String dateFormatted, end;
	//private ClickerUploaderTask mClickerUploaderTask;
	private static String urlString = "https://smrtclkr.appspot.com/submission";
	private static final int CONNECTION_TIMEOUT = 60000;
	private File file;
	
	// VARIABLES DEFINED HERE//stuff
		int count = 0;
		// The cursor for query all groups from database
		//private Cursor allGroupsCursor;
		
		// Adapter for All Groups List View
		//private SimpleCursorAdapter allGroupsListViewAdapter;
		
		// All Groups List View
		//private ListView allGroupsListView;
		
		//@Override
		public void onCreate(Bundle savedInstanceState) {
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
			
			private void getCurrTime()
			{
				SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = new Date();
				end = DATE_FORMAT.format(date);
				end = end.replace(" ", "T");
				Log.d("Team:", end);
				
				//make the filename legal!
				dateFormatted = DATE_FORMAT.format(date);
				dateFormatted = dateFormatted.replace(" ","_");
				dateFormatted = dateFormatted.replace(":", "-");
				
			}
			
			/* Checks if external storage is available for read and write */
			public boolean isExternalStorageWritable() {
			    String state = Environment.getExternalStorageState();
			    if (Environment.MEDIA_MOUNTED.equals(state)) {
			        return true;
			    }
			    return false;
			}
			
			void fileWriter(String instance)
			{
				//Log.d("Tommy", "entered filewriter");
				
				String root = Environment.getExternalStorageDirectory().toString();
				try {
				    File newFolder = new File(root + "/odk/instances/Behavior_Form_" + dateFormatted);
				    if (!newFolder.exists()) {
				        newFolder.mkdir();
				    }
				    try {
				    	
				    	String fileName = "Behavior_Form_" + dateFormatted + ".xml";
				    	Log.d("Tommy", fileName);
				        file = new File(newFolder, fileName);
				        
						if(file.exists())
						{
							file.delete();
						}
						
				        file.createNewFile();
				     
				        FileOutputStream out = new FileOutputStream(file);
						out.write(instance.getBytes());
						out.close();
						//Log.d("Tommy","Succes: " + getFilesDir());
				    } catch (Exception ex) {
				    	
				    	Log.d("Tommy", "ex: " + ex);
				    }
				} catch (Exception e) {
					Log.d("Tommy","e: " + e);
				}
			
					uploadNewFile();
					
					
//					File fileDir = new File(root + "/odk/instances");
//					fileDir.mkdirs();
//					Log.d("Tommy", fileDir.getPath());
//					String fileName = "form_" + dateFormatted + ".xml";
//					File file = new File(fileDir,fileName);
//					//file.createNewFile();
//					Log.d("Tommy", file.getPath());
//					Log.d("Tommy", instance);
					
//					if(file.exists())
//					{
//						file.delete();
//					}
//					try
//					{
//						FileOutputStream out = new FileOutputStream(file);
//						out.write(instance.getBytes());
//						//Log.d("Miguel","Succes: " + getFilesDir());
//						out.close();
//					}
//					catch(Exception e)
//					{
//						e.printStackTrace();
//					}

			}
			
			public void uploadNewFile()
			{
				Uri u = Uri.parse(urlString);
		        HttpClient httpclient = WebUtils.createHttpClient(CONNECTION_TIMEOUT);
				
		        //File instanceFile = new File(file, "submission.xml");
		        
		        HttpPost httppost = WebUtils.createOpenRosaHttpPost(u);

	            MimeTypeMap m = MimeTypeMap.getSingleton();
	            
	            //long byteCount = 0L;

	            // mime post
	            MultipartEntity entity = new MultipartEntity();

	            // add the submission file first...
	            FileBody fb = new FileBody(file, "text/xml");
	            entity.addPart("xml_submission_file", fb);
	            Log.i("Team:", "added xml_submission_file: " + file.getName());
	            //byteCount += file.length();
	            
	            httppost.setEntity(entity);

	            // prepare response and return uploaded
	            HttpResponse response = null;
	            try {
	                Log.i("Team:", "Issuing POST request for form to: " + u.toString());
	                response = httpclient.execute(httppost);//, localContext);
	                int responseCode = response.getStatusLine().getStatusCode();
	                WebUtils.discardEntityBytes(response);

	                Log.i("Team:", "Response code:" + responseCode);
	            } catch (Exception e) {
	                e.printStackTrace();
	                Log.e("Team:", e.toString());
	                WebUtils.clearHttpConnectionManager();
	            }
			}
			
			public void xmlFormater(int input)
			{	
				getCurrTime();
				//xml instance template;
				Log.d("Tommy",Environment.getExternalStorageDirectory().getAbsolutePath());

				//String xmlTest="<?xml version='1.0' ?><Behavior_Form id='Behavior_Form'><formhub><uuid>1d01462f8b4446caa6431cdcbd7849ba</uuid></formhub><correct_behavior>" + input + "</correct_behavior><end>2014-03-15T10:00:14.671-07</end><meta><instanceID>uuid:02e0e664-5198-437e-a231-88fb300dc62a</instanceID></meta></testform1>";

				String xmlTest="<?xml version='1.0' ?><Behavior_Form id='Behavior_Form'><correct_behavior>" + input + "</correct_behavior><end>" + end + "</end></Behavior_Form>";

//				String something;
//				if(input == 1){
//					 something = String.format(xmlTest, "1");
//				}
//				else{
//					//this is for a "no" response, will insert 2 in the form.
//					 something = String.format(xmlTest, "2");
//				}
				fileWriter(xmlTest);
				
			}

//			@Override
//			public void savingComplete(int saveStatus) {
//				sendSavedBroadcast();
//				
//			}
//			
//			private void sendSavedBroadcast() {
//				Intent i = new Intent();
//				i.setAction("org.odk.collect.android.FormSaved");
//				this.sendBroadcast(i);
//			}

}

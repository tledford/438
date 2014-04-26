package org.odk.collect.android.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.odk.collect.android.R;
import org.odk.collect.android.utilities.WebUtils;
import org.opendatakit.httpclientandroidlib.HttpResponse;
import org.opendatakit.httpclientandroidlib.client.HttpClient;
import org.opendatakit.httpclientandroidlib.client.methods.HttpPost;
import org.opendatakit.httpclientandroidlib.entity.mime.MultipartEntity;
import org.opendatakit.httpclientandroidlib.entity.mime.content.FileBody;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class StartLeftClickBlueClick extends Activity {

	String dateFormatted, end;

	private static String urlString = "https://smrtclkr.appspot.com/submission";
	private static final int CONNECTION_TIMEOUT = 60000;
	private File file;

	int count = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_groups);
		setTitle("Left Click Blue Click");

		//		Bundle extras = getIntent().getExtras();
		//		if(extras != null) {
		//			formSelection = extras.getInt("position");
		//		}
		//
		//		Toast.makeText(getApplicationContext(),
		//				"Position :"+formSelection, Toast.LENGTH_LONG)
		//				.show();

		// keeps app on top of lock screen and allows user to touch the screen for input
		// to the app as soon as it's awake enough to receive user input.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

		//		Button messageButton = (Button) findViewById(R.id.button1);
		//
		//		messageButton.setOnClickListener(this);

		//setupMessageButton();
	};

	//private void setupMessageButton(){
	
	@Override
	public boolean onKeyDown(int keyCode, final KeyEvent event)
	{
		//logKeyEvent(keyCode, event, true);
		
		if(keyCode == 4 && event.getSource() != InputDevice.SOURCE_KEYBOARD)
		{
			
			Log.d("Team:", "RIGHT Button");
			Toast.makeText(getApplicationContext(), "RIGHT Button", Toast.LENGTH_SHORT).show();
			xmlFormater("RIGHT");
			return true;
		}
		else
		{
			Log.d("test:", "" + event.getSource());
			finish();
			return false;
		}
			
		
	}

	@Override
	public boolean dispatchTouchEvent(final MotionEvent event)
	{

		int eventAction = event.getAction();
		int eventButton = event.getButtonState();
		switch(eventButton)
		{
		case 1:
		{
			if (eventAction == MotionEvent.ACTION_DOWN)
			{
				Log.d("Team:", "LEFT Button");
				Toast.makeText(getApplicationContext(), "LEFT Button", Toast.LENGTH_SHORT).show();
				xmlFormater("LEFT");
			}
			break;
		}
		case 2:
		{
			if (eventAction == MotionEvent.ACTION_DOWN)
			{
				Log.d("Team:", "RIGHT Button");
				Toast.makeText(getApplicationContext(), "RIGHT Button", Toast.LENGTH_SHORT).show();
				xmlFormater("RIGHT");
			}
			break;
		}
		}

		return true;
	}

	private void getCurrTime()
	{
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();

		//to insert into the xml form
		end = DATE_FORMAT.format(date);
		end = end.replace(" ", "T");
		Log.d("Team:", end);

		//to use as the file name for the saved xml file.
		dateFormatted = DATE_FORMAT.format(date);
		dateFormatted = dateFormatted.replace(" ","_");
		dateFormatted = dateFormatted.replace(":", "-");

	}

	void fileWriter(String instance)
	{
		String root = Environment.getExternalStorageDirectory().toString();
		try {
			File newFolder = new File(root + "/odk/instances/Behavior_Form_" + dateFormatted);
			if (!newFolder.exists()) {
				newFolder.mkdir();
			}
			try {
				String fileName = "Behavior_Form_" + dateFormatted + ".xml";
				Log.d("Team:", fileName);
				file = new File(newFolder, fileName);

				if(file.exists())
				{
					file.delete();
				}

				file.createNewFile();

				FileOutputStream out = new FileOutputStream(file);
				out.write(instance.getBytes());
				out.close();
			} catch (Exception ex) {

				Log.d("Team:", "ex: " + ex);
			}
		} catch (Exception e) {
			Log.d("Team:","e: " + e);
		}

		uploadNewFile();
	}

	/*
	 * Mimics the "InstanceUploaderTask.java" functionality. 
	 */
	public void uploadNewFile()
	{
		new NetworkTask().execute(urlString);
		
		//		Uri u = Uri.parse(urlString);
		//		HttpClient httpclient = WebUtils.createHttpClient(CONNECTION_TIMEOUT);
		//
		//		HttpPost httppost = WebUtils.createOpenRosaHttpPost(u);
		//
		//		MimeTypeMap m = MimeTypeMap.getSingleton();
		//
		//		// mime post
		//		MultipartEntity entity = new MultipartEntity();
		//
		//		// add the submission file first...
		//		FileBody fb = new FileBody(file, "text/xml");
		//		entity.addPart("xml_submission_file", fb);
		//		Log.i("Team:", "added xml_submission_file: " + file.getName());
		//
		//		httppost.setEntity(entity);
		//
		//		// prepare response and return uploaded
		//		HttpResponse response = null;
		//		try {
		//			Log.i("Team:", "Issuing POST request for form to: " + u.toString());
		//			response = httpclient.execute(httppost);//, localContext);
		//			int responseCode = response.getStatusLine().getStatusCode();
		//			WebUtils.discardEntityBytes(response);
		//
		//			Log.i("Team:", "Response code:" + responseCode);
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//			Log.e("Team:", e.toString());
		//			WebUtils.clearHttpConnectionManager();
		//		}
		//
		//		if(file.exists())
		//		{
		//			file.delete();
		//		}
	}

	public void xmlFormater(String button)
	{	
		getCurrTime();

		String xmlTest="<?xml version='1.0' ?><Left_Right_Clicks_Form id='Left_Right_Clicks_Form'><button_pressed>" + button + "</button_pressed><end>" + end + "</end></Left_Right_Clicks_Form>";

		fileWriter(xmlTest);
	}

	//	/* Checks if external storage is available for read and write */
	//	public boolean isExternalStorageWritable() {
	//		String state = Environment.getExternalStorageState();
	//		if (Environment.MEDIA_MOUNTED.equals(state)) {
	//			return true;
	//		}
	//		return false;
	//	}

	private class NetworkTask extends AsyncTask<String, Void, HttpResponse> {
		@Override
		protected HttpResponse doInBackground(String... params) {
			String link = params[0];

			Log.d("team:", "here");
			Uri u = Uri.parse(link);
			HttpClient httpclient = WebUtils.createHttpClient(CONNECTION_TIMEOUT);

			HttpPost httppost = WebUtils.createOpenRosaHttpPost(u);

			MimeTypeMap m = MimeTypeMap.getSingleton();

			// mime post
			MultipartEntity entity = new MultipartEntity();

			// add the submission file first...
			FileBody fb = new FileBody(file, "text/xml");
			entity.addPart("xml_submission_file", fb);
			Log.i("Team:", "added xml_submission_file: " + file.getName());

			httppost.setEntity(entity);

			// prepare response and return uploaded
			HttpResponse response = null;
			try {
				Log.i("Team:", "Issuing POST request for form to: " + u.toString());
				response = httpclient.execute(httppost);//, localContext);
				int responseCode = response.getStatusLine().getStatusCode();
				WebUtils.discardEntityBytes(response);

				Log.i("Team:", "Response code:" + responseCode);
				if(file.exists())
				{
					file.delete();
				}
				return response;
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("Team:", e.toString());
				WebUtils.clearHttpConnectionManager();
				if(file.exists())
				{
					file.delete();
				}
				return null;
			}
		}
	}
}
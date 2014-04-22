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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class StartClicksCapture extends Activity {
	
	String xmlTest;

	String dateFormatted, end;

	private static String urlString = "https://smrtclkr.appspot.com/submission";
	private static final int CONNECTION_TIMEOUT = 60000;
	private File file;

	int count = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_groups);
		setupMessageButton();

	};

	private void setupMessageButton(){

		Button messageButton = (Button) findViewById(R.id.button1);

		messageButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				count++;
				Handler handler = new Handler();
				Runnable r = new Runnable()
				{
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
				if(count == 1)
				{
					handler.postDelayed(r, 250);
				}
				else if (count == 2)
				{
					xmlFormater(2);
					Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
					count = 0;
				}
			}	
		});//end of onclicklistener
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
		Uri u = Uri.parse(urlString);
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
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Team:", e.toString());
			WebUtils.clearHttpConnectionManager();
		}

		if(file.exists())
		{
			file.delete();
		}
	}

	public void xmlFormater(int input)
	{	
		getCurrTime();

		xmlTest="<?xml version='1.0' ?><Behavior_Form id='Behavior_Form'><correct_behavior>" + input + "</correct_behavior><end>" + end + "</end></Behavior_Form>";

		
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
}

/*
 * Copyright (C) 2009 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.odk.collect.android.activities;

import org.odk.collect.android.R;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.provider.FormsProviderAPI.FormsColumns;
import org.odk.collect.android.tasks.DiskSyncTask;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Responsible for displaying all the valid forms in the forms directory. Stores the path to
 * selected form for use by {@link MainMenuActivity}.
 *
 * @author Yaw Anokwa (yanokwa@gmail.com)
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public class ClicksFormChooser extends ListActivity {

	private static final String t = "FormChooserList";
	private static final boolean EXIT = true;
	private static final String syncMsgKey = "syncmsgkey";

	private DiskSyncTask mDiskSyncTask;

	private AlertDialog mAlertDialog;

	ListView listView ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// must be at the beginning of any activity that can be called from an external intent
		try {
			Collect.createODKDirs();
		} catch (RuntimeException e) {
			//createErrorDialog(e.getMessage(), EXIT);
			return;
		}

		setContentView(R.layout.chooser_list_layout);
		setTitle(getString(R.string.clicks_form_list));

		// Get ListView object from xml
		listView = (ListView) findViewById(android.R.id.list);

		// Defined Array values to show in ListView
		//        String[] values = new String[] { "1 Click 2 Click", 
		//                                         "Red Click Blue Click" 
		//                                        };
		//
		//        // Define a new Adapter
		//        // First parameter - Context
		//        // Second parameter - Layout for the row
		//        // Third parameter - ID of the TextView to which the data is written
		//        // Forth - the Array of data
		//
		//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		//          android.R.layout.simple_list_item_1, android.R.id.text1, values);
		//
		//
		//        // Assign adapter to ListView
		//        listView.setAdapter(adapter); 

		// ListView Item Click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {
			Intent i;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{

				switch(position)
				{
				case 0: 
					i = new Intent(getBaseContext(), StartClicksCapture.class);
					break;
				case 1:
					i = new Intent(getBaseContext(), StartLeftClickBlueClick.class);
					break;
				}
				startActivity(i);



				//               // ListView Clicked item index
				//               int itemPosition     = position;
				//               
				//               // ListView Clicked item value
				//               String  itemValue    = (String) listView.getItemAtPosition(position);
				//                  
				//                // Show Alert 
				//                Toast.makeText(getApplicationContext(),
				//                  "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
				//                  .show();

			}

		}); 
	}
}


package com.example.ido.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.ido.view.ViewAllGroupsActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;

// A class consists of static functions that help the application navigation between activity
// The idea of the class is that many events can call to the same activity,
// so that every time they need to start one particular activity, just call the static function here
// and it will do the rest of the work
// This is for reusable purpose
public class ApplicationNavigationHandler {
	
	// Go to ViewAllGroupsActivity
	public static void showAllGroups(Activity sourceActivity){
		Intent showAllGroupsIntent = new Intent(sourceActivity, ViewAllGroupsActivity.class);
		sourceActivity.startActivity(showAllGroupsIntent);
	}
}

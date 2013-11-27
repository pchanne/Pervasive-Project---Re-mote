package com.example.pervasiveapp;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class Utils {
	
	private static Context context;
	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		Utils.context = context;
	}

	public static void showOKMessageBox(String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setTitle(title);
	    builder.setMessage(message);
	    builder.setIcon(android.R.drawable.ic_dialog_alert);
	    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int which) 
	        {       
	        	dialog.dismiss();
	        }
	    });             
	    AlertDialog alert = builder.create();
	    alert.show();
	}

}

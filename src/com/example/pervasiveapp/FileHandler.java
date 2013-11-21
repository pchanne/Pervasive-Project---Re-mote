package com.example.pervasiveapp;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FileHandler extends Activity {
	
	TextView fileNameTextView;
	Button openFileButton;
	
	public String fileData = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filehandler);
		fileNameTextView = (TextView) findViewById(R.id.fileNameText);
		openFileButton  = (Button) findViewById(R.id.openFileButton);
//		openFileButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//					System.out.println("Clicked!");
//					fileData = readFile();
//					System.out.println(fileData);
//			}
// 
//		});
	}
	
	private void readFile(View view){
		System.out.println("Clicked!");
//		InputStream inputStream = getResources().openRawResource(R.raw.book);
//		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
//		int i;
//		try{
//			i = inputStream.read();
//			while(i!=-1){
//				bstream.write(i);
//				i = inputStream.read();
//			}
//			inputStream.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		System.out.println(bstream.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_handler, menu);
		return true;	
	}
	
	private void printFileNameToConsole(){
		String fileName = fileNameTextView.getText().toString();
        System.out.println(fileName);
	}
	
	
//	public void addListenerOnButton() {
//		 
// 
//		openFileButton.setOnClickListener(new OnClickListener() {
// 
//			@Override
//			public void onClick(View arg0) {
// 
//			  Intent browserIntent = 
//                            new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mkyong.com"));
//			    startActivity(browserIntent);
// 
//			}
// 
//		});
// 
//	}
	
	class MyTextView extends EditText
	{
	    public MyTextView(Context context) {
			super(context);
		}
		@Override
	    public boolean onKeyDown(int keyCode, KeyEvent event)
	    {
	        if (keyCode==KeyEvent.KEYCODE_ENTER) 
	        {
	            // Just ignore the [Enter] key
	            return true;
	        }
	        // Handle all other keys in the default way
	        return super.onKeyDown(keyCode, event);
	    }
	}

}

package com.example.pervasiveapp;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;


public class FileHandler {
	private static String fileName;
	private static Context context;
	private static int index;
	
	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		FileHandler.fileName = fileName;
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		FileHandler.context = context;
	}
	
	public static String readFile(){
		final InputStream inputStream;
		final StringBuffer buffer = new StringBuffer();
		String str;
		final BufferedReader reader;
		try{
			inputStream = context.getResources().openRawResource(context.getResources().getIdentifier("raw/"+fileName, "raw", context.getPackageName()));
			reader = new BufferedReader(new InputStreamReader(inputStream));
			if (inputStream!=null) {                         
		        while ((str = reader.readLine()) != null) { 
		        	buffer.append(str + "\n" );
		        }               
		    }       
			inputStream.close(); 
		}catch(Resources.NotFoundException re){
		}
		catch(Exception e){
			e.getMessage();
		}
		return buffer.toString();
	}
		
}

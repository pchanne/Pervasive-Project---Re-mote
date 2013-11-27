package com.example.pervasiveapp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;


public class FileHandler {
	private static String fileName;
	private static Context context;
	private static int index;
	private static RandomAccessFile randomfile;
	private static ArrayList<Integer> pageNumber;
	private static int currentPageNumber ;
	private static long fileSize;
	private static long currReadsize;
	
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
	
	public static int getIndex() {
		return index;
	}

	public static void setIndex(int index) {
		FileHandler.index = index;
	}

	public static RandomAccessFile getRandomfile() {
		return randomfile;
	}
	
	public FileHandler(){
	}

	public static void setRandomfile() {
		
		File file;
		try {
			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
			randomfile = new RandomAccessFile(file, "rw");
			
			pageNumber = new ArrayList<Integer>();
			pageNumber.add(0, 0);
			index = 0;
			currentPageNumber = 0;
			fileSize = randomfile.length();
			Log.i("PervasiveApp", "TotalSize: "+"\n"+fileSize);
			currReadsize = 0;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/*public static String readFile(){
		/*final InputStream inputStream;
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
		String str = null;
		final File file;
		file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
		try {
			RandomAccessFile randomfile = new RandomAccessFile(file, "rw");
			randomfile.seek(0);
			str = randomfile.readLine();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 && curr
		return str;
	}*/
	
	public static void closeFile(RandomAccessFile file){
		try {
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static String readForward(){
		StringBuffer buff = new StringBuffer();
		int sizeOfDataRead = 0;
		
		if(canReadAhead()){
		try {
			currentPageNumber++;
			randomfile.seek(pageNumber.get(currentPageNumber));
			while(sizeOfDataRead<=1000 && currReadsize < fileSize){
				char c = (char)(randomfile.readByte());
				buff.append(c);
				sizeOfDataRead++;
				index++;
				currReadsize++;
			}
			pageNumber.add(currentPageNumber+1,index);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("PervasiveApp", "CurrentRead: "+"\n"+currReadsize);
		Log.i("PervasiveApp", "Data: "+"\n"+buff.toString());
		return buff.toString();
		}else 
		{
			return null;
		}
		
	}
	
	public static String read(){
		StringBuffer buff = new StringBuffer();
		
		int sizeOfDataRead = 0;
		try {
			randomfile.seek(pageNumber.get(currentPageNumber));
			while(sizeOfDataRead<=1000){
				char c = (char)(randomfile.readByte());
				buff.append(c);
				sizeOfDataRead++;
				index++;
				currReadsize++;
				
			}
			pageNumber.add(currentPageNumber+1,index);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("PervasiveApp", "Data: "+"\n"+buff.toString());
		return buff.toString();
	}
		
	public static String readBackward(){
		
		if(canReadBack()){
		StringBuffer buff = new StringBuffer();
		int sizeOfDataRead = 0;
		try {
			currentPageNumber--;
			randomfile.seek(pageNumber.get(currentPageNumber));
			while(sizeOfDataRead<=1000){
				char c = (char)(randomfile.readByte());
				buff.append(c);
				sizeOfDataRead++;
				index++;
				currReadsize--;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return buff.toString();
		}else{
			return null;
		}
		
	}
	
	public static Boolean canReadBack(){
		return currentPageNumber==0?false:true;
	}
	
	public static Boolean canReadAhead(){
		if(currReadsize < fileSize) return true;
		else return false;
	}
}

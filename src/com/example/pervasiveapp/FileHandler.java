package com.example.pervasiveapp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;

import android.content.Context;
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
	private static int lastPageSize;
    public static final int PAGE_SIZE = 1000;
    
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
			index = pageNumber.get(currentPageNumber);
			while(sizeOfDataRead<=PAGE_SIZE && currReadsize < fileSize){
				char c = (char)(randomfile.readByte());
				buff.append(c);
				sizeOfDataRead++;
				index++;
				currReadsize++;
			}
			if(currReadsize==fileSize){
				lastPageSize = sizeOfDataRead;
			}
			pageNumber.add(currentPageNumber+1,index);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("PervasiveApp", "CurrentRead: "+"\n"+currReadsize);
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
			index = pageNumber.get(currentPageNumber);
			while(sizeOfDataRead<=PAGE_SIZE){
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
			index = pageNumber.get(currentPageNumber);
			while(sizeOfDataRead<=PAGE_SIZE){
				char c = (char)(randomfile.readByte());
				buff.append(c);
				sizeOfDataRead++;
				index++;
			}
			if(lastPageSize>0){
				currReadsize = currReadsize - lastPageSize;
				lastPageSize = 0;
			}else
			{
				currReadsize = currReadsize-(PAGE_SIZE+1);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("PervasiveApp", "CurrentRead: "+"\n"+currReadsize);
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

package com.example.pervasiveapp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
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
		pageNumber = new ArrayList<Integer>();
		pageNumber.add(0, 0);
	}

	public static void setRandomfile() {
		
		String str = null;
		final File file;
		file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
		try {
			randomfile = new RandomAccessFile(fileName, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static String readFile(){
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
	}*/
		String str = null;
		final File file;
		file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
		try {
			RandomAccessFile randomfile = new RandomAccessFile(file, "rw");
			randomfile.seek(0);
			str = randomfile.readLine();
			randomfile.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return str;
	}
	
	public static String readForward(){
		String str;
		String returnValue = null;
		int sizeOfDataRead = 0;
		try {
			randomfile.seek(index);
			while(sizeOfDataRead<=1000000){
				str = String.valueOf(randomfile.readChar());
				sizeOfDataRead++;
				returnValue+=str;
				index++;
			}
			currentPageNumber++;
			if(pageNumber.get(currentPageNumber)!=null){
				pageNumber.add(currentPageNumber,index+1);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnValue;
		
	}
		
	public static String readBackward(){
		
		String str;
		String returnValue = null;
		int sizeOfDataRead = 0;
		try {
			currentPageNumber --;
			randomfile.seek(pageNumber.get(currentPageNumber));
			while(sizeOfDataRead<=1000000){
				str = String.valueOf(randomfile.readChar());
				returnValue+=str;
				index++;
			}
			currentPageNumber++;
			if(pageNumber.get(currentPageNumber)!=null){
				pageNumber.add(currentPageNumber,index+1);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnValue;
		
	}
	
//	public static Boolean canReadBack(){
//		
//	}
//	
//	public static Boolean canReadAhead(){
//		
//	}
}

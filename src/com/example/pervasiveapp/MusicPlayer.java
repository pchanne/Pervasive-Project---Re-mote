package com.example.pervasiveapp;

import java.io.File;
import java.net.URI;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

public class MusicPlayer extends AsyncTask<Void, Void, Void>  {

	private static Context context;
	private File musicDir;
	private File moodDirectory;
	private static int songIndex;
	File[] songs;
	private static String mood;
	
	public static String getMood() {
		return mood;
	}

	public static void setMood(String mood) {
		MusicPlayer.mood = mood;
	}

	public static int getSongIndex() {
		return songIndex;
	}

	public static void setSongIndex(int songIndex) {
		MusicPlayer.songIndex = songIndex;
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		MusicPlayer.context = context;
	}

	private MediaPlayer player; 
	public MediaPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MediaPlayer player) {
		this.player = player;
	}
	

	public MusicPlayer(){
		musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
	}
	@Override
	protected Void doInBackground(Void... params) {

		Uri filetoplay = null;
		setMoodDirectory();
		songs = moodDirectory.listFiles();
		if(songs!=null && songs.length>0){
			//choose a random song from the directory chosen
			filetoplay = Uri.fromFile(songs[chooseRandomSong(songs.length)]);
		}
		player = MediaPlayer.create(context,filetoplay); 
		player.setLooping(true); // Set looping 
		player.setVolume(25, 25); 
		player.start(); 
		return null;
	}
	
	private int chooseRandomSong(int maxIndex){
		int chosenSongIndex = 0;
		chosenSongIndex = (int)(Math.random() * maxIndex);
		return chosenSongIndex;
	}
	
	public void chooseRandomSong(){
		chooseRandomSong(songs.length);
	}
	
	public void setMoodDirectory(){
		File [] filenames = musicDir.listFiles();
		if(filenames!=null && filenames.length>0){
			for(int i=0;i<filenames.length;i++){
				//logic to get directory based on mood
				if(filenames[i].isDirectory() && filenames[i].getName().equalsIgnoreCase(mood)){
					moodDirectory =  filenames[i];
					break;
				}
			}
		}
	}


}

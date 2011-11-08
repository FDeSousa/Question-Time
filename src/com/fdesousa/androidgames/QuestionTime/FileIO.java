package com.fdesousa.androidgames.QuestionTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.Resources;
import android.os.Environment;

/**
 * Simplistic handler class for controlling the file-accessing mechanism.
 * @author Filipe De Sousa
 * @version 0.1
 */
public class FileIO {
	/**
	 * The file extension for QuestionTime files
	 */
	public final static String QUESTION_TIME_FILE_EXTENSION = ".Question-Time";
	public final static String FOLDER_SEPARATOR = File.separator;
	
	Resources resources;
	
	String externalStoragePath;	//	Path of the folder we want to use in ext storage
	File dir;	//	File instance of the folder we'll be reading from/writing to

	/**
	 * Constructor requires a String containing folder name where the app will
	 * read from/write to for user's own BASIC program files
	 * @param foldername - the folder to use for user's own written programs
	 * @param resources - the application's resources for later retrieval of stuff
	 */
	public FileIO(String foldername, Resources resources) {
		//	Ask the environment to construct the path to the external storage directory
		this.externalStoragePath = Environment
				.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ foldername 
				+ File.separator;

		//	Create a new File instance with the created external storage directory
		this.dir = new File(externalStoragePath);
		
		this.resources = resources;
	}

	/**
	 * Method for retrieving an InputStream to read in a file from the SD card.
	 * @param filename - the name of the file to input from
	 * @return InputStream for the file defined with fileName
	 * @throws IOException - thrown if any error occurs
	 */
	public InputStream readFile(String filename) throws IOException {
		return new FileInputStream(externalStoragePath + filename + QUESTION_TIME_FILE_EXTENSION);
	}

	/**
	 * Method for retrieving an OutputStream to write to a file on the SD card.
	 * @param filename - the name of the file to output to
	 * @return OutputStream for the file defined with fileName
	 * @throws IOException - thrown if any error occurs
	 */
	public OutputStream writeFile(String filename) throws IOException {
		return new FileOutputStream(externalStoragePath + filename + QUESTION_TIME_FILE_EXTENSION);
	}
	
	/**
	 * Convenience method to handle the deletion of a file
	 * @param filename the name of the file we want deleted
	 * @throws IOException if something should fail, we'll throw this
	 */
	public boolean deleteFile(String filename) throws IOException {
		return (new File(externalStoragePath + filename + QUESTION_TIME_FILE_EXTENSION).delete());
	}

	/**
	 * Convenience method to list the names of all the files contained in our program's
	 * folder in the device's external storage
	 * @return String array containing the number of files and name of each file, one per line
	 * @throws IOException - if the file is not readable will throw an IOException
	 */
	public String[] folderListing() throws IOException {
		String[] fileListing;
		File[] dirList = dir.listFiles();

		if (dirList != null) {
			fileListing = new String[dirList.length];
			for (int i = 0; i < dirList.length; i++)
				fileListing[i] = dirList[i].getName();
		} else {
			fileListing = new String[1];
			fileListing[0] = "Folder empty";
		}
		
		return fileListing;
	}
}

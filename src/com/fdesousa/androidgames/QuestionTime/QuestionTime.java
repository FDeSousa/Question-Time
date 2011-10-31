package com.fdesousa.androidgames.QuestionTime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.TextView;

public class QuestionTime extends Activity implements OnClickListener {
	/**	String used for logging purposes	*/
	public static final String TAG = "QuestionTime";

	/**	An instance of QuestionTime for later use of screen widgets	*/
	static QuestionTime INSTANCE;
	/**	Static instance of Resources for loading files	*/
	static Resources RESOURCES;
	/**	Static instance of FileIO for loading files	*/
	static FileIO FILE_IO;

	/**	Displays the current question number on screen	*/
	static TextView CURRENT_QUESTION_NUMBER;
	/**	Displays the current question to be answered	*/
	static TextView CURRENT_QUESTION_TEXT;
	/**	Represents Button for Answer 1	*/
	static Button ANSWER_BUTTON_1;
	/**	Represents Button for Answer 2	*/
	static Button ANSWER_BUTTON_2;
	/**	Represents Button for Answer 3	*/
	static Button ANSWER_BUTTON_3;
	/**	Represents Button for Answer 4	*/
	static Button ANSWER_BUTTON_4;
	
	static AnimationSet CHECKING;
	static AnimationSet CORRECT;
	static AnimationSet INCORRECT;

	DialogInterface.OnClickListener dialogClickListener;
	AlertDialog.Builder builder;
	Game game;

	/** 
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//	We don't want no stinkin' title bar here!
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		//	Now load up that thar main view!
		setContentView(R.layout.question_layout);

		INSTANCE = this;
		RESOURCES = getResources();
		FILE_IO = new FileIO(TAG);

		CURRENT_QUESTION_NUMBER = (TextView) findViewById(R.id.questionNumber);
		CURRENT_QUESTION_TEXT = (TextView) findViewById(R.id.currentQuestion);
		ANSWER_BUTTON_1 = (Button) findViewById(R.id.answer1);
		ANSWER_BUTTON_2 = (Button) findViewById(R.id.answer2);
		ANSWER_BUTTON_3 = (Button) findViewById(R.id.answer3);
		ANSWER_BUTTON_4 = (Button) findViewById(R.id.answer4);

		//	Setup the dialogClickListener so we know what to do when back is pressed
		dialogClickListener = new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE: finish();
					case DialogInterface.BUTTON_NEGATIVE: dialog.cancel();
				}
			}
		};

		//	Setup the AlertDialog.Builder to display the dialog later
		builder = new AlertDialog.Builder(this)
			.setMessage("Quit Question Time?")
			.setPositiveButton(android.R.string.yes, dialogClickListener)
			.setNegativeButton(android.R.string.no, dialogClickListener);
		//	Setup of Dialog finished

		//	Instantiate the new Game instance to run
		game = new Game();
	}

	@Override
	public void onBackPressed() {		
		//	Now simply display this dialog
		builder.show();
	}
/*
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Ignore configuration changes, we want to keep things landscape
	}
*/
	@Override
	public void onClick(View v) {
		/*
		 *	To simplify and get rid of objects, use a switch .. case
		 *+	instead of an individual onClick Listener for each button
		 *	As switch .. case requires constants, we're using the
		 *+	R.id values to match against rather than Button.getId()
		 */
		switch (v.getId()) {
		case (R.id.answer1):
			game.checkAnswer(ANSWER_BUTTON_1, 1);
			break;
		case (R.id.answer2):
			game.checkAnswer(ANSWER_BUTTON_2, 2);
			break;
		case (R.id.answer3):
			game.checkAnswer(ANSWER_BUTTON_3, 3);
			break;
		case (R.id.answer4):
			game.checkAnswer(ANSWER_BUTTON_4, 4);
			break;
		}
	}

	/**
	 *	Convenience method to enable the buttons to become clickable
	 */
	public static void enableButtons() {
		QuestionTime.ANSWER_BUTTON_1.setClickable(true);
		QuestionTime.ANSWER_BUTTON_2.setClickable(true);
		QuestionTime.ANSWER_BUTTON_3.setClickable(true);
		QuestionTime.ANSWER_BUTTON_4.setClickable(true);
	}

	/**
	 *	Convenience method to disable the buttons from being clickable
	 */
	public static void disableButtons() {
		QuestionTime.ANSWER_BUTTON_1.setClickable(false);
		QuestionTime.ANSWER_BUTTON_2.setClickable(false);
		QuestionTime.ANSWER_BUTTON_3.setClickable(false);
		QuestionTime.ANSWER_BUTTON_4.setClickable(false);
	}
}
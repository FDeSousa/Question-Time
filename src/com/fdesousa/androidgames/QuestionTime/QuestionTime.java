package com.fdesousa.androidgames.QuestionTime;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationSet;

public class QuestionTime extends Activity {
	/**	String used for logging purposes	*/
	public static final String TAG = "QuestionTime";

	/**	An instance of QuestionTime for later use of screen widgets	*/
	static QuestionTime INSTANCE;
	/**	Static instance of Resources for loading files	*/
	static Resources RESOURCES;
	/**	Static instance of FileIO for loading files	*/
	static FileIO FILE_IO;
	static UiController UI_CONTROLLER;

	static AnimationSet CHECKING;
	static AnimationSet CORRECT;
	static AnimationSet INCORRECT;

	Game game;

	/** 
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//	No title bar, load up the layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.question_layout);

		INSTANCE = this;
		FILE_IO = new FileIO(TAG, getResources());
		UI_CONTROLLER = new UiController();
		
		//	The Button backgrounds need reseting right away for some odd reason
		UiController.resetAnswerButtonBackgrounds();

		final Object retrievedData = getLastNonConfigurationInstance();
		//	Check if the application is loading for the first time
		if (retrievedData == null) {
			//	Instantiate the new Game instance to run
			game = new Game();
		} else {
			//	Add a method to setup the UI elements after retrieving saved game session
			//+	otherwise when changing device orientation the widgets' text is reset to defaults
			game = (Game) retrievedData;
			UiController.resetAnswerButtonBackgrounds();
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		//	Only thing we would like to save is the game instance
		return game;
	}

	@Override
	public void onBackPressed() {		
		//	Now simply display this dialog
		UI_CONTROLLER.displayExitConfirmationDialog();
	}

	public void answerButton(View v) {
		/*
		 *	To simplify, we are using a switch .. case instead of an
		 *+	individual onClick Listener for each button
		 *	As switch .. case requires constants, we're using the
		 *+	R.id values to match against rather than Button.getId()
		 */
		switch (v.getId()) {
		case (R.id.question_answer1):
			game.checkAnswer(UiController.ANSWER_BUTTON_1, 1);
			break;
		case (R.id.question_answer2):
			game.checkAnswer(UiController.ANSWER_BUTTON_2, 2);
			break;
		case (R.id.question_answer3):
			game.checkAnswer(UiController.ANSWER_BUTTON_3, 3);
			break;
		case (R.id.question_answer4):
			game.checkAnswer(UiController.ANSWER_BUTTON_4, 4);
			break;
		}
	}
}
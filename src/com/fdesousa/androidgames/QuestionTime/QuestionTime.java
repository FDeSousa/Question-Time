package com.fdesousa.androidgames.QuestionTime;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * <h1>QuestionTime</h1>
 * <h3>Question Time's main activity</h3>
 * Main Activity for Question Time, a simple multiple-choice question-answer
 * game running on Android. Handles resources and static instances.
 * @author Filipe De Sousa
 * @version 0.5
 */
public class QuestionTime extends Activity {
	/**	String used for logging purposes	*/
	public static final String TAG = "QuestionTime";

	/**	An instance of QuestionTime for later use of screen widgets	*/
	static QuestionTime INSTANCE;
	/**	Static instance of Resources for loading files				*/
	static Resources RESOURCES;
	/**	Static instance of FileIO for loading files					*/
	static FileIO FILE_IO;
	/**	Static instance of UiController for handling UI widgets		*/
	static UiController UI_CONTROLLER;

	Game game;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//	No title bar, load up the layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.question_layout);

		INSTANCE = this;
		FILE_IO = new FileIO(TAG);
		RESOURCES = getResources();
		UI_CONTROLLER = new UiController();

		final Object retrievedData = getLastNonConfigurationInstance();
		//	Check if the application is loading for the first time
		if (retrievedData == null) {
			//	Instantiate the new Game instance to run
			game = new Game();
		} else {
			//	Add a method to setup the UI elements after retrieving saved game session
			//+	otherwise when changing device orientation the widgets' text is reset to defaults
			game = (Game) retrievedData;
			game.setupUiControls();
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

	/**
	 * OnClick method for all four Answer buttons in the main layout
	 * @param v - instance of View, used to determine the pressed Button
	 */
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
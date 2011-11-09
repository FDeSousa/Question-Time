package com.fdesousa.androidgames.QuestionTime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.TextView;

/**
 * <h1>UiController</h1>
 * <h3>UI Controllers Handler class for Question Time</h3>
 * <p>This class handles all of the references to UI controls in the main activity
 * of the application. </p>
 * @author Filipe De Sousa
 * @version 0.5
 */
public class UiController {
	/**	Displays the current question number on screen	*/
	static TextView CURRENT_QUESTION_NUMBER;
	/**	Displays the current question to be answered	*/
	static TextView CURRENT_QUESTION_TEXT;
	/**	Represents Button for Answer 1					*/
	static Button ANSWER_BUTTON_1;
	/**	Represents Button for Answer 2					*/
	static Button ANSWER_BUTTON_2;
	/**	Represents Button for Answer 3					*/
	static Button ANSWER_BUTTON_3;
	/**	Represents Button for Answer 4					*/
	static Button ANSWER_BUTTON_4;

	/**	CHECKING animation from res/drawable/answer_checking_animation.xml		*/
	static AnimationSet CHECKING;
	/**	CORRECT animation from res/drawable/answer_correct_animation.xml		*/
	static AnimationSet CORRECT;
	/**	INCORRECT animation from res/drawable/answer_incorrect_animation.xml	*/
	static AnimationSet INCORRECT;

	/**
	 * Instantiate the static references to the on-screen Buttons, build the dialogs for later use
	 */
	public UiController() {
		CURRENT_QUESTION_NUMBER = (TextView) QuestionTime.INSTANCE.findViewById(R.id.question_questionNumber);
		CURRENT_QUESTION_TEXT = (TextView) QuestionTime.INSTANCE.findViewById(R.id.question_currentQuestion);
		ANSWER_BUTTON_1 = (Button) QuestionTime.INSTANCE.findViewById(R.id.question_answer1);
		ANSWER_BUTTON_2 = (Button) QuestionTime.INSTANCE.findViewById(R.id.question_answer2);
		ANSWER_BUTTON_3 = (Button) QuestionTime.INSTANCE.findViewById(R.id.question_answer3);
		ANSWER_BUTTON_4 = (Button) QuestionTime.INSTANCE.findViewById(R.id.question_answer4);

		buildEndGameConfirmationDialog();
		buildExitConfirmationDialog();
	}

	/**
	 * Convenience method to reset each Answer button's background resources to our defaults
	 */
	public static void resetAnswerButtonBackgrounds() {
		ANSWER_BUTTON_1.setBackgroundResource(R.drawable.button);
		ANSWER_BUTTON_2.setBackgroundResource(R.drawable.button);
		ANSWER_BUTTON_3.setBackgroundResource(R.drawable.button);
		ANSWER_BUTTON_4.setBackgroundResource(R.drawable.button);
	}

	/**
	 * Convenience method to set the text of all Main Activity's UI controls at once
	 * @param questionNumber - current Question's number text to set
	 * @param questionText - current Question's question text to set
	 * @param answerText1 - text from current Question's Answer number 1
	 * @param answerText2 - text from current Question's Answer number 2
	 * @param answerText3 - text from current Question's Answer number 3
	 * @param answerText4 - text from current Question's Answer number 4
	 */
	public static void setUiControlsText(String questionNumber, String questionText, 
			String answerText1, String answerText2, String answerText3, String answerText4) {
		CURRENT_QUESTION_NUMBER.setText(questionNumber);
		CURRENT_QUESTION_TEXT.setText(questionText);
		ANSWER_BUTTON_1.setText(answerText1);
		ANSWER_BUTTON_2.setText(answerText2);
		ANSWER_BUTTON_3.setText(answerText3);
		ANSWER_BUTTON_4.setText(answerText4);
	}

	/**
	 *	Convenience method to enable the buttons to become clickable
	 */
	public static void setButtonsEnabled(boolean enabled) {
		ANSWER_BUTTON_1.setClickable(enabled);
		ANSWER_BUTTON_2.setClickable(enabled);
		ANSWER_BUTTON_3.setClickable(enabled);
		ANSWER_BUTTON_4.setClickable(enabled);
	}

	/*	Anything to do with pop-up dialogs is below only	*/

	//	Quit dialog variables, builder and methods
	private DialogInterface.OnClickListener exitConfirmationDialogClickListener;
	private AlertDialog.Builder exitConfirmation;

	/**
	 * Method to build the Exit Confirmation dialog showed when pressing the back button.
	 */
	private void buildExitConfirmationDialog() {
		//	Setup the quitDialogClickListener so we know what to do when back is pressed
		exitConfirmationDialogClickListener = new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE: QuestionTime.INSTANCE.finish();
				case DialogInterface.BUTTON_NEGATIVE: dialog.cancel();
				}
			}
		};
		//	Setup the AlertDialog.Builder to display the dialog later
		exitConfirmation = new AlertDialog.Builder(QuestionTime.INSTANCE)
			.setMessage("Quit Question Time?")
			.setPositiveButton(android.R.string.yes, exitConfirmationDialogClickListener)
			.setNegativeButton(android.R.string.no, exitConfirmationDialogClickListener);
		//	Setup of Dialog finished
	}

	/**
	 * Convenience method to display the Exit confirmation dialog
	 */
	public void displayExitConfirmationDialog() {
		exitConfirmation.show();
	}

	//	End Game dialog variables, builder and methods
	private DialogInterface.OnClickListener endGameConfirmationDialogClickListener;
	private AlertDialog.Builder endGameConfirmation;

	/**
	 * Method to build the End Game dialog, showed when the end game condition is called
	 */
	private void buildEndGameConfirmationDialog() {
		//	Setup the dialogClickListener so we know what to do when back is pressed
		endGameConfirmationDialogClickListener = new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					QuestionTime.INSTANCE.game = new Game();
					dialog.cancel();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					QuestionTime.INSTANCE.finish();
				}
			}
		};
		//	Setup the AlertDialog.Builder to display the dialog later
		endGameConfirmation = new AlertDialog.Builder(QuestionTime.INSTANCE)
			.setPositiveButton("Play Again", endGameConfirmationDialogClickListener)
			.setNegativeButton("Quit", endGameConfirmationDialogClickListener);
		//	Setup of Dialog finished
	}
	
	/**
	 * Convenience method to display the End Game dialog, with custom message
	 * @param message - the message to display to the user in the dialog
	 */
	public void displayEndGameConfirmationDialog(String message) {
		endGameConfirmation.setMessage(message);
		endGameConfirmation.show();
	}
}
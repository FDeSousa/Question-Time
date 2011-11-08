package com.fdesousa.androidgames.QuestionTime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.TextView;

public class UiController {
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

		buildEndDialog();
		buildQuitDialog();
	}

	public static void resetAnswerButtonBackgrounds() {
		ANSWER_BUTTON_1.setBackgroundResource(R.drawable.button);
		ANSWER_BUTTON_2.setBackgroundResource(R.drawable.button);
		ANSWER_BUTTON_3.setBackgroundResource(R.drawable.button);
		ANSWER_BUTTON_4.setBackgroundResource(R.drawable.button);
	}

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
	private DialogInterface.OnClickListener quitDialogClickListener;
	private AlertDialog.Builder quit;

	private void buildQuitDialog() {
		//	Setup the quitDialogClickListener so we know what to do when back is pressed
		quitDialogClickListener = new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE: QuestionTime.INSTANCE.finish();
				case DialogInterface.BUTTON_NEGATIVE: dialog.cancel();
				}
			}
		};
		//	Setup the AlertDialog.Builder to display the dialog later
		quit = new AlertDialog.Builder(QuestionTime.INSTANCE)
			.setMessage("Quit Question Time?")
			.setPositiveButton(android.R.string.yes, quitDialogClickListener)
			.setNegativeButton(android.R.string.no, quitDialogClickListener);
		//	Setup of Dialog finished
	}

	public void displayExitConfirmationDialog() {
		quit.show();
	}

	//	End Game dialog variables, builder and methods
	private DialogInterface.OnClickListener dialogClickListener;
	private AlertDialog.Builder end;

	private void buildEndDialog() {
		//	Setup the dialogClickListener so we know what to do when back is pressed
		dialogClickListener = new DialogInterface.OnClickListener() {
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
		end = new AlertDialog.Builder(QuestionTime.INSTANCE)
			.setPositiveButton("Play Again", dialogClickListener)
			.setNegativeButton("Quit", dialogClickListener);
		//	Setup of Dialog finished
	}
	
	public void displayEndGameConfirmationDialog(String message) {
		end.setMessage(message);
		end.show();
	}
}
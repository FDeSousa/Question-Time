package com.fdesousa.androidgames.QuestionTime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;

public class Game {

	private QuestionsDataStruct questions;
	private Question currentQuestion;
	private int correctAnswers;
	private DialogInterface.OnClickListener dialogClickListener;
	private AlertDialog.Builder end;

	public Game() {
		//	Default question set to use, call other constructor with it
		this(QuestionsDataStruct.instantiateQuestions(QuestionTime.RESOURCES.getXml(R.xml.questions_1)));
	}

	public Game(QuestionsDataStruct questions) {
		//	Load up the Game class with the parsed-in QuestionsDataStruct instance
		this.questions = questions;
		correctAnswers = 0;
		updateQuestion();

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
		
		//	Reset the Answer button backgrounds, to be sure Animations aren't messing with them
		QuestionTime.ANSWER_BUTTON_1.setBackgroundResource(R.drawable.button);
		QuestionTime.ANSWER_BUTTON_2.setBackgroundResource(R.drawable.button);
		QuestionTime.ANSWER_BUTTON_3.setBackgroundResource(R.drawable.button);
		QuestionTime.ANSWER_BUTTON_4.setBackgroundResource(R.drawable.button);
	}

	public void updateQuestion() {
		//	Just in case, check if it's null, even though it probably won't be
		if (questions.hasNext()) {
			//	Get next question, check if it isn't null
			if ((currentQuestion = questions.next()) != null) {
				//1	Set the on-screen controls' text
				//2	Update the Question number
				QuestionTime.CURRENT_QUESTION_NUMBER.setText("Question number " + currentQuestion.getQuestionNumber());
				//3	Update the Question text display
				QuestionTime.CURRENT_QUESTION_TEXT.setText(currentQuestion.getQuestionText());
				//4	Update the answer buttons
				QuestionTime.ANSWER_BUTTON_1.setText(currentQuestion.getAnAnswer(0));
				QuestionTime.ANSWER_BUTTON_2.setText(currentQuestion.getAnAnswer(1));
				QuestionTime.ANSWER_BUTTON_3.setText(currentQuestion.getAnAnswer(2));
				QuestionTime.ANSWER_BUTTON_4.setText(currentQuestion.getAnAnswer(3));
			} else {	//	Otherwise, force end game condition
				endGame();
				return;
			}
		} else {	//	No more questions? Then it's the end of the game
			endGame();
			return;
		}
	}

	public void checkAnswer(Button pressed, int answerNumber) {
		QuestionTime.disableButtons();

		//	Feels stupid having to include this line here, but it's a safer option for testing
		if (currentQuestion == null)
			questions.getCurrent();

		if (currentQuestion.checkAnswer(pressed, answerNumber)) {
			//	Get the next question ready to answer, reset the button backgrounds
			updateQuestion();
			//	Reset the Answer button backgrounds, to be sure Animations aren't messing with them
			QuestionTime.ANSWER_BUTTON_1.setBackgroundResource(R.drawable.button);
			QuestionTime.ANSWER_BUTTON_2.setBackgroundResource(R.drawable.button);
			QuestionTime.ANSWER_BUTTON_3.setBackgroundResource(R.drawable.button);
			QuestionTime.ANSWER_BUTTON_4.setBackgroundResource(R.drawable.button);
			//	Increment the number of correct answers that were answered
			correctAnswers++;
		} else {
			//	Show end game dialog and condition
			endGame();
		}

		QuestionTime.enableButtons();
	}

	public void endGame() {
		//	TODO: Display the end game condition
		end.setMessage("You scored " + correctAnswers + " correct answers!");
		end.show();
	}
}

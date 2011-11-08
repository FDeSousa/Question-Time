package com.fdesousa.androidgames.QuestionTime;

import android.widget.Button;

public class Game {

	private QuestionsDataStruct questions;
	private Question currentQuestion;
	private int correctAnswers;
	

	public Game() {
		//	Default question set to use, call other constructor with it
		this(QuestionsDataStruct.instantiateQuestions(QuestionTime.RESOURCES.getXml(R.xml.questions_1)));
	}

	public Game(QuestionsDataStruct questions) {
		//	Load up the Game class with the parsed-in QuestionsDataStruct instance
		this.questions = questions;
		correctAnswers = 0;
		updateQuestion();
		
		//	Reset the Answer button backgrounds, to be sure Animations aren't messing with them
		UiController.resetAnswerButtonBackgrounds();
	}

	public void updateQuestion() {
		//	Just in case, check if it's null, even though it probably won't be
		if (questions.hasNext()) {
			//	Get next question, check if it isn't null
			if ((currentQuestion = questions.next()) != null) {
				//	Get all of the strings for convenience first
				String number = "Question number " + currentQuestion.getQuestionNumber(), 
						text = currentQuestion.getQuestionText(), 
						a1 = currentQuestion.getAnAnswer(0), 
						a2 = currentQuestion.getAnAnswer(1), 
						a3 = currentQuestion.getAnAnswer(2), 
						a4 = currentQuestion.getAnAnswer(3);
				//	Setup the UI with the static method
				UiController.setUiControlsText(number, text, a1, a2, a3, a4);
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
		UiController.setButtonsEnabled(false);

		//	Feels stupid having to include this line here, but it's a safer option for testing
		if (currentQuestion == null)
			questions.getCurrent();

		if (currentQuestion.checkAnswer(pressed, answerNumber)) {
			//	Get the next question ready to answer, reset the button backgrounds
			updateQuestion();
			//	Reset the Answer button backgrounds, to be sure Animations aren't messing with them
			UiController.resetAnswerButtonBackgrounds();
			//	Increment the number of correct answers that were answered
			correctAnswers++;
		} else {
			//	Show end game dialog and condition
			endGame();
		}

		UiController.setButtonsEnabled(true);
	}

	public void endGame() {
		//	Display the end game condition
		QuestionTime.UI_CONTROLLER.displayEndGameConfirmationDialog(
					"You scored " + correctAnswers + " correct answers!");
	}
}

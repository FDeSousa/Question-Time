package com.fdesousa.androidgames.QuestionTime;

import android.widget.Button;

/**
 * <h1>Game</h1>
 * <h3>A Game instance for Question Time</h3>
 * <p>Game class for Question Time, which handles the currently playing game
 * and checks on the game conditions, deciding the next step each time.</p>
 * @author Filipe De Sousa
 * @version 0.5
 */
public class Game {

	private QuestionsDataStruct questions;
	private Question currentQuestion;
	private int correctAnswers;

	/**
	 * Default constructor, calls the overloaded constructor to load the default
	 * included question set, useful for a demo of the application.
	 */
	public Game() {
		//	Default question set to use, call other constructor with it
		this(QuestionsDataStruct.instantiateQuestions(QuestionTime.RESOURCES.getXml(R.xml.questions_1)));
	}

	/**
	 * Overloaded constructor, will use the question set parsed to it for the current game instance.
	 * @param questions - the set of questions to use in this game instance
	 */
	public Game(QuestionsDataStruct questions) {
		//	Load up the Game class with the parsed-in QuestionsDataStruct instance
		this.questions = questions;
		correctAnswers = 0;
		updateQuestion();
	}

	/**
	 * Convenience method to update the current question. Will always attempt to
	 * get the next question in the set if one exists, otherwise it will run
	 * the end game condition.
	 */
	public void updateQuestion() {
		//	Just in case, check if it's null, even though it probably won't be
		if (questions.hasNext()) {
			//	Get next question, check if it isn't null
			if ((currentQuestion = questions.next()) != null) {
				setupUiControls();
			} else {	//	Otherwise, force end game condition
				endGame();
				return;
			}
		} else {	//	No more questions? Then it's the end of the game
			endGame();
			return;
		}
	}

	/**
	 * Convenience method to setup the UI controls with their default
	 * backgrounds, relevant text, and to be certain buttons are enabled.
	 */
	public void setupUiControls() {
		//	Get all of the strings for convenience first
		String number = "Question number " + currentQuestion.getQuestionNumber(), 
				text = currentQuestion.getQuestionText(), 
				a1 = currentQuestion.getAnAnswer(0), 
				a2 = currentQuestion.getAnAnswer(1), 
				a3 = currentQuestion.getAnAnswer(2), 
				a4 = currentQuestion.getAnAnswer(3);
		//	Setup the UI with the static method
		UiController.setUiControlsText(number, text, a1, a2, a3, a4);
		
		//	Reset the Answer button backgrounds, to be sure Animations aren't messing with them
		UiController.resetAnswerButtonBackgrounds();
		
		//	For safety, make sure buttons are enabled
		UiController.setButtonsEnabled(true);
	}

	/**
	 * Convenience method to check whether the chosen answer is correct,
	 * which then gets the next question, otherwise shows end game condition.
	 * @param pressed - instance of the button that was pressed
	 * @param answerNumber - integer related to the button pressed
	 */
	public void checkAnswer(Button pressed, int answerNumber) {
		UiController.setButtonsEnabled(false);

		//	Feels stupid having to include this line here, but it's a safer option for testing
		if (currentQuestion == null)
			questions.getCurrent();

		if (currentQuestion.checkAnswer(pressed, answerNumber)) {
			//	Get the next question ready to answer, reset the button backgrounds
			updateQuestion();
			//	Increment the number of correct answers that were answered
			correctAnswers++;
		} else {
			//	Show end game dialog and condition
			endGame();
		}
	}

	/**
	 * Convenience method to display the end game condition with a relevant
	 * message in the dialog box.
	 */
	public void endGame() {
		//	Display the end game condition
		QuestionTime.UI_CONTROLLER.displayEndGameConfirmationDialog(
					"You scored " + correctAnswers + " correct answers!");
	}
}

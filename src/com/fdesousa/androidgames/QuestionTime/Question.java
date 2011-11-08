package com.fdesousa.androidgames.QuestionTime;

import android.graphics.drawable.AnimationDrawable;
import android.widget.Button;

public class Question {

	private final int questionNumber;
	private final String question;
	private final String[] answers;
	private final int correctAnswer;

	public Question(int questionNumber, String question, String[] answers, int correctAnswer) {
		this.questionNumber = questionNumber;
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
	}

	/**
	 * Basic getter method, get the Question number
	 * @return The number associated with this Question
	 */
	public int getQuestionNumber() {
		return questionNumber;
	}
	
	/**
	 * Basic getter method, get the text of the Question to be answered
	 * @return The text associated with this Question
	 */
	public String getQuestionText() {
		return question;
	}

	/**
	 * Basic getter method, get the text of one of the possible Answers
	 * @param answerNumber - integer representing the answer number to get the text of
	 * @return Answer String corresponding to answerNumber, or null if it doesn't exist
	 */
	public String getAnAnswer(int answerNumber) {
		if (answerNumber < answers.length)
			return answers[answerNumber];
		else
			return null;
	}

	/**
	 * Checks the answer for correctness, plays the button animations for
	 * correctness or incorrectness of an answer, returns whether the answer
	 * was correct or not so that an end game condition may be handled
	 * @param answer The number of the answer selected by the user (1 to 4)
	 * @return True if the user selected the correct answer, False otherwise
	 */
	public boolean checkAnswer(Button pressed, int answerNumber) {
		//	Set the background of the button to orange
		pressed.setBackgroundResource(R.drawable.answer_checking_animation);
		AnimationDrawable pressedAnswerAnimation = (AnimationDrawable) pressed.getBackground();
		pressedAnswerAnimation.start();
		
		if (answerNumber == correctAnswer) {
			//	Correct answer selected, so show the correct answer animation
			pressed.setBackgroundResource(R.drawable.answer_correct_animation);
			pressedAnswerAnimation = (AnimationDrawable) pressed.getBackground();
			pressedAnswerAnimation.start();
		} else {
			//	Incorrect answer, so show the incorrect answer animation for the selected button,
			//+	and the correct answer animation for the button relating to the correct answer
			//	Incorrect answer animation
			pressed.setBackgroundResource(R.drawable.answer_incorrect_animation);
			pressedAnswerAnimation = (AnimationDrawable) pressed.getBackground();
			pressedAnswerAnimation.start();
			//	Correct answer animation
			Button correctAnswer = getCorrectAnswerButton();
			correctAnswer.setBackgroundResource(R.drawable.answer_correct_animation);
			AnimationDrawable correctAnswerAnimation = (AnimationDrawable) correctAnswer.getBackground();
			correctAnswerAnimation.start();
		}
		
		return answerNumber == correctAnswer;
	}
	
	/**
	 * Convenience method to determine which button has the correct answer
	 * @return the Button corresponding to the correct answer number
	 */
	private Button getCorrectAnswerButton() {
		switch (correctAnswer) {
		case 1:
			return UiController.ANSWER_BUTTON_1;
		case 2:
			return UiController.ANSWER_BUTTON_2;
		case 3:
			return UiController.ANSWER_BUTTON_3;
		case 4:
			return UiController.ANSWER_BUTTON_4;
		default:
			return null;
		}
	}
}

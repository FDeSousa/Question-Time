package com.fdesousa.androidgames.QuestionTime;

import android.widget.Button;

public class Game {

	private QuestionsDataStruct questions;
	private Question currentQuestion;

	public Game() {
		//	Default question set to use
		questions = QuestionsDataStruct.instantiateQuestions(QuestionTime.RESOURCES.getXml(R.xml.questions_1));
	}
	
	public Game(QuestionsDataStruct questions) {
		this.questions = questions;
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
		
		if (currentQuestion.checkAnswer(pressed, answerNumber)) {
			//	Get the next question ready to answer, reset the button backgrounds
			updateQuestion();
			//	Reset the Answer button backgrounds, to be sure
			QuestionTime.ANSWER_BUTTON_1.setBackgroundResource(R.drawable.answer_button_states);
			QuestionTime.ANSWER_BUTTON_2.setBackgroundResource(R.drawable.answer_button_states);
			QuestionTime.ANSWER_BUTTON_3.setBackgroundResource(R.drawable.answer_button_states);
			QuestionTime.ANSWER_BUTTON_4.setBackgroundResource(R.drawable.answer_button_states);
		} else {
			//	Show end game dialog and condition
			endGame();
		}
		
		QuestionTime.enableButtons();
	}
	
	public void endGame() {
		//	TODO: Display the end game condition
	}
}

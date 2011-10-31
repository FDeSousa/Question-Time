package com.fdesousa.androidgames.QuestionTime;

import java.io.IOException;
import java.util.Iterator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.SparseArray;

public class QuestionsDataStruct extends SparseArray<Question> implements Iterator<Question> {
	/**	Always initialises with a minimum default capacity, as defined here	*/
	private static final int DEFAULT_INITIAL_CAPACITY = 10;
	private int next = 0;
	private final String name, difficulty, subject, description;

	/**
	 * Default constructor, providing no starting options
	 */
	public QuestionsDataStruct(String name, String difficulty, String subject, String description) {
		//	As an initial capacity wasn't defined, we use the default one
		super(DEFAULT_INITIAL_CAPACITY);
		this.name = name;
		this.difficulty = difficulty;
		this.subject = subject;
		this.description = description;
	}

	/**
	 * Overloaded constructor, providing one starting option, to set the
	 * initial capacity of the SparseArray to a defined value
	 * @param initialCapacity - the required initial capacity
	 */
	public QuestionsDataStruct(String name, String difficulty, String subject, String description, int initialCapacity) {
		//	Initialise with a specified initial capacity, rather than the default
		super(initialCapacity);
		this.name = name;
		this.difficulty = difficulty;
		this.subject = subject;
		this.description = description;
	}

	@Override
	public boolean hasNext() {
		return size() > next;
	}

	@Override
	public Question next() {
		Question toReturn;
		//	Loop until we find a non-null or we pass the SparseArray's size
		while (size() > ++next) {
			//	Return only if not null
			if ((toReturn = get(next)) != null)
				return toReturn;
		}
		//	If we've reached the end, return null
		return null;
	}

	@Override
	public void remove() {
		remove(next);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the difficulty
	 */
	public String getDifficulty() {
		return difficulty;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	//	Used for identifying the current tag we're reading in an XML file
	/**	Used to denote we're reading an undefined tag 	*/
	private static final int DEFAULT_TAG = 0;
	/**	Used to denote we're reading a Question Set tag	*/
	private static final int QUESTION_SET_TAG = 1;
	/**	Used to denote we're reading a Question tag		*/
	private static final int QUESTION_TAG = 2;
	/**	Used to denote we're reading an Answer tag		*/
	private static final int ANSWER_TAG = 3;
	/**
	 * Static method for instantiating an instance of QuestionsDataStruct with resources
	 * read in from an XML file (currently only supporting included application resources)
	 * @param parser
	 * @return
	 */
	public static final QuestionsDataStruct instantiateQuestions(XmlResourceParser parser) {
		//	Define it as a SparseArray, since we only need the SparseArray methods for now
		SparseArray<Question> questions = null;
		//	Used in the loop as exit condition
		boolean run = true;
		/*
		 * Temporary attributes, used per-Question or per-Tag
		 * questionText will contain the text of each Question before appending to SparseArray
		 * answers
		 */
		String name = "";
		String difficulty = "";
		String subject = "";
		String description = "";
		int numberOfQuestions = -1;
		String questionText = "";
		//	We store each question's answer Strings here, in order
		String[] answers = new String[4];
		//	Store current question number, which answer is correct, and which answer we're currently reading
		int questionNumber = 0, correctAnswerNumber = 0, currentAnswerNumber = 0;
		int currentTag = DEFAULT_TAG;

		try {
			int eventType = parser.getEventType();
			//	Keep looping until the end of the document
			while (run) {
				//	Check the conditions for running before switch .. case begins
				if (eventType == XmlPullParser.END_DOCUMENT) {
					run = false;
				} else {
					eventType = parser.next();					
				}
				/* Switch depending on what event type we're currently encountering */
				switch (eventType) {
				//	Starting with a new tag
				case XmlPullParser.START_TAG:
					String tagName = parser.getName();
					/*
					 * If encountering a Question Set tag beginning, get the information
					 * about it, set currentTag to QUESTION_SET_TAG
					 */
					if (tagName.equals("question-set")) {
						currentTag = QUESTION_SET_TAG;
						name = parser.getIdAttribute();
						difficulty = parser.getAttributeValue(null, "difficulty");
						subject = parser.getAttributeValue(null, "subject");
						description = parser.getAttributeValue(null, "description");
						//	If there is no numberOfQuestions attribute, default to -1 so we can then...
						numberOfQuestions = parser.getAttributeIntValue(null, "questions", -1);
						//	...correctly initialise questions with the information gathered
						if (numberOfQuestions > -1) {
							questions = new QuestionsDataStruct(name, difficulty, subject, description, numberOfQuestions);
						} else {
							questions = new QuestionsDataStruct(name, difficulty, subject, description);
						}
					/*
					 * If encountering a Question tag beginning, get the question number,
					 * set currentTag to QUESTION_TAG, set the correct answer number
					 */
					} else if (tagName.equals("question") && currentTag == QUESTION_SET_TAG) {
						currentTag = QUESTION_TAG;
						questionNumber = parser.getIdAttributeResourceValue(++questionNumber);
						correctAnswerNumber = parser.getAttributeIntValue(null, "correct", 0);
						//	Reset currentAnswerNumber for safety
						currentAnswerNumber = 0;
					/*
					 * If encountering an Answer tag beginning, get the answer number
					 * (can default to pre-incrementing if it does not exist), and set
					 * currentTag to ANSWER_TAG.
					 * This is all only if we're currently residing inside a Question tag
					 */
					} else if (tagName.equals("answer") && currentTag == QUESTION_TAG) {
						currentTag = ANSWER_TAG;
						currentAnswerNumber = parser.getAttributeIntValue(null, "number", ++currentAnswerNumber);
					/*
					 * Other, unidentified tags will be ignored for now, so set the DEFAULT_TAG
					 */
					} else {
						currentTag = DEFAULT_TAG;
					} break;
				//	Ending the current tag
				case XmlPullParser.END_TAG:
					/*
					 * If we're inside a finishing Question Set tag, set currentTag back to Default,
					 * and set run to false, signalling we've finished the Question Set (for safety)
					 */
					if (currentTag == QUESTION_SET_TAG) {
						currentTag = DEFAULT_TAG;
						run = false;
					/*
					 * If we're inside a finishing Question tag, append it with the details
					 * to the questions SparseArray, set currentTag to signal a Question Set
					 */
					} else if (currentTag == QUESTION_TAG) {
						questions.append(questionNumber, new Question(questionNumber, questionText, answers, correctAnswerNumber));
						currentTag = QUESTION_SET_TAG;
					/*
					 * If we're inside a finishing Answer tag, just set currentTag to signal a Question
					 */
					} else if (currentTag == ANSWER_TAG) {
						currentTag = QUESTION_TAG;
					} break;
				//	Encountering the text element contained within the current tag
				case XmlPullParser.TEXT:
					/*
					 * Determine if we're inside a Question tag when we find a Text tag,
					 * then place the full text into questionText for later use
					 */
					if (currentTag == QUESTION_TAG) {
						questionText = parser.getText();
					/*
					 * If we're inside an Answer tag when we find a Text tag, then place
					 * the full text into a cell of answers array
					 */
					} else if (currentTag == ANSWER_TAG) {
						if (currentAnswerNumber < answers.length)
							answers[currentAnswerNumber] = parser.getText();
					} break;
				}
			}
		} catch (XmlPullParserException e) {
			Log.e(QuestionTime.TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(QuestionTime.TAG, e.getMessage());
		} finally {
			parser.close();
		}
		return (QuestionsDataStruct) questions;
	}
}

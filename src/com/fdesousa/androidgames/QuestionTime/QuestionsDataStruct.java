package com.fdesousa.androidgames.QuestionTime;

import java.io.IOException;
import java.util.Iterator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.SparseArray;

/**
 * <h1>QuestionsDataStruct</h1>
 * <h3>Data Structure and Question Handler for Question Time</h3>
 * <p>Used to control the Question instances in a set and in each individual Game instance/session.<br />
 * Extends SparseArray to provide the organised storage of
 * Questions with key:value pairs, and implements Iterator to allow easy
 * iteration over Questions in the SparseArray.</p>
 * @author Filipe De Sousa
 * @version 0.5
 */
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
		while (size() > next++) {
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
	 * Convenience method to return the Question instance currently pointed to
	 * @return the current Question instance
	 */
	public Question getCurrent() {
		return get(next);
	}

	/**
	 * Convenience method for getting the currently loaded Question Set's basic information.<br />
	 * The order of the information in the array is as defined by the static values in:<ul>
	 * <li>QUESTION_NAME</li>
	 * <li>QUESTION_DIFFICULTY</li>
	 * <li>QUESTION_SUBJECT</li>
	 * <li>QUESTION_DESCRIPTION</li>
	 * <li>QUESTION_NUMBERS</li>
	 * </ul>
	 * @return a String array containing the read information, in order
	 */
	public String[] getCurrentQuestionSetInformation() {
		//	Instantiate new String[] with default values, just in case
		String[] information = { "name", "difficulty", "subject", "description", "number" };
		//	Set the new values into the array, in their defined order
		information[QUESTION_NAME] = name;
		information[QUESTION_DIFFICULTY] = difficulty;
		information[QUESTION_SUBJECT] = subject;
		information[QUESTION_DESCRIPTION] = description;
		information[QUESTION_NUMBERS] = Integer.toString(size());
		
		return information;
	}

	/**	Position Question Name is in array returned by getQuestionsInformation(parser)			*/
	public static final int QUESTION_NAME = 0;
	/**	Position Question Difficulty is in array returned by getQuestionsInformation(parser)	*/
	public static final int QUESTION_DIFFICULTY = 1;
	/**	Position Question Subject is in array returned by getQuestionsInformation(parser)		*/
	public static final int QUESTION_SUBJECT = 2;
	/**	Position Question Description is in array returned by getQuestionsInformation(parser)	*/
	public static final int QUESTION_DESCRIPTION = 3;
	/**	Position Question Numbers is in array returned by getQuestionsInformation(parser)		*/
	public static final int QUESTION_NUMBERS = 4;
	
	/**
	 * Convenience method for reading in just the basic information on a Question Set from XML.<br />
	 * The order of the information in the array is as defined by the static values in:<ul>
	 * <li>QUESTION_NAME</li>
	 * <li>QUESTION_DIFFICULTY</li>
	 * <li>QUESTION_SUBJECT</li>
	 * <li>QUESTION_DESCRIPTION</li>
	 * <li>QUESTION_NUMBERS</li>
	 * </ul>
	 * @param parser - an instance of XmlResourceParser that already has the XML file loaded
	 * @return a String array containing the read information, in order
	 */
	public static final String[] readQuestionSetInformationOnly(XmlResourceParser parser) {
		String[] information = { "name", "difficulty", "subject", "description", "number" };
		boolean run = true;
		int numberOfQuestions = -1;

		int eventType;
		try {
			eventType = parser.getEventType();
			while (run) {
				if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equals("question-set")) {
						information[QUESTION_NAME] = parser.getIdAttribute();
						information[QUESTION_DIFFICULTY] = parser.getAttributeValue(null, "difficulty");
						information[QUESTION_SUBJECT] = parser.getAttributeValue(null, "subject");
						information[QUESTION_DESCRIPTION] = parser.getAttributeValue(null, "description");
						//	If there is no numberOfQuestions attribute, default to -1 so we can then...
						numberOfQuestions = parser.getAttributeIntValue(null, "questions", -1);
						//	...correctly initialise questions with the information gathered
						if (numberOfQuestions > -1) {
							information[QUESTION_NUMBERS] = Integer.toString(numberOfQuestions);
						} else {
							information[QUESTION_NUMBERS] = "Unknown";
						}
					}
				}
			}
		} catch (XmlPullParserException e) {
			Log.e(QuestionTime.TAG, e.getMessage());
		}

		return information;
	}

	//	Below values are used exclusively for identifying the current tag we're reading in an XML file
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
	 * read in from an XML file (currently only supports pre-compiled XML in res/xml)
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
				/* Switch depending on what event type we're currently encountering */
				switch (eventType) {
				//	Starting with a new tag
				case XmlPullParser.START_TAG:
					String tagName = parser.getName();
					if (tagName.equals("question-set")) {
						/*
						 * If encountering a Question Set tag beginning, get the information
						 * about it, set currentTag to QUESTION_SET_TAG
						 */
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
					} else if (tagName.equals("question") && currentTag == QUESTION_SET_TAG) {
						/*
						 * If encountering a Question tag beginning, get the question number,
						 * set currentTag to QUESTION_TAG, set the correct answer number
						 */
						currentTag = QUESTION_TAG;
						questionNumber = parser.getIdAttributeResourceValue(++questionNumber);
						correctAnswerNumber = parser.getAttributeIntValue(null, "correct", 0);
						//	Reset currentAnswerNumber for safety
						currentAnswerNumber = 0;
						answers = new String[4];
					} else if (tagName.equals("answer") && currentTag == QUESTION_TAG) {
						/*
						 * If encountering an Answer tag beginning, get the answer id number,
						 * using the current answer number if necessary, using a safe +1 default
						 * This is all only if we're currently residing inside a Question tag
						 */
						currentTag = ANSWER_TAG;
						//	For safety, use currentAnswerNumber +2 (to account for the -1) as default,
						//+	though shouldn't be needed, and take 1 away as the IDs count from 1 not 0
						currentAnswerNumber = parser.getAttributeIntValue(null, "id", currentAnswerNumber + 1);
					} else {
						/*
						 * Other, unidentified tags will be ignored for now, so set the DEFAULT_TAG
						 */
						currentTag = DEFAULT_TAG;
					} break;
					//	Ending the current tag
				case XmlPullParser.END_TAG:
					if (currentTag == QUESTION_SET_TAG) {
						/*
						 * If we're inside a finishing Question Set tag, set currentTag back to Default,
						 * and set run to false, signalling we've finished the Question Set (for safety)
						 */
						currentTag = DEFAULT_TAG;
						run = false;
					} else if (currentTag == QUESTION_TAG) {
						/*
						 * If we're inside a finishing Question tag, append it with the details
						 * to the questions SparseArray, set currentTag to signal a Question Set
						 */
						questions.append(questionNumber, new Question(questionNumber, questionText, answers, correctAnswerNumber));
						currentTag = QUESTION_SET_TAG;
					} else if (currentTag == ANSWER_TAG) {
						/*
						 * If we're inside a finishing Answer tag, just set currentTag to signal a Question
						 */
						currentTag = QUESTION_TAG;
					} break;
				case XmlPullParser.TEXT:
					//	Encountering the text element contained within the current tag
					if (currentTag == QUESTION_TAG) {
						/*
						 * Determine if we're inside a Question tag when we find a Text tag,
						 * then place the full text into questionText for later use
						 */
						questionText = parser.getText();
					} else if (currentTag == ANSWER_TAG) {
						/*
						 * If we're inside an Answer tag when we find a Text tag, then place
						 * the full text into a cell of answers array
						 */
						if (currentAnswerNumber <= answers.length)
							answers[currentAnswerNumber - 1] = parser.getText();
					} break;
				}
				eventType = parser.next();
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

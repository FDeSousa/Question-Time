package com.fdesousa.androidgames.QuestionTime;

import java.lang.reflect.Field;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class QuestionsLoader extends Activity implements OnItemSelectedListener, OnClickListener {
	ArrayAdapter<String> adapter;
	SparseArray<String> list;
	SparseArray<String[]> information;
	static Spinner QUESTION_SETS;
	static TextView QUESTION_TEXT;
	static TextView QUESTION_DIFFICULTY;
	static TextView QUESTION_SUBJECT;
	static TextView QUESTION_DESCRIPTION;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//	No title bar, load up layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.question_loader_layout);
		
		QUESTION_SETS = (Spinner) findViewById(R.id.loader_questionSetSpinner);
		QUESTION_TEXT = (TextView) findViewById(R.id.loader_questionsText);
		QUESTION_DIFFICULTY = (TextView) findViewById(R.id.loader_difficultyText);
		QUESTION_SUBJECT = (TextView) findViewById(R.id.loader_subjectText);
		QUESTION_DESCRIPTION = (TextView) findViewById(R.id.loader_descriptionText);

		populateSpinner();
	}
	
	private void populateSpinner() {
		//	Instantiate the adapter to later add items to Spinner
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		//	Place all of the names and IDs into a list for populating the spinner
		list = new SparseArray<String>();
		information = new SparseArray<String[]>();
		Field[] fields = R.xml.class.getFields();

		for (Field f : fields) {
			try {
				//	We need both the IDs and the Names, hence the use of SparseArray
				int id = f.getInt(null);
				String[] info = QuestionsDataStruct
						.getQuestionsInformation(QuestionTime.RESOURCES.getXml(id));
				list.append(id, info[QuestionsDataStruct.QUESTION_NAME]);
				information.append(id, info);
				adapter.add(info[QuestionsDataStruct.QUESTION_NAME]);
			} catch (IllegalArgumentException e) {
				Log.e(QuestionTime.TAG, e.getMessage());
			} catch (IllegalAccessException e) {
				Log.e(QuestionTime.TAG, e.getMessage());
			}
		}
		
		QUESTION_SETS.setAdapter(adapter);
	}

	@Override
	public void finish() {
		// TODO: Allow this class to finish the whole application
		super.finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO: Get the selected item from the spinner, handle it
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Must be overridden, but doesn't have to do anything
	}

	@Override
	public void onClick(View v) {
		// TODO: Figure out what button was clicked, handle it
		
	}
}

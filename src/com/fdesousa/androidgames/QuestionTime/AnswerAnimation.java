package com.fdesousa.androidgames.QuestionTime;

import android.util.FloatMath;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class AnswerAnimation extends Animation {
	private final int totalBlinks;

	public AnswerAnimation() {
		totalBlinks = 3;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float period = interpolatedTime * totalBlinks * 3.14f + (3.14f / 2);
		t.setAlpha(Math.abs(FloatMath.cos(period)));
	}

	@Override
	public boolean willChangeBounds() {
		return false;
	}

	@Override
	public boolean willChangeTransformationMatrix() {
		return false;
	}
}

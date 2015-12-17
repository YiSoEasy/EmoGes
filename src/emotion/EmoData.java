package emotion;

import java.io.*;

/**
 * class for emotion data extracted from device
 * implement serializable interface  
 * @author Yi
 *
 */
@SuppressWarnings("serial")
public class EmoData implements Serializable {

	//all emotion data
	private float excitementShortTermScore;
	private float excitementLongTermScore;
	private float engagementBoredomScore;
	private float frustrationScore;
	private float meditationScore;

	public EmoData(float ests, float elts, float ebs, float fs, float ms) {
		this.excitementShortTermScore = ests;
		this.excitementLongTermScore = elts;
		this.engagementBoredomScore = ebs;
		this.frustrationScore = fs;
		this.meditationScore = ms;
	}
	
	public float getExcitementShortTermScore() {
		return excitementShortTermScore;
	}

	public float getExcitementLongTermScore() {
		return excitementLongTermScore;
	}

	public float getEngagementBoredomScore() {
		return engagementBoredomScore;
	}

	public float getFrustrationScore() {
		return frustrationScore;
	}

	public float getMeditationScore() {
		return meditationScore;
	}

	
	
}

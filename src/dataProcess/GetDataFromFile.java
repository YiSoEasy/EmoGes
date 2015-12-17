package dataProcess;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.leapmotion.leap.*;

import emotion.EmoData;

/**
 * 
 * get data from file and process data
 * @author Yi
 *
 */
public class GetDataFromFile {
	private String fileName = null;
	private FileInputStream fis = null;
	private Controller controller = null;
	//create four different queue to store frame into different emotion
	private ArrayList<Frame> angryFrame = null;
	private ArrayList<Frame> happyFrame = null;
	private ArrayList<Frame> sadFrame = null;
	private ArrayList<Frame> relaxFrame = null;

	float gv1, gv2, gv3, pv1;
	
	public GetDataFromFile(String f) throws Exception {
		this.controller = new Controller();
		this.fileName = f;
		this.fis = new FileInputStream(this.fileName);
		this.angryFrame = new ArrayList<Frame>();
		this.happyFrame = new ArrayList<Frame>();
		this.sadFrame = new ArrayList<Frame>();
		this.relaxFrame = new ArrayList<Frame>();
		getAvergeGrabVeloctiy();
	}

	/**
	 * de-serialize frame data and put them into byte array
	 * @return byte[]
	 * @throws IOException
	 */
	public byte[] getFrameData() throws IOException {
		byte[] len = new byte[4];
		int n = 0;
		n = fis.read(len, 0, 4);
		if (n != 4)
			return null;

		int frameLen = fromByteArray(len);
		byte[] data = new byte[frameLen];
		n = fis.read(data, 0, frameLen);
		if (n != frameLen)
			return null;

		return data;
	}

	/**
	 * de-serialize emotion data and store them into byte array
	 * @return byte[]
	 * @throws IOException
	 */
	public byte[] getEmotiData() throws IOException {
		byte[] len = new byte[4];
		int n = 0;
		n = fis.read(len, 0, 4);
		if (n != 4)
			return null;
		int emoLen = fromByteArray(len);
		byte[] data = new byte[emoLen];
		n = fis.read(data, 0, emoLen);
		if (n != emoLen)
			return null;
		return data;
	}
	
	public void getGrabVelocityUnnderHappy() {
		float gv = getAverageGrabVeloctiy(this.happyFrame);
		System.out.println("Grab Velocity: " + gv);
	}
	
	public void getPutVelocityUnnderHappy() {
		float pv = this.getAveragePutVeloctiy(happyFrame);
		System.out.println("Put Velocity: " + pv);
	}

	
	/**
	 * get both time series data from file
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void getTimeSeriesData() throws ClassNotFoundException, IOException {
		byte[] FrameBytes = null;
		byte[] EmotiBytes = null;

		// int i = 0;
		while ((FrameBytes = getFrameData()) != null
				&& (EmotiBytes = getEmotiData()) != null) {

			Frame curFrame = new Frame();
			curFrame.deserialize(FrameBytes);
			EmoData emoData = (EmoData) deserialize(EmotiBytes);
			float x = curFrame.hands().get(0).palmVelocity().getX();
			float y = curFrame.hands().get(0).palmVelocity().getY();
			float z = curFrame.hands().get(0).palmVelocity().getZ();
			double v = Math.sqrt(x * x + y * y + z * z);
			//System.out.print(v + ",");
			System.out.print(emoData.getExcitementShortTermScore() + ",");
		}
	}
	
	
	public void getGrabVelocityUnder33Emotion() throws Exception {
		byte[] FrameBytes = null;
		byte[] EmotiBytes = null;

		// int i = 0;
		while ((FrameBytes = getFrameData()) != null
				&& (EmotiBytes = getEmotiData()) != null) {

			Frame curFrame = new Frame();
			curFrame.deserialize(FrameBytes);
			EmoData emoData = (EmoData) deserialize(EmotiBytes);

			if (isHappy(emoData)) {
				this.happyFrame.add(curFrame);
			} else if (isRelax(emoData)) {
				this.relaxFrame.add(curFrame);
			} else if (isSad(emoData)) {
				this.sadFrame.add(curFrame);
			} else if (isAngry(emoData)) {
				this.angryFrame.add(curFrame);
			}
		}
		
		print3Emotion();
	}
	
	public void print3Emotion() {
		System.out.println("Happy: " + getAverageGrabVeloctiy(this.happyFrame));
		System.out.println("Relax: " + getAverageGrabVeloctiy(this.relaxFrame));
		System.out.println("Sad:   " + getAverageGrabVeloctiy(this.sadFrame));
	
	}
	

	public float getAverageGrab(ArrayList<Frame> list) {
		float count = 0;
		for (int i = 0; i < list.size(); i++) {
			Frame cur = list.get(i);
			count += cur.hands().get(0).grabStrength();
		}
		return count / list.size();
	}

	public float getAverageGrabVeloctiy(ArrayList<Frame> list) {
		float count = 0;
		for (int i = 0; i < list.size(); i++) {
			Frame cur = list.get(i);
			if (!isGrab(cur))
				continue;
			float x = cur.hands().get(0).palmVelocity().getX();
			float y = cur.hands().get(0).palmVelocity().getY();
			float z = cur.hands().get(0).palmVelocity().getZ();
			count += Math.sqrt(x * x + y * y + z * z);
		}
		return count / list.size();
	}
	
	public float getAveragePutVeloctiy(ArrayList<Frame> list) {
		float count = 0;
		for (int i = 0; i < list.size(); i++) {
			Frame cur = list.get(i);
			if (!isPut(cur))
				continue;
			float x = cur.hands().get(0).palmVelocity().getX();
			float y = cur.hands().get(0).palmVelocity().getY();
			float z = cur.hands().get(0).palmVelocity().getZ();
			count += Math.sqrt(x * x + y * y + z * z);
		}
		return count / list.size();
	}

	@SuppressWarnings("unused")
	public boolean isGrab(Frame f) {
		if ((f.hands().frontmost().direction().getX() < 0 && f.hands().frontmost().direction().getY() < 0 && f.hands().frontmost().direction().getZ() > 0) || f!=null)
			return true;
		return false;
	}

	@SuppressWarnings("unused")
	public boolean isPut(Frame f) {
		if ((f.hands().frontmost().direction().getX() > 0 && f.hands().frontmost().direction().getY() > 0 && f.hands().frontmost().direction().getZ() < 0 ) || f!=null)
			return true;
		return false;
	
	}

	/**
	 * 
	 * de-serialize for emotion data
	 * @param data
	 * @return 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object deserialize(byte[] data) throws IOException,
			ClassNotFoundException {
		if (data == null)
			return null;
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}

	int fromByteArray(byte[] bytes) {
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16
				| (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}

	// public void process() throws IOException, ClassNotFoundException {
	// byte[] preFrameBytes = getFrameData();
	// byte[] preEmotiBytes = getEmotiData();
	//
	// // int i = 0;
	// while (preEmotiBytes != null) {
	// // System.out.println(i++);
	// ArrayList<Frame> list = new ArrayList<Frame>();
	//
	// byte[] curFrameBytes = preFrameBytes;
	// byte[] curEmotiBytes = preEmotiBytes;
	// EmoData preEmodata = (EmoData) deserialize(preEmotiBytes);
	// EmoData curEmodata = (EmoData) deserialize(curEmotiBytes);
	// System.out.println(preEmodata.getExcitementShortTermScore());
	// while (curEmodata != null
	// && curEmodata.getExcitementShortTermScore() == preEmodata
	// .getExcitementShortTermScore()) {
	//
	// Frame curFrame = new Frame();
	// curFrame.deserialize(curFrameBytes);
	// list.add(curFrame);
	//
	// curFrameBytes = getFrameData();
	// curEmotiBytes = getEmotiData();
	//
	// curEmodata = (EmoData) deserialize(curEmotiBytes);
	// }
	// // getData(list);
	// // i += list.size();
	// // System.out.println(i);
	// preFrameBytes = curFrameBytes;
	// preEmotiBytes = curEmotiBytes;
	// }
	// }
	//
	// public boolean isSameMotion(EmoData e1, EmoData e2) {
	// return false;
	// }

	public boolean isHappy(EmoData emoData) {
		if (emoData.getExcitementShortTermScore() > 0.6)
			return true;
		return false;
	}

	public boolean isSad(EmoData emoData) {
		if (emoData.getExcitementShortTermScore() < 0.4)
			return true;
		return false;
	}

	public boolean isAngry(EmoData emoData) {
		if (emoData.getExcitementShortTermScore() < 0.8
				&& emoData.getExcitementShortTermScore() > 0.7)
			return true;
		return false;
	}

	public boolean isRelax(EmoData emoData) {
		if (emoData.getExcitementShortTermScore() >= 0.4
				&& emoData.getExcitementShortTermScore() <= 0.6)
			return true;
		return false;
	}
	
	@SuppressWarnings("resource")
	public void getAvergeGrabVeloctiy() throws Exception {
		String name = "C:/Users/Yi/Desktop/code/examples_Java/EmoStateLog/dzx/" + fileName;
		BufferedReader in = new BufferedReader(new FileReader(name));
		gv1 = Float.valueOf(in.readLine());
		gv2 = Float.valueOf(in.readLine());
		gv3 = Float.valueOf(in.readLine());
		pv1 = Float.valueOf(in.readLine());
	}
	
	public void getGrabVelocityUnder3Emotion() throws Exception {
		printt3Emotion();
	}
	
	public void printt3Emotion() throws Exception {
	
		System.out.println("Happy: " + gv1);
		System.out.println("Relax: " + gv2);
		System.out.println("Sad:   " + gv3);
		System.out.println();
	}
	
	public void getPutVelocityUnderHappy() {
		System.out.println("Put Velocity: " + pv1);
	}
	
	public void getGrabVelocityUnderHappy() {
		System.out.println("Grab Velocity: " + gv1);
	}
	
}

package gesture;

import com.leapmotion.leap.*;

import emotion.EmoData;

import java.io.*;
import java.util.concurrent.BlockingQueue;

/**
 * serialize data, and then store them into file 
 * @author Yi
 *
 */
public class DataToFile extends Thread {
	BlockingQueue<Frame> queueFrame = null;
	BlockingQueue<EmoData> queueEmoti = null;
	String fileName = null;
	FileOutputStream out = null;

	//initiate class, and get two queues from outer class
	public DataToFile(String name, BlockingQueue<Frame> qf,
			BlockingQueue<EmoData> qe) throws FileNotFoundException {
		this.fileName = name;
		this.queueFrame = qf;
		this.queueEmoti = qe;
		this.out = new FileOutputStream(fileName, true);
	}

	//get data from queue and then serialize them into file
	public void writeDataIntoFile() throws IOException {
		Frame frame = queueFrame.poll();
		EmoData emoti = queueEmoti.poll();
		// for emotion data
		if (frame != null && emoti != null) {
			byte[] frameData = frame.serialize();
			byte[] emotiData = serializeEmotion(emoti);
			System.out.println("Frame  " + frameData.length);
			System.out.println("Emoti  " + emotiData.length);

			byte[] frameLen = toBytes(frameData.length);
			byte[] emotiLen = toBytes(emotiData.length);
			out.write(frameLen);
			out.write(frameData);

			out.write(emotiLen);
			out.write(emotiData);
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				writeDataIntoFile();
				Thread.sleep(10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//serialize the emotion data
	public byte[] serializeEmotion(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}

	//change int to bytes array
	public byte[] toBytes(int i) {
		byte[] result = new byte[4];

		result[0] = (byte) (i >> 24);
		result[1] = (byte) (i >> 16);
		result[2] = (byte) (i >> 8);
		result[3] = (byte) (i /* >> 0 */);

		return result;
	}
}

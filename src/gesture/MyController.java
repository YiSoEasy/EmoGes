package gesture;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.leapmotion.leap.*;

import emotion.Edk;
import emotion.EmoData;


/**
 * create two queue to store frame and emotion data
 * and combine listen and controller
 * run controller to get time sereis data
 * @author Yi
 *
 */
public class MyController {

	@SuppressWarnings("deprecation")
	public void lanuch() throws IOException {
		// create a blocking queueFrame
		BlockingQueue<Frame> queueFrame = new ArrayBlockingQueue<Frame>(1024);
		BlockingQueue<EmoData> queueEmoti = new ArrayBlockingQueue<EmoData>(1024);
		

		// Create a sample listener, controller and a gestureDataformat
		Controller controller = new Controller();
		LeapEventListener listener = new LeapEventListener(queueFrame, queueEmoti);
		
		
		DataToFile df = new DataToFile("data12.txt", queueFrame, queueEmoti);

		// Have the sample listener receive events from the controller
		controller.addListener(listener);


		// retrieve data from queueFrame and store them into file
		df.start();

		// Keep this process running until Enter is pressed
		System.out.println("Press Enter to quit...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// stop df thread
		df.stop();
		// Remove the sample listener when done
		controller.removeListener(listener);
		//disconnect the eeg
		Edk.INSTANCE.EE_EngineDisconnect();
		System.out.println("Disconnected!");


	}
}

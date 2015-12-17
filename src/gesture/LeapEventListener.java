package gesture;

import java.util.concurrent.BlockingQueue;

import com.leapmotion.leap.*;

import emotion.EmoData;
import emotion.EmoStateLog;

/**
 * for certain frame rate, Listener will call onFrame() at controller's frame
 * rate LeapEventListener extends Listener
 * 
 * @author Yi
 */
public class LeapEventListener extends Listener {
	BlockingQueue<Frame> queueFrame = null;
	BlockingQueue<EmoData> queueEmo = null;
	EmoStateLog esl = null;

	public LeapEventListener(BlockingQueue<Frame> qf, BlockingQueue<EmoData> qe) {
		this.queueFrame = qf;
		this.queueEmo = qe;
		esl = new EmoStateLog();
	}

	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}

	/**
	 * for every frame, we collect data from leap motion and EEG
	 * 
	 * @param controller
	 */
	public void onFrame(Controller controller) {
		// get the data from leap motion
		Frame frame = controller.frame();
		// get the data from emotion device
		EmoData emoData = esl.getEmoData();

		if (frame == null || frame.hands().count() == 0 || emoData == null) {
			return;
		}

		//put information value into emo and frame queue
		queueEmo.offer(emoData);
		queueFrame.offer(frame);
		
		// System.out.println(st++);
		displayDemo(frame, emoData);
	}

	/**
	 * display position and velocity of hand and finger
	 * display excitement and frustration value
	 * @param frame
	 * @param emodata
	 */
	public void displayDemo(Frame frame, EmoData emodata) {
		Hand leftHand = frame.hands().get(0);
		System.out.println("Frame id: "
				+ frame.id()
				+ ", timestamp: "
				+ frame.timestamp()

				// ====================hand
				// position================================//
				+ ", left hand position: " + leftHand.palmPosition()
				+ ", left hand velocity: " + leftHand.palmVelocity()
				+ ", left finger1 position: "
				+ leftHand.fingers().get(0).tipPosition()
				+ ", left finger1 velocity: "
				+ leftHand.fingers().get(0).tipVelocity());

		// ====================Emotive Data==================================//
		System.out.println("ExcitementShortTerm: "
				+ emodata.getExcitementShortTermScore()
				+ ", ExcitementLongTerm: "
				+ emodata.getExcitementLongTermScore() + ", FrustrationScore: "
				+ emodata.getExcitementLongTermScore());
	}

}

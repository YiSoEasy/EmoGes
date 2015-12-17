package emotion;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;

/** Simple example of JNA interface mapping and usage. */
public class EmoStateLog {

	Pointer eEvent;
	Pointer eState;
	IntByReference userID;
	short composerPort;
	int option;
	int state;

	public EmoStateLog() {
		eEvent = Edk.INSTANCE.EE_EmoEngineEventCreate();
		eState = Edk.INSTANCE.EE_EmoStateCreate();
		userID = null;
		composerPort = 1726;
		option = 1;
		state = 0;

		userID = new IntByReference(0);

		switch (option) {
		case 1: {
			if (Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK
					.ToInt()) {
				System.out.println("Emotiv Engine start up failed.");
				return;
			}
			break;
		}
		case 2: {
			System.out.println("Target IP of EmoComposer: [127.0.0.1] ");

			if (Edk.INSTANCE.EE_EngineRemoteConnect("127.0.0.1", composerPort,
					"Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
				System.out
						.println("Cannot connect to EmoComposer on [127.0.0.1]");
				return;
			}
			System.out.println("Connected to EmoComposer on [127.0.0.1]");
			break;
		}
		default:
			System.out.println("Invalid option...");
			return;
		}
	}
	

	public EmoData getEmoData() {
		EmoData emoData = null;
		// while (true)
		{
			state = Edk.INSTANCE.EE_EngineGetNextEvent(eEvent);
							
			// New event needs to be handled
			if (state == EdkErrorCode.EDK_OK.ToInt()) {

				int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(eEvent);
				Edk.INSTANCE.EE_EmoEngineEventGetUserId(eEvent, userID);

				// Log the EmoState if it has been updated
				if (eventType == Edk.EE_Event_t.EE_EmoStateUpdated.ToInt()) {

					Edk.INSTANCE.EE_EmoEngineEventGetEmoState(eEvent, eState);
					float timestamp = EmoState.INSTANCE
							.ES_GetTimeFromStart(eState);
					System.out.println(timestamp + " : New EmoState from user "
							+ userID.getValue());

					System.out.print("WirelessSignalStatus: ");
					System.out.println(EmoState.INSTANCE
							.ES_GetWirelessSignalStatus(eState));
					if (EmoState.INSTANCE.ES_ExpressivIsBlink(eState) == 1)
						System.out.println("Blink");
					if (EmoState.INSTANCE.ES_ExpressivIsLeftWink(eState) == 1)
						System.out.println("LeftWink");
					if (EmoState.INSTANCE.ES_ExpressivIsRightWink(eState) == 1)
						System.out.println("RightWink");
					if (EmoState.INSTANCE.ES_ExpressivIsLookingLeft(eState) == 1)
						System.out.println("LookingLeft");
					if (EmoState.INSTANCE.ES_ExpressivIsLookingRight(eState) == 1)
						System.out.println("LookingRight");
					
					float ests, elts, ebs, fs, ms;
					ests = EmoState.INSTANCE.ES_AffectivGetExcitementShortTermScore(eState);
//					System.out.print("ExcitementShortTerm: ");
//					System.out.println(ests);
					
					elts = EmoState.INSTANCE.ES_AffectivGetExcitementLongTermScore(eState);
//					System.out.print("ExcitementLongTerm: ");
//					System.out.println(elts);
					
					
					ebs = EmoState.INSTANCE.ES_AffectivGetEngagementBoredomScore(eState);
//					System.out.print("EngagementBoredom: ");
//					System.out.println(ebs);
					
					fs = EmoState.INSTANCE.ES_AffectivGetFrustrationScore(eState);
//					System.out.print("FrustrationScore: ");
//					System.out.println(fs);
					
					ms = EmoState.INSTANCE.ES_AffectivGetMeditationScore(eState);
//					System.out.print("MeditationScore: ");
//					System.out.println(ms);
					
					emoData = new EmoData(ests, elts, ebs, fs, ms);
				}
			} else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
				System.out.println("Internal error in Emotiv Engine!");
			}
		}
		return emoData;
	}
}

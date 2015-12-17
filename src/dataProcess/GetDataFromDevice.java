package dataProcess;

import gesture.MyController;

import java.io.IOException;

/**
 * collect data from device, run the controller
 * @author Yi
 *
 */
public class GetDataFromDevice {
	MyController controller = null;
	public GetDataFromDevice() throws IOException {
		controller = new MyController();
	}
	
	public void launch() throws IOException {
		controller.lanuch();
	}
}

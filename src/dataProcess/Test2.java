package dataProcess;

public class Test2 {
	public void test() throws Exception {
		GetDataFromFile gdff1 = new GetDataFromFile("data1.txt");
		gdff1.getGrabVelocityUnderHappy();	
		gdff1.getPutVelocityUnderHappy();
		
		GetDataFromFile gdff2 = new GetDataFromFile("data2.txt");
		gdff2.getGrabVelocityUnderHappy();	
		gdff2.getPutVelocityUnderHappy();
		
		GetDataFromFile gdff3 = new GetDataFromFile("data3.txt");
		gdff3.getGrabVelocityUnderHappy();	
		gdff3.getPutVelocityUnderHappy();

		GetDataFromFile gdff4 = new GetDataFromFile("data4.txt");
		gdff4.getGrabVelocityUnderHappy();	
		gdff4.getPutVelocityUnderHappy();
		
		GetDataFromFile gdff5 = new GetDataFromFile("data5.txt");
		gdff5.getGrabVelocityUnderHappy();	
		gdff5.getPutVelocityUnderHappy();
	}
}

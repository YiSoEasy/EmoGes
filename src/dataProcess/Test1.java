package dataProcess;

public class Test1 {
	public void test() throws Exception {
		GetDataFromFile gdff1 = new GetDataFromFile("data1.txt");
		gdff1.getGrabVelocityUnderHappy();	

		GetDataFromFile gdff2 = new GetDataFromFile("data2.txt");
		gdff2.getGrabVelocityUnderHappy();	

		GetDataFromFile gdff3 = new GetDataFromFile("data3.txt");
		gdff3.getGrabVelocityUnderHappy();	

		GetDataFromFile gdff4 = new GetDataFromFile("data4.txt");
		gdff4.getGrabVelocityUnderHappy();	

		GetDataFromFile gdff5 = new GetDataFromFile("data5.txt");
		gdff5.getGrabVelocityUnderHappy();	
	}
}

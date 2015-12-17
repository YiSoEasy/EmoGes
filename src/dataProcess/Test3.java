package dataProcess;

public class Test3 {
	public void test() throws Exception {
		GetDataFromFile gdff1 = new GetDataFromFile("data1.txt");
		gdff1.getGrabVelocityUnder3Emotion();	
		
		GetDataFromFile gdff2 = new GetDataFromFile("data2.txt");
		gdff2.getGrabVelocityUnder3Emotion();	
		
		GetDataFromFile gdff3 = new GetDataFromFile("data3.txt");
		gdff3.getGrabVelocityUnder3Emotion();	
		
		GetDataFromFile gdff4 = new GetDataFromFile("data4.txt");
		gdff4.getGrabVelocityUnder3Emotion();		
		
		GetDataFromFile gdff5 = new GetDataFromFile("data5.txt");
		gdff5.getGrabVelocityUnder3Emotion();		
		
		
	}
	
}

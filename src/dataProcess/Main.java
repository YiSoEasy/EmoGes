package dataProcess;


public class Main {
	public static void main(String[] args) throws Exception,
			ClassNotFoundException {
		
		//run both device, get data from device
		 GetDataFromDevice gdfd = new GetDataFromDevice();
		 gdfd.launch();
		 
		 
//		GetDataFromFile gdff = new GetDataFromFile("data2.txt"); 
//		gdff.getTimeSeriesData();
		
//		Test1 t1 = new Test1();
//		t1.test();
		
//		Test1 t2 = new Test1();
//		t2.test();
		
//		Test3 t3 = new Test3();
//		t3.test();
		
	}
}

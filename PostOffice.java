import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class PostOffice {

	//global variables needed to be passed from CustThr to WorkThr
  public static int custId;
	public static int task;
	public static int custWithWorker[] = new int[50];

	public static void main(String args[]) {
		final int NUMCUSTOMERS	= 50;
		final int NUMWORKERS 	= 3;

		ArrayList<Semaphore> satisfiedCust 	= new ArrayList<Semaphore>();	//	makes a semaphore for every customer to indicate their completion status
		Semaphore maxRoom	= new Semaphore(10, true);		//	makes sure only ten NUMCUSTOMERS are inside the post office
		Semaphore workAvail = new Semaphore(3, true);		//	controls number of NUMCUSTOMERS able to be helped at once
		Semaphore custRequest	= new Semaphore(0, true);		//	lets worker know customer is ready
		Semaphore setCustInfo = new Semaphore(1, true);		//	protects global variable access
		Semaphore getCustInfo = new Semaphore(0, true);		//	ensures customer requests assistance at appropriate time
		Semaphore handleReq	= new Semaphore(0, true);	//	forces customer to request service before worker can begin working
		Semaphore scalesAvail	= new Semaphore(1, true);		//	ensures only one worker can use the scales at a time
		Semaphore finished = new Semaphore(0 , true);		//	determines whether a worker is currently helping a customer

		System.out.println("Simulating Post Office with 50 customers and 3 postal workers");

		//thread initialization
		CustThr customers[] = new CustThr[NUMCUSTOMERS];
		Thread cThread[] = new Thread[NUMCUSTOMERS];

		WorkThr workers[] = new WorkThr[NUMWORKERS];
		Thread wThread[] = new Thread[NUMWORKERS];

		for(int i=0; i < NUMCUSTOMERS; i++) //initialize satisfiedCust with semaphores
			satisfiedCust.add(i, new Semaphore(0, true));

		for(int i = 0; i < NUMWORKERS; i++) { //create workers
			workers[i] = new WorkThr(i, satisfiedCust, workAvail, custRequest, finished, getCustInfo, handleReq, scalesAvail);
			wThread[i] = new Thread(workers[i]);
			wThread[i].start();
		} //end for

		for(int i = 0; i < NUMCUSTOMERS; i++) { //create customers
			customers[i] = new CustThr(i, satisfiedCust, maxRoom, workAvail, custRequest, finished, setCustInfo, getCustInfo, handleReq);
			cThread[i] = new Thread(customers[i]);
			cThread[i].start();
		} //end for

		// re-integrate customers into original process once they have finished
		for(int i = 0; i < NUMCUSTOMERS; i++) {
			try {
				cThread[i].join();
				System.out.println("Joined customer " + i);
			} //end try
			catch(InterruptedException e){}
		} //end for

		System.exit(0); //finished handling all 50 customers
	} //end main
} //end PostOffice

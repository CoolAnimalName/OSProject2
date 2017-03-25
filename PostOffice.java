/******************************************************************************
* Matthew Villarreal (miv140130)
* CS 4348.002
* Project 2
*******************************************************************************
*******************************************************************************
*                               PostOffice.java
*
* This program initializes all semaphores and threads necessary to coordinate
* the actions of the workers and customers in a post office. After
* initialization of a worker or customer thread, it is ran, and then join back
* to the original process. It also sets the size of the post office, the
* number of workers in the post office, and any global varaible needed in
* order to share specific data values with the worker and customer.
******************************************************************************/

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

		ArrayList<Semaphore> satisfiedCust 	= new ArrayList<Semaphore>();	//
		Semaphore maxRoom	= new Semaphore(10, true); //only 10 customers in the post office at a time
		Semaphore workAvail = new Semaphore(3, true);	//only 3 workers to help customers
		Semaphore setCustInfo = new Semaphore(1, true);	//customer sets global variables for worker
		Semaphore custRequest	= new Semaphore(0, true);	//customer finished setting global variables
		Semaphore getCustInfo = new Semaphore(0, true);	//worker gets global variables
		Semaphore handleReq	= new Semaphore(0, true);	//worker does proper wait time
		Semaphore scalesAvail	= new Semaphore(1, true);	//only one worker can use the scales
		Semaphore finished = new Semaphore(0 , true);	//worker finished task for customer

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

		//join customer threads back to the original process
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

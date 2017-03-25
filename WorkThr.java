/******************************************************************************
* Matthew Villarreal (miv140130)
* CS 4348.002
* Project 2
*******************************************************************************
*******************************************************************************
*																	WorkThr.java
*
*	This class defines the series of actions a worker thread will take to handle
* a task for the customer. It takes in the necessary semaphores from
* PostOffice.java, as well as places to hold the necessary data for the
* customer they are servicing. The workers will continue to run until the
* program is finished. While they run, a customer will come up to them if
* they are available. They will get the customer info, then the customer will
* tell them their request, then the worker will handle the request and employ
* the proper wait times necessary for the task. If they worker needs to use
* the scales, they will wait until it is not being used then hande the task.
* After they finish the task the customer will leave the post office and the
* worker can service another customer.
******************************************************************************/

import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.ArrayList;

public class WorkThr implements Runnable {
  private int workId;
	private int custId;
	private int task;
	private ArrayList<Semaphore> satisfiedCust;
	private Semaphore workAvail;
	private Semaphore custRequest;
	private Semaphore finished;
	private Semaphore getCustInfo;
	private Semaphore handleReq;
	private Semaphore scalesAvail;

	//constructor
	WorkThr(int workId, ArrayList<Semaphore> satisfiedCust, Semaphore workAvail, Semaphore custRequest, Semaphore finished, Semaphore getCustInfo, Semaphore handleReq, Semaphore scalesAvail) {
		this.satisfiedCust = satisfiedCust;
		this.workId	= workId;
		this.workAvail = workAvail;
		this.custRequest = custRequest;
		this.finished = finished;
		this.getCustInfo = getCustInfo;
		this.handleReq = handleReq;
		this.scalesAvail = scalesAvail;
	} //end WorkThr

	public void run() {
		System.out.println( "Postal worker " + workId + " created." );

		while(true)	{ //runs until program ends
			try	{
				custRequest.acquire(); //waits for customer to be done with global variables
			} //end try
			catch(InterruptedException e) { }

			//listentoCust()
			this.custId = PostOffice.custId;
			this.task = PostOffice.task;
			PostOffice.custWithWorker[this.custId] = this.workId;

			getCustInfo.release(); //worker is done reading global variables

			try {
				handleReq.acquire(); //waits for customer to finish giving the task
			} //end try
			catch(InterruptedException e) { }

			if(this.task == 2) { //customer wants to mail a package
				try {
					scalesAvail.acquire();
				} //end try
				catch(InterruptedException e) { }

				//useScales()
				System.out.println("Scales in use by postal worker " + workId);

				try {
					Thread.sleep(2000); //mailPackage()
				} //end try
				catch(InterruptedException e) { }

				System.out.println("Scales released by postal worker " + workId);
				scalesAvail.release();
			} //end if
			else if(this.task == 1) { //mail a letter
				try {
					Thread.sleep(1500); //mailLetter()
				} //end try
				catch(InterruptedException e) { }
			} //end else if
			else { //buy stamps
				try {
				Thread.sleep(1000); //buyStamps()
				} //end try
				catch(InterruptedException e) { }
			} //end else

			System.out.println("Postal worker " + workId + " finished serving customer " + this.custId);

			satisfiedCust.get(this.custId).release(); //worker finished customer's request

			try {
				finished.acquire();		//wait for customer to walk away from worker
			} //end try
			catch(InterruptedException e) { }

			workAvail.release();	//worker is ready to help another customer
		} //end while
	} //end run
} //end WorkThr

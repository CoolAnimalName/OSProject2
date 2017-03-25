/******************************************************************************
* Matthew Villarreal (miv140130)
* CS 4348.002
* Project 2
*******************************************************************************
*******************************************************************************
*                                   Worker.java
*
*
******************************************************************************/

import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.ArrayList;

public class WorkThr implements Runnable {
  private int workId;
	private int custId;
	private int task;
	private ArrayList<Semaphore> done;
	private Semaphore workAvail;
	private Semaphore custRequest;
	private Semaphore finished;
	private Semaphore getCustInfo;
	private Semaphore handleReq;
	private Semaphore scalesAvail;

	WorkThr(int workId, ArrayList<Semaphore> done, Semaphore workAvail, Semaphore custRequest, Semaphore finished, Semaphore getCustInfo, Semaphore handleReq, Semaphore scalesAvail) {
		this.done	= done;
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
				custRequest.acquire();
			} //end try
			catch(InterruptedException e) { }

			//gets global data from PostOffice about the customer they are servicing
			this.custId = PostOffice.custId;
			this.task = PostOffice.task;
			PostOffice.custWithWorker[this.custId] = this.workId;

			getCustInfo.release();

			try {
				handleReq.acquire();
			} //end try
			catch(InterruptedException e) { }

			if(this.task == 2) { //customer wants to mail a package
				try {
					scalesAvail.acquire();
				} //end try
				catch(InterruptedException e) { }

				System.out.println("Scales in use by postal worker " + workId);

				try {
					Thread.sleep(2000); //mailing package time
				}
				catch(InterruptedException e) { }

				System.out.println("Scales released by postal worker " + workId);
				scalesAvail.release();
			}
			else if(this.task == 1) { //mail a letter
				try {
					Thread.sleep(1500); //mailing letter time
				}
				catch(InterruptedException e) { }
			}
			else { //buy stamps
				try {
				Thread.sleep(1000); //buying stamps time
				} //end try
				catch(InterruptedException e) { }
			} //end else

			System.out.println("Postal worker " + workId + " finished serving customer " + this.custId);

			done.get(this.custId).release();

			try {
				finished.acquire();		//	waits for customer to exit
			} //end try
			catch(InterruptedException e) { }

			workAvail.release();	//	allows another customer to be helped
		} //end while
	} //end run
} //end WorkThr

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

public class CustThr implements Runnable {

  private int custId, task;
  private ArrayList<Semaphore> done;
  private Semaphore maxRoom, workAvail, custRequest, finished, setCustInfo, getCustInfo, handleReq;

  CustThr(int custId, ArrayList<Semaphore> done, Semaphore maxRoom, Semaphore workAvail, Semaphore custRequest, Semaphore finished, Semaphore setCustInfo, Semaphore getCustInfo, Semaphore handleReq) {
    this.done = done;
    this.custId = custId;
    this.maxRoom = maxRoom;
    this.workAvail = workAvail;
    this.custRequest 	= custRequest;
    this.finished 	= finished;
    this.setCustInfo = setCustInfo;
    this.getCustInfo = getCustInfo;
    this.handleReq 	= handleReq;

    Random randNum = new Random();
    this.task = randNum.nextInt(3);
  } //end CustThr

  public void run() {
    System.out.println("Customer " + custId + " created");
    try {
      maxRoom.acquire();	//	get inside the store
    } //end try
    catch(InterruptedException e) { }

    System.out.println("Customer " + custId + " enters post office");

    try {
      workAvail.acquire(); 	// 	become one of three customers being served
    } //end try
    catch(InterruptedException e) { }

    try {
      setCustInfo.acquire();	//	customer and postal worker tied to each other, only this thread can write to global variables
    } //end try
    catch(InterruptedException e) { }

    PostOffice.custId = this.custId;
    PostOffice.task = this.task;

    custRequest.release();	//	allows worker to begin reading data

    try {
      getCustInfo.acquire();			//	waits for worker to acquire data
    } //end try
    catch(InterruptedException e) { }

    switch(this.task) {
      case 0: //buy stamps
        System.out.println("Customer " + custId + " asks postal worker " + PostOffice.custWithWorker[custId] + " to buy stamps");
        break;

      case 1: //mail a letter
        System.out.println("Customer " + custId + " asks postal worker " + PostOffice.custWithWorker[custId] + " to mail a letter");
        break;

      case 2: //mail a package
        System.out.println("Customer " + custId + " asks postal worker " + PostOffice.custWithWorker[custId] + " to mail a package");
        break;

      default:
        break; //do nothing
    } //end switch

    handleReq.release();	//	allows worker to begin finished on task

    setCustInfo.release();		//	other threads can now write to global variables

    try {
      done.get(this.custId).acquire();	//	pauses until done being served
    } //end try
    catch(InterruptedException e) { }

    finished.release();		//	customer is satisfied and is leaving

    System.out.println( "Customer " + custId + " leaves post office" );

    maxRoom.release();		//	allows another customer to enter post office
  } //end run
} //end CustThr

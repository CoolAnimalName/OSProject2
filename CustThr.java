/******************************************************************************
* Matthew Villarreal (miv140130)
* CS 4348.002
* Project 2
*******************************************************************************
*******************************************************************************
*                                  CustThr.java
*
* This class defines the series of actions a customer will take when going to
* the post office. It takes in all the necessary semaphores from
* PostOffice.java, and has variables that identify the customer's ID number,
* and the randomly assigned task they need done. If the post office is full
* they must wait before they can enter, and then they must wait for an
* available post office worker to service them. Once there is a worker
* available, the customer will make their info available in the global
* variables in PostOffice.java, wait for the worker to receive this info, then
* they will tell the worker the task they want done. They will wait for the
* worker to finish, then walk out of the post office.
******************************************************************************/

import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.ArrayList;

public class CustThr implements Runnable {

  private int custId, task;
  private ArrayList<Semaphore> satisfiedCust;
  private Semaphore maxRoom, workAvail, custRequest, finished, setCustInfo, getCustInfo, handleReq;

  //constructor
  CustThr(int custId, ArrayList<Semaphore> satisfiedCust, Semaphore maxRoom, Semaphore workAvail, Semaphore custRequest, Semaphore finished, Semaphore setCustInfo, Semaphore getCustInfo, Semaphore handleReq) {
    this.satisfiedCust  = satisfiedCust;
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
      maxRoom.acquire();	//wait for room in post office
    } //end try
    catch(InterruptedException e) { }

    //enterPO()
    System.out.println("Customer " + custId + " enters post office");

    try {
      workAvail.acquire(); //wait for worker to be ready
    } //end try
    catch(InterruptedException e) { }

    try {
      setCustInfo.acquire();	//mutual exclustion on the global variables
    } //end try
    catch(InterruptedException e) { }

    //giveInfo()
    PostOffice.custId = this.custId;
    PostOffice.task = this.task;

    custRequest.release(); //finished with global variables

    try {
      getCustInfo.acquire(); //worker finished with global variables
    } //end try
    catch(InterruptedException e) { }

    //ask()
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

    handleReq.release();	//customer done explaining task

    setCustInfo.release();		//another customer can write to global variables

    try {
      satisfiedCust.get(this.custId).acquire();	//wait until worker is done
    } //end try
    catch(InterruptedException e) { }

    finished.release();		//customer is ready to leave

    System.out.println( "Customer " + custId + " leaves post office" );

    maxRoom.release();		//one more spot in post office available
  } //end run
} //end CustThr

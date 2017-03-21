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
  private int id;
  private int task;
  private int custId;

  private Semaphore workerCoord, taskToDo, scaleCoord;
  private Semaphore custReady, custFinished;
  private ArrayList<Semaphore> workFinished;

  WorkThr(int id, Semaphore workerCoord, Semaphore taskToDo, Semaphore scaleCoord, Semaphore custReady, Semaphore custFinished, ArrayList<Semaphore> workFinished) {
    this.id = id;
    this.workerCoord = workerCoord;
    this.scaleCoord = scaleCoord;
    this.custReady = custReady;
    this.workFinished = workFinished;
  } //end WorkSem(1)

  public void run() {

    System.out.println("Postal worker " + id + " created");
    while(true) {
      try {
        custReady.acquire();
      } //end try
      catch(InterruptedException e) { }

      try {
        workerCoord.acquire();
      }
      catch(InterruptedException e) { }

      System.out.println("Postal worker " + id + " serving customer ");// + CustSem.id);

      this.task = PostOffice.custTask;

      switch(task) {

        case 1:
          try {
            Thread.sleep(1000);
          }
          catch(InterruptedException e) { }
          break;

        case 2:
          try {
            Thread.sleep(1500);
          }
          catch(InterruptedException e) { }
          break;

        case 3:
          try {
          Thread.sleep(2000);
          }
          catch(InterruptedException e) { }
          break;

        default:
          break;
      } //end switch

      custFinished.release();

      System.out.println("Postal worker " + id + " finished serving customer ");// + CustSem.id);

      workFinished.get(this.custId).release();

      workerCoord.release();

    } //end while

  } //end run

} //end WorkSem

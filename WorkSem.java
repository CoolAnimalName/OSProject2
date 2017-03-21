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

import java.io.*;
import java.lang.Runtime;
import java.util.Scanner;
import java.util.Random;

public class WorkSem {

  private int id;
  private static Semaphore work = new Semaphore(0, true);

  WorkSem(int i) {
    this.id = i;
  } //end WorkSem(1)

  public void run() {

    System.out.println("Postal worker " + id + " created");
    while(true) {
      try {
        work.acquire(custReady);
        work.acquire(workerCoord);

        System.out.println("Postal worker " + id + " serving customer " + CustSem.id);

        work.release(custFinished);

        System.out.println("Postal worker " + id + " finished serving customer " + CustSem.id);

        work.release(workFinished);
        work.release(workerCoord);
      } //end try
      catch(InterruptedException e) { }

    } //end while

  } //end run


} //end WorkSem

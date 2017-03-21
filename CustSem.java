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

public class CustSem extends Runnable {

   int id;
   private int task;
   private static Semaphore cust = new Semaphore(0, true);

   public CustSem(int id, int task) {
      this.id = id;
      this.task = task;
   } //end Customer(1)

   public void run() {

      System.out.println("Customer " + id + " created");

      try {
         cust.acquire(maxRoom);

         System.out.println("Customer " + id + " enters post office");

         cust.release(custReady);
         cust.acquire(custFinished);

         switch(task) {

            case 1: //buy stamps
               break;

            case 2: //mail letter
               break;

            case 3: //mail package
               break;

            default:
               break;
         } //end switch

         cust.acquire(workFinished);
      } //end try
      catch(InterruptedException h) {
      } //end catch
   } //end run
} //end CustSem

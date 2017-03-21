import java.lang.Runtime;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class PostOffice extends Runnable {

  public static void main(String args[]) {

    final int NUMCUSTOMERS = 50;
    final int NUMWORKERS = 3;

    Semaphore maxRoom = 10;
    Semaphore workerCoord = 3;
    Semaphore custReady = 0, custFinished = 0, workFinished = 0;

    CustSem customers[] = new CustSem[NUMCUSTOMERS];
    Thread cThread[] = new Thread[NUMCUSTOMERS];

    WorkSem workers[] = new WorkSem[NUMWORKERS];
    Thread wThread[] = new Thread[NUMWORKERS];

    for(int i = 0; i < NUMWORKERS; ++i) { //create workers
      workers[i] = new WorkSem(i);
      wThread[i] = new Thread(workers[i]);
      wThread[i].start();
    } //end for

    for(int i = 0; i < NUMCUSTOMERS; ++i) { //create customers
      Random rand = new Random();
      int task = rand.nextInt(3) + 1; //randomly assigned task

      customers[i] = new CustSem(i, task);
      cThread[i] = new Thread(customers[i]);
      cThread[i].start();
    } //end for

  } //end main
} //end PostOffice

import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class PostOffice {
  public static int custId;
  public static int custTask;
  public static int customerWithWorker[] = new int[50];

  public static void main(String args[]) {

    final int NUMCUSTOMERS = 50;
    final int NUMWORKERS = 3;

    Semaphore maxRoom = new Semaphore(10, true);
    Semaphore workerCoord = new Semaphore(3, true);
    Semaphore scaleCoord = new Semaphore(1, true);
    Semaphore taskNeeded = new Semaphore(0, true);
    Semaphore taskToDo = new Semaphore(0, true);
    Semaphore withWorker = new Semaphore(0, true);
    Semaphore custReady = new Semaphore(0, true);
    Semaphore custFinished = new Semaphore(0, true);
    ArrayList<Semaphore> workFinished 	= new ArrayList<>();

    CustThr customers[] = new CustThr[NUMCUSTOMERS];
    Thread cThread[] = new Thread[NUMCUSTOMERS];

    WorkThr workers[] = new WorkThr[NUMWORKERS];
    Thread wThread[] = new Thread[NUMWORKERS];

    System.out.println("Simulating Post Ofice with 50 customers and 3 postal workers\n");

    for(int i = 0; i < NUMCUSTOMERS; ++i) {
      workFinished.add(i, new Semaphore(0, true));
    }

    for(int i = 0; i < NUMWORKERS; ++i) { //create workers
      workers[i] = new WorkThr(i, workerCoord, scaleCoord, taskToDo, custReady, custFinished, workFinished);
      wThread[i] = new Thread(workers[i]);
      wThread[i].start();
    } //end for

    for(int i = 0; i < NUMCUSTOMERS; ++i) { //create customers
      customers[i] = new CustThr(i, maxRoom, workerCoord, taskNeeded, taskToDo, custReady, custFinished, workFinished);
      cThread[i] = new Thread(customers[i]);
      cThread[i].start();
    } //end for

    for(int i = 0; i < NUMCUSTOMERS; ++i) {
      try {
        cThread[i].join();
      }
      catch(InterruptedException e) { }

      System.out.println("Joined customer " + i);
    }

  } //end main
} //end PostOffice

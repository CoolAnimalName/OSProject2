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

    private int id;
    private int task;
    private Semaphore maxRoom;
    private Semaphore workerCoord, taskToDo;
    private Semaphore custReady, custFinished;
    private ArrayList<Semaphore> workFinished;

    public CustThr(int id, Semaphore maxRoom, Semaphore workerCoord, Semaphore taskToDo, Semaphore custReady, Semaphore custFinished, ArrayList<Semaphore> workFinished) {
        this.id = id;
        this.maxRoom = maxRoom;
        this.workerCoord = workerCoord;
        this.taskToDo = taskToDo;
        this.custReady = custReady;
        this.custFinished = custFinished;
        this.workFinished = workFinished;

        Random rTask = new Random();
        this.task = rTask.nextInt(3) + 1; //task assignment
    } //end Customer(1)

    public void run() {

        System.out.println("Customer " + id + " created");

        try {
            maxRoom.acquire();
        }
        catch(InterruptedException e) { };


        System.out.println("Customer " + id + " enters post office");

        custReady.release();

        try {
            custFinished.acquire();
        }
        catch(InterruptedException e) { };

        switch(task) {

            case 1: //buy stamps
                System.out.println("Customer " + id + "asks postal worker " + " to buy stamps");
                break;

            case 2: //mail letter
                System.out.println("Customer " + id + "asks postal worker " + " to mail a letter");
                break;

            case 3: //mail package
                System.out.println("Customer " + id + "asks postal worker " + " to mail a package");
                break;

            default:
                break;
            } //end switch

        try {
            workFinished.get(this.id).acquire();
        } //end try
        catch(InterruptedException e) { } //end catch

        maxRoom.release();

        System.out.println("Customer " + id + " leaves the post office");

    } //end run
} //end CustSem

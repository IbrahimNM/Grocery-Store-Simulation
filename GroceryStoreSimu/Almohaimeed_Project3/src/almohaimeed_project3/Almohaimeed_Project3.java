/**
 * This is a grocery store simulation.
 * Most lines are commented and explained, there are two essential components
 * required in order to run the simulation properly, they are the two files:
 * 1- Arrival.txt which contains all the customers data formated as follows:
 * arrivaTime(double) space|tab itemSize(int) space|tap pickupItemAver(double)
 * 2- Checkout.txt which contains all checkout lines data formated as follows:
 * numberOfLines(int)
 * itemProcess(double)  space|tab  payment(double)
 * A log file will be provided at the end of the simulation.
 */
package almohaimeed_project3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * CS1181-C05 
 * instructor: Cheatham, Michelle 
 * TA: Shimizu, Cogan
 * @author Ibrahim Almohaimeed
 */
public class Almohaimeed_Project3 {
    public static void main(String[] args){
        // Instantiat & format the log File: 
        File logFile = new File("Log.txt");
        PrintWriter logWriter = null;
            try {
                logWriter = new PrintWriter(logFile);
                logWriter.format(Locale.US, "%-5s", "ID");
                logWriter.format(Locale.US, "%15s", "Arrival");
                logWriter.format(Locale.US, "%18s", "End shopping");
                logWriter.format(Locale.US, "%15s", "End waiting");
                logWriter.format(Locale.US, "%15s\n", "End checkout");      
            } catch (FileNotFoundException ex) {System.out.println("File wasn't found");}
        
        
        // Read custoemrs data & get their arrival events
        ArrayList<Customer> customersList = getCustomers();
        PriorityQueue<Events> events = getEvents(customersList);
        
        // Read and save checkout lanes data:
        ArrayList<CheckoutLine> regularLanes = getRegularCheckoutLanes();
        ArrayList<CheckoutLine> expressLanes = getExpressCheckoutLanes(regularLanes.size());
            
        // We have a list of all valid customers & their events & checkout lanes data
        
            // CALL THE SIMULATION DRIVER:
            driver(logFile, logWriter,events, expressLanes, regularLanes );
            
            // print brief info.
            logWriter.println("________________________________________________");
            logWriter.println("Number Of Customers: " + customersList.size());
            logWriter.println("Number Of Regular Lines: " + regularLanes.size());
            logWriter.println("Number Of Express Lines: " + expressLanes.size());
            
            
            // STATISTICS SECTION:
            getStatistics(regularLanes, expressLanes, logWriter);
             
            // Close 
            logWriter.close();
            
            System.out.println("The Simulation ended.");
    }
    /**
     * The Driver of the simulation.
     * This driver will drive the simulation proberly to achive the goals of the 
     * the program.
     * @param logFile
     * @param logWriter
     * @param events
     * @param expressLanes
     * @param regularLanes 
     */
    private static void driver(File logFile, PrintWriter logWriter, PriorityQueue<Events> events, ArrayList<CheckoutLine> expressLanes,
            ArrayList<CheckoutLine> regularLanes) {
        // Start Time 
            SimClock clock = new SimClock();
            CheckoutLine register;
            Events current;
            EndShoppingEvent endEvent;
            CheckOutEvent outEvent;
            Customer aCustomer;
        while( !events.isEmpty() ){
        // Get smallest arrival time if found
            current = events.poll();
        
            // Modifie time:
            clock.time(current.getTime());
            
            // USING THE TYPE TO DETERMINE THE NEXT STEP:
            switch (current.getType()) {
                case "ArrivalEvent":
                    // New EndShoppingEvent:
                    endEvent = getEndshoppingEvent(current.getCustomer(), current.getTime());
                    // Add to the event List
                    events.offer(endEvent);
                    break;
                case "EndShoppingEvent":
                    aCustomer = current.getCustomer();
                    // Check customer items size:
                    if (aCustomer.getOrderSize() < 12 && expressLanes.size() >= 1){
                        //if items are < 12 && there's an express line 
                        register = getSmallestLine(expressLanes);
                    } else {
                        // item are > 12 or items are < 12 but there's no express line.
                        register = getSmallestLine(regularLanes);
                    }
                    // Get new checkoutEvent:
                    outEvent = getCheckOutEvent(current.getCustomer(),register);
                    // add Customer to Line:
                    register.addCustomer(aCustomer);
                    // Add CheckoutEvent to evensList:
                    events.offer(outEvent);
                    break;
                default:
                    // CUSTOMER ENDS SHOPPING --
                    
                    // Remove Customer from Line:
                    ((CheckOutEvent)current).getLine().removeCustomer();
                    // Add customer data to the log file:    
                    logFile(current.getCustomer(), logFile, logWriter);
                    break;   
            }
        }
        
                logWriter.println("\t\tLast checkout time: "+ clock.time());
    }
    /**
     * This method will read all valid customers data from file.
     * Customers with invalid arrival time will not be added to the list
     * @return A list of all customers.
     */
    private static ArrayList<Customer> getCustomers()  {
        
        ArrayList<Customer> customersList = new ArrayList<>();
        Scanner in;
        try {
            in = new Scanner(new File("Arrival.txt"));
            int count = 0;
            while(in.hasNext()){
                // Check Arrival time in case Close!
                
                   try { 
                        double arrival = in.nextDouble();
                        int size = in.nextInt();
                        double ave = in.nextDouble();
                        if (arrival < 960){
                            Customer w1 = new Customer(arrival,size, ave, ++count);
                            customersList.add(w1);
                        }
                   } catch (InputMismatchException e){
                       System.out.println("Arrival file format is not valid!");
                   }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error! Arrival file was not found! make sure the name "
                    + "of file is 'Arrival.txt'");
        }
        
        
        return customersList;
    }
    /**
     * This method is assigned to create an new arrival event for a customer.
     * @param customer
     * @return Arrival event.
     */
    private static Events getArrivalEvents(Customer customer) {
        return new ArrivalEvent(customer.getTimeOfArrival(), customer);
    }
    /**
     * This method is assigned to create EndShoppingEvent for customers.
     * @param c
     * @return EndShpping event for a customer. 
     */
    private static EndShoppingEvent getEndshoppingEvent(Customer e , double ArrivalTime ) {
        // Add Shpping time to time of the arrival
        double timeOfShopping = (e.getOrderSize() * e.getAveragePerItem()) 
                + ArrivalTime;
        return new EndShoppingEvent(timeOfShopping, e);
    }
    /**
     * This method will create for all customers an arrival event.
     * @param The list of all customers.
     * @return A priorityQueue filled with arrival events for all the customers.
     */
    private static PriorityQueue<Events> getEvents(ArrayList<Customer> customersList) {
        PriorityQueue<Events> events = new PriorityQueue<>();
        for(Customer c: customersList){
                    events.offer(getArrivalEvents(c));
        }
        return events;
    }
    /**
     * This method is assigned to create a Checkout event for the given customer.
     * @param Customer c1
     * @param register: the line the customer is standing on.
     * @return A checkout event.
     */
    private static CheckOutEvent getCheckOutEvent(Customer customer, CheckoutLine register) {
        /* 
            The math: 
            (customerItemNumber * lineAveragePerItem) + PaymentTimeforLine
                       + EndShoppingTime + WaitingTimeOnLine
        */
        double CustomerCheckoutTime = 
                (customer.getOrderSize() * register.getProcessItemTime())
                + register.getProcessPaymentTime() + customer.getTimeOfEndShopping()
                + getTotalWaiting(register);
                
                // Adjust Checkout Time & waiting time for the customer;
                customer.setTimeofEndCheckingOut(CustomerCheckoutTime);
                customer.setTimeOfWaiting(getTotalWaiting(register));
                
        return  new CheckOutEvent(CustomerCheckoutTime, customer, register);
    }
    /**
     * This method will only calculate the waiting time for the customer, based
     * on which line and the number of customers are there in front of him.
     * @param register: The line.
     * @return The waiting time. 
     */
    private static double getTotalWaiting(CheckoutLine register) {
            
            // Count all items in line: 
            int numberOfAllItemsInLine = 0;
            
            for(Customer c: register.getCustomersInLine()){
                numberOfAllItemsInLine += c.getOrderSize();
            }
            /*
                The math:
                    (AllItemsInline * processPerItemInLine) + 
                    (numberOfCustomersInLine * paymentProcessTime)
            */
            return ((numberOfAllItemsInLine * register.getProcessItemTime()) 
                    + (register.getSize() * register.getProcessPaymentTime()));
    }
    /**
     * This method will read & create the regular checkout lanes.
     * @return A list of all regular lanes.
     */
    private static ArrayList<CheckoutLine> getRegularCheckoutLanes()  {
        ArrayList<CheckoutLine> regularLanesList = new ArrayList<>();
        Scanner in;   
        try {
            in = new Scanner(new File("Checkout.txt"));
            if(in.hasNextInt()){
                int numberOfLines = in.nextInt();
                for(int i = 0; i < numberOfLines; i++){
                    regularLanesList.add(new CheckoutLine(in.nextDouble(), 
                            in.nextDouble(), "Regular "+ (1+i)));
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error! Checkout file was not found!"
                    + " Make sure the file name is 'Checkout.txt'");
        }  
        return regularLanesList;
    }
    /**
     * This method will read & create the regular checkout lanes.
     * @param toSkip: number of regular lanes in file to skip.
     * @return A list oof all express lanes.
     */
    private static ArrayList<CheckoutLine> getExpressCheckoutLanes(int toSkip){
        ArrayList<CheckoutLine> expressLines = new ArrayList<>();
        Scanner in;
        try {
            in = new Scanner(new File("Checkout.txt"));
            // skip the regular lines.
            for(int i = 0; i < toSkip+1; i++){
                in.nextLine();
            }
            int num = 0;
            if(in.hasNextInt()){
                int numberOfExpress = in.nextInt();
                for(int j = 0 ; j < numberOfExpress; j++){
                    expressLines.add(new CheckoutLine(in.nextDouble(), 
                            in.nextDouble(), "Express " + (++num)));
                }
            }
            
        } catch (FileNotFoundException ex) {
            System.out.println("Error! Checkout file was not found!"
                    + " Make sure the file name is 'Checkout.txt'");
        }
        return expressLines;
    }
    /**
     * This method will return the smallest/shortest lane in the given list.
     * @param Lanes
     * @return shortest lane
     */
    private static CheckoutLine getSmallestLine(ArrayList<CheckoutLine> Lanes) {
        CheckoutLine Line = null;
        try{
            Line= Lanes.get(0);
            
            for(CheckoutLine r: Lanes){
                if (Line.getSize() > r.getSize()){
                Line = r;
                }
            }
        } catch( IndexOutOfBoundsException e){
            System.out.println("Error! No lines founded!");
        }
        return Line;
    }
    /**
     * This method is responsiable to add customers data into the log file.
     * @param customer
     * @param logFile: A file name "Log.txt".
     * @param pw: the printWriter.
     */
    private static void logFile(Customer customer, File logFile, PrintWriter writer) {
            /* 
              The data that will be written are: 
              CUSTOMERID + ARRIVAL + ENDSHOPPING + WAITING + ENDWAITING + ENDCHECKOUT
            */ 
            if(logFile.exists()){
                writer.format(Locale.US, "%-5d", customer.getCustomerID());
                writer.format(Locale.US, "%15f", customer.getTimeOfArrival());
                writer.format(Locale.US, "%18f", customer.getTimeOfEndShopping());
                writer.format(Locale.US, "%15f", customer.getTimeOfWaiting() + customer.getTimeOfEndShopping());
                writer.format(Locale.US, "%15f\n", customer.getTimeofEndCheckingOut());
            }
    }
    /**
     * This method will calculate the number of all customers who were served.
     * @param regularLanes: checkout lanes
     * @param expressLanes: checkout lanes
     * @return number of served customers.
     */
    private static int getNumberOfCustomersServed(ArrayList<CheckoutLine> regularLanes, ArrayList<CheckoutLine> expressLanes) {
        int noCustomers = 0;
        for(CheckoutLine r: regularLanes){
            noCustomers += r.getCountCustomer();
        }
        for(CheckoutLine e: expressLanes){
            noCustomers += e.getCountCustomer();
        }
        return noCustomers;
    }
    /**
     * This method is going to return the following checkout lanes information:
     * 1- Number of customers served 
     * 2- Maximum length of the line 
     * 3-Waiting average
     * @param aLane
     * @return A String. 
     */
    private static String getLineData(CheckoutLine aLane) {
        return aLane.getId() + "\n\tServed: " + aLane.getCountCustomer()
                + "\n\tMaximum length: " + aLane.getMaximumLength()
                + "\n\tWaiting average: " + 
                (double)(aLane.getWaitingInLine()/aLane.getCountCustomer());
    }
    /**
     * This method is the driver of the statistics part.
     * @param regularLanes: lanesList to be used.
     * @param expressLanes: lanesList to be used.
     */
    private static void getStatistics(ArrayList<CheckoutLine> regularLanes, ArrayList<CheckoutLine> expressLanes, PrintWriter logWriter) {
        // TOTAL NUMBER OF CUSTOEMRS SERVED
            int totalCustomers = getNumberOfCustomersServed(regularLanes, expressLanes);
            logWriter.println("Total Served: " + totalCustomers );
       // AVERAGE LINE LENGTH
            int lengths = 0;
            for(CheckoutLine r: regularLanes){
                lengths += r.getMaximumLength();
            }
            for(CheckoutLine ex: expressLanes){
                lengths += ex.getMaximumLength();
            }
            logWriter.printf("Average lines Length: %.0f\n", (double)lengths/(regularLanes.size()+expressLanes.size()));
        // Get lines data:
            for(CheckoutLine r: regularLanes){
                logWriter.println(getLineData(r));
            }
            for(CheckoutLine ex: expressLanes){
                logWriter.println(getLineData(ex));
            }
    }
}


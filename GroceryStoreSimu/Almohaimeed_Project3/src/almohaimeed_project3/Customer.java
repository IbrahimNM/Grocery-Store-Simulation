/**
 * Everything in this class is quite simple. 
 * You shouldn't find any problem reading the functions of each method below
 * because there are either a setter or a getter.
 */
package almohaimeed_project3;

public class Customer {

    final private double timeOfArrival, timeOfEndShopping,averagePerItem;
    final private int orderSize,customerID;
    private double timeofEndCheckingOut;
    private double timeOfWaitingOnLine;
    
    public Customer(double arrival, int size, double averageItem, int id) {
        this.timeOfArrival = arrival;
        this.orderSize = size;
        this.customerID = id;
        this.averagePerItem = averageItem;
        this.timeOfEndShopping = (size * averageItem) + arrival;
        this.timeofEndCheckingOut = 0.0;
        this.timeOfWaitingOnLine = 0.0;
    }

    public double getTimeOfWaiting() {
        return timeOfWaitingOnLine;
    }

    public void setTimeofEndCheckingOut(double timeofEndCheckingOut) { 
        this.timeofEndCheckingOut = timeofEndCheckingOut;
    }
    
    public void setTimeOfWaiting(double timeOfWaiting) {
        this.timeOfWaitingOnLine = timeOfWaiting;
    }

    // GETTER:
    public double getTimeOfArrival() {
        return timeOfArrival;
    }

    public double getTimeOfEndShopping() {
        return this.timeOfEndShopping;
    }

    public double getTimeofEndCheckingOut() {
        return timeofEndCheckingOut;
    }

    public int getOrderSize() {
        return orderSize;
    }

    public double getAveragePerItem() {
        return averagePerItem;
    }
    
    public int getCustomerID() {
        return customerID;
    }
    @Override
    public String toString() {
        return "{ Arrival: " + timeOfArrival + " Size: " + orderSize + " Ave.:" +
                averagePerItem + " ID: " + customerID +" End: "+this.timeOfEndShopping+" Out: "+ 
                this.timeofEndCheckingOut +'}';
    }
}

package almohaimeed_project3;

public class Events implements Comparable<Events> {

    private String type;
    private double time;
    private Customer customer;

    public Events() {
        this.time = 0;
        this.customer = null;
        this.type = getType();
    }

    public Events(double time, Customer customer) {
        this.time = time;
        this.customer = customer;
        this.type = getType();
    }

    public String getType() {
        return this.type;
    }

    public double getTime() {
        return time;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return '{' + getType() + ", Time: " + time + ", CustomerID: " + customer.getCustomerID() + '}';
    }

    @Override
    public int compareTo(Events o) {
        if (this.time > o.time){
            return 1;
        }if (this.time < o.time){
            return -1;
        }
        return 0;
    }

}

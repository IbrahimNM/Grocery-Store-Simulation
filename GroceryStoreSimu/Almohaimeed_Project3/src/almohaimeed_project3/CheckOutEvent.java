/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package almohaimeed_project3;

/**
 *
 * @author Ibrahim
 */
public class CheckOutEvent extends Events {

    final private String type;
    final private CheckoutLine Line;
    public CheckOutEvent( double time, Customer customer, CheckoutLine lane) {
        super(time, customer);
        this.type = "CheckOutEvent";
        this.Line = lane;
    }
    @Override
    public String getType(){
        return this.type;
    }
    public String getCheckoutLaneID() {
        return Line.getId();
    }

    public CheckoutLine getLine() {
        return Line;
    }
    @Override
    public String toString() {
        return '{' + getType() + ", Time: " + super.getTime() + 
                ", Line: " + this.Line.getId() +
                ", CustomerID: " + super.getCustomer().getCustomerID()+'}';
    }
}

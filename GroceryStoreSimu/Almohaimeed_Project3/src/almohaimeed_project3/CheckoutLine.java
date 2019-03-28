/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package almohaimeed_project3;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *
 * @author Ibrahim
 */
public class CheckoutLine {
    final private double processItemTime, processPaymentTime;
    private int countCustomer, maximumLength;
    final private String id;
    private double WaitingInLine;

    public double getWaitingInLine() {
        return WaitingInLine;
    }
    private Queue<Customer> customersInLine;
    
    public CheckoutLine(double timePerItem, double payment, String id){
        this.processItemTime = timePerItem;
        this.processPaymentTime = payment;
        this.id = id;
        this.customersInLine = new ArrayDeque<>();
        this.countCustomer = 0;
        this.maximumLength = 0;
    }

    public double getProcessItemTime() {
        return processItemTime;
    }

    public double getProcessPaymentTime() {
        return processPaymentTime;
    }

    public Queue<Customer> getCustomersInLine() {
        return customersInLine;
    }
    
    public void addCustomer(Customer c){
        this.customersInLine.offer(c);
        // modefie average waiting:
        WaitingInLine += c.getTimeOfWaiting();
        // get maximum:
        if (customersInLine.size() > maximumLength){
            maximumLength = customersInLine.size();
        }
    }

    public int getMaximumLength() {
        return maximumLength;
    }

    public int getCountCustomer() {
        return countCustomer;
    }

    public Customer removeCustomer(){
        ++countCustomer;
        return this.customersInLine.poll();
        
    }
    
    public int getSize(){
        return this.customersInLine.size();
    }
    public String getId() {
        return  id ;
    }
    @Override
    public String toString(){
        return id + ", "+ this.processItemTime +", "+ this.processPaymentTime +", "+this.customersInLine.size();
    }
}

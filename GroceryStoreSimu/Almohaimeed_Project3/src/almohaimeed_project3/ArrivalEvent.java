
package almohaimeed_project3;


public class ArrivalEvent extends Events {
    final private String type;
    
    public ArrivalEvent(double time, Customer customer) {
        super(time, customer);
        this.type = "ArrivalEvent";
    }

    public ArrivalEvent() {
        this.type = "ArrivalEvent";
    }

    @Override
    public String getType() {
        return type;
    }
    
}

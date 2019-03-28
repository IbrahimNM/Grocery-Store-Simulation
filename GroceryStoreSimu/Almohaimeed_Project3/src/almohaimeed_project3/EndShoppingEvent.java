package almohaimeed_project3;

public class EndShoppingEvent extends Events {

    final private String type;

    public EndShoppingEvent( double time, Customer customer) {
        super(time, customer);
        this.type = "EndShoppingEvent";
    }

    @Override
    public String getType() {
        return type;
    }
}

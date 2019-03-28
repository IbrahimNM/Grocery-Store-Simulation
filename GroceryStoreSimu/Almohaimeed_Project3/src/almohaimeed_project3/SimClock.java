package almohaimeed_project3;
/** 
     * Cogan Shimizu
     * CS-1181L-90
     * Kurtis Glendenning
     * Michael Ondrasek
     * 
     * SIMCLOCK:
     * provides a standardized method of keeping time. Provided to us in lab7
     */
public class SimClock {
    
    private static double now;
    
    /**
     * default constructor, initialises start time to 0
     */
    public SimClock()
    {
        now = 0.0;
    }
    /**
     * time method returns current time
     * @return
     */
    public double time() {
        return now;
    }
    /**
     * time method sets time = to parameter
     * @param tm
     */
    public void time(double tm) {
        now = tm;
    }
}

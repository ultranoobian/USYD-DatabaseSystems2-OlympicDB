package usyd.it.peerpark.data;

import java.util.Date;

public class BayBooking {
    int car_;
    int bay_;
    Date startTime_;
    Date endTime_;
    double cost_;

    public BayBooking(int car, int bay, Date startTime, Date endTime, double cost) {
            car_=car;
            bay_=bay;
            startTime_= startTime;
            endTime_=endTime;
            cost_ = cost;
    }

    public int getCar() {
            return car_;
    }

    public int getParkingBay() {
            return bay_;
    }

    public Date getStartTime() {
            return startTime_;
    }

    public Date getEndTime() {
            return endTime_;
    }
    
    public double getCost() {
            return cost_;
    }

}

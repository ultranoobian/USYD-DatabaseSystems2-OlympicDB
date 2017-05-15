package usyd.it.olympics.data;

import java.util.Date;

/**
 *
 * @author Bryn Jeffries
 */
public class BayBookingListLine {
    int bookingId_;
    String carName_;
    String bayLocation_;
    Date startTime_;
    Date endTime_;

    public BayBookingListLine(int bookingId, String carName, String bayLocation, Date startTime, Date endTime ) {
            carName_=carName;
            bayLocation_=bayLocation;
            startTime_= startTime;
            endTime_=endTime;
    }

    
    public int getBookingId() {
        return bookingId_;
    }
    
    public String getCarName() {
            return carName_;
    }

    public String getBayLocation() {
            return bayLocation_;
    }

    public Date getStartTime() {
            return startTime_;
    }

    public Date getEndTime() {
            return endTime_;
    }

}

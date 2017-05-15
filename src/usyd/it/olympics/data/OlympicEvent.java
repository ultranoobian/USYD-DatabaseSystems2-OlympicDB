package usyd.it.olympics.data;

import java.util.Date;

/**
 * @author INFO2120
 */
public class OlympicEvent {
    String name_;
    Date starts_;
    String resultType_;
    String sportName_;
    String venueName_;

    public OlympicEvent(String name, Date starts, String resultType, String sportName, String venue) {
        name_ = name;
        starts_ = starts;
        resultType_ = resultType;
        sportName_ = sportName;
        venueName_ = venue;
    }

    public String getName(){
        return name_;
    }

    public Date getStart(){
        return starts_;
    }

    public String getResultType(){
        return resultType_;
    }

    public String getSportName(){
        return sportName_;
    }

    public String getVenueName(){
        return venueName_;
    }
}

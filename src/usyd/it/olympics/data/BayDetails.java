package usyd.it.olympics.data;

/**
 * Details of an individual bay
 * @author Bryn
 */
public class BayDetails extends BayListLineDetails {
    public static final String BAYID = "bay_id";
	public static final String SITE = "site";
	public static final String STREET = "street";
	public static final String CITY = "city";
	public static final String HOUSENUM = "house_num";
	public static final String GPSLONG = "gps_long";
	public static final String GPSLAT = "gps_lat";
	private final double gps_long_;
    private final double gps_lat_;
//    private final int[] todayHours_;
    
    public BayDetails(int bayId, String site, String houseNum, String address, String city,
            double gps_long, double gps_lat
            //int[] todayHours
        ) {
        super(bayId, site, houseNum, address, city);
        gps_long_ = gps_long;
        gps_lat_ = gps_lat;
//        todayHours_ = todayHours;
    }

    /**
     * @return the gps_long_
     */
    public double getGpsLong() {
        return gps_long_;
    }

    /**
     * @return the gps_lat_
     */
    public double getGpsLat() {
        return gps_lat_;
    }

//    /**
//     * @return the todayHours_
//     */
//    public int[] getTodayHours() {
//        return todayHours_;
//    }

}

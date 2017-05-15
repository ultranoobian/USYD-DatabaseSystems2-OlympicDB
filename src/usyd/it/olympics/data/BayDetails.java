package usyd.it.olympics.data;

/**
 * Details of an individual bay
 * @author Bryn
 */
public class BayDetails extends BayListLineDetails {
    private final double gps_long_;
    private final double gps_lat_;
    private final double width_, height_, length_;
    private final int wk_avail_start_, wk_avail_end_, we_avail_start_, we_avail_end_;
    private final int[] todayHours_;
    
    public BayDetails(int bayId, String site, String houseNum, String address, String city,
            double gps_long, double gps_lat,
            double width, double height, double length,
            int wk_avail_start, int wk_avail_end, int we_avail_start, int we_avail_end,
            int[] todayHours
        ) {
        super(bayId, site, houseNum, address, city);
        gps_long_ = gps_long;
        gps_lat_ = gps_lat;
        width_ = width;
        height_ = height;
        length_ = length;
        wk_avail_start_ = wk_avail_start;
        wk_avail_end_ = wk_avail_end;
        we_avail_start_ = we_avail_start;
        we_avail_end_ = we_avail_end;
        todayHours_ = todayHours;
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

    /**
     * @return the width_
     */
    public double getWidth() {
        return width_;
    }

    /**
     * @return the height_
     */
    public double getHeight() {
        return height_;
    }

    /**
     * @return the length_
     */
    public double getLength() {
        return length_;
    }

    /**
     * @return the wk_avail_start_
     */
    public int getWkAvailStart() {
        return wk_avail_start_;
    }

    /**
     * @return the wk_avail_end_
     */
    public int getWkAvailEnd() {
        return wk_avail_end_;
    }

    /**
     * @return the we_avail_start_
     */
    public int getWeAvailStart() {
        return we_avail_start_;
    }

    /**
     * @return the we_avail_end_
     */
    public int getWeAvailEnd() {
        return we_avail_end_;
    }

    /**
     * @return the todayHours_
     */
    public int[] getTodayHours() {
        return todayHours_;
    }

}

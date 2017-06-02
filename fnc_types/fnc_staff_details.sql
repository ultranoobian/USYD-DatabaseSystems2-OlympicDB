--DROP FUNCTION staff_details(character varying);

CREATE OR REPLACE FUNCTION staff_details(IN search_id character varying)
  RETURNS TABLE(bookings_made_count bigint) AS
$$
DECLARE

BEGIN


RETURN QUERY SELECT COUNT(*) FROM booking WHERE booking.booked_by = search_id;


END ; $$

LANGUAGE plpgsql;
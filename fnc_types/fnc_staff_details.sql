-- Function: staff_details(character varying)

DROP FUNCTION IF EXISTS staff_details(character varying);

CREATE OR REPLACE FUNCTION staff_details(search_id character varying)
  RETURNS TABLE(bookings_made_count bigint) AS
$BODY$
DECLARE

BEGIN


RETURN QUERY SELECT COUNT(*) FROM booking WHERE booking.booked_by = search_id;


END ; $BODY$

LANGUAGE plpgsql;

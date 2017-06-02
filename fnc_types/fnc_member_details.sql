--DROP FUNCTION member_details(character varying);

CREATE OR REPLACE FUNCTION member_details(search_id varchar(10)) 
RETURNS TABLE(
	member_id varchar(10)
	,type varchar
	,title varchar(4)
	,family_name varchar(30)
	,given_names varchar(30)
	,country_name varchar(40)
	,accom varchar(80)
	,bookings_count bigint
 ) AS 
$BODY$
BEGIN

RETURN QUERY SELECT 
	search_id
	,member_type(search_id)
	,m.title
	,m.family_name
	,m.given_names
	,c.country_name
	,pl.place_name
	,(SELECT count(*) FROM booking WHERE booked_for = search_id)
	FROM
	member m JOIN country c ON (m.country_code = c.country_code)
	JOIN place pl ON (m.accommodation = pl.place_id)
	WHERE m.member_id = search_id;

END ; $BODY$

LANGUAGE plpgsql;
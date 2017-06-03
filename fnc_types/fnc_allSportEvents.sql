--DROP FUNCTION member_details(character varying);

CREATE OR REPLACE FUNCTION allSportEvents(search_id integer) 
RETURNS TABLE(event_id integer
	,sport_id integer
	,event_name varchar(50)
	,event_gender character(1)
	,sport_venue varchar(80)
	,event_start timestamp without time zone
 ) AS 
$BODY$
BEGIN

RETURN QUERY SELECT e.event_id
,e.sport_id
,e.event_name
,e.event_gender
,place.place_name
,e.event_start
FROM event e JOIN place ON e.sport_venue = place.place_id
WHERE e.sport_id = search_id;


END ; $BODY$

LANGUAGE plpgsql;
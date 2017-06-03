-- Function: findjourneys(character varying, character varying, date)

-- DROP FUNCTION findjourneys(character varying, character varying, date);

CREATE OR REPLACE FUNCTION findJourneys(fromPlaceArg varchar, toPlaceArg varchar, dateArg date)
RETURNS TABLE( journey_id integer
	,fromPlace varchar
	,toPlace varchar
	,depart_time timestamp without time zone
	,arrive_time timestamp without time zone
	,vehicle_code char(8)
	,remaining_seats integer
 ) AS
 $BODY$
 BEGIN
 --Find journeys, then get their
 RETURN QUERY SELECT j.journey_id, fromPlace.place_name, toPlace.Place_name, j.depart_time, j.arrive_time, j.vehicle_code, (vehicle.capacity -  j.nbooked) as remaining
 FROM journey j JOIN place fromPlace on j.from_place = fromPlace.place_id JOIN place toPlace on j.to_place = toPlace.place_id
 JOIN vehicle ON vehicle.vehicle_code = j.vehicle_code
 WHERE fromPlace.place_name = fromPlaceArg AND toPlace.place_name = toPlaceArg AND j.depart_time::date = dateArg;


 END ; $BODY$

LANGUAGE plpgsql;

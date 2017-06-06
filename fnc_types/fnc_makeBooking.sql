CREATE OR REPLACE FUNCTION makeBooking(byStaff_Arg varchar, forMember_Arg varchar, vehicle_code_Arg varchar, departureTime_Arg timestamp without time zone)
  RETURNS TABLE(
	journey_id integer
	,vehicle char(8)
	,depart_time timestamp without time zone
	,arrive_time timestamp without time zone
	,toPlace varchar
	,fromPlace varchar
	,booked_by_name varchar
	,booked_for_name varchar
	,booked_made_time timestamp without time zone
  ) AS
$BODY$
DECLARE
_journey record;
_staffID varchar;
_staffName varchar;
_memberID varchar;
_memberName varchar;

BEGIN

--DEFINITION
SELECT j.journey_id as journey_id, v.capacity as capacity, j.nbooked as nbooked INTO _journey FROM Journey j JOIN Vehicle v ON j.vehicle_code = v.vehicle_code WHERE j.depart_time = departureTime_Arg AND v.vehicle_code = vehicle_code_Arg;
	
--_staffID := (SELECT member_id FROM Member WHERE byStaff_Arg = (Member.given_names || ' ' || Member.family_name));
_memberID := forMember_Arg;
_memberName := (SELECT  family_name || ', ' || given_names  FROM Member WHERE forMember_Arg = Member.member_id);
_staffID := byStaff_Arg;
_staffName := (SELECT family_name || ', ' || given_names FROM Member WHERE byStaff_Arg = Member.member_id);

-- Verify Journey exists
IF (_journey.journey_id IS NULL OR _journey.capacity IS NULL OR _journey.nbooked IS NULL) THEN
	RAISE EXCEPTION 'JOURNEY NULL';
END IF;

-- Verify Staff status - Returns if not staff member
IF (member_type(_staffID) != 'staff') THEN 
	RAISE EXCEPTION 'A call to makeBooking was made with a non-staff member id'
	USING HINT = 'Use a staff member id';
	--RETURN QUERY (SELECT null,null,null,null,null,null);
END IF;

/* Check if journey is already booked
	Then returns previous booking
*/
RAISE NOTICE 'booked_for = %, jID = %', _memberID, _journey.journey_id;

IF ((SELECT COUNT(*) FROM Booking b WHERE b.booked_for = _memberID AND b.journey_id = _journey.journey_id) > 0) THEN
	RETURN QUERY (SELECT j.journey_id
			,j.vehicle_code
			,j.depart_time
			,j.arrive_time
			,p2.place_name
			,p1.place_name
			, _staffName
			, _memberName
			, b.when_booked
		FROM Booking b JOIN Journey j ON b.journey_id = j.journey_id
		JOIN Place p1 ON j.from_Place = p1.place_id
		JOIN Place p2 ON j.to_Place = p2.place_id
		JOIN Member m ON b.booked_by = m.member_id
		WHERE j.journey_id = _journey.journey_id
		AND b.booked_by = _staffID
		AND b.booked_for = _memberID);
	RAISE EXCEPTION 'Booking already exists for Member % (%)and Journey %', forMember_Arg, _memberID, _journey.journey_id;
END IF;


--CRITICAL SECTION--
/*If the journey is not full, 
	Then the booking is made and Journeys updated
	Then Return the resultant booking
Else returns empty table.
*/
IF((_journey.capacity - _journey.nbooked) > 0) THEN
	--INSERT 
	INSERT INTO Booking VALUES(_memberID, _staffID, now()::timestamp without time zone, _journey.journey_id);
	--UPDATE
	UPDATE Journey SET nbooked = nbooked + 1
	WHERE Journey.journey_id = _journey.journey_id;
	--RETURN 
	RAISE EXCEPTION 'Success';
	RETURN QUERY (SELECT j.journey_id
			,j.vehicle_code
			,j.depart_time
			,j.arrive_time
			,p2.place_name
			,p1.place_name
			, _staffName
			, _memberName
			, b.when_booked
		FROM Booking b JOIN Journey j ON b.journey_id = j.journey_id
		JOIN Place p1 ON j.from_Place = p1.place_id
		JOIN Place p2 ON j.to_Place = p2.place_id
		JOIN Member m ON b.booked_by = m.member_id
		WHERE j.journey_id = _journey.journey_id
		AND b.booked_by = _staffID
		AND b.booked_for = _memberID
		);
	
ELSE
	RAISE EXCEPTION 'Journey Full';
	--RETURN QUERY (SELECT null,null,null,null,null,null);
END IF;

END; $BODY$

LANGUAGE plpgsql;

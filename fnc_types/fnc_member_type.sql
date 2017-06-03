-- Function: member_type(character varying)

DROP FUNCTION IF EXISTS member_type(character varying);

CREATE OR REPLACE FUNCTION member_type(search_id varchar(10))
RETURNS VARCHAR AS
$BODY$
DECLARE
type VARCHAR := 'NULL';
BEGIN
-- Important determinant first
IF EXISTS(SELECT * FROM Athlete a WHERE a.member_id = search_id) THEN
	type:= 'athlete';
ELSIF EXISTS(SELECT * FROM Staff s WHERE s.member_id = search_id) THEN
	type:= 'staff';
ELSIF EXISTS(SELECT * FROM Official o WHERE o.member_id = search_id) THEN
	type:= 'official';
ELSE
	type:= 'NotFound';
END IF;

RETURN type;

END ; $BODY$

LANGUAGE plpgsql;

-- Function: official_details(character varying)

DROP FUNCTION IF EXISTS official_details(character varying);

DROP FUNCTION official_details(character varying);

CREATE OR REPLACE FUNCTION official_details(IN search_id character varying)
  RETURNS TABLE(running_events_count bigint, running_events text[]) AS
$BODY$
DECLARE

BEGIN


RETURN QUERY SELECT count(*),
(SELECT array_agg(event_name::text) FROM runsevent JOIN event ON (runsevent.event_id = event.event_id)
 WHERE runsevent.member_id = search_id)
FROM runsevent WHERE runsevent.member_id = search_id;


END ; $BODY$

LANGUAGE plpgsql;

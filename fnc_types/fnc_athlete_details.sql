DROP FUNCTION athlete_details(character varying);

CREATE OR REPLACE FUNCTION athlete_details(IN search_id character varying)
  RETURNS TABLE(g bigint, s bigint, b bigint, teams text[]) AS
$$
DECLARE

BEGIN


RETURN QUERY SELECT 
	(SELECT COUNT(*) FROM participates WHERE participates.athlete_id = search_id AND participates.medal = 'G')
	,(SELECT COUNT(*) FROM participates WHERE participates.athlete_id = search_id AND participates.medal = 'S')
	,(SELECT COUNT(*) FROM participates WHERE participates.athlete_id = search_id AND participates.medal = 'B')
	,(SELECT array_agg(team_name::text) FROM teammember WHERE teammember.athlete_id = search_id)
	;

END ; $$

LANGUAGE plpgsql;
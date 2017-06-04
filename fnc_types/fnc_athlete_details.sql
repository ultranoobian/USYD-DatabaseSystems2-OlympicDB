DROP FUNCTION IF EXISTS athlete_details(character varying);

CREATE OR REPLACE FUNCTION athlete_details(IN search_id character varying)
  RETURNS TABLE(g integer, s integer, b integer, teams text[]) AS
$BODY$
DECLARE
teamList text[];
goldCount integer := 0;
silverCount integer := 0;
bronzeCount integer := 0;
item varchar := '';

BEGIN
goldCount :=  goldCount + (SELECT COUNT(*) FROM participates WHERE participates.athlete_id = search_id AND participates.medal = 'G');
silverCount :=  silverCount + (SELECT COUNT(*) FROM participates WHERE participates.athlete_id = search_id AND participates.medal = 'S');
bronzeCount := bronzeCount + (SELECT COUNT(*) FROM participates WHERE participates.athlete_id = search_id AND participates.medal = 'B');

teamList := (SELECT array_agg(team_name::text) FROM teammember WHERE teammember.athlete_id = search_id);
IF teamList != '{}' THEN
FOREACH item IN ARRAY teamList
LOOP
	goldCount :=  goldCount + (SELECT COUNT(*) FROM team tm WHERE (tm.team_name = item) AND tm.medal = 'G');
	silverCount :=  silverCount + (SELECT COUNT(*) FROM team tm WHERE (tm.team_name = item) AND tm.medal = 'S');
	bronzeCount := bronzeCount + (SELECT COUNT(*) FROM team tm WHERE (tm.team_name = item) AND tm.medal = 'B');
END LOOP;	
END IF;
RETURN QUERY SELECT goldCount, silverCount, bronzeCount, teamList;
END ; $BODY$

LANGUAGE plpgsql;

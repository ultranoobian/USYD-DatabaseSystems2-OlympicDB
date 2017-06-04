IF EXIST output (RMDIR /S /Q output)
ECHO 'Creating File Structure'

MKDIR output >nul
MKDIR output\src
MKDIR output\sql

ROBOCOPY src output\src /MIR >nul
TYPE fnc_types\*.sql > output\sql\ddl_dcl.sql
ROBOCOPY .\ output\ olympicsdb.properties

"C:\Program Files\7-Zip\7z.exe" a -tzip output.zip "./output/*" 
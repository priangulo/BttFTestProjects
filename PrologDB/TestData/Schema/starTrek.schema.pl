dbase(starTrek,[crewman, commander, lieutenant]).

table(crewman,[cid,fname,lname]).
table(commander,[rank]).
table(lieutenant,[specialty]).

subtable(crewman,[commander,lieutenant]).

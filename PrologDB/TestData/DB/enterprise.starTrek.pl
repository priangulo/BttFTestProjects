dbase(starTrek,[crewman, commander, lieutenant]).

table(crewman,[cid,fname,lname]).
crewman(c1,mr,spock).

table(commander,[cid,fname,lname,rank]).
commander(c2,james,kirk,captain).

table(lieutenant,[cid,fname,lname,specialty]).
lieutenant(c3,hikaru,sulu,navigation).

subtable(crewman,[commander,lieutenant]).

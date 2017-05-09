dbase(hierarchy,[one,two,three]).

table(one,[a,"b",c]).
table(two,[d,"e",f,x]).
table(three,[g,h,i,z,"r"]).

subtable(one,[two]).
subtable(two,[three]).

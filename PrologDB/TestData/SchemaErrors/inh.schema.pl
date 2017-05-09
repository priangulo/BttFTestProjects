dbase(inh,[one,two,three]).

table(one,[a,"b",c]).
table(two,[a,"b",c,"d","e",f]).
table(three,[a,"b",c,g,h,i]).

subtable(one,[two,four]).
#reference to table four which doesn't exist

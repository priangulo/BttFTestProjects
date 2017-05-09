:-style_check(-discontiguous).

dbase(inh,[one,two,three]).

table(one,[a,"b",c]).
:- dynamic one/3.

table(two,[a,"b",c,"d","e",f]).
:- dynamic two/6.

table(three,[a,"b",c,g,h,i]).
:- dynamic three/6.

subtable(one,[two,three]).

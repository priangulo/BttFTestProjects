:-style_check(-discontiguous).

dbase(inh,[one,two,three]).

table(one,[a,"b",c]).
one(1,'b',3).
one(2,'bbb',4).
one(3,'bbbb',5).

table(two,[a,"b",c,"d","e",f]).
:- dynamic two/6.

table(three,[a,"b",c,g,h,i]).
:- dynamic three/6.

subtable(one,[two,three]).

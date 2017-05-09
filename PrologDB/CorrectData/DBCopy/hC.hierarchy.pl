:-style_check(-discontiguous).

dbase(hierarchy,[one,two,three]).

table(one,[a,"b",c]).
one(1,'b',c1).
one(2,'cc',b2).
one(3,'eee',x).

table(two,[a,"b",c,"e",f,x]).
two(1,'b',c1,'e',1,x).
two(2,'cc',b2,'ee',2,x).

table(three,[a,"b",c,d,"e",f,x,g,h,i,z,"r"]).
:- dynamic three/12.

subtable(one,[two]).
subtable(two,[three]).

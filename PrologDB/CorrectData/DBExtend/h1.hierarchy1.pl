:-style_check(-discontiguous).

dbase(hierarchy1,[one,two,three]).

table(one,[a,"b",c,length]).
one(1,'b',c1,1).
one(2,'cc',b2,2).
one(3,'eee',x,3).

table(two,[a,"b",c,"e",f,x,length]).
two(1,'b',c1,'e',1,x,1).
two(2,'cc',b2,'ee',2,x,2).

table(three,[a,"b",c,d,"e",f,x,g,h,i,z,"r",length]).
:- dynamic three/13.

subtable(one,[two]).
subtable(two,[three]).

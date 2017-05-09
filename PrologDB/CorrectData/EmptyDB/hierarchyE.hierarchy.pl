:-style_check(-discontiguous).

dbase(hierarchy,[one,two,three]).

table(one,[a,"b",c]).
:- dynamic one/3.

table(two,[a,"b",c,d,"e",f,x]).
:- dynamic two/7.

table(three,[a,"b",c,d,"e",f,x,g,h,i,z,"r"]).
:- dynamic three/12.

subtable(one,[two]).
subtable(two,[three]).

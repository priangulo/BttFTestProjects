:-style_check(-discontiguous).

dbase(violetdb1,[violetInterface,violetClass,violetAssociation,violetMiddleLabels]).

table(violetInterface,[id,"name","methods",x,y,length]).
violetInterface(unconnected3,'int0','',625,115,4).
violetInterface(unconnected4,'int1','m()',653,294,4).
violetInterface(unconnected5,'int2','m();n()',640,472,4).
violetInterface(unconnected7,'int3','m();n();z()',638,623,4).

table(violetClass,[id,"name","fields","methods",x,y,length]).
violetClass(unconnected0,'class0','','',210,111,6).
violetClass(unconnected1,'class1','a','m()',215,304,6).
violetClass(unconnected2,'class2','a;b','m1(a);m2(b)',230,455,6).
violetClass(unconnected6,'class3','a;"b";c','m1(a);m2(b);m(3)',221,647,6).

table(violetAssociation,[id,"role1","arrow1",type1,"role2","arrow2",type2,"lineStyle",cid1,cid2,length]).
:- dynamic violetAssociation/11.

table(violetMiddleLabels,[id,cid1,cid2,"label",length]).
:- dynamic violetMiddleLabels/5.


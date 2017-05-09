:-style_check(-discontiguous).

dbase(violetdb,[violetInterface,violetClass,violetAssociation,violetMiddleLabels]).

table(violetInterface,[id,"name","methods",x,y]).
:- dynamic violetInterface/5.

table(violetClass,[id,"name","fields","methods",x,y]).
:- dynamic violetClass/6.

table(violetAssociation,[id,"role1","arrow1",type1,"role2","arrow2",type2,"lineStyle",cid1,cid2]).
:- dynamic violetAssociation/10.

table(violetMiddleLabels,[id,cid1,cid2,"label"]).
:- dynamic violetMiddleLabels/4.


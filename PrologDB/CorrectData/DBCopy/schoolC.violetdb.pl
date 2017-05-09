:-style_check(-discontiguous).

dbase(violetdb,[violetInterface,violetClass,violetAssociation,violetMiddleLabels]).

table(violetInterface,[id,"name","methods",x,y]).
:- dynamic violetInterface/5.

table(violetClass,[id,"name","fields","methods",x,y]).
violetClass(classnode0,'person','id;"name"','',157,85).
violetClass(classnode1,'professor','deptid','',28,254).
violetClass(classnode2,'student','utid','',263,251).
violetClass(classnode3,'department','id;"name";"building"','',416,88).

table(violetAssociation,[id,"role1","arrow1",type1,"role2","arrow2",type2,"lineStyle",cid1,cid2]).
violetAssociation(id0,'','',classnode,'','TRIANGLE',classnode,'',classnode1,classnode0).
violetAssociation(id1,'','',classnode,'','TRIANGLE',classnode,'',classnode2,classnode0).

table(violetMiddleLabels,[id,cid1,cid2,"label"]).
:- dynamic violetMiddleLabels/4.


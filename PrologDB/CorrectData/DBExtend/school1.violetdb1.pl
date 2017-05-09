:-style_check(-discontiguous).

dbase(violetdb1,[violetInterface,violetClass,violetAssociation,violetMiddleLabels]).

table(violetInterface,[id,"name","methods",x,y,length]).
:- dynamic violetInterface/6.

table(violetClass,[id,"name","fields","methods",x,y,length]).
violetClass(classnode0,'person','id;"name"','',157,85,6).
violetClass(classnode1,'professor','deptid','',28,254,9).
violetClass(classnode2,'student','utid','',263,251,7).
violetClass(classnode3,'department','id;"name";"building"','',416,88,10).

table(violetAssociation,[id,"role1","arrow1",type1,"role2","arrow2",type2,"lineStyle",cid1,cid2,length]).
violetAssociation(id0,'','',classnode,'','TRIANGLE',classnode,'',classnode1,classnode0,0).
violetAssociation(id1,'','',classnode,'','TRIANGLE',classnode,'',classnode2,classnode0,0).

table(violetMiddleLabels,[id,cid1,cid2,"label",length]).
:- dynamic violetMiddleLabels/5.


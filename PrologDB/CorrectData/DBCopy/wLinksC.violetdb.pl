:-style_check(-discontiguous).

dbase(violetdb,[violetInterface,violetClass,violetAssociation,violetMiddleLabels]).

table(violetInterface,[id,"name","methods",x,y]).
violetInterface(interfacenode0,'aint','',665,483).
violetInterface(interfacenode1,'bint','',229,473).

table(violetClass,[id,"name","fields","methods",x,y]).
violetClass(classnode0,'aclass','','',209,131).
violetClass(classnode1,'bclass','','',666,130).
violetClass(classnode2,'cclass','','',209,292).
violetClass(classnode3,'dclass','','',670,308).

table(violetAssociation,[id,"role1","arrow1",type1,"role2","arrow2",type2,"lineStyle",cid1,cid2]).
violetAssociation(id0,'b;c','',classnode,'a','V',classnode,'',classnode0,classnode1).
violetAssociation(id1,'','',classnode,'','TRIANGLE',classnode,'',classnode3,classnode1).
violetAssociation(id2,'','',interfacenode,'','TRIANGLE',interfacenode,'',interfacenode0,interfacenode1).
violetAssociation(id3,'','',classnode,'','TRIANGLE',interfacenode,'DOTTED',classnode3,interfacenode0).
violetAssociation(id4,'x','DIAMOND',classnode,'y','',classnode,'',classnode2,classnode3).
violetAssociation(id5,'','BLACK_DIAMOND',classnode,'z','',classnode,'',classnode0,classnode2).

table(violetMiddleLabels,[id,cid1,cid2,"label"]).
:- dynamic violetMiddleLabels/4.


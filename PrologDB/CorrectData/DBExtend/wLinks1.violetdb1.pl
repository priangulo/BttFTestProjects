:-style_check(-discontiguous).

dbase(violetdb1,[violetInterface,violetClass,violetAssociation,violetMiddleLabels]).

table(violetInterface,[id,"name","methods",x,y,length]).
violetInterface(interfacenode0,'aint','',665,483,4).
violetInterface(interfacenode1,'bint','',229,473,4).

table(violetClass,[id,"name","fields","methods",x,y,length]).
violetClass(classnode0,'aclass','','',209,131,6).
violetClass(classnode1,'bclass','','',666,130,6).
violetClass(classnode2,'cclass','','',209,292,6).
violetClass(classnode3,'dclass','','',670,308,6).

table(violetAssociation,[id,"role1","arrow1",type1,"role2","arrow2",type2,"lineStyle",cid1,cid2,length]).
violetAssociation(id0,'b;c','',classnode,'a','V',classnode,'',classnode0,classnode1,3).
violetAssociation(id1,'','',classnode,'','TRIANGLE',classnode,'',classnode3,classnode1,0).
violetAssociation(id2,'','',interfacenode,'','TRIANGLE',interfacenode,'',interfacenode0,interfacenode1,0).
violetAssociation(id3,'','',classnode,'','TRIANGLE',interfacenode,'DOTTED',classnode3,interfacenode0,0).
violetAssociation(id4,'x','DIAMOND',classnode,'y','',classnode,'',classnode2,classnode3,1).
violetAssociation(id5,'','BLACK_DIAMOND',classnode,'z','',classnode,'',classnode0,classnode2,0).

table(violetMiddleLabels,[id,cid1,cid2,"label",length]).
:- dynamic violetMiddleLabels/5.


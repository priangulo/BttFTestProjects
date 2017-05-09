:-style_check(-discontiguous).

dbase(violetdb,[violetInterface,violetClass,violetAssociation,violetMiddleLabels]).

table(violetInterface,[id,"name","methods",x,y]).
:- dynamic violetInterface/5.

table(violetClass,[id,"name","fields","methods",x,y]).
violetClass(classnode0,'violetClass','"name";"fields";"methods";x;y','',107,47).
violetClass(classnode1,'violetInterface','"name";"methods";x;y','',105,397).
violetClass(classnode2,'violetAssociation','"role1";arrow1;"type1";"role2";arrow2;"type2";"lineStyle"','',641,34).
violetClass(classnode3,'violetInterfaceExtends','','',86,661).
violetClass(classnode4,'violetClassImplements','','',362,308).
violetClass(classnode5,'violetMiddleLabels','"label"','',639,257).
violetClass(classnode6,'endPoint','','',366,80).

table(violetAssociation,[id,"role1","arrow1",type1,"role2","arrow2",type2,"lineStyle",cid1,cid2]).
violetAssociation(id0,'idb;idx','',classnode,'','',classnode,'',classnode1,classnode3).
violetAssociation(id1,'cid','',classnode,'','',classnode,'',classnode0,classnode4).
violetAssociation(id2,'iid','',classnode,'','',classnode,'',classnode1,classnode4).
violetAssociation(id3,'','',classnode,'','TRIANGLE',classnode,'',classnode0,classnode6).
violetAssociation(id4,'','',classnode,'','TRIANGLE',classnode,'',classnode1,classnode6).
violetAssociation(id5,'cid1;cid2','',classnode,'','',classnode,'',classnode6,classnode2).
violetAssociation(id6,'','',classnode,'cid1;cid2','',classnode,'',classnode5,classnode6).

table(violetMiddleLabels,[id,cid1,cid2,"label"]).
:- dynamic violetMiddleLabels/4.


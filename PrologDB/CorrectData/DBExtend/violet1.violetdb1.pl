:-style_check(-discontiguous).

dbase(violetdb1,[violetInterface,violetClass,violetAssociation,violetMiddleLabels]).

table(violetInterface,[id,"name","methods",x,y,length]).
:- dynamic violetInterface/6.

table(violetClass,[id,"name","fields","methods",x,y,length]).
violetClass(classnode0,'violetClass','"name";"fields";"methods";x;y','',107,47,11).
violetClass(classnode1,'violetInterface','"name";"methods";x;y','',105,397,15).
violetClass(classnode2,'violetAssociation','"role1";arrow1;"type1";"role2";arrow2;"type2";"lineStyle"','',641,34,17).
violetClass(classnode3,'violetInterfaceExtends','','',86,661,22).
violetClass(classnode4,'violetClassImplements','','',362,308,21).
violetClass(classnode5,'violetMiddleLabels','"label"','',639,257,18).
violetClass(classnode6,'endPoint','','',366,80,8).

table(violetAssociation,[id,"role1","arrow1",type1,"role2","arrow2",type2,"lineStyle",cid1,cid2,length]).
violetAssociation(id0,'idb;idx','',classnode,'','',classnode,'',classnode1,classnode3,7).
violetAssociation(id1,'cid','',classnode,'','',classnode,'',classnode0,classnode4,3).
violetAssociation(id2,'iid','',classnode,'','',classnode,'',classnode1,classnode4,3).
violetAssociation(id3,'','',classnode,'','TRIANGLE',classnode,'',classnode0,classnode6,0).
violetAssociation(id4,'','',classnode,'','TRIANGLE',classnode,'',classnode1,classnode6,0).
violetAssociation(id5,'cid1;cid2','',classnode,'','',classnode,'',classnode6,classnode2,9).
violetAssociation(id6,'','',classnode,'cid1;cid2','',classnode,'',classnode5,classnode6,0).

table(violetMiddleLabels,[id,cid1,cid2,"label",length]).
:- dynamic violetMiddleLabels/5.


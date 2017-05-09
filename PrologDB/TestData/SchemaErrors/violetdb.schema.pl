dbase(violetdb,[violetInterface,violetClass,violetAssociation,violetMiddleLabels]).

table(violetInterface,[id,"name","methods",x,y]).
table(violetClass,[id,"name","fields","methods",x,y]).
table(violetAssociation,[id,"role1","arrow1",type1,"role2","arrow2",type2,"lineStyle",cid1,cid2]).
#missing decl for violetMiddleLabels



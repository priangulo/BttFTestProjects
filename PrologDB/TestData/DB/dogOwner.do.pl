:-style_check(-discontiguous).

dbase(do,[dog,owner,when]).

table(dog,[did,"name","breed", color]).
dog(d1,'kelsey','aussie',bluemerle).
dog(d2,'lassie','collie',sable).
dog(d3,'scarlett','aussie',blacktri).
dog(d4,'duke','hound dog',brown).

table(owner,[oid,"name"]).
owner(o1,'timmy').
owner(o2,'don').
owner(o3,'helen').
owner(o4,'jed').

table(when,[wid,did,oid,"date"]).
when(w1,d1,o2,'88-95').
when(w2,d2,o1,'58-71').
when(w3,d3,o3,'07-12').
when(w4,d3,o2,'12-').
when(w5,d4,o4,'58-69').

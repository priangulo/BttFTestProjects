:-style_check(-discontiguous).

dbase(petdb,[pet,cat,dog]).

table(pet,[did,"name"]).
pet(d0,'dumbo').
pet(d00,'nemo').

table(dog,[did,"name","breed", color]).
dog(d1,'kelsey','aussie',bluemerle).
dog(d2,'lassie','collie',sable).
dog(d3,'scarlett','aussie',blacktri).
dog(d4,'duke','hound dog',brown).

table(cat,[did,"name","type"]).
cat(d5,'tigger','bengal').
cat(d6,'lucy','tabby').

subtable(pet,[dog,cat]).

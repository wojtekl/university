var pl = {
  pinezkiWitaja: "Pinezki witają!", 
};

var en = {
  tytul: " Home &ndash; Map Tacks &ndash; ", 
  pinezkiWitaja: "Welcome to Map Tacks!", 
};

var Slownik = pl;

var jezyk = navigator.language;
if(null == jezyk){
  jezyk = navigator.browserLanguage;
};

if(
  ('pl' != jezyk) && 
  ('pl-PL' != jezyk) 
){
  Slownik = en;
  document.getElementsByTagName("title")[0].innerHTML = Slownik.tytul;
};


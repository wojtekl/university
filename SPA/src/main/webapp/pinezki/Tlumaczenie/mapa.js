var pl = {
};

var en = {
  tytul: " Map &ndash; Map Tacks &ndash; ", 
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


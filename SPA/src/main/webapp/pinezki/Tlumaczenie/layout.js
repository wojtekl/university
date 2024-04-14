var layout_pl = {
  stronaGlowna: "Strona główna", 
  wspolrzedne: "Współrzędne", 
  mapa: "Mapa", 
  konto: "Konto", 
  wyjdz: "Wyjdź", 
  problemZWyjsciem: "Problem z wyjściem!", 
};

var layout_en = {
  tytul: " Home &ndash; Map Tacks &ndash; ", 
  stronaGlowna: "Home", 
  wspolrzedne: "Coordinates", 
  mapa: "Map", 
  konto: "Account", 
  wyjdz: "Sign Out", 
  problemZWyjsciem: "Sign out problem!", 
};

var Slownik_layout = layout_pl;

var jezyk = navigator.language;
if(null == jezyk){
  jezyk = navigator.browserLanguage;
};

if(
  ('pl' != jezyk) && 
  ('pl-PL' != jezyk) 
){
  jezyk = 'en';
  Slownik_layout = layout_en;
} else{
  jezyk = 'pl';
};


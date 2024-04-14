var pl = {
  wspolrzedne: "Współrzędne", 
  ponizejZnajdujeSie: "Poniżej znajduje się lista współrzędnych twoich pinezek", 
  dodajPinezke: "Dodaj pinezkę", 
  nr: "Nr", 
  nazwa: "Nazwa", 
  szerokosc: "Szerokość", 
  dlugosc: "Długość", 
  operacja: "Operacja", 
  usun: "Usuń", 
  poleWymagane: "Pole wymagane", 
  zamknij: "Zamknij", 
  zatwierdz: "Zatwierdź", 
  uaktualnij: "Uaktualnij",
};

var en = {
  tytul: " Coordinates &ndash; Map Tacks &ndash; ", 
  wspolrzedne: "Coordinates", 
  ponizejZnajdujeSie: "Coordinates list of your Map Tacks is shown below", 
  dodajPinezke: "Add map tack", 
  nr: "No", 
  nazwa: "Name", 
  szerokosc: "Latitude", 
  dlugosc: "Longitude", 
  operacja: "Function", 
  usun: "Remove", 
  poleWymagane: "Required", 
  zamknij: "Close", 
  zatwierdz: "Confirm", 
  uaktualnij: "Update",
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


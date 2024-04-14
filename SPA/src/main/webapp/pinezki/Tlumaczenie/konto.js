var pl = {
  konto: "Konto", 
  wypelnijFormularzAby: "Wypełnij formularz, aby dokonać zmiany", 
  adresEMail: "Adres e-mail", 
  twojeHaslo: "Twoje hasło", 
  poleWymagane: "Pole wymagane", 
  noweHaslo: "Nowe hasło", 
  od8do: "od 8 do 40 znaków", 
  usunKonto: "Usuń konto", 
  pozostawPuste: "POZOSTAW PUSTE", 
  wpiszUsunAby: "Wpisz USUŃ aby wykonać. Operacji nie da się cofnąć!", 
  potwierdzHaslem: "Potwierdź hasłem", 
  sprawdzDokladnieZmiany: "Sprawdź dokładnie zmiany!", 
  zatwierdz: "Zatwierdź", 
  wprowadzPoprawneHaslo: "Wprowadź poprawne hasło!", 
  haslaNiezgodne: "Hasła niezgodne!", 
  kontoZostaloUsuniete: "Konto zostało usunięte, nastąpi wylogowanie!", 
  hasloMusiMiec: "Hasło musi mieć co najmniej 8 znaków!", 
  zachowano: "Zachowano!", 
};

var en = {
  tytul: " Your account &ndash; Map Tacks &ndash; ", 
  konto: "Account", 
  wypelnijFormularzAby: "Fill in the form below to make changes", 
  adresEMail: "E-mail", 
  twojeHaslo: "Your Password", 
  poleWymagane: "Required", 
  noweHaslo: "New Password", 
  od8do: " 8 to 40 characters", 
  usunKonto: "Delete Account", 
  pozostawPuste: "SET CLEAR", 
  wpiszUsunAby: "Enter REMOVE to proceed. It can't be undone!", 
  potwierdzHaslem: "Enter Password to Commit", 
  sprawdzDokladnieZmiany: "Check changes carefuly!", 
  zatwierdz: "Confirm", 
  wprowadzPoprawneHaslo: "Enter correct password!", 
  haslaNiezgodne: "Passwords don't match!", 
  kontoZostaloUsuniete: "Your account has been removed, you will be logged out!", 
  hasloMusiMiec: "Password must be a least 8 characters long!", 
  zachowano: "Saved!", 
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


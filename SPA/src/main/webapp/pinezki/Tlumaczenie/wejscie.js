function tlumaczenie(jezykStrona){
  var jezyk = navigator.language;
  if(null == jezyk){
    jezyk = navigator.browserLanguage;
  };
  if(
    ('pl' != jezyk) && 
    ('pl-PL' != jezyk) && 
    ('en' != jezykStrona) 
  ){
    window.location.replace("wejscie-en.html");
  };
  if(
    (('pl' === jezyk) || 
    ('pl-PL' === jezyk)) && 
    ('pl' != jezykStrona) 
  ){
    window.location.replace("wejscie.html");
  };
};


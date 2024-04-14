/* m.route(document.getElementById("main"), "/index", {
  "/index": {
    render: function(){
      return m(WidokIndex);
    }, 
  }, 
  "/wskazniki": {
    render: function(){
      return m(WidokWskazniki);
    }, 
  }, 
  "/mapa": {
    render: function(){
      return m(WidokMapa);
    }, 
  }, 
}); */

if("true" !== document.cookie.split("=")[1]){
  window.location.href = Serwer.ADRES_PREZENTACJA;
};

function menu(widok){
  divMenu = '\
    <a class="pure-menu-heading" href="#">Menu</a>\
    <ul class="pure-menu-list">\
    ';
      
  if('index' != widok){
    divMenu = divMenu + '\
      <li class="pure-menu-item">\
      <a href="index.html" class="pure-menu-link">' + Slownik_layout.stronaGlowna + '</a>\
      </li>\
    ';
  } else{
    divMenu = divMenu + '\
      <li class="pure-menu-item menu-item-divided pure-menu-selected">\
      <a href="#" class="pure-menu-link">' + Slownik_layout.stronaGlowna + '</a>\
      </li>\
    ';
  };
  if('wspolrzedne' != widok){
    divMenu = divMenu + '\
      <li class="pure-menu-item">\
      <a href="wspolrzedne.html" class="pure-menu-link">' + Slownik_layout.wspolrzedne + '</a>\
      </li>\
    ';
  } else{
    divMenu = divMenu + '\
      <li class="pure-menu-item menu-item-divided pure-menu-selected">\
      <a href="#" class="pure-menu-link">' + Slownik_layout.wspolrzedne + '</a>\
      </li>\
    ';
  };
  if('mapa' != widok){
    divMenu = divMenu + '\
      <li class="pure-menu-item">\
      <a href="mapa.html" class="pure-menu-link">' + Slownik_layout.mapa + '</a>\
      </li>\
    ';
  } else{
    divMenu = divMenu + '\
      <li class="pure-menu-item menu-item-divided pure-menu-selected">\
      <a href="#" class="pure-menu-link">' + Slownik_layout.mapa + '</a>\
      </li>\
    ';
  };
  if('konto' != widok){
    divMenu = divMenu + '\
      <li class="pure-menu-item">\
      <a href="konto.html" class="pure-menu-link">' + Slownik_layout.konto + '</a>\
      </li>\
    ';
  } else{
    divMenu = divMenu + '\
      <li class="pure-menu-item menu-item-divided pure-menu-selected">\
      <a href="#" class="pure-menu-link">' + Slownik_layout.konto + '</a>\
      </li>\
    ';
  };
      
  divMenu = divMenu + '\
    <li id="wyjdz" class="pure-menu-item">\
    <a href="#" class="pure-menu-link">' + Slownik_layout.wyjdz + '</a>\
    </li>\
    </ul>\
  ';
  document.getElementById("menu").childNodes[1].innerHTML = divMenu;
  document.getElementById("wyjdz").onclick = function(){
    m.request({
      method: "DELETE", 
      url: Serwer.ADRES_KONTO, 
      headers: {
        "Content-type": "application/x-www-form-urlencoded", 
      }, 
      data: null, 
      serialize: function(data){return data;}, 
    }).then(function(wynik){
      if(true === wynik[0]){
        document.cookie = "obecny=false";
        window.location.href = Serwer.ADRES_WEJDZ;
      } else{
        alert(Slownik_layout.problemZWyjsciem);
      }
    });
  };
}


var ModelWspolrzedne = {
  lista: [], 
  zaladuj: function(){
    return m.request({
      method: "GET", 
      url: Serwer.ADRES_PINEZKA, 
    }).then(function(wynik){
      ModelWspolrzedne.lista = wynik;
    });
  }, 
  usun: function(index){
    return m.request({
      method: "DELETE", 
      url: Serwer.ADRES_PINEZKA, 
      headers: {
        "Content-type": "application/x-www-form-urlencoded", 
      }, 
      data: "identyfikator=" + ModelWspolrzedne.lista[index][0], 
      serialize: function(data){return data;}, 
    }).then(function(wynik){
      ModelWspolrzedne.lista.splice(index, 1);
    });
  }, 
  identyfikator: -1, 
  nazwa: "", 
  szerokosc: 0.0, 
  dlugosc: 0.0, 
  dodaj: function(){
    return m.request({
      method: "PUT", 
      url: Serwer.ADRES_PINEZKA, 
      headers: {
        "Content-type": "application/x-www-form-urlencoded", 
      }, 
      data: "nazwa=" + ModelWspolrzedne.nazwa 
        + "&szerokosc=" + ModelWspolrzedne.szerokosc 
        + "&dlugosc=" + ModelWspolrzedne.dlugosc, 
      serialize: function(data){return data;}, 
    }).then(function(wynik){
      ModelWspolrzedne.zaladuj();
      m.render();
    });
  }, 
  uaktualnij: function(){
    return m.request({
      method: "POST", 
      url: Serwer.ADRES_PINEZKA, 
      headers: {
        "Content-type": "application/x-www-form-urlencoded", 
      }, 
      data: "identyfikator=" + ModelWspolrzedne.identyfikator 
        + "&nazwa=" + ModelWspolrzedne.nazwa 
        + "&szerokosc=" + ModelWspolrzedne.szerokosc 
        + "&dlugosc=" + ModelWspolrzedne.dlugosc, 
      serialize: function(data){return data;}, 
    }).then(function(wynik){
      ModelWspolrzedne.zaladuj();
      m.render();
    });
  },
};

var WidokWspolrzedne = {
  oninit: ModelWspolrzedne.zaladuj,
  view: function(){
    return [
      m(".header", [
        m("h1", Slownik.wspolrzedne), 
        m("h2", ""), 
      ]), 
      m(".content", [
        m("h2.content-subhead", Slownik.ponizejZnajdujeSie), 
        m(".pure-g", 
          m(".pure-u-1", [
            m(".pure-controls", [
              m("button.pure-button.pure-button-primary", {
                onclick: function(e){
                  e.preventDefault();
                  ModelWspolrzedne.identyfikator = -1;
                  ModelWspolrzedne.nazwa = "";
                  ModelWspolrzedne.szerokosc = 0.0;
                  ModelWspolrzedne.dlugosc = 0.0;
                  document.getElementById("modal").style.display = "block";
                }, 
              }, Slownik.dodajPinezke), 
            ]), 
            m("table.pure-table", [
              m("thead", 
                m("tr", [
                  m("th", Slownik.nr), 
                  m("th", Slownik.nazwa), 
                  m("th", Slownik.szerokosc), 
                  m("th", Slownik.dlugosc), 
                  m("th", Slownik.operacja), 
                ])
              ), 
              m("tbody", ModelWspolrzedne.lista.map(
                function(model, index){
                  return m("tr.pure-table-odd", [
                    m("td", String(index + 1) + '.'), 
                    m("td", model[1]), 
                    m("td", model[2]), 
                    m("td", model[3]), 
                    m("td", [
                      m("button.pure-button", {
                        onclick: function(e){
                          e.preventDefault();
                          ModelWspolrzedne.identyfikator = model[0];
                          ModelWspolrzedne.nazwa = model[1];
                          ModelWspolrzedne.szerokosc = model[2];
                          ModelWspolrzedne.dlugosc = model[3];
                          document.getElementById("modal").style.display = "block";
                        }, 
                      }, Slownik.uaktualnij), 
                      m("button.pure-button", {
                        onclick: function(e){
                          e.preventDefault();
                          ModelWspolrzedne.usun(index);
                        }, 
                      }, Slownik.usun), 
                    ]),
                  ])
                }
              )
              ), 
            ])
          ])
        ), 
      ]), 
      m("#modal", [
        m(".modal-content", 
          m("form.pure-form.pure-form-aligned", {onsubmit: function(e){
              e.preventDefault();
              if(-1 < ModelWspolrzedne.identyfikator){
                ModelWspolrzedne.uaktualnij();
              }
              else{
                ModelWspolrzedne.dodaj();
              }
              document.getElementById("modal").style.display = "none";
            }
          }, 
            m("fieldset", [
              m(".pure-control-group", [
                m("label[for=form-nazwa]", Slownik.nazwa), 
                m("input#form-nazwa[type=text][placeholder=" + Slownik.nazwa + "]", {
                  oninput: m.withAttr("value", function(value){
                    ModelWspolrzedne.nazwa = value;
                  }), 
                  value: ModelWspolrzedne.nazwa, 
                  }), 
                m("span.pure-form-message-inline", Slownik.poleWymagane), 
              ]), 
              m(".pure-control-group", [
                m("label[for=form-szerokosc]", Slownik.szerokosc), 
                m("input#form-szerokosc[type=text][placeholder=" + Slownik.szerokosc + "]", {
                  oninput: m.withAttr("value", function(value){
                    ModelWspolrzedne.szerokosc = value;
                  }), 
                  value: ModelWspolrzedne.szerokosc, 
                  }), 
                m("span.pure-form-message-inline", Slownik.poleWymagane), 
              ]), 
              m(".pure-control-group", [
                m("label[for=form-dlugosc]", Slownik.dlugosc), 
                m("input#form-dlugosc[type=text][placeholder=" + Slownik.dlugosc + "]", {
                  oninput: m.withAttr("value", function(value){
                    ModelWspolrzedne.dlugosc = value;
                  }), 
                  value: ModelWspolrzedne.dlugosc, 
                  }), 
                m("span.pure-form-message-inline", Slownik.poleWymagane), 
              ]), 
              m(".pure-controls", [
                m("button.pure-button", {
                    onclick: function(e){
                    e.preventDefault();
                    document.getElementById("modal").style.display = "none";
                  }, 
                }, Slownik.zamknij), 
                m("button.pure-button.pure-button-primary[type=submit]", Slownik.zatwierdz), 
              ]), 
            ])
          )
        ), 
      ]), 
    ];
  }
};

menu("wspolrzedne");

m.mount(document.getElementById("main"), WidokWspolrzedne);


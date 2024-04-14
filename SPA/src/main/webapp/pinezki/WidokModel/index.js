var ModelIndex = {
  lista: [], 
  zaladuj: function(){
    return m.request({
      method: "GET", 
      url: Serwer.ADRES_OGLOSZENIE + "?liczba=3&jezyk=" + jezyk, 
    }).then(function(wynik){
      ModelIndex.lista = wynik;
    });
  }, 
};

var WidokIndex = {
  oninit: ModelIndex.zaladuj,
  view: function(){
    return [
      m(".header", [
        m("h1", Slownik.pinezkiWitaja), 
        m("h2", ""), 
      ]),
      m(".content", ModelIndex.lista.map(
        function(model, index){
          return [
            m("h2.content-subhead", model[1]), 
            m(".pure-g", 
              m(".pure-u-1", model[2])
            ), 
          ]
        }
      )
      ), 
    ];
  }
};

menu("index");

m.mount(document.getElementById("main"), WidokIndex);


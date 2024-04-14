var ModelKonto = {
  lista: [], 
  zaladuj: function(){
    return m.request({
      method: "GET", 
      url: Serwer.ADRES_UZYTKOWNIK, 
    }).then(function(wynik){
      ModelKonto.lista = wynik;
      ModelKonto.lista[2] = null; ModelKonto.lista[3] = null;
      ModelKonto.lista[4] = null; ModelKonto.lista[5] = null;
    });
  }, 
  zapisz: function(){
    var haslo = ModelKonto.lista[2];
    if(
      (8 > haslo.length) || 
      (255 < haslo.length) || 
      (/\s/g.test(haslo))
    ){
      return alert(Slownik.wprowadzPoprawneHaslo);
    }
    if(haslo != ModelKonto.lista[5]){
      return alert(Slownik.haslaNiezgodne);
    }
    if("USUŃ" === ModelKonto.lista[4]){
      return m.request({
        method: "DELETE", 
        url: Serwer.ADRES_UZYTKOWNIK, 
        headers: {
          "Content-type": "application/x-www-form-urlencoded", 
        }, 
        data: "haslo=" + haslo, 
        serialize: function(data){return data;}, 
      }).then(function(wynik){
        if(true === wynik[0]){
          document.cookie = "obecny=false";
          ModelKonto.lista = null;
          alert(Slownik.kontoZostaloUsuniete);
          window.location.href = Serwer.ADRES_WEJDZ;
        }
      });
    }
    var noweHaslo = ModelKonto.lista[3];
    if(
      (8 > noweHaslo.length) || 
      (255 < noweHaslo.length) || 
      (/\s/g.test(noweHaslo))
    ){
      return alert(Slownik.hasloMusiMiec);
    } else{
      return m.request({
        method: "POST", 
        url: Serwer.ADRES_UZYTKOWNIK, 
        headers: {
          "Content-type": "application/x-www-form-urlencoded", 
        }, 
        data: "haslo=" + haslo + "&haslonowe=" + noweHaslo, 
        serialize: function(data){return data;}, 
      }).then(function(wynik){
        if(true === wynik[0]){
          alert(Slownik.zachowano);
        }
      });
    }
  }, 
};

var WidokKonto = {
  oninit: ModelKonto.zaladuj,
  view: function(){
    return [
      m(".header", [
        m("h1", Slownik.konto), 
        m("h2", ""), 
      ]),
      m(".content", [
        m("h2.content-subhead", Slownik.wypelnijFormularzAby), 
        m(".pure-g", 
          m(".pure-u-1", 
            m("form.pure-form.pure-form-aligned", {onsubmit: function(e){
                e.preventDefault();
                ModelKonto.zapisz();
              }
            }, 
              m("fieldset", [
                m(".pure-control-group", [
                  m("label[for=form-adres-e-mail]", Slownik.adresEMail), 
                  m("input#form-adres-e-mail[type=email][placeholder=" + Slownik.adresEMail + "][readonly]", {value: ModelKonto.lista[1],}), 
                ]), 
                m(".pure-control-group", [
                  m("label[for=form-haslo]", Slownik.twojeHaslo), 
                  m("input#form-haslo[type=password][placeholder=" + Slownik.twojeHaslo + "]", {
                    oninput: m.withAttr("value", function(value){
                      ModelKonto.lista[2] = trimAll(value);
                    }), 
                    value: ModelKonto.lista[2], 
                  }), 
                  m("span.pure-form-message-inline", Slownik.poleWymagane), 
                ]), 
                m(".pure-control-group", [
                  m("label[for=form-haslo-nowe]", Slownik.noweHaslo), 
                  m("input#form-haslo-nowe[type=password][placeholder=" + Slownik.od8do + "]", {
                    oninput: m.withAttr("value", function(value){
                      ModelKonto.lista[3] = trimAll(value);
                    }), 
                    value: ModelKonto.lista[3], 
                  }), 
                ]), 
                m(".pure-control-group", [
                  m("label[for=form-konto-usun]", Slownik.usunKonto), 
                  m("input#form-konto-usun[type=text][placeholder=" + Slownik.pozostawPuste + "]", {
                    oninput: m.withAttr("value", function(value){
                      ModelKonto.lista[4] = value;
                    }), 
                    value: ModelKonto.lista[4], 
                    }), 
                  m("span.pure-form-message-inline", Slownik.wpiszUsunAby), 
                ]), 
                m(".pure-control-group", [
                  m("label[for=form-haslo-potwierdz]", Slownik.potwierdzHaslem), 
                  m("input#form-haslo-potwierdz[type=password][placeholder=" + Slownik.potwierdzHaslem + "]", {
                    oninput: m.withAttr("value", function(value){
                      ModelKonto.lista[5] = trimAll(value);
                    }), 
                    value: ModelKonto.lista[5], 
                    }), 
                  m("span.pure-form-message-inline", Slownik.sprawdzDokladnieZmiany), 
                ]), 
                m(".pure-controls", [
                  m("button.pure-button.pure-button-primary[type=submit]", Slownik.zatwierdz), 
                ]), 
              ])
            )
          )
        ), 
      ]), 
    ];
  }
};

menu("konto");

m.mount(document.getElementById("main"), WidokKonto);

function trimAll(text){
  return text.replace(/\s/g, '');
};


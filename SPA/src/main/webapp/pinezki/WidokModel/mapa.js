var ModelMapa = {
  lista: [], 
  zaladuj: function(){
    return m.request({
      method: "GET", 
      url: Serwer.ADRES_PINEZKA, 
    }).then(function(wynik){
      ModelMapa.lista = wynik;
      var image = "Obrazki/beachflag.png";
      var l = ModelMapa.lista.length;
      for(var i = 0; i < l; ++i){
        var marker = new google.maps.Marker({
          position: {
            lat: ModelMapa.lista[i][2], 
            lng: ModelMapa.lista[i][3], 
          }, 
          title: String(i + 1) + '. ' + ModelMapa.lista[i][1], 
          map: map, 
          draggable: true, 
          animation: google.maps.Animation.DROP, 
          icon: image,
        });
        marker.addListener("click", function(){
          var infoWindow = new google.maps.InfoWindow({
            content: 
              this.title 
              + "<br />" 
              + this.getPosition().lat() 
              + "; " 
              + this.getPosition().lng(), 
          });
          var mark = this;
          infoWindow.addListener("closeclick", function(){
            mark.setAnimation(null);
          });
          infoWindow.open(map, this);
          this.setAnimation(google.maps.Animation.BOUNCE);
        });
      };
      if(0 < l){
        map.setCenter({lat: ModelMapa.lista[l - 1][2], lng: ModelMapa.lista[l - 1][3]});
      }
    });
  }, 
};

var WidokMapa = {
  oninit: ModelMapa.zaladuj, 
  view: function(){
  }, 
};

menu("mapa");

m.mount(document.getElementById("widok"), WidokMapa);


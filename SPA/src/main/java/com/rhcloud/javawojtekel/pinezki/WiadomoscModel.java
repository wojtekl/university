package com.rhcloud.javawojtekel.pinezki;

import java.util.Date;

public class WiadomoscModel
{
  public WiadomoscModel()
  {
  }
  
  private int identyfikator;
  
  public int getIdentyfikator()
  {
    return identyfikator;
  }
  
  public void setIdentyfikator(final int identyfikator)
  {
    this.identyfikator = identyfikator;
  }
  
  private ModelUzytkownik uzytkownik;
  
  public ModelUzytkownik getUzytkownik()
  {
    return uzytkownik;
  }
  
  public void setUzytkownik(final ModelUzytkownik uzytkownik)
  {
    this.uzytkownik = uzytkownik;
  }
  
  private GrupaModel grupa;
  
  public GrupaModel getGrupa()
  {
    return grupa;
  }
  
  public void setGrupa(final GrupaModel grupa)
  {
    this.grupa = grupa;
  }
  
  private String tresc;
  
  public String getTresc()
  {
    return tresc;
  }
  
  public void setTresc(final String tresc)
  {
    this.tresc = tresc;
  }
  
  private Date czas;
  
  public Date getCzas()
  {
    return czas;
  }
  
  public void setCzas(final Date czas)
  {
    this.czas = czas;
  }
  
  public String toJSON()
  {
    return new StringBuilder()
      .append("[\"")
      .append(identyfikator)
      .append("\",\"")
      .append(uzytkownik.getIdentyfikator())
      .append("\",\"")
      .append(grupa.getIdentyfikator())
      .append("\",\"")
      .append(tresc)
      .append("\",\"")
      .append(czas)
      .append("\"]")
      .toString();
  }
}


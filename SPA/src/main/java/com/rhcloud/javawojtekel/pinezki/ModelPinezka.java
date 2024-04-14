package com.rhcloud.javawojtekel.pinezki;

public class ModelPinezka
{
  public ModelPinezka()
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
  
  private String nazwa;
  
  public String getNazwa()
  {
    return nazwa;
  }
  
  public void setNazwa(final String nazwa)
  {
    this.nazwa = nazwa;
  }
  
  private double szerokosc;
  
  public double getSzerokosc()
  {
    return szerokosc;
  }
  
  public void setSzerokosc(final double szerokosc)
  {
    this.szerokosc = szerokosc;
  }
  
  private double dlugosc;
  
  public double getDlugosc()
  {
    return dlugosc;
  }
  
  public void setDlugosc(final double dlugosc)
  {
    this.dlugosc = dlugosc;
  }
  
  private long czasAktualizacji;
  
  public long getCzasAktualizacji()
  {
    return czasAktualizacji;
  }
  
  public void setCzasAktualizacji(final long czasAktualizacji)
  {
    this.czasAktualizacji = czasAktualizacji;
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
  
  public String toJSON()
  {
    return new StringBuilder()
      .append("[")
      .append(identyfikator)
      .append(",\"")
      .append(nazwa)
      .append("\",")
      .append(szerokosc)
      .append(",")
      .append(dlugosc)
      .append(",")
      .append(czasAktualizacji)
      .append(",")
      .append(uzytkownik.getIdentyfikator())
      .append("]")
      .toString();
  }
}


package com.rhcloud.javawojtekel.pinezki;

public class ModelUzytkownikNowy
{
  public ModelUzytkownikNowy()
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
  
  private String email;
  
  public String getEmail()
  {
    return email;
  }
  
  public void setEmail(final String email)
  {
    this.email = email;
  }
  
  private String haslo;
  
  public String getHaslo()
  {
    return haslo;
  }
  
  public void setHaslo(final String haslo)
  {
    this.haslo = haslo;
  }
  
  private String kodAktywacyjny;
  
  public String getKodAktywacyjny()
  {
    return kodAktywacyjny;
  }
  
  public void setKodAktywacyjny(final String kodAktywacyjny)
  {
    this.kodAktywacyjny = kodAktywacyjny;
  }
  
  private long czasStworzenia;
  
  public long getCzasStworzenia()
  {
    return czasStworzenia;
  }
  
  public void setCzasStworzenia(final long czasStworzenia)
  {
    this.czasStworzenia = czasStworzenia;
  }
}


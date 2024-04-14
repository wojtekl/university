package com.rhcloud.javawojtekel.pinezki;

public class ModelUzytkownik
{
  public ModelUzytkownik()
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
  
  private long czasStworzenia;
  
  public long getCzasStworzenia()
  {
    return czasStworzenia;
  }
  
  public void setCzasStworzenia(final long czasStworzenia)
  {
    this.czasStworzenia = czasStworzenia;
  }
  
  private long czasWaznosci;
  
  public long getCzasWaznosci()
  {
    return czasWaznosci;
  }
  
  public void setCzasWaznosci(final long czasWaznosci)
  {
    this.czasWaznosci = czasWaznosci;
  }
  
  public String toJSON()
  {
    return new StringBuilder()
      .append("[")
      .append(identyfikator)
      .append(",\"")
      .append(email)
      .append("\",")
      .append(czasStworzenia)
      .append(",")
      .append(czasWaznosci)
      .append("]")
      .toString();
  }
}


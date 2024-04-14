package com.rhcloud.javawojtekel.pinezki;

public class GrupaModel
{
  public GrupaModel()
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
  
  public String toJSON()
  {
    return new StringBuilder()
      .append("[\"")
      .append(identyfikator)
      .append("\",\"")
      .append(nazwa)
      .append("\"]")
      .toString();
  }
}


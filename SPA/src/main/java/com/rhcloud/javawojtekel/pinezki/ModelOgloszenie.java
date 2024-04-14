package com.rhcloud.javawojtekel.pinezki;

public class ModelOgloszenie
{
  public ModelOgloszenie()
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
  
  private String tytul;
  
  public String getTytul()
  {
    return tytul;
  }
  
  public void setTytul(final String tytul)
  {
    this.tytul = tytul;
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
  
  private long czasAktualizacji;
  
  public long getCzasAktualizacji()
  {
    return czasAktualizacji;
  }
  
  public void setCzasAktualizacji(final long czasAktualizacji)
  {
    this.czasAktualizacji = czasAktualizacji;
  }
  
  private String jezyk;
  
  public String getJezyk()
  {
    return jezyk;
  }
  
  public void setJezyk(final String jezyk)
  {
    this.jezyk = jezyk;
  }
  
  public String toJSON()
  {
    return new StringBuilder()
      .append("[")
      .append(identyfikator)
      .append(",\"")
      .append(tytul)
      .append("\",\"")
      .append(tresc)
      .append("\",")
      .append(czasAktualizacji)
      .append("]")
      .toString();
  }
}


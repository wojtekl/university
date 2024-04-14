package com.rhcloud.javawojtekel.pinezki;

public class UzytkownikGrupaModel
{
  public UzytkownikGrupaModel()
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
}


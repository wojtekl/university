package com.jwycieczki;

public class Wiadomosc
{
	
	protected String nadawca;
	protected String odbiorca;
	protected String tresc;
	protected String data;
	
	protected Wiadomosc(final String nadawca, final String odbiorca, final String tresc, final String data)
	{
		
		this.nadawca = nadawca;
		this.odbiorca = odbiorca;
		this.tresc = tresc;
		this.data = data;
		
	}
	
	protected void addWiadomosc(String tresc)
	{
		this.tresc=tresc;
		//this.adresat=adresat;
		//this.odbiorcy=odbiorcy;
	}
	
	protected void addWiadomosc(String tresc, String adresat, String odbiorca)
	{
		this.tresc=tresc;
		this.nadawca=odbiorca;
		this.odbiorca=odbiorca;
	}
	
}

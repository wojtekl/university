package com.jwycieczki;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

public class Uzytkownik
{
	
	public String identyfikator;
	public String imie;
	public String nazwisko;
	public String email;
	public String haslo;
	public int status;
	Wycieczka wycieczka;
	
	public Uzytkownik(String identyfikator, String haslo, int status, String email, String imie, String nazwisko)
	{
		
		this.identyfikator = identyfikator;
		this.haslo = haslo;
		this.email = email;
		this.status = status;
		this.imie = imie;
		this.nazwisko = nazwisko;
		
	}
	
	public Uzytkownik(final String identyfikator, final String imie, final String nazwisko, final String email)
	{
		
		this.identyfikator = identyfikator;
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.email = email;
		
	}
	
	public Uzytkownik()
	{
		
	}
	
	public void pobierz(final int identyfikator)
	{
		
		List<NameValuePair> parametry = new ArrayList<NameValuePair>();
		parametry.add(new BasicNameValuePair("id", String.valueOf(identyfikator)));
		try
		{
			
			JSONArray json = new JSONArray(BazaDanych.wykonaj_skrypt("profil_pobierz.php", parametry));
			this.identyfikator = json.getString(0);
			this.imie = json.getString(1);
			this.nazwisko = json.getString(2); 
			this.email = json.getString(3);
			
		}
		catch(JSONException exception)
		{
			
		}
		
	}
	
}

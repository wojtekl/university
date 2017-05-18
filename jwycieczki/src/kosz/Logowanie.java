package com.jwycieczki;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Logowanie
{
	
	public String Zaloguj(String identyfikator, String haslo)
	{
		
		if(identyfikator.isEmpty() || haslo.isEmpty())
		{
			
			return "Nie wpisano Danych";
			
		}
		else
		{
			
	        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
	        nvp.add(new BasicNameValuePair("nick", identyfikator));
	        nvp.add(new BasicNameValuePair("password", haslo));
	        String r = BazaDanych.wykonaj_skrypt("login.php", nvp);
	        int i = Integer.parseInt(r);
	        if(-1 < i)
	        {
    			return "zalogowano";
    			
    		}
	        else if(-3 == i)
	        {
	        	
    			return "Niepoprawny login lub haslo";
    			
    		}
	        else if(-2 == i)
	        {
	        	
    			return "aktywuj konto";
    			
    		}
	        else
	        {
	        	
    			return r+" jakis b��d";
    			
    		}
	        
		}
		
	}
	
}

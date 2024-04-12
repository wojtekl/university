package com.jwycieczki;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_HasloNowe extends Activity
{
	
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nowe_haslo);
		
	}
	
	public void button_wygeneruj(View v)
	{
		
		String komunikat;
		List<NameValuePair> parametry = new ArrayList<NameValuePair>();
        parametry.add(new BasicNameValuePair("nick", ((EditText)findViewById(R.id.nowehaslonick)).getText().toString()));
        parametry.add(new BasicNameValuePair("email", ((EditText)findViewById(R.id.nowehasloemail)).getText().toString()));
        String wynik = BazaDanych.wykonaj_skrypt("password.php", parametry);
        if(wynik.equals("1"))
        {
        	
        	komunikat = "Hasło zmienione.";
        	
        }
        else if(wynik.equals("0"))
        {
        	
        	komunikat = "Podano błędne dane.";
        	
        }
        else if(wynik.equals("2"))
        {
        	
        	komunikat = "Aktywuj konto.";
        	
        }
        else
        {
        	
        	komunikat = wynik;
        	
        }
        Toast.makeText(getApplicationContext(), komunikat, Toast.LENGTH_LONG).show();
        
	}
	
}
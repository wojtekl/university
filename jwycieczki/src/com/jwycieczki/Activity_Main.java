package com.jwycieczki;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Main extends Activity
{
	
	private Context context;
	private static final String preferencesName = "jwycieczki";
	private static final String preferencesKey = "identyfikator";
	
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logowanie);
		context = this.getApplicationContext();
		SharedPreferences sharedPreferences = this.getSharedPreferences(preferencesName, 0);
		int identyfikator = sharedPreferences.getInt(preferencesKey, -1);
		if(-1 < identyfikator)
		{
			
			Intent intent = new Intent(context, Activity_ObszarRoboczy.class);
			intent.putExtra("IdUzytkownika", identyfikator);
			startActivity(intent);
			this.finish();
			
		}
		
	}
    
    public void button_zaloguj(View v)
    {
    	
    	String identyfikator = ((EditText)findViewById(R.id.etLoginL)).getText().toString();
    	String haslo = ((EditText)findViewById(R.id.etHasloL)).getText().toString();
    	String komunikat;
    	if(identyfikator.isEmpty() || haslo.isEmpty())
		{
			
			komunikat = "Nie wpisano Danych";
			
		}
		else
		{
			
	        List<NameValuePair> parametry = new ArrayList<NameValuePair>();
	        parametry.add(new BasicNameValuePair("nick", identyfikator));
	        parametry.add(new BasicNameValuePair("password", haslo));
	        String wynik = BazaDanych.wykonaj_skrypt("login.php", parametry);
	        final int wynikInt = Integer.parseInt(wynik);
	        if(-1 < wynikInt)
	        {
	        	
    			komunikat = "zalogowano";
    			Intent intent = new Intent(context, Activity_ObszarRoboczy.class);
    			intent.putExtra("IdUzytkownika", identyfikator);
    			startActivity(intent);
    			SharedPreferences.Editor preferencesEditor = this.getSharedPreferences(preferencesName, 0).edit();
    			preferencesEditor.putInt(preferencesKey, wynikInt);
    			preferencesEditor.apply();
    			this.finish();
    			
    		}
	        else if(-3 == wynikInt)
	        {
	        	
    			komunikat = "Niepoprawny login lub haslo";
    			
    		}
	        else if(-2 == wynikInt)
	        {
	        	
    			komunikat = "aktywuj konto";
    			
    		}
	        else
	        {
	        	
    			komunikat = wynik + " jakis błąd";
    			
    		}
	        Toast.makeText(context, komunikat, Toast.LENGTH_LONG).show();
	        
		}
    	
    }
    
    public void button_rejestracja(View v)
    {
    	
		startActivity(new Intent(context, Activity_Rejestracja.class));
		
    }
    
    public void edittext_hasloNowe(View v)
    {
    	
		startActivity(new Intent(context, Activity_HasloNowe.class));
		
	}
    
    public void edittext_zakoncz()
    {
    	
    	TextView zakoncz = (TextView) findViewById(R.id.tvZamknijL);
    	zakoncz.setOnClickListener(new OnClickListener()
    	{
    		
    		@Override
    		public void onClick(View v)
    		{
    			
    			finish();
    			
    		}
    		
    	}
    	);
    	
    }
    
}
package com.jwycieczki;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_Rejestracja extends Activity
{
	
	private Context context;
	private Resources resources;
	
	private Uzytkownik uzytkownik;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rejestracja);
		context = getApplicationContext();
		resources = getResources();
		
	}
	
	public void button_zarejestruj(View v)
	{
		
		String komunikat = "";
		uzytkownik.identyfikator = ((EditText)findViewById(R.id.etLoginR)).getText().toString();
		uzytkownik.haslo = ((EditText)findViewById(R.id.etHasloR)).getText().toString();
		uzytkownik.email = ((EditText)findViewById(R.id.etEmailR)).getText().toString();
		String pHaslo = ((EditText)findViewById(R.id.etPHasloR)).getText().toString();
		boolean regulamin = ((CheckBox)findViewById(R.id.cbAkceptujeR)).isChecked();
		boolean poprawne = true;
		if(
				uzytkownik.identyfikator.isEmpty() || 
				uzytkownik.haslo.isEmpty() || 
				uzytkownik.email.isEmpty() || 
				pHaslo.isEmpty() 
		)
		{
			
			poprawne = false;
			komunikat = resources.getString(R.string.Uzupelnij_pola);
			
		}
		if(false == regulamin)
		{
			
			poprawne = false;
			komunikat = resources.getString(R.string.Akceptacja_regulaminu);
			
		}
		if(8 > uzytkownik.haslo.length())
		{
			
			poprawne = false;
			komunikat = resources.getString(R.string.Haslo_krotkie);
			
		}
		if(!uzytkownik.haslo.equals(pHaslo))
		{
			
			poprawne = false;
			komunikat = resources.getString(R.string.Hasla_rozne);
			
		}
		if(poprawne)
		{
			
			List<NameValuePair> parametry = new ArrayList<NameValuePair>();
			parametry.add(new BasicNameValuePair("nick", uzytkownik.identyfikator));
			parametry.add(new BasicNameValuePair("email", uzytkownik.email));
			parametry.add(new BasicNameValuePair("password", uzytkownik.email));
			String wynik = BazaDanych.wykonaj_skrypt("signup.php", parametry);
			if(wynik.equals("1"))
			{
				
				Intent intent = new Intent(context,Activity_Main.class );
				startActivity(intent);
				this.finish();
				
			}
			else if(wynik.equals("-4"))
			{
				
				komunikat = resources.getString(R.string.Nazwa_niedozwolona);
				
			}
			else if(wynik.equals("-3"))
			{
				
				komunikat = resources.getString(R.string.Email_niedozwolony);
				
			}
			else
			{
				
				komunikat = wynik;
				
			}
			
		}
		Toast.makeText(context, komunikat, Toast.LENGTH_LONG).show();
		
	}
	
	public void button_Regulamin(View v)
	{
		
		startActivity(new Intent(context, Activity_Regulamin.class));
		
	}
	
	public void onClickAnuluj(View v)
	{
		Toast.makeText(context, resources.getString(R.string.Anulowano), Toast.LENGTH_LONG).show();
		startActivity(new Intent(context, Activity_Main.class));
		this.finish();
	}
	
}
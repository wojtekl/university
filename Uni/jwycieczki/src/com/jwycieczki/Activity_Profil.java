package com.jwycieczki;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Profil extends Activity
{
	
	private Context context;
	private int identyfikator;
	private Uzytkownik uzytkownik;
	private TextView textView_profil_nazwa;
	private LinearLayout linearLayout_profil_zmien;
	private LinearLayout linearLayout_obecneHaslo;
	private LinearLayout linearLayout_zmienHaslo;
	private LinearLayout linearLayout_zmienAdresEmail;
	private EditText editText_obecneHaslo;
	private EditText editText_adresEmail;
	private EditText editText_noweHaslo;
	private EditText editText_powtorzNoweHaslo;
	
	protected void onCreate(final Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profil);
		context = this.getApplicationContext();
		identyfikator = this.getIntent().getIntExtra("IdUzytkownika", -1);
		textView_profil_nazwa = (TextView)findViewById(R.id.textView_profil_nazwa);
		linearLayout_profil_zmien = (LinearLayout)findViewById(R.id.linearLayout_profil_zmien);
		linearLayout_obecneHaslo = (LinearLayout)findViewById(R.id.linearLayout_obecneHaslo);
		linearLayout_zmienHaslo = (LinearLayout)findViewById(R.id.linearLayout_zmienHaslo);
		linearLayout_zmienAdresEmail = (LinearLayout)findViewById(R.id.linearLayout_zmienAdresEmail);
		editText_obecneHaslo = (EditText)findViewById(R.id.editText_obecneHaslo);
		editText_adresEmail = (EditText)findViewById(R.id.editText_adresEmail);
		editText_noweHaslo = (EditText)findViewById(R.id.editText_noweHaslo);
		editText_powtorzNoweHaslo = (EditText)findViewById(R.id.editText_powtorzNoweHaslo);
		if(-1 < identyfikator)
		{
			
			uzytkownik = new Uzytkownik();
			uzytkownik.pobierz(identyfikator);
			textView_profil_nazwa.setText(uzytkownik.identyfikator);
			
		}
		
	}
	
	public void button_zmienHaslo(View view)
	{
		
		linearLayout_zmienHaslo.setVisibility(View.VISIBLE);
		linearLayout_obecneHaslo.setVisibility(View.VISIBLE);
		linearLayout_profil_zmien.setVisibility(View.GONE);
		
	}
	
	public void button_zmienHaslo_zrezygnuj(View view)
	{
		
		linearLayout_zmienHaslo.setVisibility(View.GONE);
		linearLayout_obecneHaslo.setVisibility(View.GONE);
		linearLayout_profil_zmien.setVisibility(View.VISIBLE);
		
	}
	
	public void button_zmienHaslo_zapisz(View view)
	{
		String obecneHaslo = editText_obecneHaslo.getText().toString();
		String noweHaslo = editText_noweHaslo.getText().toString();
		if(noweHaslo.equals(editText_powtorzNoweHaslo.getText().toString()))
		{
			
			List<NameValuePair> parametry = new ArrayList<NameValuePair>();
			parametry.add(new BasicNameValuePair("id", String.valueOf(identyfikator)));
			parametry.add(new BasicNameValuePair("haslo_poprzednie", obecneHaslo));
			parametry.add(new BasicNameValuePair("haslo_nowe", noweHaslo));
			String dane = BazaDanych.wykonaj_skrypt("profil_haslo.php", parametry);
			if(dane.equals("1"))
			{
				
				Toast.makeText(context, "Haslo zostalo zmienione", Toast.LENGTH_LONG).show();
				linearLayout_zmienHaslo.setVisibility(View.GONE);
				linearLayout_obecneHaslo.setVisibility(View.GONE);
				linearLayout_profil_zmien.setVisibility(View.VISIBLE);
				
			}

			
		}
		
	}
	
	public void button_zmienAdresEmail(View view)
	{
		
		editText_adresEmail.setText(uzytkownik.email);
		linearLayout_zmienAdresEmail.setVisibility(View.VISIBLE);
		linearLayout_obecneHaslo.setVisibility(View.VISIBLE);
		linearLayout_profil_zmien.setVisibility(View.GONE);
		
	}
	
	public void button_zmienAdresEmail_zrezygnuj(View view)
	{
		
		linearLayout_zmienAdresEmail.setVisibility(View.GONE);
		linearLayout_obecneHaslo.setVisibility(View.GONE);
		linearLayout_profil_zmien.setVisibility(View.VISIBLE);
		
	}
	
	public void button_zmienAdresEmail_zapisz(View view)
	{
		
		linearLayout_zmienAdresEmail.setVisibility(View.GONE);
		linearLayout_obecneHaslo.setVisibility(View.GONE);
		linearLayout_profil_zmien.setVisibility(View.VISIBLE);
		
	}
	
}
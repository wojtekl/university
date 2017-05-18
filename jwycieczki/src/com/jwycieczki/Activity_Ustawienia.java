package com.jwycieczki;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Activity_Ustawienia extends Activity
{
	
	private Context context;
	private int identyfikator;
	
	private List<TrzyWartosci> lista_wycieczek;
	private List<String> pl;
	private ArrayAdapter<String> adapter;
	
	protected void onCreate(final Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ustawienia);
		context = this.getApplicationContext();
		identyfikator = this.getIntent().getIntExtra("IdUzytkownika", -1);
		if(-1 < identyfikator)
		{
        	
			pl = new ArrayList<String>();
			lista_wycieczek = new ArrayList<TrzyWartosci>();
			List<NameValuePair> parametry = new ArrayList<NameValuePair>();
			parametry.add(new BasicNameValuePair("id", String.valueOf(identyfikator)));
			try
			{
				
				JSONArray json = new JSONArray(BazaDanych.wykonaj_skrypt("nowe_wycieczki.php", parametry));
				for(int i = 0; i < json.length(); ++i)
				{
					
					if(json.getJSONArray(i).getString(2).toLowerCase().contains("czlonek"))
					{
						
						pl.add(json.getJSONArray(i).getString(0)+" (zapisany)");
						lista_wycieczek.add(new TrzyWartosci(
								json.getJSONArray(i).getString(0)+" (zapisany)", 
								json.getJSONArray(i).getString(1), 
								json.getJSONArray(i).getString(2)
								));
						
					}
					else
					{
						
						pl.add(json.getJSONArray(i).getString(0));
						lista_wycieczek.add(new TrzyWartosci(
								json.getJSONArray(i).getString(0), 
								json.getJSONArray(i).getString(1), 
								json.getJSONArray(i).getString(2)
								));
						
					}
					
				}
				
			}
			catch(JSONException exception)
			{
				
			}
			ListView l = (ListView)findViewById(R.id.lvListaWycieczekU);
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pl);
			l.setAdapter(adapter);
			l.setOnItemClickListener(mMessageClickedHandler);
			
		}
		
	}
	
	public void onClickPowrot(View v)
	{
		
		Intent intent = new Intent(context, Activity_ObszarRoboczy.class);
		intent.putExtra("IdUzytkownika", identyfikator);
		startActivity(intent);
		
	}
	
	public void onClickSzukaj(View v)
	{
		
		String nazwaWycieczki=((EditText) findViewById(R.id.etSzukajWycieczkiU)).getText().toString();
		pl.clear();
		for(int i = 0; i < lista_wycieczek.size(); ++i)
		{
			
			if(lista_wycieczek.get(i).pierwsza.toLowerCase().contains(nazwaWycieczki.toLowerCase()))
			{
				
				pl.add(lista_wycieczek.get(i).pierwsza);
				
			}
			
		}
		adapter.notifyDataSetChanged();
		
	}
	
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener()
	{
		
		int pozycja = -1;
		public void onItemClick(AdapterView parent, View v, int position, long id)
		{
			
			for(int i = 0; i < lista_wycieczek.size(); ++i)
			{
				
				if(lista_wycieczek.get(i).pierwsza.equals(pl.get(position)))
				{
					
					pozycja = i;
					break;
					
				}
				
			}
			if(lista_wycieczek.get(pozycja).trzecia.toLowerCase().equals("null"))
			{
				
				AlertDialog.Builder dialogZapiszSie = new AlertDialog.Builder(Activity_Ustawienia.this);
				dialogZapiszSie.setTitle("zapisz sie na wycieczke "+lista_wycieczek.get(position).pierwsza);
				dialogZapiszSie.setPositiveButton("zapisz sie", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						
						List<NameValuePair> parametry = new ArrayList<NameValuePair>();
						parametry.add(new BasicNameValuePair("id_uzytkownika", String.valueOf(identyfikator)));
						parametry.add(new BasicNameValuePair("id_wycieczki", lista_wycieczek.get(pozycja).druga));
						String dane = BazaDanych.wykonaj_skrypt("wycieczka_zapisz_sie.php", parametry);
						if(dane.equals("1"))
						{
							
							Toast.makeText(context, "zapisany", Toast.LENGTH_LONG).show();
							
						}
						dialog.dismiss();
						
					}
					
				});
				dialogZapiszSie.setNegativeButton(getResources().getString(R.string.Wyjdz), 
						new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						
						dialog.cancel();
						
					}
					
				});
				dialogZapiszSie.show();
				
			}
			
		}
		
	};
	
	private class TrzyWartosci
	{
		
		public String pierwsza, druga, trzecia;
		
		public TrzyWartosci(final String pierwsza, final String druga, final String trzecia)
		{
			
			this.pierwsza = pierwsza;
			this.druga = druga;
			this.trzecia = trzecia;
			
		}
		
	}
	
}
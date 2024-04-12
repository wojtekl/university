package com.jwycieczki;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public abstract class BazaDanych
{
	
	protected static String wykonaj_skrypt(final String nazwa, final List<NameValuePair> parametry)
	{
		
		final HttpPost httpPost = new HttpPost("http://jwycieczki.ugu.pl/" + nazwa);
		try
		{
			
			httpPost.setEntity(new UrlEncodedFormEntity(parametry));
			return (new pobierz().execute(httpPost)).get();
			
		}
		
		catch(final Exception exception)
		{
			
			return "!" + exception.getLocalizedMessage();
			
		}
		
	}
	
	private static class pobierz extends AsyncTask<HttpPost, Integer, String>
	{
		
		protected String doInBackground(final HttpPost... httpPost)
		{
			
			final HttpClient httpClient = new DefaultHttpClient();
			try
			{
				
				final HttpResponse httpResponse = httpClient.execute(httpPost[0]);
				final HttpEntity httpEntity = httpResponse.getEntity();
				return filtruj_odpowiedz(httpEntity.getContent());
			
			}
			catch(final Exception exception)
			{
				
				return "!" + exception.getLocalizedMessage();
				
			}
			
		}
		
	}
	
	private static String filtruj_odpowiedz(final InputStream inputStream)
	{
		
		try
		{
			
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-2"), 8);
			final StringBuilder stringBuilder = new StringBuilder();
			String wiersz = bufferedReader.readLine();
			int sekcja = 0; // usunac gdy nie na hostingu
			while(null != wiersz)
			{
				/*
				 * gdy nie na hostingu zamienic kod zawarty pomiedzy
				 * "// <###" i "// ###>" z ponizszym
				 * sb.append(l);
				*/
				// <###
				if(1 == sekcja)
				{
					
					stringBuilder.append(wiersz);
					if(wiersz.contains("</jwycieczki>"))
					{
						sekcja = 0;
						
					}
					
				}
				else
				{
					
					if(wiersz.contains("<jwycieczki>"))
					{
						
						sekcja = 1;
						
					}
					
				}
				// ###>
				wiersz = bufferedReader.readLine();
				
			}
			bufferedReader.close();
			String wynik = stringBuilder.toString();
			wynik = wynik.substring(wynik.lastIndexOf("<jwycieczki>")+1, wynik.indexOf("</jwycieczki>")); // usunac gdy nie na hostingu
			return wynik;
			
		}
		catch(final Exception exception)
		{
			
			return "!" + exception.getLocalizedMessage();
			
		}
		
	}
	
}
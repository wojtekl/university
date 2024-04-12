package com.jwycieczki;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public abstract class ObrazPobierz
{
	
	protected static Bitmap pobierz(String nazwa)
	{
		
		try
		{
			
			URL url = new URL("http://jwycieczki.ugu.pl/galeria/" + nazwa);
			return (new Pobierz()).execute(url).get();
			
		}
		catch(Exception exception)
		{
			
			return null;
			
		}
		
	}
	
	private static class Pobierz extends AsyncTask<URL, Integer, Bitmap>
	{
		
		protected Bitmap doInBackground(URL... url)
		{
			
			try
			{
				
				HttpURLConnection urlConnection = (HttpURLConnection)url[0].openConnection();
				InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				inputStream.close();
				urlConnection.disconnect();
				return bitmap;
				
			}
			catch(Exception exception)
			{
				
				return null;
				
			}
			
		}
		
	}
	
}
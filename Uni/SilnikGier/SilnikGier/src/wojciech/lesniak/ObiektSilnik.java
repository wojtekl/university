package wojciech.lesniak;

import android.content.Context;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ObiektSilnik
{
       
    public ArrayList<TrojkatySilnik[]> obiekt;
    
    public TeksturaSilnik[] tekstury;
    
    private final Context context;
    
    private TrojkatySilnik poza[];
    
    public float predkoscA;
    
    private String tex[];
    
    public void rysuj(final float[] mWidok, final float[] mProjekcja, final OswietlenieSilnikA oswietlenie)
    {
        
        for(int i = 0; i < poza.length; ++i)
        {
            
            poza[i].rysuj(mWidok, mProjekcja, oswietlenie, tekstury[poza[i].nTekstura]);
        
        }
        
    }
    
    public void animuj(final int krok)
    {
        
        final int k = (int)(krok * predkoscA);
        final int r = obiekt.size();
        final int t = k / r;
        poza = obiekt.get(k - r * t);
        
    }
    
    public void przesun(final float x, final float y, final float z)
    {
        
        for(int i = 0; i < poza.length; ++i)
        {

            poza[i].przesun(x, y, z);

        }
        
    }
    
    public void obroc(final float s, final float x, final float y, final float z)
    {
        
        for(int i = 0; i < poza.length; ++i)
        {

            poza[i].obroc(s, x, y, z);

        }
        
    }
    
    public void skaluj(final float x, final float y, final float z)
    {
        
        for(int i = 0; i < poza.length; ++i)
        {

            poza[i].skaluj(x, y, z);

        }
        
    }
    
    public void przywroc()
    {
        
        for(int i = 0; i < poza.length; ++i)
        {

            poza[i].przywroc();

        }
        
    }
    
    public void wczytaj_tekstury(final int minFilter, final int magFilter, final int mipmapy)
    {
        
        tekstury = new TeksturaSilnik[tex.length];
        for(int i = 0; i < tex.length; ++i)
        {
            
            tekstury[i] = new TeksturaSilnik(context, tex[i], minFilter, magFilter, mipmapy);
            
        }
        
    }
    
    public ObiektSilnik(final Context c, final String sciezka)
    {
        
        context = c;
        
        predkoscA = 0.05f;
        
        int texture = 0;
        float ver[], col[], nor[];
        StringTokenizer st;
        String line;
        List<Float> ve = new ArrayList<Float>(), co = new ArrayList<Float>(), no = new ArrayList<Float>();
        List<String> te = new ArrayList<String>();
        List<TrojkatySilnik> triangles = new ArrayList<TrojkatySilnik>();
        obiekt = new ArrayList<TrojkatySilnik[]>();
        
        try
        {
            
            BufferedReader bf = new BufferedReader(new FileReader(sciezka));
            while(null != (line = bf.readLine()))
            {
                
                if(line.equals("ts"))
                {
                    
                    while(!(line = bf.readLine()).equals("tse"))
                    {
                        
                        te.add(line);
                        
                    }
                    
                }
                if(line.equals("p"))
                {
                    
                    triangles = new ArrayList<TrojkatySilnik>();
                    
                }
                if(line.equals("pe"))
                {
                    
                    obiekt.add((new TrojkatySilnik[triangles.size()]));
                    for(int i = 0; i < triangles.size(); ++i)
                    {
                        
                        obiekt.get(obiekt.size()-1)[i] = triangles.get(i);
                        
                    }
                    
                }
                if(line.startsWith("o "))
                {
                    
                    ve = new ArrayList<Float>(); co = new ArrayList<Float>(); no = new ArrayList<Float>();
                    
                }
                if(line.equals("oe"))
                {
                    
                    ver = new float[ve.size()];
                    for(int i = 0; i < ve.size(); ++i)
                    {
                        
                        ver[i] = ve.get(i).floatValue();
                        
                    }
                    col = new float[co.size()];
                    for(int i = 0; i < co.size(); ++i)
                    {
                        
                        col[i] = co.get(i).floatValue();
                        
                    }
                    nor = new float[no.size()];
                    for(int i = 0; i < no.size(); ++i)
                    {
                        
                        nor[i] = no.get(i).floatValue();
                        
                    }
                    triangles.add(new TrojkatySilnik(ver, col, nor, texture));
                    //ve = new ArrayList<Float>(); co = new ArrayList<Float>(); no = new ArrayList<Float>();
                    
                }
                if(line.equals("t"))
                {
                    
                    texture = Integer.parseInt(bf.readLine());
                    
                }
                if(line.equals("v"))
                {
                    
                    while(!(line = bf.readLine()).equals("ve"))
                    {
                        
                        st = new StringTokenizer(line);
                        int l = st.countTokens();
                        if(3 == l)
                        {
                            
                            while(0 < l)
                            {
                                
                                ve.add(Float.valueOf(st.nextToken()));
                                --l;
                                
                            }
                            
                        }
                        
                    }
                    
                }

                if(line.equals("c"))
                {
                    
                    while(!(line = bf.readLine()).equals("ce"))
                    {
                        
                        st = new StringTokenizer(line);
                        int l = st.countTokens();
                        if(4 == l)
                        {
                            
                            while(0 < l)
                            {
                                
                                co.add(Float.valueOf(st.nextToken()));
                                --l;
                                
                            }
                            
                        }
                        
                    }
                    
                }

                if(line.equals("n"))
                {
                    
                    while(!(line = bf.readLine()).equals("ne"))
                    {
                        
                        st = new StringTokenizer(line);
                        int l = st.countTokens();
                        if(3 == l)
                        {
                            
                            while(0 < l)
                            {
                                
                                no.add(Float.valueOf(st.nextToken()));
                                --l;
                                
                            }
                            
                        }
                        
                    }
                    
                }
                
            }
            bf.close();
            String textury = sciezka.substring(0, sciezka.lastIndexOf('/') + 1);
            tex = new String[te.size()];
            for(int i = 0; i < te.size(); ++i)
            {
                
                tex[i] = new StringBuilder(textury).append(te.get(i)).toString();
                
            }
            poza = obiekt.get(0);
            
        }
        catch(final Exception exception)
        {
            
            Toast.makeText(context, exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            
        }
        
    }
    
}

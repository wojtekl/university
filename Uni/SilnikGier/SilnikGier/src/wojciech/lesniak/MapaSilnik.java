package wojciech.lesniak;

import android.content.Context;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MapaSilnik
{
    
    private final Context context;
    
    public TrojkatySilnik[] scena;
    
    public SektorSilnik[] sektory;
    
    public TeksturaSilnik[] tekstury;
    
    private List<Integer> teksturyN;
    
    public int bs;
    
    private String tex[];
    
    public void rysuj(final float[] mWidok, final float[] mProjekcja, final OswietlenieSilnikA oswietlenie)
    {
        
        for(int i = 0; i < scena.length; ++i)
        {
            
            scena[i].rysuj(mWidok, mProjekcja, oswietlenie, tekstury[teksturyN.get(i)]);
        
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
    
    public MapaSilnik(final Context c, final String sciezka)
    {
        
        context = c;
        
        bs = 0;
        
        float ver[], col[], nor[];
        String line;
        StringTokenizer st;
        int texture = 0;
        final String textury = sciezka.substring(0, sciezka.lastIndexOf('/') + 1);
        List<Float> ve = new ArrayList<Float>(), co = new ArrayList<Float>(), no = new ArrayList<Float>();
        List<String> te = new ArrayList<String>();
        List<SektorSilnik> se = new ArrayList<SektorSilnik>();
        teksturyN = new ArrayList<Integer>();
        List<TrojkatySilnik> triangles=new ArrayList<TrojkatySilnik>();
        
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
                    triangles.add(new TrojkatySilnik(ver, col, nor, 0));
                    teksturyN.add(texture);
                    
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
                if(line.equals("t"))
                {
                    
                    texture = Integer.parseInt(bf.readLine());
                    
                }
                if(line.equals("s"))
                {
                    
                    float sek1[] = null;
                    int sek2[];
                    while(!(line = bf.readLine()).equals("se"))
                    {
                        
                        st = new StringTokenizer(line);
                        int l = st.countTokens();
                        String t;
                        if(5 == l)
                        {
                            
                            t = st.nextToken();
                            --l;
                            if(t.equals("g"))
                            {
                                
                                sek1 = new float[4];
                                while(0 < l){
                                    sek1[4-l] = Float.parseFloat(st.nextToken());
                                    --l;
                                }
                            }
                            if(t.equals("p") && (null != sek1))
                            {
                                
                                sek2 = new int[4];
                                while(0 < l)
                                {
                                    
                                    sek2[4-l] = Integer.parseInt(st.nextToken());
                                    --l;
                                    
                                }
                                se.add(new SektorSilnik(sek1[0], sek1[1], sek1[2], 
                                        sek1[3], sek2[0], sek2[1], sek2[2], sek2[3]));
                                sek1 = null;
                                
                            }
                            
                        }
                        
                    }
                    
                }
                
            }
            bf.close();
            scena = new TrojkatySilnik[triangles.size()];
            for(int i = 0; i < triangles.size(); ++i)
            {
                
                scena[i] = triangles.get(i);
                
            }
            tex = new String[te.size()];
            for(int i = 0; i < te.size(); ++i)
            {
                
                tex[i] = new StringBuilder(textury).append(te.get(i)).toString();
                
            }
            sektory = new SektorSilnik[se.size()];
            for(int i = 0; i < se.size(); ++i)
            {
                
                sektory[i] = se.get(i);
                
            }
            
        }
        catch(final Exception exception)
        {
            
            Toast.makeText(context, exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            
        }
        
    }
    
}

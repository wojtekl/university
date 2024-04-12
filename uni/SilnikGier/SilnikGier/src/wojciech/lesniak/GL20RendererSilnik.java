package wojciech.lesniak;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GL20RendererSilnik implements GLSurfaceView.Renderer
{
    
    private final Context context;
    private final Activity activity;
    
    public int czas;
    
    private float ratio;
    private float blisko, daleko;
    public float bx, by, bz, wx, wy, wz, sx, sy, sz;
    private float[] mWidok, mProjekcja;
    
    private OswietlenieRozproszoneSilnik oswietlenieR;
    private OswietlenieOtaczajaceSilnik oswietlenieO;
    
    private MapaSilnik mapa;
    
    private List<PostacGra> potworyP;
    private ObiektSilnik potworO;
    
    private PrzedmiotGra portalP;
    private ObiektSilnik portalO;
    
    private PrzedmiotGra krysztalP;
    private ObiektSilnik krysztalO;
    
    private ArrayList<PociskGra> pociskiP;
    private ObiektSilnik pociskO;
    
    private int zycie, zycieK, zycieP;
    
    private final Random random;
    
    private final float rad2deg;
    
    private SterowanieGra sterowanie;
    
    public int stop;
    
    private float a1;
    
    public GL20RendererSilnik(final Context c, final Activity a, final String sciezka)
    {
        
        context = c;
        activity = a;
        
        czas = 0;
        
        blisko = 1.0f; daleko = 50.0f;
        bx = 0.0f; by = 0.0f; bz = -0.5f;
        sx = 0.0f; sy = 1.0f; sz = 0.0f;
        mWidok = new float[16]; mProjekcja = new float[16];
        
        mapa = new MapaSilnik(context, sciezka);
        
        potworO = new ObiektSilnik(context, "/mnt/sdcard/Download/postac.txt");
        potworyP = new ArrayList<PostacGra>();
        potworyP.add(new PostacGra(4.0f, 0.0f, -4.0f));
        potworyP.add(new PostacGra(-4.0f, 0.0f, -4.0f));
        
        portalO = new ObiektSilnik(context, "/mnt/sdcard/Download/portal.txt");
        portalP = new PrzedmiotGra(12.0f, 0.0f, -12.0f);
        
        krysztalO = new ObiektSilnik(context, "/mnt/sdcard/Download/krysztal.txt");
        krysztalP = new PrzedmiotGra(4.0f, 0.0f, 4.0f);
        
        pociskO = new ObiektSilnik(context, "/mnt/sdcard/Download/pocisk.txt");
        pociskiP = new ArrayList<PociskGra>();
        
        zycie = 1; zycieK = 0; zycieP = 0;
        
        random = new Random();
        
        rad2deg = 180.0f / 3.1415f;
        
        sterowanie = new SterowanieGra();
       
        stop = 0;
        
    }
    
    @Override
    public void onSurfaceCreated(final GL10 glUnused, final EGLConfig config)
    {
        
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        
        Matrix.setLookAtM(mWidok, 0, bx, by, bz, wx, wy, wz, sx, sy, sz);
        
        oswietlenieR=new OswietlenieRozproszoneSilnik();
        oswietlenieR.przesun(0.0f, 4.0f, 0.0f);
        oswietlenieR.przygotuj(mWidok);
        
        oswietlenieO=new OswietlenieOtaczajaceSilnik();
        
        mapa.wczytaj_tekstury(GLES20.GL_LINEAR_MIPMAP_NEAREST, GLES20.GL_NEAREST, 1);
        
        potworO.wczytaj_tekstury(GLES20.GL_NEAREST, GLES20.GL_NEAREST, 1);
        
        portalO.wczytaj_tekstury(GLES20.GL_NEAREST, GLES20.GL_NEAREST, 1);
        
        krysztalO.wczytaj_tekstury(GLES20.GL_NEAREST, GLES20.GL_NEAREST, 1);
        
        pociskO.wczytaj_tekstury(GLES20.GL_NEAREST, GLES20.GL_NEAREST, 1);
        
        sterowanie.tekstury(context);
        
    }
    
    @Override
    public void onSurfaceChanged(final GL10 glUnused, final int width, final int height)
    {
        
        GLES20.glViewport(0, 0, width, height);
        ratio = ((float)width) / ((float)height);
        Matrix.frustumM(mProjekcja, 0, -ratio, ratio, -1.0f, 1.0f, blisko, daleko);
        sterowanie.dopasuj(ratio, height);
        
    }

    @Override
    public void onDrawFrame(final GL10 glUnused)
    {
        
        GLES20.glClearColor(0.8f, 0.8f, 0.8f, 0.8f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.setLookAtM(mWidok, 0, bx, by, bz, wx, wy, wz, sx, sy, sz);
        
        mapa.rysuj(mWidok, mProjekcja, oswietlenieR);
        
        PostacGra potwor;
        for(int i = 0; i < potworyP.size(); ++i)
        {
            
            potwor = potworyP.get(i);
            potworO.animuj(czas);
            potworO.przywroc();
            potworO.przesun(potwor.x, -1, potwor.z);
            potworO.obroc(0.1f * a1, 0.0f, 1.0f, 0.0f);
            potworO.rysuj(mWidok, mProjekcja, oswietlenieO);
            
        }
        
        PociskGra pocisk;
        for(int i = 0; i < pociskiP.size(); ++i)
        {
            
            pocisk = pociskiP.get(i);
            pociskO.przywroc();
            pociskO.przesun(pocisk.cx, pocisk.cy, pocisk.cz);
            pociskO.rysuj(mWidok, mProjekcja, oswietlenieO);
            
        }
        
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        
        krysztalO.przywroc();
        krysztalO.przesun(krysztalP.x, krysztalP.y, krysztalP.z);
        krysztalO.obroc(czas % 360, 0.0f, 1.0f, 0.0f);
        krysztalO.rysuj(mWidok, mProjekcja, oswietlenieO);
        
        portalO.przywroc();
        portalO.przesun(portalP.x, portalP.y, portalP.z);
        portalO.rysuj(mWidok, mProjekcja, oswietlenieO);
        
        sterowanie.rysuj(mProjekcja, oswietlenieO);
        
        GLES20.glDisable(GLES20.GL_BLEND);
    }
    
    public void zdarzenia(final float sinalpha, final float cosalpha, int k)
    {
        
        float pr = 0.05f;
        float ks = sinalpha * pr;
        float kc = cosalpha * pr;
        
        if(0 < mapa.sektory.length)
        {
            
            float kr = ratio * (float)k;
            float tx = bx + ks * (float)k + kr;
            float tz = bz + kc * (float)k + kr;
            
            int sektor = mapa.sektory[mapa.bs].sprawdz(tx, tz);
            if(-1 < sektor)
            {
                
                mapa.bs = sektor;
                
            }
            else
            {
                
                k = 0;
                
            }
            
        
        }
        
        bz = bz + kc * (float)k;
        bx = bx + ks * (float)k;
        wx = bx + sinalpha;
        wz = bz + cosalpha;
        
        przedmioty(sinalpha, cosalpha);
        
        potwory();
        
        postrzaly();
        
        a1 = -90 + (float)Math.atan(bz / bx) * rad2deg;
    
    }
    
    private void przedmioty(final float sinalpha, final float cosalpha)
    {
        
        if(1 == portalP.sprawdz(bx, 0.0f, bz))
        {
            
            bx = 0.0f;
            bz = 0.0f;
            wx = bx + sinalpha;
            wz = bz + cosalpha;
            Toast.makeText(context, "portal", Toast.LENGTH_LONG).show();
            
        }
        
        if(1 == krysztalP.sprawdz(bx, 0.0f, bz))
        {
            
            if(1 != zycieK)
            {
                
                ++zycie;
                zycieK = 1;
                Toast.makeText(context, "zycie", Toast.LENGTH_LONG).show();
                
            }
            
        }
        else
        {
            
            zycieK=0;
            
        }
        
    }
    
    private void potwory()
    {
        
        int pl = 0;
        
        for(int i = 0; i < potworyP.size(); ++i)
        {
            
            potworyP.get(i).ruch(bx, 0.0f, bz);
            
            if(1 == potworyP.get(i).sprawdz(bx, 0.0f, bz))
            {
                
                if(0 == zycieP)
                {
                    
                    --zycie;
                    //Toast.makeText(context, String.valueOf(i), Toast.LENGTH_LONG).show();
                    if(1 > zycie)
                    {
                        
                        stop = 1;
                        activity.finish();
                        Toast.makeText(context, "potwor", Toast.LENGTH_LONG).show();
                        
                    }
                    
                    ++zycieP;
                
                }
                
                ++pl;

            }
            
        }
        
        if(0 == pl)
        {
            
            zycieP = 0;
            
        }
        
    }
    
    public void strzal(final float kx, final float ky, final float kz)
    {
        
        pociskiP.add(new PociskGra(wx, wy, wz, kx, ky, kz, 15.0f));
        
    }
    
    private void postrzaly()
    {
        
        PociskGra pocisk;
        PostacGra potwor;
        
        for(int i = 0; i < pociskiP.size(); ++i)
        {
            
            pocisk = pociskiP.get(i);
            pocisk.lot();
            
            for(int j = 0; j < potworyP.size(); ++j)
            {
                
                potwor = potworyP.get(j);
                
                if(1 == pocisk.sprawdz(potwor.x, potwor.y, potwor.z))
                {
                    
                    pociskiP.remove(i);
                    --potwor.zycie;
                    if(1 > potwor.zycie)
                    {
                        
                        potworyP.remove(j);
                        
                    }
                    
                    if(potworyP.isEmpty())
                    {
                        
                        Toast.makeText(context, "Wygrałeś!!! :)", Toast.LENGTH_LONG).show();
                        stop = 1;
                        activity.finish();
                        
                    }
                    
                }
                
            }
            
        }
        
    }
    
}

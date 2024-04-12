package wojciech.lesniak;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class SterowanieGra
{
    
    private final float kolory[], normalne[];
    
    private TrojkatySilnik celownik, bron, mysz, wsad;
    
    public TeksturaSilnik celownikT, bronT, myszT, wsadT;
    
    public SterowanieGra()
    {
        
        kolory = new float[]
        {
            
            1.0f, 1.0f, 1.0f, 1.0f, 
            1.0f, 1.0f, 1.0f, 1.0f,
            
        };
        normalne = new float[]
        {
            
            0.0f, 0.0f, 1.0f, 
            0.0f, 0.0f, 1.0f, 
            0.0f, 0.0f, 1.0f, 
            0.0f, 0.0f, 1.0f, 
            0.0f, 0.0f, 1.0f, 
            0.0f, 0.0f, 1.0f,
            
        };
        
        final float celownikV[] = new float[]
        {
            
            0.05f, -0.05f, -1.0f, 
            0.05f, 0.05f, -1.0f, 
            -0.05f, 0.05f, -1.0f,
            -0.05f, -0.05f, -1.0f, 
            0.05f, -0.05f, -1.0f, 
            -0.05f, 0.05f, -1.0f,
            
        };
        celownik = new TrojkatySilnik(celownikV, kolory, normalne, 0);
        celownik.przywroc();
        
        float bronV[] = new float[]
        {
            
            0.2f, -1.0f, -1.0f, 
            0.2f, -0.6f, -1.0f, 
            -0.2f, -0.6f, -1.0f, 
            -0.2f, -1.0f, -1.0f, 
            0.2f, -1.0f, -1.0f,
            -0.2f, -0.6f, -1.0f,
            
        };
        bron = new TrojkatySilnik(bronV, kolory, normalne, 0);
        bron.przywroc();
        
    }
    
    public void tekstury(final Context context)
    {
        
        celownikT = new TeksturaSilnik(context, R.drawable.celownik, GLES20.GL_NEAREST, GLES20.GL_NEAREST, 1);
        bronT = new TeksturaSilnik(context, R.drawable.bron, GLES20.GL_NEAREST, GLES20.GL_NEAREST, 1);
        wsadT = new TeksturaSilnik(context, R.drawable.wsad, GLES20.GL_NEAREST, GLES20.GL_NEAREST, 1);
        myszT = new TeksturaSilnik(context, R.drawable.mysz, GLES20.GL_NEAREST, GLES20.GL_NEAREST, 1);
        
    }
    
    public void dopasuj(final float ratio, final float height)
    {
        
        final float s = 210.0f / height;
        float wsadV[] = new float[]
        {
            
            -ratio + s, -s, -1.0f, 
            -ratio + s, 0.0f, -1.0f, 
            -ratio, 0.0f, -1.0f, 
            -ratio, -s, -1.0f, 
            -ratio + s, -s, -1.0f, 
            -ratio, 0.0f, -1.0f,
            
        };
        wsad = new TrojkatySilnik(wsadV, kolory, normalne, 0);
        wsad.przywroc();
        
        final float myszV[] = new float[]
        {
            
            ratio, -s, -1.0f, 
            ratio, 0.0f, -1.0f, 
            ratio - s, 0.0f, -1.0f, 
            ratio - s, -s, -1.0f, 
            ratio, -s, -1.0f, 
            ratio - s, 0.0f, -1.0f,
            
        };
        mysz = new TrojkatySilnik(myszV, kolory, normalne, 0);
        mysz.przywroc();
        
    }
    
    public void rysuj(final float[] mProjekcja, final OswietlenieSilnikA oswietlenie)
    {
        
        float mWidok[]=new float[16];
        Matrix.setIdentityM(mWidok, 0);
        
        celownik.rysuj(mWidok, mProjekcja, oswietlenie, celownikT);
        
        bron.rysuj(mWidok, mProjekcja, oswietlenie, bronT);
        
        wsad.rysuj(mWidok, mProjekcja, oswietlenie, wsadT);
        
        mysz.rysuj(mWidok, mProjekcja, oswietlenie, myszT);
        
    }
    
}

package wojciech.lesniak;

import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TrojkatySilnik
{
    
    public final int nTekstura;
    
    private float[] mModel;
    
    private final FloatBuffer bWierzcholki;
    private final FloatBuffer bKolory;
    private final FloatBuffer bNormalne;
    private final FloatBuffer bTeksturaW;
    
    private final int lWierzcholki;
    
    public TrojkatySilnik(final float wspolrzedne[], final float kolory[], final float normalne[], final int numerTekstura)
    {
        
        nTekstura=numerTekstura;
        
        mModel = new float[16];
        Matrix.setIdentityM(mModel, 0);
        
        lWierzcholki=wspolrzedne.length/3;
        ByteBuffer bb = ByteBuffer.allocateDirect(wspolrzedne.length*4);
        bb.order(ByteOrder.nativeOrder());
        bWierzcholki = bb.asFloatBuffer();
        bWierzcholki.put(wspolrzedne);
        bWierzcholki.position(0);
        
        final int koloryCount=lWierzcholki*4;
        ByteBuffer bb3=ByteBuffer.allocateDirect(koloryCount*4);
        bb3.order(ByteOrder.nativeOrder());
        bKolory=bb3.asFloatBuffer();
        float f2[]=new float[koloryCount];
        int k=0;
        for(int i=0; i<koloryCount; ++i){
            if(k>=kolory.length){
                k=0;
            }
            f2[i]=kolory[k];
            ++k;
        }
        bKolory.put(f2);
        bKolory.position(0);
        
        ByteBuffer bb2=ByteBuffer.allocateDirect(normalne.length*4);
        bb2.order(ByteOrder.nativeOrder());
        bNormalne=bb2.asFloatBuffer();
        bNormalne.put(normalne);
        bNormalne.position(0);
        
        int r=lWierzcholki*2;
        ByteBuffer bb1 = ByteBuffer.allocateDirect(r*4);
        bb1.order(ByteOrder.nativeOrder());
        bTeksturaW = bb1.asFloatBuffer();
        float f[]=new float[]
        {
            1.0f, 1.0f, 
            1.0f, 0.0f, 
            0.0f, 0.0f, 
            0.0f, 1.0f, 
            1.0f, 1.0f, 
            0.0f, 0.0f
        };
        float f1[]=new float[r];
        int j=0;
        for(int i=0; i<r; ++i){
            if(j>11){
                j=0;
            }
            f1[i]=f[j];
            ++j;
        }
        bTeksturaW.put(f1);
        bTeksturaW.position(0);
        
    }
    
    public void rysuj(final float[] mWidok, final float[] mProjekcja, final OswietlenieSilnikA oswietlenie, final TeksturaSilnik tekstura)
    {
        
        GLES20.glUseProgram(oswietlenie.shadery.program);
        
        float[] mMWP=new float[16];
        
        Matrix.multiplyMM(mMWP, 0, mWidok, 0, mModel, 0);
        int uMMW = GLES20.glGetUniformLocation(oswietlenie.shadery.program, "uMacierzMW");
        GLES20.glUniformMatrix4fv(uMMW, 1, false, mMWP, 0);                
        
        Matrix.multiplyMM(mMWP, 0, mProjekcja, 0, mMWP, 0);
        int uMMWP = GLES20.glGetUniformLocation(oswietlenie.shadery.program, "uMacierzMWP");
        GLES20.glUniformMatrix4fv(uMMWP, 1, false, mMWP, 0);
        
        int uWspolrzedne = GLES20.glGetAttribLocation(oswietlenie.shadery.program, "aWierzcholki");
        //bWspolrzedne.position(0);		
        GLES20.glVertexAttribPointer(uWspolrzedne, 3, GLES20.GL_FLOAT, false, 0, bWierzcholki);        
        GLES20.glEnableVertexAttribArray(uWspolrzedne);
        
        int uKolory = GLES20.glGetAttribLocation(oswietlenie.shadery.program, "aKolory");
        //bKolory.position(0);		
        GLES20.glVertexAttribPointer(uKolory, 4, GLES20.GL_FLOAT, false, 0, bKolory);        
        GLES20.glEnableVertexAttribArray(uKolory);
        //GLES20.glUniform4fv(mColorHandle, 4, kolory, 0);
        
        int uNormalne = GLES20.glGetAttribLocation(oswietlenie.shadery.program, "aNormalne");
        //bNormalne.position(0);
        GLES20.glVertexAttribPointer(uNormalne, 3, GLES20.GL_FLOAT, false, 0, bNormalne);
        GLES20.glEnableVertexAttribArray(uNormalne);
        
        int uWspolrzedneTekstury = GLES20.glGetAttribLocation(oswietlenie.shadery.program, "aTeksturaW");
        int uTekstura = GLES20.glGetUniformLocation(oswietlenie.shadery.program, "uTekstura");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tekstura.uchwyt);
        GLES20.glUniform1i(uTekstura, 0);
        //bTeksturaW.position(0);
        GLES20.glVertexAttribPointer(uWspolrzedneTekstury, 2, GLES20.GL_FLOAT, false, 0, bTeksturaW);
        GLES20.glEnableVertexAttribArray(uWspolrzedneTekstury);
        
        oswietlenie.uzyj();
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, lWierzcholki);
        
    }
    
    public void przesun(final float x, final float y, final float z)
    {
        
        Matrix.translateM(mModel, 0, x, y, z);
        
    }
    
    public void obroc(final float s, final float x, final float y, final float z)
    {
        
        Matrix.rotateM(mModel, 0, s, x, y, z);
        
    }
    
    public void skaluj(final float x, final float y, final float z)
    {
        
        Matrix.scaleM(mModel, 0, x, y, z);
        
    }
    
    public void przywroc()
    {
        
        Matrix.setIdentityM(mModel, 0);
        
    }
    
}

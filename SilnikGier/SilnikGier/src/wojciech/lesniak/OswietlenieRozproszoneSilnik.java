package wojciech.lesniak;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class OswietlenieRozproszoneSilnik extends OswietlenieSilnikA
{
    
    private final float[] mModel=new float[16];
    private final float[] posInModelSpace=new float[]{0.0f, 0.0f, 0.0f, 1.0f};
    private final float[] posInWorldSpace=new float[4];
    private final float[] posInEyeSpace=new float[4];
    
    public OswietlenieRozproszoneSilnik()
    {
        
        shadery=new ShaderySilnik(vertexShader, fragmentShader);
        
        Matrix.setIdentityM(mModel, 0);
        
    }
    
    public void przygotuj(final float[] mWidok)
    {
        
        Matrix.multiplyMV(posInWorldSpace, 0, mModel, 0, posInModelSpace, 0);
        Matrix.multiplyMV(posInEyeSpace, 0, mWidok, 0, posInWorldSpace, 0);
        
    }
    
    public void uzyj()
    {
        
        int mLightPosHandle = GLES20.glGetUniformLocation(shadery.program, "uOswietlenieW");
        GLES20.glUniform3f(mLightPosHandle, posInEyeSpace[0], posInEyeSpace[1], posInEyeSpace[2]);
        
    }
    
    private final String vertexShader = 
            "uniform mat4 uMacierzMW;" 
            + "uniform mat4 uMacierzMWP;" 
            
            + "attribute vec4 aWierzcholki;" 
            + "attribute vec4 aKolory;" 
            + "attribute vec3 aNormalne;" 
            + "attribute vec2 aTeksturaW;" 
            
            + "varying vec3 vWierzcholki;" 
            + "varying vec4 vKolory;" 
            + "varying vec3 vNormalne;" 
            + "varying vec2 vTeksturaW;" 
            
            + "void main()" 
            + "{" 
            
            + "    vWierzcholki = vec3(uMacierzMW * aWierzcholki);" 
            + "    vKolory = aKolory;" 
            + "    vNormalne = vec3(uMacierzMW * vec4(aNormalne, 0.0));" 
            + "    vTeksturaW = aTeksturaW;" 
            + "    gl_Position = uMacierzMWP * aWierzcholki;" 
            
            + "}";
    
    private final String fragmentShader = 
            "precision mediump float;" 
            
            + "uniform vec3 uOswietlenieW;"  
            
            + "varying vec3 vWierzcholki;" 
            + "varying vec4 vKolory;" 
            + "varying vec3 vNormalne;" 
            + "varying vec2 vTeksturaW;" 
            
            + "uniform sampler2D uTekstura;" 
            
            + "void main()" 
            + "{" 
            
            + "    vec3 wektorO = uOswietlenieW - vWierzcholki;" 
            + "    float odleglosc = length(wektorO);" 
            //+ "    vec3 wektorON = normalize(wektorO);" 
            //+ "    float rozproszenie = max(dot(vNormalne, wektorON), 0.1);" 
            //+ "    rozproszenie = rozproszenie / (odleglosc * odleglosc);" 
            //+ "    gl_FragColor = texture2D(uTekstura, vTeksturaW) * vKolory * rozproszenie;" 
            + "    gl_FragColor = texture2D(uTekstura, vTeksturaW) * vKolory * (max(dot(vNormalne, normalize(wektorO)), 0.0) / (0.2 * odleglosc * odleglosc) + 0.8);" 
            
            + "}";
    
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

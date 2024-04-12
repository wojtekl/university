package wojciech.lesniak;

import android.opengl.GLES20;

public class OswietlenieOtaczajaceSilnik extends OswietlenieSilnikA
{
    
    private final float[] kolor;
    
    public OswietlenieOtaczajaceSilnik()
    {
        
        kolor=new float[]{0.8f, 0.8f, 0.8f, 0.8f};
        
        shadery=new ShaderySilnik(vertexShader, fragmentShader);
        
    }

    @Override
    public void uzyj()
    {
        
        int mLightColorHandle = GLES20.glGetUniformLocation(shadery.program, "uOswietlenieK");
        GLES20.glUniform4f(mLightColorHandle, kolor[0], kolor[1], kolor[2], kolor[3]);
        
    }
    
    private final String vertexShader = 
            "uniform mat4 uMacierzMWP;" 
            
            + "attribute vec4 aWierzcholki;" 
            + "attribute vec4 aKolory;" 
            + "attribute vec3 aNormalne;"
            + "attribute vec2 aTeksturaW;" 
            
            + "varying vec4 vKolory;" 
            + "varying vec2 vTeksturaW;" 
            
            + "void main()" 
            + "{" 
            
            + "    vKolory = aKolory;" 
            + "    vTeksturaW = aTeksturaW;" 
            + "    gl_Position = uMacierzMWP * aWierzcholki;" 
            
            + "}";
    
    private final String fragmentShader = 
            "precision mediump float;" 
            
            + "uniform vec4 uOswietlenieK;"  
            
            + "varying vec4 vKolory;" 
            + "varying vec2 vTeksturaW;" 
            
            + "uniform sampler2D uTekstura;" 
            
            + "void main()" 
            + "{" 
            
            + "    gl_FragColor = (vKolory * uOswietlenieK * texture2D(uTekstura, vTeksturaW));" 
            
            + "}";
    
}

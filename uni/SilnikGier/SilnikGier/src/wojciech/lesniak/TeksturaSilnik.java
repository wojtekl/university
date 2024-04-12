/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wojciech.lesniak;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 *
 * @author user
 */
public class TeksturaSilnik
{
    
    public int uchwyt;
    
    public TeksturaSilnik(final Context context, final String sciezka, final int minFilter, final int magFilter, final int mipmapy)
    {
        
        int[] uchwytPomocniczy=new int[1];
        GLES20.glGenTextures(1, uchwytPomocniczy, 0);
        if (0!=uchwytPomocniczy[0]){
            final BitmapFactory.Options options=new BitmapFactory.Options();
            options.inScaled=false;
            final Bitmap bitmap=BitmapFactory.decodeFile(sciezka, options);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, uchwytPomocniczy[0]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }
        uchwyt=uchwytPomocniczy[0];
        if(-1<mipmapy)
        {
            
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
            
        }
        
    }
    
    public TeksturaSilnik(final Context context, final int resourceId, final int minFilter, final int magFilter, final int mipmapy)
    {
        
        int[] uchwytPomocniczy=new int[1];
        GLES20.glGenTextures(1, uchwytPomocniczy, 0);
        if (0!=uchwytPomocniczy[0]){
            final BitmapFactory.Options options=new BitmapFactory.Options();
            options.inScaled=false;
            final Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(), resourceId, options);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, uchwytPomocniczy[0]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }
        uchwyt=uchwytPomocniczy[0];
        if(-1<mipmapy)
        {
            
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
            
        }
        
    }
    
}

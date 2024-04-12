/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wojciech.lesniak;

import android.opengl.GLES20;

/**
 *
 * @author user
 */
public class ShaderySilnik
{
    private final int vertex;
    private final int fragment;
    public final int program;
    
    public ShaderySilnik(final String vertexShaderCode, final String fragmentShaderCode)
    {
        
        vertex = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertex, vertexShaderCode);
        GLES20.glCompileShader(vertex);
        
        fragment = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragment, fragmentShaderCode);
        GLES20.glCompileShader(fragment);
        
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertex);
        GLES20.glAttachShader(program, fragment);
        GLES20.glLinkProgram(program);
        
    }
    
}

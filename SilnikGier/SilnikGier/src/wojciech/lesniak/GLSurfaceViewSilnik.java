package wojciech.lesniak;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.view.MotionEvent;

public class GLSurfaceViewSilnik extends GLSurfaceView
{
    
    private float maxx, maxy2;
    private float dotyk_x, dotyk_y;
    
    private float x, y, xt, yt;
    private float alpha, widok, sinalpha, cosalpha;
    
    private int pointer_index, pointer_mysz, pointer_wsad;
    private int kierunek_wsad;
    private float przyspieszenie_mysz;
    
    private GL20RendererSilnik renderer;
    private Handler handler;
    private Runnable runnable;
    
    public GLSurfaceViewSilnik(final Context c, final Activity a, final String sciezka)
    {
        
        super(c);
        
        if(0x20000<=((ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().reqGlEsVersion)
        {
            
            this.setEGLContextClientVersion(2);
            renderer=new GL20RendererSilnik(c, a, sciezka);
            
            alpha=3.1415f; widok=0.0f;
            sinalpha=(float)Math.sin(alpha); cosalpha=(float)Math.cos(alpha);
            renderer.wx=renderer.bx+sinalpha;
            renderer.wy=renderer.by+widok;
            renderer.wz=renderer.bz+cosalpha;
            pointer_mysz=-1; pointer_wsad=-1; kierunek_wsad=0;
            przyspieszenie_mysz=0.005f;
            
            runnable=new Runnable(){
                public void run(){
                    if(0 == renderer.stop)
                    {
                        
                        handler.postDelayed(this, 20);
                        
                    }
                    renderer.zdarzenia(sinalpha, cosalpha, kierunek_wsad);
                    requestRender();
                    renderer.czas=renderer.czas+1;
                }
            };
            handler=new Handler();
            handler.postDelayed(runnable, 20);
            setRenderer(renderer);
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent m)
    {
        maxx=(float)getWidth(); maxy2=(float)getHeight()/2.0f;
        pointer_index=m.getActionIndex();
        
        switch(m.getActionMasked())
        {
            
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                dotyk_x=m.getX(pointer_index); dotyk_y=m.getY(pointer_index);
                if(dotyk_y > (maxy2))
                {
                    
                    if(115 > dotyk_x)
                    {
                        
                        if(dotyk_y < (maxy2 + 35))
                        {
                            
                            if(35 > dotyk_x)
                            {
                                
                            }
                            else if(70 < dotyk_x)
                            {
                                
                                renderer.strzal(sinalpha, 0.0f, cosalpha);
                                
                            }
                            else 
                            {
                                
                                kierunek_wsad = 1;
                                pointer_wsad = m.getPointerId(pointer_index);
                                
                            }
                            
                        } else if(dotyk_y > (maxy2 + 70))
                        {
                            
                            if(35 > dotyk_x)
                            {
                                
                            }
                            else if(70 < dotyk_x)
                            {
                                
                            }
                            else
                            {
                                
                            }
                            
                        }
                        else
                        {
                            
                            if(35 > dotyk_x)
                            {
                                
                            }
                            else if(70 < dotyk_x)
                            {
                                
                            }
                            else
                            {
                                
                                kierunek_wsad=-1;
                                pointer_wsad=m.getPointerId(pointer_index);
                                
                            }
                            
                        }
                        
                    }
                    else if(dotyk_x > (maxx - 105))
                    {
                        
                        if(dotyk_y < maxy2 + 105)
                        {
                            
                            xt=dotyk_x; yt=dotyk_y;
                            pointer_mysz=m.getPointerId(pointer_index);
                            
                        }
                        
                    }
                    
                }
                
                return true;
                
            case MotionEvent.ACTION_MOVE:
                if(m.getPointerId(pointer_index) == pointer_mysz)
                {
                    
                    dotyk_x=m.getX(pointer_index); dotyk_y=m.getY(pointer_index);
                    x=dotyk_x-xt; y=yt-dotyk_y;
                    alpha=alpha-x*przyspieszenie_mysz;
                    widok=widok+y*przyspieszenie_mysz;
                    sinalpha=(float)Math.sin(alpha);
                    cosalpha=(float)Math.cos(alpha);
                    if(1.0f < widok)
                    {
                        
                        widok=1.0f;
                        
                    }
                    else if(-1.0f > widok)
                    {
                        
                        widok=-1.0f;
                        
                    }
                    renderer.wx=renderer.bx+sinalpha;
                    renderer.wz=renderer.bz+cosalpha;
                    renderer.wy=renderer.by+widok;
                    requestRender();
                    xt=dotyk_x; yt=dotyk_y;
                    
                }
                return true;
                
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                
                if(m.getPointerId(pointer_index) == pointer_mysz)
                {
                    
                    pointer_mysz=-1;
                    
                }
                else if(m.getPointerId(pointer_index) == pointer_wsad)
                {
                    
                    kierunek_wsad = 0;
                    pointer_wsad = -1;
                    
                }
                return true;
                
            default:
                
                return super.onTouchEvent(m);
                
        }
        
    }
    
}

package wojciech.lesniak;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

public class ActivitySilnik extends Activity
{
    
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(new GLSurfaceViewSilnik(this, this, "/mnt/sdcard/Download/mapa.txt"));
        
    }
    
}

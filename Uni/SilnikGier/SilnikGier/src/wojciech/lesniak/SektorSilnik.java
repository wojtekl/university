package wojciech.lesniak;

public class SektorSilnik
{
    
    private final float x1, y1, x2, y2;
    
    private final int nx1, ny1, nx2, ny2;
    
    public int sprawdz(final float tx, final float tz)
    {
        
        if(x1 > tx)
        {
            
            if(-1 < nx1)
            {
                
                return nx1;
                
            }
            else
            {
                
                return -1;
                
            }
            
        }
        if(x2 < tx)
        {
            
            if(-1 < nx2)
            {
                
                return nx2;
                
            }
            else
            {
                
                return -1;
                
            }
            
        }
        if(y1 > tz)
        {
            
            if(-1 < ny1)
            {
                
                return ny1;
            
            } else
            {
                
                return -1;
            
            }
        
        }
        if(y2 < tz)
        {
            
            if(-1 < ny2)
            {
                
                return ny2;
            
            }
            else
            {
                
                return -1;
            
            }
        
        }
        
        return 0;
        
    }
    
    public SektorSilnik()
    {
        
        x1=0.0f; nx1=-1;
        y1=0.0f; ny1=-1;
        x2=0.0f; nx2=-1;
        y2=0.0f; ny2=-1;
        
    }
    
    public SektorSilnik(final float x1p, final float y1p, final float x2p, final float y2p, 
            final int nx1p, final int ny1p, final int nx2p, final int ny2p)
    {
        
        x1=x1p; nx1=nx1p;
        y1=y1p; ny1=ny1p;
        x2=x2p; nx2=nx2p;
        y2=y2p; ny2=ny2p;
        
    }
    
}

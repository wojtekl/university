package wojciech.lesniak;

public class PostacGra
{
    
    public float x, y, z;
    public float p;
    public int zycie;
    
    public PostacGra(final float xp, final float yp, final float zp)
    {
        
        x = xp;
        y = yp;
        z = zp;
        p = 0.001f;
        zycie = 2;
        
    }
    
    public int sprawdz(final float xp, final float yp, final float zp)
    {
        
        float dx = xp- x;
        dx = dx * dx;
        float dy = yp - y;
        dy = dy * dy;
        float dz = zp - z;
        dz = dz * dz;
        
        if((0.4f > dx) & /*(0.4f > dy) &&*/ (0.4f > dz))
        {
            
            return 1;
            
        }
        
        return 0;
        
    }
    
    public void ruch(final float xp, final float yp, final float zp)
    {
        
        x = x + (xp - x) * p;
        y = y + (yp - y) * p;
        z = z + (zp - z) * p;
        
    }
    
}

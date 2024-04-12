package wojciech.lesniak;

public class PrzedmiotGra
{
    
    public float x, y, z;
    
    public PrzedmiotGra(final float xp, final float yp, final float zp)
    {
        
        x = xp;
        y = yp;
        z = zp;
        
    }
    
    public int sprawdz(final float xp, final float yp, final float zp)
    {
        
        float dx = x - xp;
        dx = dx * dx;
        float dy = y - yp;
        dy = dy * dy;
        float dz = z - zp;
        dz = dz * dz;
        
        if((1.0f > dx) && (1.0f > dy) && (1.0f > dz))
        {
            
            return 1;
            
        }
        
        return 0;
        
    }
    
}

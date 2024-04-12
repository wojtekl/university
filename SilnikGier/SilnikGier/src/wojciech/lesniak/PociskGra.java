package wojciech.lesniak;

public class PociskGra
{
    
    private float px, py, pz;
    public float cx, cy, cz;
    private float kx, ky, kz;
    private float zp;
    
    public PociskGra(final float x, final float y, final float z, final float lx, final float ly, final float lz, final float za)
    {
        
        px = x;
        py = y;
        pz = z;
        cx = x;
        cy = y;
        cz = z;
        kx = lx;
        ky = ly;
        kz = lz;
        zp = za;
        
    }
    
    public int lot()
    {
        
        cx = cx + kx;
        cy = cy + ky;
        cz = cz + kz;
        
        float dx = cx - px;
        float dy = cy - py;
        float dz = cz - pz;
        
        if(zp < Math.sqrt(dx * dx + dy * dy + dz * dz))
        {
            
            return 1;
            
        }
        
        return 0;
        
    }
    
    public int sprawdz(final float xp, final float yp, final float zp)
    {
        
        float dx = cx - xp;
        dx = dx * dx;
        float dy = cy - yp;
        dy = dy * dy;
        float dz = cz - zp;
        dz = dz * dz;
        
        if((0.4f > dx) && (0.4f > dy) && (0.4f > dz))
        {
            
            return 1;
            
        }
        
        return 0;
        
    }
    
}

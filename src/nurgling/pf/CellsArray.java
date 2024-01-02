package nurgling.pf;

import haven.*;
import nurgling.*;

public class CellsArray
{
    public Coord begin;
    public Coord end;
    public short [][] cells;

    public int x_len;
    public int y_len;

    public CellsArray(Gob gob, byte scale)
    {
        this(gob.ngob.hitBox, gob.a, gob.rc, scale);
    }
    
    
    public CellsArray(NHitBox hb, double angl, Coord2d rc, byte scale)
    {
        
        if ((Math.abs((angl * 180 / Math.PI) / 90) % 1 < 0.01))
        {
            Coord2d a1 = hb.begin.rotate(angl).shift(rc);
            Coord a = Utils.toPfGrid(a1,scale);
            Coord b = Utils.toPfGrid(hb.end.rotate(angl).shift(rc),scale);
            begin = new Coord(Math.min(a.x, b.x), Math.min(a.y, b.y));
            end = new Coord(Math.max(a.x, b.x), Math.max(a.y, b.y));
            x_len = end.x - begin.x + 1;
            y_len = end.y - begin.y + 1;
            cells = new short[x_len][y_len];
            for (int i = 0; i < x_len; i++)
            {
                for (int j = 0; j < y_len; j++)
                {
                    cells[i][j] = 1;
                }
            }
        }
        else
        {
            Coord2d ad = hb.begin.rotate(angl).shift(rc);
            Coord2d bd = new Coord2d(hb.end.x, hb.begin.y).rotate(angl).shift(rc);
            Coord2d cd = hb.end.rotate(angl).shift(rc);
            Coord2d dd = new Coord2d(hb.begin.x, hb.end.y).rotate(angl).shift(rc);
            Coord a = Utils.toPfGrid(ad,scale);
            Coord b = Utils.toPfGrid(bd,scale);
            Coord c = Utils.toPfGrid(cd,scale);
            Coord d = Utils.toPfGrid(dd,scale);
//            Coord2d a1 = Utils.pfGridToWorld(a,scale);
//            Coord2d b1 = Utils.pfGridToWorld(b,scale);
//            Coord2d c1 = Utils.pfGridToWorld(c,scale);
//            Coord2d d1 = Utils.pfGridToWorld(d,scale);
            begin = new Coord(Math.min(Math.min(a.x, b.x), Math.min(c.x, d.x)), Math.min(Math.min(a.y, b.y), Math.min(c.y, d.y)));
            end = new Coord(Math.max(Math.max(a.x, b.x), Math.max(c.x, d.x)), Math.max(Math.max(a.y, b.y), Math.max(c.y, d.y)));
            x_len = end.x - begin.x+1;
            y_len = end.y - begin.y+1;
            cells = new short[x_len][y_len];
            Coord2d start = Utils.pfGridToWorld(begin, scale);
            Coord2d pos = new Coord2d(start.x, start.y);
            Coord2d a_b = bd.sub(ad).norm();
            Coord2d b_c = cd.sub(bd).norm();
            Coord2d c_d = dd.sub(cd).norm();
            Coord2d d_a = ad.sub(dd).norm();
            int factor = 1;
            double delta = Math.abs(Math.min(hb.end.x-hb.begin.x,hb.end.y-hb.begin.y));
            while (delta>MCache.tilehsz.x)
            {
                delta /= 2;
                factor *= 2;
            }

            short[][] dcells = new short[x_len*factor + 1 ][y_len*factor + 1];
            for (int i = 0; i < x_len*factor; i++)
            {
                for (int j = 0; j < y_len*factor; j++)
                {
                    dcells[i][j] = (short) ((a_b.dot(pos.sub(ad).norm()) >= 0 && b_c.dot(pos.sub(bd).norm()) >= 0 && c_d.dot(pos.sub(cd).norm()) >= 0 && d_a.dot(pos.sub(dd).norm()) >= 0) ? 1 : 0);
                    pos.y += (MCache.tilehsz.y/factor);
                }
                pos.x += (MCache.tilehsz.x/factor);
                pos.y = start.y;
            }


            for (int i = 0; i < x_len; i++)
            {
                for (int j = 0; j < y_len; j++)
                {
                    if(factor>1)
                    {
                        short res = 0;
                        for(int k = 0; k <= factor; k++)
                        {
                            for(int n = 0; n <= factor; n++)
                            {
//                                if((factor * i + k) < x_len*factor && (factor * j + n)<y_len*factor)
                                    res |= dcells[factor * i + k][factor * j + n];
                            }
                        }
                        cells[i][j] = res;
                    }
                    else
                    {
                        cells[i][j] = (short) ((dcells[i][j] == 1 || dcells[i + 1][j] == 1 || dcells[i][j + 1] == 1 || dcells[i + 1][j + 1] == 1) ? 1 : 0);
                    }
                }
            }

        }
    }
}

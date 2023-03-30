package SwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class JImagePanel extends JPanel {

    private BufferedImage img;
    private BufferedImage rescaled;

    private static BufferedImage scale(BufferedImage src, int w, int h)
    {

        w=Math.max(1,w);
        h=Math.max(1,h);

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        int x, y;
        int ww = src.getWidth();
        int hh = src.getHeight();
        int[] ys = new int[h];
        for (y = 0; y < h; y++)
            ys[y] = y * hh / h;
        for (x = 0; x < w; x++) {
            int newX = x * ww / w;
            for (y = 0; y < h; y++) {
                int col = src.getRGB(newX, ys[y]);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }

    public void setImage(BufferedImage img) {


        if (img != null) {
            this.img = img;
            System.out.println("rescaled: " + this.getWidth() + " | " + this.getHeight());
            this.rescaled=scale(img,this.getWidth(),this.getHeight());

            repaint();
        }
    }

    public BufferedImage getImage(){
        return rescaled;
    }

    public BufferedImage getRawImage(){
        return img;
    }

    public void setSize(int width, int height){
        super.setSize(width,height);
        if(img!=null)
        this.rescaled=scale(img,this.getWidth(),this.getHeight());
    }

    public void setSize(Dimension d){

        super.setSize(d);
        if(img!=null)
        this.rescaled=scale(img,this.getWidth(),this.getHeight());

    }


    protected void paintComponent(Graphics g){

        Graphics2D g2d= (Graphics2D) g.create();

        super.paintComponent(g2d);

        RenderingHints hints=new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);

        if(g2d.getClipBounds().x!=this.getX()||g2d.getClipBounds().y!=this.getY()||
                g2d.getClipBounds().width!=this.getWidth()||g2d.getClipBounds().height!=this.getHeight())
            g2d.setClip(new Rectangle(this.getX(),this.getY(),this.getWidth(),this.getHeight()));
        /*for(Component c:this.getComponents()){

            if(!(c.getBackground()==null||c.getBackground().equals(new Color(0,0,0,0))))
                a.subtract(new Area(new Rectangle(c.getX(),c.getY(),c.getWidth(),c.getHeight())));

        }
        */

        BasicStroke stroke = new BasicStroke(1);
        g2d.setStroke(stroke);

        if(img!=null) {
            //super.setBackground(new Color(0,0,0,0));
            g2d.drawImage(img, this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
        }

        //g2d.setColor(Color.blue);
        //g2d.drawRect(this.getBounds().x,this.getBounds().y,this.getWidth(),this.getHeight());
        g2d.setClip(null);
        g2d.dispose();
        System.out.println("Image drawn");


    }


}

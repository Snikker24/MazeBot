package SwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class JImagePanel extends JPanel {

    private BufferedImage img;
    private BufferedImage rescaled;

    private static BufferedImage scale(BufferedImage src, int w, int h)
    {
        if(w*h>0) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            at.scale(2.0, 2.0);
            AffineTransformOp transfOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            img = transfOp.filter(src, img);


            System.out.println("Proper scaling: "+(w==img.getWidth()&&img.getHeight()==h&&w!=src.getWidth()&&h!=src.getHeight()));

            return img;
        }

        return null;
    }

    public void setImage(BufferedImage img) {


        if (img != null) {
            this.img = img;
            //System.out.println("rescaled: " + this.getWidth() + " | " + this.getHeight());
            this.rescaled=scale(this.img,this.getWidth(),this.getHeight());

            repaint();
        }
    }

    public BufferedImage getImage(){
        return img;
    }

    public BufferedImage getScaledImage(){
        return rescaled;
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

        g2d.setClip(new Rectangle(0,0,getParent().getWidth(),getParent().getHeight()));

        if(rescaled!=null) {

            //g2d.setColor(Color.CYAN);
            //g2d.fillRect(getX(),getY(),getWidth(),getHeight());
            g2d.drawImage(rescaled,0,0,null);
        }

        g2d.setClip(null);

        System.out.println("Image drawn at: "+this.getX()+" | "+this.getY()+" | ID: "+this.hashCode());


    }


}

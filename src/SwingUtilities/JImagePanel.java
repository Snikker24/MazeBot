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
    private boolean b=false;
    private int x=0,y=0;

    public static BufferedImage scale(BufferedImage src, int w, int h)
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


    public void setImageFromParent(){

        if(getParent() instanceof JImagePanel){

            b=true;
            this.img=((JImagePanel)(getParent())).img;
            this.rescaled=scale(img,getWidth(),getHeight());

        }


    }

    public void setImage(BufferedImage img) {

        this.img = img;
        //System.out.println("rescaled: " + this.getWidth() + " | " + this.getHeight());
        if(img!=null)
            this.rescaled = scale(this.img, this.getWidth(), this.getHeight());

        repaint();
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

        if(img!=null) {

            //g2d.setColor(Color.CYAN);
            //g2d.fillRect(getX(),getY(),getWidth(),getHeight());
            if(b)
                g2d.drawImage(img,-getX(),-getY(),getParent().getWidth(),getParent().getHeight(),null);
            else
                g2d.drawImage(img,0,0,getWidth(),getHeight(),null);
        }

        g2d.setClip(null);

        //System.out.println("Image drawn at: "+this.getX()+" | "+this.getY()+" |"+"Res: "+rescaled.getWidth()+" | "+rescaled.getHeight()+" | ID: "+this.hashCode());


    }


}

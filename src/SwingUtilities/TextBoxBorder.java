package SwingUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

public class TextBoxBorder extends AbstractBorder {

    private Color color;
    private int thickness = 4;
    private int radii = 8;
    private int pointerSize = 7;
    private Insets insets = null;
    private BasicStroke stroke = null;
    private int strokePad;
    private int pointerPad = 4;
    private boolean left = true;
    RenderingHints hints;

    public TextBoxBorder(
            Color color) {
        this(color, 4, 8, 7);
    }

    public TextBoxBorder(
            Color color, int thickness, int radii, int pointerSize) {
        this.thickness = thickness;
        this.radii = radii;
        this.pointerSize = pointerSize;
        this.color = color;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + strokePad;
        int bottomPad = pad + pointerSize + strokePad;
        insets = new Insets(pad, pad, bottomPad, pad);
    }

    public TextBoxBorder(
            Color color, int thickness, int radii, int pointerSize, boolean left) {
        this(color, thickness, radii, pointerSize);
        this.left = left;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(
            Component c,
            Graphics g,
            int x, int y,
            int width, int height) {

        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - thickness - pointerSize;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
                0 + strokePad,
                0 + strokePad,
                width - thickness,
                bottomLineY,
                radii,
                radii);

        Polygon pointer = new Polygon();

        if (left) {
            // left point
            pointer.addPoint(
                    strokePad + radii + pointerPad,
                    bottomLineY);
            // right point
            pointer.addPoint(
                    strokePad + radii + pointerPad + pointerSize,
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    strokePad + radii + pointerPad + (pointerSize / 2),
                    height - strokePad);
        } else {
            // left point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad),
                    bottomLineY);
            // right point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad + pointerSize),
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad + (pointerSize / 2)),
                    height - strokePad);
        }

        Area area = new Area(bubble);
        area.add(new Area(pointer));
        g2.setRenderingHints(hints);

        // Paint the BG color of the parent, everywhere outside the clip
        // of the text bubble.

        Component parent  = c.getParent();
        if (parent!=null) {

            Color bg = parent.getBackground();
            Rectangle rect = new Rectangle(0, 0, width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);

            if(parent instanceof JImagePanel){
                BufferedImage img=((JImagePanel) parent).getImage();


                //&&c.getX()+c.getWidth()<=img.getWidth()&&c.getY()+c.getHeight()<=img.getHeight()
                if(img!=null) {

                    //g2.drawRect(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight());


                    System.out.println("Coords: "+c.getX()+" | "+c.getY());
                    System.out.println("Dim: "+c.getWidth()+" | "+c.getHeight());


                    System.out.println("IMG-Dim: "+img.getWidth()+" | "+img.getHeight());

                    //img=JImagePanel.scale(img,parent.getWidth(),parent.getHeight());
                    //System.out.println("Parent: "+parent.getSize().toString());
                    //System.out.println("Child: "+c.getSize().toString());
                    //img=img.getSubimage(c.getX(),c.getY(),c.getWidth(),c.getHeight());
                    //g2.translate(c.getX(),c.getY());
                    g2.drawImage(img,-c.getX(),-c.getY(),parent.getWidth(),parent.getHeight(),null);
                    //g2.translate(-c.getX(),-c.getY());

                }
            }
        }

       /* if(c instanceof JImagePanel){

            Rectangle rect = new Rectangle(0, 0, width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            Area region=new Area(rect);
            region.subtract(borderRegion);

            Graphics g2d=c.getGraphics();
            g2d.setClip(region);
            ((JImagePanel) c).paintComponent(g2d);
        }
        */

        g2.setClip(null);
        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);

        if(c instanceof JImagePanel)
            System.out.println("Border drawn for: "+c.hashCode());

    }
}
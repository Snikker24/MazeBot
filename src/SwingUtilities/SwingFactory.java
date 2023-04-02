package SwingUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SwingFactory {




    public static BufferedImage GetImageFromPath(String path){

        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static JComponent SetCardOf(JComponent parent, JComponent card, String name){

        if(parent.getLayout() instanceof CardLayout){
            parent.add(card,name);
        }else{
            parent.setLayout(new CardLayout());
            parent.invalidate();
            parent.add(card,name);
        }

        return parent;

    }

    public static JFrame FullscreenJFrame(int monitor){

        monitor=Math.abs(monitor);
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        monitor%=ge.getScreenDevices().length;
        DisplayMode dm=ge.getScreenDevices()[monitor].getDisplayMode();
        JFrame frame=new JFrame();
        frame.setSize(dm.getWidth(),dm.getHeight());


        return frame;
    }

    public static Font ImportedFont(String path, float size){

        Font f=null;

        try {
            f=Font.createFont(Font.TRUETYPE_FONT, new File(path));
            f=f.deriveFont(size);

        }catch (FontFormatException | IOException e){
            e.printStackTrace();
        }

        return f;
    }

}

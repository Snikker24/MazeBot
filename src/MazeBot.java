import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import SwingUtilities.*;

import static SwingUtilities.SwingFactory.*;

public class MazeBot {

    private static int x,y;
    private static int difficulty=12;
    private final static String player="./textures/player.png";
    private final static String tile="./textures/tile.png";
    private final static String path="./textures/path.png";
    private final static String background="./textures/background.png";
    private final static String casePath="./textures/case.png";
    private final static String screenPath="./textures/screen.png";
    private final static String titleFontPath="./textfonts/titleFont.ttf";
    private final static String fontPath="./textfonts/font.ttf";


    public static void main(String ... args){

        JFrame frame=SwingFactory.FullscreenJFrame(0);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setVisible(true);

        TextBoxBorder border=new TextBoxBorder(null,1,30,0),btnBorder=new TextBoxBorder(null,1,30,0);

        Font titleFont=ImportedFont(titleFontPath,35f),font=ImportedFont(fontPath,35f);


        BufferedImage playerImg= GetImageFromPath(player),pathImg=GetImageFromPath(path)
                ,tileImg= GetImageFromPath(tile),backgroundImg= GetImageFromPath(background),
                sideImg=GetImageFromPath(casePath),screenImg=GetImageFromPath(screenPath);

        //setting up ui

        JImagePanel sidePanel=new JImagePanel();
        sidePanel.setImage(sideImg);
        sidePanel.setSize((int) (frame.getWidth()*0.25f),frame.getHeight());
        sidePanel.setPreferredSize(sidePanel.getSize());
        sidePanel.setBounds(0,0,sidePanel.getWidth(),sidePanel.getHeight());
        sidePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc=new GridBagConstraints();

        JTextPane title=new JTextPane();
        title.setBackground(new Color(0,0,0,0));
        title.setForeground(Color.BLACK);
        title.setText("MazeBot\nNavigate, Learn, Code!");
        title.setSize(new Dimension(sidePanel.getWidth(),300));
        title.setFont(titleFont);
        title.setEditable(false);
        title.setSelectedTextColor(title.getForeground());
        title.setSelectionColor(new Color(0,0,0,0));
        title.setHighlighter(null);
        title.getCaret().setSelectionVisible(false);
        title.getCaret().setVisible(false);
        title.getCaret().deinstall(title);
        title.setMargin(new Insets(15,0,0,0));

        StyledDocument doc = title.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(center,titleFont.getFamily());
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        doc.setCharacterAttributes(0,doc.getLength(),center,false);

        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=3;
        gbc.gridheight=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.weightx=0.1;
        gbc.weighty=0.1;

        sidePanel.add(title,gbc);

        //setting up nav screen

        JImagePanel screen=new JImagePanel();
        screen.setSize(sidePanel.getWidth()-50,sidePanel.getHeight()-title.getHeight()-50);
        screen.setPreferredSize(screen.getSize());
        screen.setOpaque(false);
        screen.setBorder(border);
        screen.setBackground(new Color(0,0,0,0));
        screen.setImageFromParent();

        CardLayout layout=new CardLayout();

        System.out.println("_____________________________________________\nGAPS "+layout.getHgap()+" | "+layout.getVgap());
        layout.setHgap(-30);
        layout.setVgap(-30);
        layout.preferredLayoutSize(screen);

        screen.setLayout(layout);



        gbc.gridy++;
        gbc.gridwidth=3;
        gbc.gridheight=6;
        gbc.fill=GridBagConstraints.NONE;
        gbc.insets.set(-100,0,0,0);
        gbc.weightx=0.1;
        gbc.weighty=0.1;

        sidePanel.add(screen,gbc);

        JButton btn=new JButton("Turn off");
        btn.setBackground(new Color(130,0,0));
        btn.setSize(150,20);
        btn.setBorder(border);
        btn.setForeground(Color.BLACK);
        btn.setFont(titleFont);
        btn.setFocusPainted(false);
        btn.setVerticalTextPosition(SwingConstants.CENTER);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        gbc.gridy+=6;
        gbc.gridwidth=3;
        gbc.gridheight=1;
        gbc.fill=GridBagConstraints.NONE;
        gbc.ipady=-20;
        gbc.ipadx=30;

        sidePanel.add(btn,gbc);

        //setting up different menu screens

        JImagePanel mainScreen=new JImagePanel();

        mainScreen.setSize(screen.getWidth(),screen.getHeight());
        mainScreen.setPreferredSize(mainScreen.getSize());
        mainScreen.setBorder(border);
        mainScreen.setBackground(Color.WHITE);
        mainScreen.setImage(screenImg);
        mainScreen.setLocation(screen.getLocation());
        mainScreen.setOpaque(false);

        mainScreen.setLayout(new GridBagLayout());

        //layout.setHgap(screen.getX()-mainScreen.getX());
        //layout.setVgap(screen.getY()-mainScreen.getY());

        gbc.gridx=gbc.gridy=0;
        gbc.gridwidth=gbc.gridheight=1;
        gbc.ipadx=25;
        gbc.insets.set(0,0,0,0);
        gbc.ipady=-30;

        btn=new JButton("//play:");
        btn.setBackground(new Color(40,225,16));
        btn.setSize(150,20);
        btn.setBorder(btnBorder);
        btn.setForeground(Color.BLACK);
        btn.setFont(font);
        btn.setFocusPainted(false);
        btn.setVerticalTextPosition(SwingConstants.CENTER);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        mainScreen.add(btn);


        screen.add(mainScreen,"main");
        //setting up maze display area;


        MazeRender mazeRender=new MazeRender(new Maze(difficulty),playerImg,pathImg,tileImg,backgroundImg);
        mazeRender.setBackground(Color.BLUE);
        mazeRender.setSize(frame.getWidth()-sidePanel.getWidth(),frame.getHeight());
        mazeRender.setPreferredSize(mazeRender.getSize());
        mazeRender.setPadding(0.25f);
        mazeRender.getMaze().resize(12);
        mazeRender.setBounds(sidePanel.getWidth(),0,mazeRender.getWidth(),mazeRender.getHeight());



        JPanel content=new JPanel();
        content.setSize(frame.getSize());
        content.setBounds(0,0,frame.getWidth(),frame.getHeight());
        content.setLayout(new BorderLayout());
        content.add(sidePanel,BorderLayout.WEST);
        content.add(mazeRender);
        frame.setContentPane(content);




    }


}

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import Blocks.CodeBlock;
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

        TextBoxBorder border=new TextBoxBorder(null,0,30,0),btnBorder=new TextBoxBorder(new Color(50,200,80),2,30,0);

        Font titleFont=ImportedFont(titleFontPath,35f),font=ImportedFont(fontPath,35f);


        BufferedImage playerImg= GetImageFromPath(player),pathImg=GetImageFromPath(path)
                ,tileImg= GetImageFromPath(tile),backgroundImg= GetImageFromPath(background),
                sideImg=GetImageFromPath(casePath),screenImg=GetImageFromPath(screenPath);

        //setting up ui

        JLayeredPane mainPanel=new JLayeredPane();
        mainPanel.setSize((int) (frame.getWidth()*0.25f),frame.getHeight());
        mainPanel.setPreferredSize(mainPanel.getSize());
        mainPanel.setBounds(0,0,mainPanel.getWidth(),mainPanel.getHeight());
        CardLayout layout=new CardLayout();
        mainPanel.setLayout(layout);

        JImagePanel card1=NewCard(frame),card2=NewCard(frame),card3=NewCard(frame),screen;
        mainPanel.add(card1,"main");
        mainPanel.add(card2,"comms");
        mainPanel.add(card3,"end");
        layout.show(mainPanel,"main");

        screen=GetScreen(card1);

        GridBagConstraints gbc=new GridBagConstraints();

        JButton playBtn=new JButton("//Play:"),
                commBtn=new JButton("//Commands:"),
                backBtn=new JButton("//Back:"),
                addBtn=new JButton("//Add:");
        LinkedHashMap<CodeBlock,JPanel> comms=new LinkedHashMap<>();



        if(screen!=null){

            playBtn.setBackground(new Color(50,200,80));
            playBtn.setSize(150,20);
            playBtn.setBorder(border);
            playBtn.setForeground(Color.BLACK);
            playBtn.setFont(font);
            playBtn.setFocusPainted(false);
            playBtn.setVerticalTextPosition(SwingConstants.CENTER);
            playBtn.setHorizontalTextPosition(SwingConstants.CENTER);
            playBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //mainPanel.moveToFront(card2);
                }
            });

            AtomicReference<JButton> pRef=new AtomicReference<>(playBtn);

            gbc.ipady=-20;
            gbc.ipadx=0;
            gbc.gridx= gbc.gridy=0;
            gbc.gridheight=1;
            gbc.gridwidth=2;
            gbc.weightx=0.1f;
            gbc.weighty=0.1f;
            screen.add(playBtn,gbc);

            commBtn.setBackground(new Color(50,200,80));
            commBtn.setSize(150,20);
            commBtn.setBorder(border);
            commBtn.setForeground(Color.BLACK);
            commBtn.setFont(font);
            commBtn.setFocusPainted(false);
            commBtn.setVerticalTextPosition(SwingConstants.CENTER);
            commBtn.setHorizontalTextPosition(SwingConstants.CENTER);

            AtomicReference<JButton> cRef=new AtomicReference<>(commBtn);

            commBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    layout.show(mainPanel,"comms");

                }
            });

            gbc.gridheight=1;
            gbc.gridwidth=2;
            gbc.gridy++;
            gbc.weightx=0.1f;
            gbc.weighty=0.1f;
            screen.add(commBtn,gbc);

        }

        screen=GetScreen(card2);

        if(screen!=null){

            backBtn.setBackground(new Color(50,200,80));
            backBtn.setSize(150,20);
            backBtn.setBorder(border);
            backBtn.setForeground(Color.BLACK);
            backBtn.setFont(font);
            backBtn.setFocusPainted(false);
            backBtn.setVerticalTextPosition(SwingConstants.CENTER);
            backBtn.setHorizontalTextPosition(SwingConstants.CENTER);
            backBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    layout.show(mainPanel,"main");
                }
            });

            gbc.gridx= gbc.gridy=0;
            gbc.gridheight=1;
            gbc.gridwidth=1;
            gbc.weightx=0.1f;
            gbc.weighty=0.1f;
            screen.add(backBtn,gbc);


            JPanel commBox=new JPanel();
            commBox.setForeground(new Color(50,200,80));

            addBtn.setBackground(new Color(50,200,80));
            addBtn.setSize(150,20);
            addBtn.setBorder(border);
            addBtn.setForeground(Color.BLACK);
            addBtn.setFont(font);
            addBtn.setFocusPainted(false);
            addBtn.setVerticalTextPosition(SwingConstants.CENTER);
            addBtn.setHorizontalTextPosition(SwingConstants.CENTER);
            addBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });

            gbc.gridx++;
            gbc.gridy=0;
            gbc.gridheight=1;
            gbc.gridwidth=1;
            gbc.weightx=0.1f;
            gbc.weighty=0.1f;
            gbc.insets.set(0,10,0,0);

            screen.add(addBtn,gbc);

            JComboBox<String> commType=new JComboBox<>();
            commType.addItem("up");
            commType.addItem("down");
            commType.addItem("left");
            commType.addItem("right");
            commType.setForeground(new Color(50,200,80));
            commType.setBackground(Color.BLACK);
            commType.setBorder(btnBorder);
            commType.setSize(300,25);
            //commType.setPreferredSize(commType.getSize());

            gbc.gridx=0;
            gbc.gridy=1;
            gbc.gridwidth=2;
            gbc.ipady=20;
            gbc.insets.set(0,0,0,0);

            screen.add(commType,gbc);

            JPanel commBox=new JPanel();
            commBox.setForeground(new Color(50,200,80));

            JScrollPane commPanel=new JScrollPane();
            commPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            commPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            //ommPanel.setBackground(Color.black);
            commPanel.setBorder(border);
            commPanel.setSize(screen.getWidth(),350);
            commPanel.setPreferredSize(commPanel.getSize());
            commPanel.setBackground(Color.black);

            commBox.setSize(commPanel.getSize());
            commBox.setPreferredSize(commPanel.getSize());
            commBox.setBackground(Color.BLACK);

            commPanel.setViewportView(commBox);

            gbc.gridx=0;
            gbc.gridy=2;
            gbc.ipadx=0;
            gbc.ipady=0;
            gbc.weightx=gbc.weighty=0.6;
            gbc.gridheight= gbc.gridwidth=2;
            gbc.fill=GridBagConstraints.BOTH;
            screen.add(commPanel,gbc);

        }

        //setting up maze display area;


        MazeRender mazeRender=new MazeRender(new Maze(difficulty),playerImg,pathImg,tileImg,backgroundImg);
        mazeRender.setBackground(Color.BLUE);
        mazeRender.setSize(frame.getWidth()-mainPanel.getWidth(),frame.getHeight());
        mazeRender.setPreferredSize(mazeRender.getSize());
        mazeRender.setPadding(0.25f);
        mazeRender.getMaze().resize(12);
        mazeRender.setBounds(mainPanel.getWidth(),0,mazeRender.getWidth(),mazeRender.getHeight());



        JPanel content=new JPanel();
        content.setSize(frame.getSize());
        content.setBounds(0,0,frame.getWidth(),frame.getHeight());
        content.setLayout(new BorderLayout());
        content.add(mainPanel,BorderLayout.WEST);
        content.add(mazeRender);
        frame.setContentPane(content);




    }

    private static JImagePanel NewCard(JFrame frame){
        TextBoxBorder border=new TextBoxBorder(null,1,30,0),btnBorder=new TextBoxBorder(null,1,30,0);

        Font titleFont=ImportedFont(titleFontPath,35f),font=ImportedFont(fontPath,35f);


        BufferedImage playerImg= GetImageFromPath(player),pathImg=GetImageFromPath(path)
                ,tileImg= GetImageFromPath(tile),backgroundImg= GetImageFromPath(background),
                sideImg=GetImageFromPath(casePath),screenImg=GetImageFromPath(screenPath);

        //setting up ui

        JImagePanel mainPanel=new JImagePanel();
        mainPanel.setImage(sideImg);
        mainPanel.setSize((int) (frame.getWidth()*0.25f),frame.getHeight());
        mainPanel.setPreferredSize(mainPanel.getSize());
        mainPanel.setBounds(0,0,mainPanel.getWidth(),mainPanel.getHeight());
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc=new GridBagConstraints();

        JTextPane title=new JTextPane();
        title.setBackground(new Color(0,0,0,0));
        title.setForeground(Color.BLACK);
        title.setText("MazeBot\nNavigate, Learn, Code!");
        title.setSize(new Dimension(mainPanel.getWidth(),300));
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

        mainPanel.add(title,gbc);

        //setting up nav screen

        JImagePanel screen=new JImagePanel();
        screen.setSize(mainPanel.getWidth()-50,mainPanel.getHeight()-title.getHeight()-50);
        screen.setPreferredSize(screen.getSize());
        screen.setOpaque(false);
        screen.setBorder(border);
        screen.setBackground(new Color(0,0,0,0));
        screen.setImage(screenImg);
        screen.setName("screen");
        screen.setLayout(new GridBagLayout());



        gbc.gridy++;
        gbc.gridwidth=3;
        gbc.gridheight=6;
        gbc.fill=GridBagConstraints.NONE;
        gbc.insets.set(-100,0,0,0);
        gbc.weightx=0.1;
        gbc.weighty=0.1;

        mainPanel.add(screen,gbc);

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

        mainPanel.add(btn,gbc);

        return mainPanel;
    }

    private static JImagePanel GetScreen(JImagePanel card){

        for(Component c:card.getComponents())
            if(c.getName()!=null&&(c instanceof JImagePanel))
                if(c.getName().equals("screen"))
                    return (JImagePanel) c;

        return null;

    }


}

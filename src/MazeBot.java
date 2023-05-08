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
import Blocks.Instruction;
import SwingUtilities.*;

import static SwingUtilities.SwingFactory.*;

public class MazeBot {

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

        MazeRender mazeRender=new MazeRender(new Maze(difficulty),playerImg,pathImg,tileImg,backgroundImg);
        mazeRender.setDoubleBuffered(true);

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
        LinkedHashMap<CodeBlock,String> comms=new LinkedHashMap<>();


        JPanel commBox=new JPanel();
        JScrollPane commPanel=new JScrollPane(commBox);

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

                    for(CodeBlock c:comms.keySet()){
                        JLabel name=new JLabel(comms.get(c));
                        name.setFont(font);
                        name.setBackground(Color.YELLOW);
                        name.setForeground(new Color(50,200,80));
                        name.setSize(commBox.getWidth()/2,25);
                        name.setPreferredSize(name.getSize());

                        JButton deleteBtn=new JButton("//delete:");
                        deleteBtn.setBackground(Color.BLACK);
                        deleteBtn.setSize(name.getWidth(),50);
                        deleteBtn.setPreferredSize(deleteBtn.getSize());
                        deleteBtn.setBorder(btnBorder);
                        deleteBtn.setForeground(new Color(50,200,80));
                        deleteBtn.setFont(font);
                        deleteBtn.setFocusPainted(false);
                        deleteBtn.setVerticalTextPosition(SwingConstants.CENTER);
                        deleteBtn.setHorizontalTextPosition(SwingConstants.CENTER);
                        deleteBtn.setBounds(name.getWidth(),name.getY(),deleteBtn.getWidth(),deleteBtn.getHeight());

                        deleteBtn.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                comms.remove(c);
                                commBox.remove(name);
                                commBox.remove(deleteBtn);

                                if(commBox.getHeight()>commPanel.getHeight())
                                    commBox.setSize(commBox.getWidth(),commBox.getHeight()-name.getHeight()-deleteBtn.getHeight());

                                commBox.validate();
                                //commPanel.setViewportView(commBox);
                                commPanel.validate();
                                commPanel.getViewport().setSize(commPanel.getSize());
                                commPanel.getViewport().setPreferredSize(commPanel.getSize());
                                commBox.repaint();
                                commPanel.repaint();
                            }
                        });

                        commBox.add(name);
                        commBox.add(deleteBtn);

                        if(comms.size()*deleteBtn.getHeight()+comms.size()*name.getHeight()>commPanel.getHeight()) {
                            commBox.setSize(commBox.getWidth(), commBox.getHeight() + name.getHeight() + deleteBtn.getHeight());
                            commBox.setPreferredSize(commBox.getSize());
                        }
                    }

                    commBox.setLayout(new GridLayout(2*comms.size(),1));
                    commBox.validate();
                    commBox.repaint();
                    //commPanel.setViewportView(commBox);
                    commPanel.getViewport().setPreferredSize(commPanel.getSize());
                    commPanel.validate();
                    commPanel.repaint();
                    card2.repaint();


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
                    commBox.removeAll();
                    commBox.setSize(commPanel.getSize());
                    commBox.setPreferredSize(commPanel.getSize());
                    layout.show(mainPanel,"main");
                }
            });

            gbc.gridx= gbc.gridy=0;
            gbc.gridheight=1;
            gbc.gridwidth=1;
            gbc.weightx=0.1f;
            gbc.weighty=0.1f;
            screen.add(backBtn,gbc);







            commBox.setForeground(new Color(50,200,80));

            JComboBox<String> commType=new JComboBox<>();

            commPanel.setSize(screen.getWidth()-100,screen.getWidth()-100);
            commPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            commPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            commPanel.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
            commPanel.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));
            commPanel.getVerticalScrollBar().setUnitIncrement(10);
            commPanel.getHorizontalScrollBar().setUnitIncrement(10);
            //commPanel.setBackground(Color.black);
            commPanel.setBorder(null);
            //commPanel.setPreferredSize(commPanel.getSize());
            commPanel.setBackground(Color.YELLOW);
            commPanel.setBounds(commPanel.getX(),commPanel.getY(),commPanel.getWidth(),commPanel.getHeight());
            //commPanel.getRowHeader().getView().setSize(new Dimension(0,0));
            //commPanel.getColumnHeader().getView().setSize(new Dimension(0,0));
            commPanel.setOpaque(false);
            //commPanel.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER,null);
            commPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

            commBox.setBackground(Color.BLACK);
            commBox.setSize(commPanel.getWidth(),commPanel.getHeight());
            //commBox.setPreferredSize(commBox.getSize());
            commBox.setBorder(border);
            GridLayout commLayout=new GridLayout(comms.size(), 2);
            //commLayout.setAlignment(FlowLayout.CENTER);
            //commLayout.setAlignOnBaseline(true);
            commBox.setLayout(commLayout);
            commBox.getLayout().layoutContainer(commBox);
            //commBox.setBounds(commBox.getX(),commBox.getY(),commBox.getWidth(),commBox.getHeight());

            //commPanel.add(commBox);
            commPanel.setViewportView(commBox);
            commPanel.getViewport().setSize(commPanel.getSize());

            addBtn.setBackground(new Color(50,200,80));
            addBtn.setSize(150,20);
            addBtn.setBorder(border);
            addBtn.setForeground(Color.BLACK);
            addBtn.setFont(font);
            addBtn.setFocusPainted(false);
            addBtn.setVerticalTextPosition(SwingConstants.CENTER);
            addBtn.setHorizontalTextPosition(SwingConstants.CENTER);
            JImagePanel finalScreen = screen;
            addBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    Instruction i=new Instruction() {
                        @Override
                        protected void action() {

                        }
                    };


                    switch((String)commType.getSelectedItem())
                    {
                        case "up"->{

                            i=new Instruction() {
                                @Override
                                protected void action() {
                                    if(mazeRender.getMaze().getPositionY()-1>=0&&mazeRender.getMaze().getPositionY()-1<mazeRender.getMaze().size())
                                        mazeRender.getMaze().setPositionY(mazeRender.getMaze().getPositionY()-1);

                                    mazeRender.paintComponent(mazeRender.getGraphics().create());
                                    mazeRender.repaint();
                                }
                            };



                        }

                        case "down"->{
                            i=new Instruction() {
                                @Override
                                protected void action() {
                                    if(mazeRender.getMaze().getPositionY()+1>=0&&mazeRender.getMaze().getPositionY()+1<mazeRender.getMaze().size())
                                        mazeRender.getMaze().setPositionY(mazeRender.getMaze().getPositionY() + 1);

                                    //mazeRender.repaint();
                                    mazeRender.paintComponent(mazeRender.getGraphics().create());
                                    mazeRender.repaint();
                                }
                            };
                        }

                        case "left"->{
                            i=new Instruction() {
                                @Override
                                protected void action() {

                                    if(mazeRender.getMaze().getPositionX()-1>=0&&mazeRender.getMaze().getPositionX()-1<mazeRender.getMaze().size())
                                        mazeRender.getMaze().setPositionX(mazeRender.getMaze().getPositionX() - 1);

                                    mazeRender.paintComponent(mazeRender.getGraphics().create());
                                    mazeRender.repaint();
                                }
                            };
                        }

                        case "right"->{
                            i=new Instruction() {
                                @Override
                                protected void action() {
                                    if(mazeRender.getMaze().getPositionX()+1>=0&&mazeRender.getMaze().getPositionX()+1<mazeRender.getMaze().size())
                                        mazeRender.getMaze().setPositionX(mazeRender.getMaze().getPositionX() + 1);

                                    mazeRender.paintComponent(mazeRender.getGraphics().create());
                                    mazeRender.repaint();
                                }
                            };
                        }

                    }


                    JLabel name=new JLabel((String)commType.getSelectedItem());
                    name.setFont(font);
                    name.setBackground(Color.YELLOW);
                    name.setForeground(new Color(50,200,80));
                    name.setSize(commBox.getWidth()/4,25);
                    //name.setPreferredSize(name.getSize());

                    JButton deleteBtn=new JButton("//delete:");
                    deleteBtn.setBackground(Color.BLACK);
                    deleteBtn.setSize(name.getWidth()*3,50);
                    deleteBtn.setPreferredSize(deleteBtn.getSize());
                    deleteBtn.setBorder(btnBorder);
                    deleteBtn.setForeground(new Color(50,200,80));
                    deleteBtn.setFont(font);
                    deleteBtn.setFocusPainted(false);
                    deleteBtn.setVerticalTextPosition(SwingConstants.CENTER);
                    deleteBtn.setHorizontalTextPosition(SwingConstants.CENTER);
                    deleteBtn.setBounds(name.getWidth(),name.getY(),deleteBtn.getWidth(),deleteBtn.getHeight());
                    //deleteBtn.setMargin(new Insets(0,0,0,0));


                    comms.put(i,name.getText());

                    AtomicReference<Instruction> ir=new AtomicReference<>(i);

                    deleteBtn.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            comms.remove(ir.get());
                            commBox.remove(name);
                            commBox.remove(deleteBtn);

                            if(commBox.getHeight()>commPanel.getHeight()){
                                commBox.setSize(commBox.getWidth(),commBox.getHeight()-name.getHeight()-deleteBtn.getHeight());
                                commBox.setPreferredSize(commBox.getSize());
                            }

                            //commBox.setPreferredSize(commBox.getSize());
                            commBox.validate();
                            commPanel.setViewportView(commBox);
                            commPanel.validate();
                            commPanel.getViewport().setSize(commPanel.getSize());
                            commPanel.getViewport().setPreferredSize(commPanel.getSize());

                            commBox.repaint();
                            commPanel.repaint();
                            finalScreen.repaint();
                        }
                    });

                    commBox.add(name);
                    commBox.add(deleteBtn);

                    //commLayout.putConstraint(SpringLayout.EAST,name,0,SpringLayout.WEST,deleteBtn);
                    //commLayout.putConstraint(SpringLayout.NORTH,name,name.getHeight(),SpringLayout.NORTH,deleteBtn);
                    //commLayout.putConstraint(SpringLayout.SOUTH,name,name.getHeight(),SpringLayout.SOUTH,deleteBtn);
                    //commLayout.putConstraint(SpringLayout.WEST,name,name.getWidth()+deleteBtn.getWidth(),SpringLayout.EAST,deleteBtn);

                    if(comms.size()*deleteBtn.getHeight()+comms.size()*name.getHeight()>commPanel.getHeight()) {
                        commBox.setSize(commBox.getWidth(), commBox.getHeight() + name.getHeight() + deleteBtn.getHeight());
                        commBox.setPreferredSize(commBox.getSize());
                    }
                    //commBox.setPreferredSize(commBox.getSize());
                    commBox.setLayout(new GridLayout(comms.size()*2,1));
                    commBox.validate();
                    commBox.repaint();
                    //commPanel.setViewportView(commBox);
                    //commPanel.getViewport().setPreferredSize(commPanel.getSize());
                    commPanel.validate();
                    commPanel.repaint();
                    //commPanel.setViewportView(commBox);
                    //finalScreen.repaint();

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


            gbc.gridx=0;
            gbc.gridy=2;
            gbc.ipadx=0;
            gbc.ipady=0;
            gbc.weightx=gbc.weighty=0.6f;
            gbc.gridheight= gbc.gridwidth=4;
            gbc.fill=GridBagConstraints.BOTH;
            screen.add(commPanel,gbc);


            playBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    playBtn.setEnabled(false);
                    playBtn.setBackground(new Color(50,200,80,128));

                    Thread master=new Thread(){
                        @Override
                        public synchronized void start() {
                            super.start();

                            try {
                                this.wait(1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }

                            if (mazeRender.getMaze().getPositionX() == mazeRender.getMaze().getStartX()
                                    && mazeRender.getMaze().getPositionY() == mazeRender.getMaze().getStartY()) {

                                Thread t;
                                for (CodeBlock i : comms.keySet()) {

                                    t = new Thread() {
                                        public synchronized void run() {
                                            super.run();
                                            i.run();
                                            //mazeRender.repaint();

                                        }
                                    };
                                    t.start();

                                    try {
                                        this.wait(500);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }

                                }

                                if (mazeRender.getMaze().getPositionX() == mazeRender.getMaze().getEndX()
                                        && mazeRender.getMaze().getPositionY() == mazeRender.getMaze().getEndY())
                                    layout.show(mainPanel, "end");
                                else {
                                    mazeRender.getMaze().setPositionX(mazeRender.getMaze().getStartX());
                                    mazeRender.getMaze().setPositionY(mazeRender.getMaze().getStartY());
                                    mazeRender.repaint();
                                }

                                playBtn.setEnabled(true);
                                playBtn.setBackground(new Color(50,200,80));
                            }
                        }
                    };


                    master.start();

                }
            });

        }

        screen=GetScreen(card3);


        if(screen!=null){
            JLabel label=new JLabel("You win!");
            label.setForeground(Color.GREEN);
            label.setBackground(null);
            label.setFont(font);
            screen.add(label);

        }

        //setting up maze display area;

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


        commPanel.getGraphics().setColor(Color.BLUE);
        commPanel.getGraphics().drawRoundRect(commPanel.getX(),commPanel.getY(),commPanel.getWidth(), commPanel.getHeight(), 20,20);



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

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import SwingUtilities.*;

public final class MazeBot_redundant {

    public static void main(String ... args) throws IOException {

        Maze m=new Maze(12);

        JFrame frame=new JFrame();

        File player=new File("player.png"),path=new File("path.png"),
                tile=new File("tile.png"),background=new File("background.png"),
        screen=new File("screen.png");

        System.out.println(player.getAbsolutePath());
        System.out.println(path.getAbsolutePath());
        System.out.println(tile.getAbsolutePath());
        System.out.println(background.getAbsolutePath());

        MazeRender mazeRender=new MazeRender(m,ImageIO.read(player),
                ImageIO.read(path),ImageIO.read(tile),
                ImageIO.read(background));


        GraphicsDevice monitor=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        DisplayMode optimal=monitor.getDisplayModes()[0];

        for(DisplayMode d: monitor.getDisplayModes()) {

            if (d.getHeight() * d.getWidth() > optimal.getHeight() * optimal.getWidth())
                optimal = d;

        }

        System.out.println("OPTIMAL PC RESOLUTION: "+optimal.getWidth()+" X "+optimal.getHeight());

        frame.setSize(optimal.getWidth(),optimal.getHeight());

        mazeRender.setBackground(Color.BLUE);
        mazeRender.setPadding(0.25f);


        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        //frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        TextBoxBorder border=new TextBoxBorder(null,0,25,0);

        JImagePanel sidePanel=new JImagePanel();
        sidePanel.setSize(450, frame.getHeight());

        sidePanel.setImage(ImageIO.read(new File("./case.png")));
        sidePanel.setLayout(new GridBagLayout());
        //menuPanel.setLayout(new BorderLayout());
        sidePanel.setBackground(Color.blue);
        sidePanel.setForeground(Color.BLACK);
        //sidePanel.setBorder(border);

        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.ipady=0;
        gbc.gridwidth=3;
        gbc.gridheight=1;
        gbc.weightx=0.0f;
        gbc.weighty=0.0f;
        //gbc.anchor=GridBagConstraints.PAGE_START;
        gbc.insets.set(0,0,300,0);

        Font titleFont=null;
        Font consoleFont=null;

        try {
            titleFont=Font.createFont(Font.TRUETYPE_FONT, new File("./titleFont.ttf"));
            titleFont=titleFont.deriveFont(35f);

            consoleFont=Font.createFont(Font.TRUETYPE_FONT, new File("./font.ttf"));
            consoleFont=consoleFont.deriveFont(35f);

        }catch (FontFormatException e){
            e.printStackTrace();
        }

        titleFont =(titleFont==null? new Font("Verdana", Font.BOLD | Font.ITALIC, 35):titleFont);
        consoleFont =(consoleFont==null? new Font("Verdana", Font.BOLD | Font.ITALIC, 35):consoleFont);

        //menuPanel.setFont(f);
        JTextPane title=new JTextPane();

        title.setBackground(new Color(0,0,0,0));
        title.setForeground(Color.BLACK);

        title.setText("MazeBot_redundant\nNavigate, Learn, Code!");
        title.setSize(new Dimension(sidePanel.getWidth(),300));
        title.setFont(titleFont);
        //title.setBounds(menuPanel.getX(),menuPanel.getY(),title.getWidth(),title.getHeight());
        title.setEditable(false);
        title.setSelectedTextColor(title.getForeground());
        title.setSelectionColor(new Color(0,0,0,0));
        title.setHighlighter(null);
        title.getCaret().setVisible(false);
        title.getCaret().deinstall(title);
        title.setMargin(new Insets(15,0,45,0));
        title.setBounds(new Rectangle(0,0,title.getWidth(),title.getHeight()));

        //title.setBorder(null);
        //title.setLineWrap(true);
        //title.setWrapStyleWord(true);

        StyledDocument doc = title.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(center,titleFont.getFamily());
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        doc.setCharacterAttributes(0,doc.getLength(),center,false);

        gbc.insets.set(-title.getHeight(),0,0,0);

        sidePanel.add(title, gbc);

        gbc.anchor=GridBagConstraints.CENTER;
        gbc.weighty=0.0f;
        gbc.weightx=0.1f;

        JImagePanel screenPanel;
        screenPanel=new JImagePanel();
        screenPanel.setPreferredSize(new Dimension(sidePanel.getWidth()-50,sidePanel.getHeight()-title.getHeight()-50));
        screenPanel.setSize(screenPanel.getPreferredSize());
        screenPanel.setBackground(Color.RED);
        screenPanel.setBorder(border);
        CardLayout layout=new CardLayout();
        screenPanel.setImage(ImageIO.read(screen));
        screenPanel.setLayout(layout);

        System.out.println("Screen size: "+screenPanel.getWidth()+" | "+screenPanel.getHeight());

        JPanel screenContent=new JImagePanel();
        screenContent.setSize(screenPanel.getSize());
        screenContent.setPreferredSize(screenPanel.getSize());
        //screenContent.setImage(ImageIO.read(screen));
        screenContent.setBackground(null);
        screenContent.setBorder(border);
        screenContent.setPreferredSize(screenPanel.getSize());

        //System.out.println("Screen image size: "+screenContent.getScaledImage().getWidth()+" | "+screenContent.getScaledImage().getHeight());

        screenPanel.add(screenContent,"main");

        layout.show(screenPanel,"main");

        //screenPanel.setBounds(new Rectangle(0,0,screenPanel.getWidth(),sidePanel.getHeight()));



        gbc.gridy++;
        gbc.insets.set(0,0,0,0);
        gbc.gridheight=6;
        gbc.gridwidth=3;
        gbc.fill=GridBagConstraints.NONE;
        gbc.insets.set(0,0,-title.getHeight(),0);

        System.out.println("Actual: "+screenPanel.getX()+" | "+screenPanel.getY()+"| ID: "+screenPanel.hashCode());
        System.out.println("Actual: "+sidePanel.getX()+" | "+sidePanel.getY()+"| ID: "+sidePanel.hashCode());

        sidePanel.add(screenPanel,gbc);

        //((GridBagLayout)sidePanel.getLayout()).setConstraints(screenPanel,gbc);
        System.out.println(screenPanel.getSize().toString());

        JButton btn=new JButton();
        btn.setFocusPainted(false);
        //btn.setSize(180,20);
        btn.setBackground(Color.GREEN);
        btn.setText("Execute");
        //btn.setPreferredSize(btn.getSize());
        btn.setForeground(Color.BLACK);
        btn.setBorder(border);
        btn.setOpaque(true);
        btn.setFont(consoleFont.deriveFont(30f));
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JButton.CENTER);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.setOpaque(true);

        //gbc.insets.set(100,0,0,0);
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridy+=6;
        gbc.gridwidth=1;
        gbc.gridheight=1;
        gbc.ipady=-20;
        gbc.weighty=0.1f;
        gbc.weightx=0.1f;
        //sidePanel.add(btn,gbc);


        btn=new JButton();
        btn.setFocusPainted(false);
        btn.setSize(180,20);
        btn.setBackground(Color.GREEN);
        btn.setText("Instructions");
        //btn.setPreferredSize(btn.getSize());
        btn.setForeground(Color.BLACK);
        btn.setBorder(border);
        btn.setOpaque(true);
        btn.setFont(consoleFont.deriveFont(30f));
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JButton.CENTER);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);

        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx++;
        //sidePanel.add(btn,gbc);


        frame.setResizable(false);
        sidePanel.setBounds(0,0,sidePanel.getWidth(),sidePanel.getHeight());


        JLayeredPane content=new JLayeredPane();

        content.setSize(optimal.getWidth(),optimal.getHeight());

        mazeRender.setSize(frame.getWidth()-sidePanel.getWidth(),frame.getHeight());
        mazeRender.setPreferredSize(new Dimension(content.getWidth()-sidePanel.getWidth(),content.getHeight()));
        mazeRender.setBounds(new Rectangle(sidePanel.getWidth(),0,mazeRender.getWidth(),mazeRender.getHeight()));
        System.out.println("Actual maze size: "+mazeRender.getWidth()+" | "+mazeRender.getHeight());

        System.out.println("Side resolution: "+sidePanel.getSize().toString());

        content.add(mazeRender,1);
        content.add(sidePanel,0);
        //content.moveToFront(sidePanel);



        //content.moveToFront(sidePanel);

        frame.setContentPane(content);
        frame.setVisible(true);

        System.out.println("Final: "+screenPanel.getX()+" | "+screenPanel.getY()+"| ID: "+screenPanel.hashCode());
        System.out.println("Final: "+sidePanel.getX()+" | "+sidePanel.getY()+"| ID: "+sidePanel.hashCode());


    }


}

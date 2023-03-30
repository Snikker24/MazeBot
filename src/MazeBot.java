import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import SwingUtilities.*;

public final class MazeBot {

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

        frame.setSize(1920,1080);

        mazeRender.setSize(frame.getSize());
        mazeRender.setPreferredSize(new Dimension(mazeRender.getWidth(),mazeRender.getHeight()));
        mazeRender.setBackground(Color.BLUE);


        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        TextBoxBorder border=new TextBoxBorder(null,0,25,0);

        JImagePanel sidePanel=new JImagePanel();
        sidePanel.setSize(450,mazeRender.getHeight());

        sidePanel.setImage(ImageIO.read(new File("./case.png")));
        sidePanel.setLayout(new GridBagLayout());
        //menuPanel.setLayout(new BorderLayout());
        sidePanel.setBackground(Color.RED);
        sidePanel.setForeground(Color.BLACK);
        //sidePanel.setBorder(border);

        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.ipady=0;
        gbc.gridwidth=3;
        gbc.gridheight=1;
        gbc.weightx=0.5f;
        gbc.weighty=0.5f;
        gbc.anchor=GridBagConstraints.PAGE_START;

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

        title.setText("MazeBot\nNavigate, Learn, Code!");
        title.setSize(new Dimension(sidePanel.getWidth(),300));
        title.setFont(titleFont);
        //title.setBounds(menuPanel.getX(),menuPanel.getY(),title.getWidth(),title.getHeight());
        title.setEditable(false);
        title.setSelectedTextColor(title.getForeground());
        title.setSelectionColor(new Color(0,0,0,0));
        title.setHighlighter(null);
        title.getCaret().deinstall(title);
        title.setMargin(new Insets(15,0,0,0));

        //title.setBorder(null);
        //title.setLineWrap(true);
        //title.setWrapStyleWord(true);

        StyledDocument doc = title.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(center,titleFont.getFamily());
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        doc.setCharacterAttributes(0,doc.getLength(),center,false);

        sidePanel.add(title, gbc);
        gbc.anchor=GridBagConstraints.CENTER;
        //;
        gbc.weighty=1f;
        gbc.weightx=1f;

        new JImagePanel();
        JImagePanel screenPanel;
        screenPanel=new JImagePanel();
        System.out.println(sidePanel.getWidth()-50+" | width");
        screenPanel.setPreferredSize(new Dimension(sidePanel.getWidth()-50,sidePanel.getWidth()-50));
        //screenPanel.setSize(new Dimension(sidePanel.getWidth()-50,sidePanel.getWidth()-50));
        screenPanel.setBackground(Color.red);
        screenPanel.setImage(ImageIO.read(screen));
        System.out.println(screenPanel.getImage().getWidth(null)+" | "+screenPanel.getImage().getHeight(null));
        screenPanel.setBorder(border);

        gbc.gridy++;
        gbc.gridheight=3;
        gbc.gridwidth=3;
        gbc.fill=GridBagConstraints.NONE;

        sidePanel.add(screenPanel,gbc);


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


        gbc.fill=GridBagConstraints.NONE;
        gbc.gridy+=3;
        gbc.gridwidth=1;
        gbc.gridheight=1;
        gbc.ipady=-20;
        gbc.weighty=0.5f;
        gbc.weightx=0.5f;
        sidePanel.add(btn,gbc);

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
        sidePanel.add(btn,gbc);


        frame.setResizable(false);

        sidePanel.setBackground(Color.BLACK);
        sidePanel.setBounds(0,0,sidePanel.getWidth(),sidePanel.getHeight());


        JLayeredPane content=new JLayeredPane();
        content.add(mazeRender,1);
        content.add(sidePanel,0);
        //content.moveToFront(sidePanel);

        frame.setContentPane(content);
        frame.setVisible(true);


    }


}

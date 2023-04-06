import javax.swing.*;
import java.awt.*;

public class MazeRender extends JPanel {

    private Image player;
    private Image pathBlock;
    private Image tileBlock;
    private Image background;

    private Maze maze;
    private float padding;

    public MazeRender(Maze maze, Image player, Image pathBlock, Image tileBlock, Image background){
        this.maze=maze;
        this.player=player;
        this.pathBlock=pathBlock;
        this.tileBlock=tileBlock;
        this.background=background;


    }

    public void  setPadding(float padding){
        this.padding=(Math.abs(padding));
        this.padding-=((int)this.padding);
    }

    public float getPadding() {
        return padding;
    }

    public void setSize(int width, int height){
        super.setSize(width,height);
        repaint();
    }

    public void setSize(Dimension d){
        super.setSize(d);
        repaint();
    }

    public void setPreferredSize(Dimension d){
        super.setPreferredSize(d);
        repaint();
    }


    protected synchronized final void paintComponent(Graphics g){

        Graphics2D g2d= (Graphics2D) g;
        super.paintComponent(g2d);

        g2d.setClip(new Rectangle(this.getWidth(),this.getHeight()));

        g2d.drawImage(background,0,0, this.getWidth(),this.getHeight(),null);

        System.out.println("Maze resolution: "+getWidth()+"x"+getHeight());
        int posX,posY;
        int padX,padY;
        padX= (int) (padding*getWidth());
        padY= (int) (padding*getHeight());

        int cellW,cellH;

        while((getWidth()-padX)%maze.size()!=0)
            padX--;

        while((getHeight()-padY)%maze.size()!=0)
            padY--;


        System.out.println("Padding: "+padX+" | "+padY);

        cellW=Math.abs(getWidth()-padX)/maze.size();
        cellH=Math.abs(getHeight()-padY)/maze.size();

        System.out.println("Cell size: "+cellW+"|"+cellH);

        posX=Math.abs(padX)/2;
        posY=Math.abs(padY)/2;

        for(int i=0;i<maze.size();i++)
        {

            for(int j=0;j<maze.size();j++){


                if(maze.getTile(i,j))
                    g2d.drawImage(pathBlock,posX+i*cellW,posY+j*cellH,cellW,cellH,null);
                else
                    g2d.drawImage(tileBlock,posX+i*cellW,posY+j*cellH,cellW,cellH,null);

                if(i==maze.getPositionX()&&j==maze.getPositionY())
                    g2d.drawImage(player,posX+i*cellW,posY+j*cellH,cellW,cellH,null);

                if(i==maze.getEndX()&&j==maze.getEndY()) {

                    Font f=new Font("sans",Font.BOLD,40);
                    g.setFont(f);
                    g.setColor(Color.RED);
                    g.drawString("X",posX+i*cellW+f.getSize(),posY+j*cellH+(f.getSize()));
                }


            }
        }

        g.setClip(null);

    }

    public Maze getMaze(){
        return maze;
    }


}

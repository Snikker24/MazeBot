import javax.swing.*;
import java.awt.*;

public class MazeRender extends JPanel {

    private Image player;
    private Image pathBlock;
    private Image tileBlock;
    private Image background;

    private Maze maze;

    public MazeRender(Maze maze, Image player, Image pathBlock, Image tileBlock, Image background){
        this.maze=maze;
        this.player=player;
        this.pathBlock=pathBlock;
        this.tileBlock=tileBlock;
        this.background=background;


    }

    protected final void paintComponent(Graphics g){

        //super.paintComponent(g2d);


        g.setClip(new Rectangle(this.getWidth(),this.getHeight()));

        g.drawImage(background,0,0, this.getWidth()-10,this.getHeight()-10,null);

        int posX,posY;

        posX= Math.abs(this.getWidth()-maze.size()*80)/2;
        posY= Math.abs(this.getHeight()-maze.size()*80)/2;

        for(int i=0;i<maze.size();i++)
        {

            for(int j=0;j<maze.size();j++){


                if(maze.getTile(i,j))
                    g.drawImage(pathBlock,posX+i*80,posY+j*80,80,80,null);
                else
                    g.drawImage(tileBlock,posX+i*80,posY+j*80,80,80,null);

                if(i==maze.getPositionX()&&j==maze.getPositionY())
                    g.drawImage(player,posX+i*80+10,posY+j*80+10,60,60,null);

                if(i==maze.getEndX()&&j==maze.getEndY()) {

                    Font f=new Font("sans",Font.BOLD,40);
                    g.setFont(f);
                    g.setColor(Color.RED);
                    g.drawString("X",posX+i*80+30,posY+j*80+50);
                }


            }
        }

    }

    public Maze getMaze(){
        return maze;
    }


}

import java.util.Arrays;
import java.util.Random;

public class Maze{

    private boolean[][] map;
    private int size;
    private int startX, startY, endX, endY;
    private int posX,posY;

    public Maze(int size){
        resize(size);
    }

    private void generate(){

        for(boolean[] booleans : map) Arrays.fill(booleans, false);

        Random random = new Random();

        switch (random.nextInt(4)) {
            case 0 -> { // top edge
                startX = 0;
                startY = random.nextInt(size);
                endX = size - 1;
                endY = random.nextInt(size);
            }
            case 1 -> { // right edge
                startX = random.nextInt(size);
                startY = 0;
                endX = random.nextInt(size);
                endY = size - 1;
            }
            case 2 -> { // bottom edge
                startX = size - 1;
                startY = random.nextInt(size);
                endX = 0;
                endY = random.nextInt(size);
            }
            case 3 -> { // left edge
                startX = random.nextInt(size);
                startY = size - 1;
                endX = random.nextInt(size);
                endY = 0;
            }
        }

        // mark the starting and ending points
        map[startX][startY] = true;
        map[endX][endY] = true;


        int numExitPaths=random.nextInt(1,3);

        generateMaze(startX,startY,endX,endY,numExitPaths,random);

        posX=startX;
        posY=startY;

        System.out.println(numExitPaths);

        for(boolean[] b:map){
            for(boolean b0:b)
                System.out.print(b0+" ");

            System.out.println();
        }

    }

    private void generateMaze(int x, int y, int ex, int ey, int numExitPaths, Random random) {
        int[] directions = {-1, 0, 1, 0, -1}; // left, up, right, down
        randomizeArray(directions, random);

        for (int i = 0; i < 4; i++) {
            int dx = directions[i];
            int dy = directions[i + 1];

            int nx = x + dx;
            int ny = y + dy;

            if (nx >= 0 && nx < size && ny >= 0 && ny < size && !map[nx][ny]) {
                map[x + dx / 2][y + dy / 2] = true;

                double d=random.nextDouble();

                System.out.println("Maze weight: "+d);
                // randomly branch out or continue straight
                if (numExitPaths > 0 && (d > 0.999f||d<0.0001f) && !(nx == ex && ny == ey)) {
                    // create an exit path
                    generateMaze(nx, ny, ex, ey, numExitPaths - 1, random);
                } else if (d > 0.999f||d<0.0001f) {
                    // create a dead-end path
                    generateMaze(nx, ny, x, y, 0, random);
                } else {
                    // continue straight
                    generateMaze(nx, ny, x, y, numExitPaths, random);
                }
            }
        }
    }

    private void randomizeArray(int[] arr, Random random) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public void resize(int size){

        size=Math.abs(size);
        this.size=size;
        map=new boolean[size][size];
        generate();

    }

    public int size(){
        return size;
    }

    public boolean getTile(int row, int column ) {
        return map[row][column];
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX(){
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getPositionX() {
        return posX;
    }

    public int getPositionY() {
        return posY;
    }

    public void setPositionX(int posX) {

        posX=Math.abs(posX);
        posX%=size;

        if(map[posX][posY])
            this.posX = posX;
    }

    public void setPositionY(int posY) {

        posY=Math.abs(posY);
        posY%=size;

        if(map[posX][posY])
            this.posY = posY;
    }
}

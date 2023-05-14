import java.util.Random;

public class Robot {
    private char[][] game_maze;
    private int xLocation;
    private int yLocation;


    public Robot(char[][] game_maze, int xLocation, int yLocation) {
        this.game_maze = game_maze;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
    }

    public char[][] getGame_maze() {
        return game_maze;
    }

    public int getxLocation() {
        return xLocation;
    }

    public int getyLocation() {
        return yLocation;
    }

    public void setxLocation(int xLocation) {
        this.xLocation = xLocation;
    }

    public void setyLocation(int yLocation) {
        this.yLocation = yLocation;
    }

    public boolean robotMovement(){
        Random rnd = new Random();

        boolean flag=true;

        if(isAbleToMove()) {

            while (true) {
                int direction = rnd.nextInt(4);

                if(xLocation>0  && direction ==0 ) { //YUKARI
                    if((game_maze[xLocation-1][yLocation] == ' '|| game_maze[xLocation-1][yLocation]=='P')){
                        game_maze[xLocation][yLocation] = ' ';
                        setxLocation(xLocation-1);
                        if(game_maze[xLocation][yLocation]=='P') {

                            game_maze[xLocation][yLocation] = 'X';
                            flag=false;

                        }
                        game_maze[xLocation][yLocation] = 'X';
                        break;
                    }


                }

                else if (xLocation<=21  && direction==1 ){ //AŞAĞI
                    if((game_maze[xLocation+1][yLocation] == ' ' || game_maze[xLocation+1][yLocation]=='P')){
                        game_maze[xLocation][yLocation] = ' ';
                        setxLocation(xLocation+1);
                        if(game_maze[xLocation][yLocation]=='P') {

                            game_maze[xLocation][yLocation] = 'X';

                            flag=false;

                        }
                        game_maze[xLocation][yLocation] = 'X';
                        break;
                    }

                }
                else if (yLocation>0 && direction==2 ){ //SOL
                    if((game_maze[xLocation][yLocation-1] == ' ' || game_maze[xLocation][yLocation-1]=='P')){
                        game_maze[xLocation][yLocation] = ' ';
                        setyLocation(yLocation-1);
                        if(game_maze[xLocation][yLocation]=='P') {

                            game_maze[xLocation][yLocation] = 'X';
                            flag=false;

                        }
                        game_maze[xLocation][yLocation] = 'X';
                        break;
                    }

                }
                else if ( yLocation<=51 && direction==3 ){ //SAĞ
                    if(( game_maze[xLocation][yLocation+1] == ' ' || game_maze[xLocation][yLocation+1]=='P')){
                        game_maze[xLocation][yLocation] = ' ';
                        setyLocation(yLocation+1);
                        if(game_maze[xLocation][yLocation]=='P') {

                            game_maze[xLocation][yLocation] = 'X';

                            flag=false;

                        }
                        game_maze[xLocation][yLocation] = 'X';
                        break;
                    }

                }

            }
        }
        return flag;



    }

    public boolean isAbleToMove() {

        if( xLocation>0 && (game_maze[xLocation-1][yLocation]=='P' || game_maze[xLocation-1][yLocation]==' ')) {

            return true;
        }
        else if(xLocation<=21 && ( game_maze[xLocation+1][yLocation] == ' ' || game_maze[xLocation+1][yLocation]=='P')) {

            return true;
        }
        else if(yLocation>0 && ( game_maze[xLocation][yLocation-1] == ' ' || game_maze[xLocation][yLocation-1]=='P')) {

            return true;
        }
        else if(yLocation<=51 && ( game_maze[xLocation][yLocation+1] == ' ' || game_maze[xLocation][yLocation+1]=='P')) {

            return true;
        }
        else {

            return false;
        }



    }

}



public class Boulder {

    private static Boulder[] boulders=new Boulder[Game.maze.length*Game.maze[0].length];
    static int boulderCount=0;
    int myOrder=0;
    private boolean isMovedInTime=false;
    private int isUnderPlayer=0;
    private int isUnderRobot=0;
    private int posX;
    private int posY;


    public Boulder(int posX,int posY) {
        this.posX=posX;
        this.posY=posY;
        boulders[boulderCount]=this;
        myOrder=boulderCount;
        boulderCount+=1;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int tposX) {
        this.posX = tposX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int tposY) {
        this.posY = tposY;
    }

    public void tryPush(String from) {

        if(from.equalsIgnoreCase("right")) {

            if(Game.maze[posY][posX+1]==' ' && Game.maze[posY+1][posX]!=' ') {
                Game.maze[posY][posX+1]='O';
                setPosX(posX+1);
            }
        }else {
            if(Game.maze[posY][posX-1]==' ' && Game.maze[posY+1][posX]!=' ') {
                Game.maze[posY][posX-1]='O';
                setPosX(posX-1);
            }
        }
    }

    public static boolean tryFallAllBoulders() {
        for (int i = 0; i < boulderCount; i++) {

            Boulder currentBoulder = boulders[i];
            int y= currentBoulder.getPosY();
            int x= currentBoulder.getPosX();

            if (y+1<23) {

                if (Game.maze[y+1][x]==' ' && !currentBoulder.isMovedInTime) {

                    Game.maze[y][x]=' ';
                    currentBoulder.setPosY(y+1);
                    Game.maze[y+1][x]='O';

                    if (currentBoulder.posY+1<23) {

                        if (Game.maze[currentBoulder.posY+1][currentBoulder.posX]=='P') {
                            currentBoulder.isUnderPlayer+=1;
                        }else {
                            currentBoulder.isUnderPlayer=0;
                        }

                        if (Game.maze[currentBoulder.posY+1][currentBoulder.posX]=='X') {
                            currentBoulder.isUnderRobot+=1;
                        }else {
                            currentBoulder.isUnderRobot=0;
                        }

                    }

                    currentBoulder.isMovedInTime = true;


                }else if (Game.maze[y+1][x]=='O' && y+1<23 && x-1>0 && !currentBoulder.isMovedInTime && x+1<53){

                    String fallDirection=Game.BOULDER_FALLING_DIRECTION;

                    if (fallDirection.equalsIgnoreCase("RIGHT_LEFT")) {
                        int rand=(int)(Math.random()*(2))+1;
                        if (rand==1) {
                            fallDirection="RIGHT";
                        }else {
                            fallDirection="LEFT";
                        }
                    }



                    if (fallDirection.equalsIgnoreCase("RIGHT") && Game.maze[y+1][x+1]==' ') {

                        Game.maze[y][x]=' ';
                        currentBoulder.setPosY(y+1);
                        currentBoulder.setPosX(x+1);
                        Game.maze[y+1][x+1]='O';


                        if (currentBoulder.posY+1<23) {
                            if (Game.maze[currentBoulder.posY+1][currentBoulder.posX]=='P') {
                                currentBoulder.isUnderPlayer+=1;
                            }else {
                                currentBoulder.isUnderPlayer=0;
                            }

                            if (Game.maze[currentBoulder.posY+1][currentBoulder.posX]=='X') {
                                currentBoulder.isUnderRobot+=1;
                            }else {
                                currentBoulder.isUnderRobot=0;
                            }
                        }


                    }else if(fallDirection.equalsIgnoreCase("LEFT") && Game.maze[y+1][x-1]==' ') {


                        Game.maze[y][x]=' ';
                        currentBoulder.setPosY(y+1);
                        currentBoulder.setPosX(x-1);
                        Game.maze[y+1][x-1]='O';

                        if (currentBoulder.posY+1<23) {

                            if (Game.maze[currentBoulder.posY+1][currentBoulder.posX]=='P') {
                                currentBoulder.isUnderPlayer+=1;
                            }else {
                                currentBoulder.isUnderPlayer=0;
                            }

                            if (Game.maze[currentBoulder.posY+1][currentBoulder.posX]=='X') {
                                currentBoulder.isUnderRobot+=1;
                            }else {
                                currentBoulder.isUnderRobot=0;
                            }
                        }


                    }
                    currentBoulder.isMovedInTime=true;
                }

                if (currentBoulder.isUnderRobot>=1 && !currentBoulder.isMovedInTime) {
                    Game.maze[currentBoulder.posY+1][currentBoulder.posX]=' ';
                    Game.score+=900;
                }
                if (currentBoulder.isUnderPlayer>=1 && !currentBoulder.isMovedInTime) {
                    return false;
                }

            }

            Game.maze[currentBoulder.posY][currentBoulder.posX]='O';
        }


        refreshMoveBools();
        return true;
    }



    public static void refreshMoveBools() {
        for (int i = 0; i < boulders.length; i++) {
            if (boulders[i]!=null) {
                boulders[i].isMovedInTime=false;
            }
        }
    }


    public static Boulder getBoulder(int psx,int psy) {
        for (int i = 0; i < boulderCount; i++) {
            if (boulders[i].getPosX()==psx && boulders[i].getPosY()==psy) {
                return boulders[i];
            }
        }
        return null;
    }


}

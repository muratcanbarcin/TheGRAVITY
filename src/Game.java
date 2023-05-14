import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import enigma.console.TextAttributes;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;


public class Game {
    private int GAME_TIME_UNIT;
    private int ADDITION_CHARACTER_TIME;
    public static String BOULDER_FALLING_DIRECTION;
    public static char[][] maze = new char[23][53];
    private int[][] xRobots = new int[23][53];
    public  static int score = 0;



    public enigma.console.Console cn = Enigma.getConsole("The GRAVITY",100,30,20);
    public TextMouseListener tmlis;
    public KeyListener klis;

    // ------ Standard variables for mouse and keyboard ------
    public int mousepr;          // mouse pressed?
    public int mousex, mousey;   // mouse text coords.
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)
    // ----------------------------------------------------


    public Game() throws Exception {   // --- Contructor

        do {
            cn.getTextWindow().output("Please, Enter Falling Direction of Boulders in Game as --RIGHT / LEFT / RIGHT_LEFT-- : ");
            BOULDER_FALLING_DIRECTION = cn.readLine();

        } while (!(BOULDER_FALLING_DIRECTION.equalsIgnoreCase("RIGHT") || BOULDER_FALLING_DIRECTION.equalsIgnoreCase("LEFT") || BOULDER_FALLING_DIRECTION.equalsIgnoreCase("RIGHT_LEFT")));
        cn.getTextWindow().output("Please, Enter Time Unit in Game as --MILLISECOND-- : ");
        GAME_TIME_UNIT = Integer.parseInt(cn.readLine());
        cn.getTextWindow().output("Please, Enter Addition Characters Time in Game as --SECOND-- : ");
        ADDITION_CHARACTER_TIME = Integer.parseInt(cn.readLine());

        clear();
        Random rnd = new Random();

        int[] pCoordinate = fillingArea();
        int px = pCoordinate[1], py = pCoordinate[0];
        Stack backpack = new Stack(8);
        CircularQueue inputQueue = new CircularQueue(15); // Circular Queue data structure
        int teleportRight = 3;

        int time = 0;
        boolean gameover=true;
        for(int i = 0; i < 15; i++) //initial fully filling the inputQueue
            addElement(inputQueue);


        // ------ Standard code for mouse and keyboard ------ Do not change
        tmlis = new TextMouseListener() {
            public void mouseClicked(TextMouseEvent arg0) {
            }

            public void mousePressed(TextMouseEvent arg0) {
                if (mousepr == 0) {
                    mousepr = 1;
                    mousex = arg0.getX();
                    mousey = arg0.getY();
                }
            }

            public void mouseReleased(TextMouseEvent arg0) {
            }
        };
        cn.getTextWindow().addTextMouseListener(tmlis);

        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        cn.getTextWindow().addKeyListener(klis);
        // ----------------------------------------------------



        while (true) { // game finish situation will be determined.
            printScreen(backpack, inputQueue, teleportRight, score, time,gameover);





            if (mousepr == 1) {  // if mouse button pressed
                cn.getTextWindow().output(mousex, mousey, '#');  // write a char to x,y position without changing cursor position
                px = mousex;
                py = mousey;

                mousepr = 0;     // last action
            }
            if (keypr == 1) {    // if keyboard button pressed
                if (rkey == KeyEvent.VK_LEFT && inBound(px - 1, py) && isVisitable(px - 1, py)) {
                    maze[py][px] = ' ';
                    px--;

                    if(maze[py][px] == 'O')
                        Boulder.getBoulder(px, py).tryPush("left");

                } else if (rkey == KeyEvent.VK_RIGHT && inBound(px + 1, py) && isVisitable(px + 1, py)) {
                    maze[py][px] = ' ';
                    px++;

                    if(maze[py][px] == 'O')
                        Boulder.getBoulder(px, py).tryPush("right");



                } else if (rkey == KeyEvent.VK_UP && inBound(px, py - 1) && isVisitable(px, py - 1)) {
                    maze[py][px] = ' ';
                    py--;
                    if(maze[py][px] == 'O') // boulder will act like a wall.
                        continue;

                } else if (rkey == KeyEvent.VK_DOWN && inBound(px, py + 1) && isVisitable(px, py + 1)) {
                    maze[py][px] = ' ';
                    py++;

                    if(maze[py][px] == 'O') // boulder will act like a wall.
                        continue;
                }


                char rckey = (char) rkey;
                //        left          right          up            down
                if (rckey == '%' || rckey == '\'' || rckey == '&' || rckey == '(') {

                    char character = maze[py][px];

                    if (character == '1' || character == '2' || character == '3') //if there is treasure on the next square

                        if (backpack.isEmpty())
                            backpack.push(character);
                        else if (backpack.isFull()) { //if backpack is full, firstly pop, then check the backpack
                            backpack.pop();
                            if(backpack.peek() == character) { // this is element before the last
                                backpack.pop();

                                int[] tempArr = backpackImp(character, score, teleportRight);
                                score = tempArr[0];
                                teleportRight = tempArr[1];
                            }
                            else
                                backpack.push(character);

                        }
                        else if (backpack.peek() == character) {
                            backpack.pop();

                            int[] tempArr = backpackImp(character, score, teleportRight);
                            score = tempArr[0];
                            teleportRight = tempArr[1];
                        }
                        else
                            backpack.push(character);



                    maze[py][px] = 'P';
                }
            }
            else if (rkey == KeyEvent.VK_SPACE && teleportRight > 0) { // teleport daima sifirlaniyor
                maze[py][px] = ' ';
                do {
                    px = rnd.nextInt(53);
                    py = rnd.nextInt(23);
                }while(maze[py][px]=='#' || maze[py][px]=='O');

                maze[py][px] = 'P';
                teleportRight--;
                rkey = 'T';    // used to run this part only one time. I chanced the rkey status.
            }

            keypr = 0;    // last action


            ///////ROBOTSSSSS


            for(int searchX =0; searchX<xRobots.length;searchX++){ //Düşman hareketleri
                for(int searchY =0; searchY<xRobots[0].length;searchY++){

                    if(xRobots[searchX][searchY] == 1){
                        int xloc = searchX;
                        int yloc = searchY;

                        Robot x = new Robot(maze,xloc,yloc);
                        if(x.robotMovement()==false) {


                            gameover=false;
                            break;
                        }

                        maze = x.getGame_maze();
                    }
                }
                if(!gameover) {

                    break;
                }
            }

            if(!gameover) {
                cn.getTextWindow().setCursorPosition(0, 0);


                for (int i = 0; i < 23; i++) {
                    for (int j = 0; j < 53; j++) {
                        maze[i][j] = ' ';
                    }
                }

                // "GAME OVER" yazısını diziye yerleştir
                String gameOver0 = "GAME OVER";
                int startRow = 10; // yazının başlangıç satırı
                int startCol = 20; // yazının başlangıç sütunu
                for (int i = 0; i < gameOver0.length(); i++) {
                    maze[startRow][startCol + i] = gameOver0.charAt(i);
                }
                printScreen(backpack, inputQueue, teleportRight, score, time,gameover);

                break;
            }

            for(int sX =0; sX<maze.length;sX++){  //xROBOTS güncellemesi
                for(int sY =0; sY<maze[0].length;sY++){
                    if(maze[sX][sY] == 'X' || maze[sX][sY] == 'x'){
                        xRobots[sX][sY] =1;
                    }
                    else {
                        xRobots[sX][sY] =0;
                    }
                }
            }





            ///////ROBOTSSSSS SON



            gameover=Boulder.tryFallAllBoulders();
            Thread.sleep(GAME_TIME_UNIT);
            time += GAME_TIME_UNIT;

            if(time % (ADDITION_CHARACTER_TIME * 1000) == 0) {
                char action = inputQueue.dequeue();
                inputActions(action);
                addElement(inputQueue);
            }

            cn.getTextWindow().setCursorPosition(0, 0);
        }

    }


    public void inputActions(char action)
    {
        Random rnd = new Random();

        int x = rnd.nextInt(53);
        int y = rnd.nextInt(23);


        switch (action)
        {
            case '1':
                if(maze[y][x] == ' ' || maze[y][x] == ':') {
                    maze[y][x] = '1';
                }
                else {

                    x = rnd.nextInt(53);
                    y = rnd.nextInt(23);

                    while(maze[y][x]!=' ' && maze[y][x]!=':') {

                        x = rnd.nextInt(53);
                        y = rnd.nextInt(23);

                    }

                    maze[y][x] = '1';
                }
                break;

            case '2':
                if(maze[y][x] == ' ' || maze[y][x] == ':') {
                    maze[y][x] = '2';
                }
                else {

                    x = rnd.nextInt(53);
                    y = rnd.nextInt(23);

                    while(maze[y][x]!=' ' && maze[y][x]!=':') {

                        x = rnd.nextInt(53);
                        y = rnd.nextInt(23);

                    }

                    maze[y][x] = '2';
                }
                break;

            case '3':
                if(maze[y][x] == ' ' || maze[y][x] == ':') {
                    maze[y][x] = '3';
                }
                else {

                    x = rnd.nextInt(53);
                    y = rnd.nextInt(23);

                    while(maze[y][x]!=' ' && maze[y][x]!=':') {

                        x = rnd.nextInt(53);
                        y = rnd.nextInt(23);

                    }

                    maze[y][x] = '3';
                }
                break;

            case 'X':
                if(maze[y][x] == ' ' || maze[y][x] == ':') {
                    maze[y][x] = 'X';
                }
                else {

                    x = rnd.nextInt(53);
                    y = rnd.nextInt(23);

                    while(maze[y][x]!=' ' && maze[y][x]!=':') {

                        x = rnd.nextInt(53);
                        y = rnd.nextInt(23);

                    }

                    maze[y][x] = 'X';
                }
                break;

            case 'O':

                x = rnd.nextInt(53);
                y = rnd.nextInt(23);

                while(maze[y][x]!=' ' && maze[y][x]!=':' && maze[y][x]!='O') {

                    x = rnd.nextInt(53);
                    y = rnd.nextInt(23);

                }



                if(maze[y][x] == ' ' || maze[y][x] == ':') {
                    maze[y][x] = 'O';


                    do {
                        x = rnd.nextInt(53);
                        y = rnd.nextInt(23);
                    } while (maze[y][x] != 'O');
                    maze[y][x] = ':';
                    break;
                }
                else if(maze[y][x] == 'O') {
                    maze[y][x] = ':';
                    do {
                        x = rnd.nextInt(53);
                        y = rnd.nextInt(23);
                    } while (!(maze[y][x] == ' ' || maze[y][x] == ':'));
                    maze[y][x] = 'O';
                    break;
                }
            case ':':
                if(maze[y][x] == ' ') {
                    maze[y][x] = ':';
                }
                else {

                    x = rnd.nextInt(53);
                    y = rnd.nextInt(23);

                    while(maze[y][x]!=' ') {

                        x = rnd.nextInt(53);
                        y = rnd.nextInt(23);

                    }

                    maze[y][x] = ':';
                }
                break;

            case 'e':
                if(maze[y][x] == ':') {
                    maze[y][x] = ' ';
                }
                else {

                    x = rnd.nextInt(53);
                    y = rnd.nextInt(23);

                    while(maze[y][x]!=':') {

                        x = rnd.nextInt(53);
                        y = rnd.nextInt(23);

                    }

                    maze[y][x] = ' ';
                }
                break;


        }

    }
    public int[] backpackImp(char character, int score, int teleportRight)
    {
        switch (character) {
            case '1':
                score += 10;
                break;
            case '2':
                score += 40;
                break;
            case '3':
                score += 90;
                teleportRight++;
        }

        int[] arr = {score, teleportRight};
        return arr;

    }
    public void addElement(CircularQueue inputQueue) // adding element to input Queue with given responsibilities
    {
        Random rnd = new Random();

        int val = rnd.nextInt(41);

        if(val < 6)           //it is importont to understand this part.
            inputQueue.enqueue('1');
        else if(val < 11)
            inputQueue.enqueue('2');
        else if(val < 15)
            inputQueue.enqueue('3');
        else if(val == 15)
            inputQueue.enqueue('X');
        else if(val < 26)
            inputQueue.enqueue('O');
        else if(val < 35)
            inputQueue.enqueue(':');
        else
            inputQueue.enqueue('e');
    }


    public void printScreen(Stack backpackS, CircularQueue inputQueue, int teleportRight, int score, int time,boolean gameover)
    {
        cn.getTextWindow().setCursorPosition(0,0);

        Stack tempStack = new Stack(8);

        cn.getTextWindow().output("#######################################################\n");
        for (int i = 0; i < maze.length; i++) {
            cn.getTextWindow().output('#');
            for (int j = 0; j < maze[0].length; j++) {

                if(maze[i][j]=='P') {

                    cn.getTextWindow().output('P',new TextAttributes(Color.green));
                }
                else if(maze[i][j]=='X') {

                    cn.getTextWindow().output('X',new TextAttributes(Color.yellow));
                }
                else if(maze[i][j]=='1' || maze[i][j]=='2' || maze[i][j]=='3') {

                    cn.getTextWindow().output(maze[i][j],new TextAttributes(Color.red));
                }
                else if(maze[i][j]=='O' && gameover==true) {

                    cn.getTextWindow().output('O',new TextAttributes(Color.gray));
                }
                else if(maze[i][j]==':') {

                    cn.getTextWindow().output(':',new TextAttributes(Color.pink));
                }
                else if(maze[i][j]==' ') {

                    cn.getTextWindow().output(' ',new TextAttributes(Color.black));
                }
                else if(maze[i][j]=='#') {

                    cn.getTextWindow().output('#');
                }
                else {

                    cn.getTextWindow().output(maze[i][j],new TextAttributes(Color.red));
                }
            }

            cn.getTextWindow().output("#        ");


            switch (i) {
                case 0:
                    cn.getTextWindow().output("Input");
                    break;
                case 1:case  3:
                    cn.getTextWindow().output("<<<<<<<<<<<<<<<");
                    break;
                case 2:
                    for (int j = 0; j < inputQueue.size(); j++) {
                        cn.getTextWindow().output(inputQueue.peek());
                        inputQueue.enqueue(inputQueue.dequeue());
                    }
                    break;
                case 6: case 7:case 8:case 9:case 10:case 11:case 12:case 13:
                    cn.getTextWindow().output('|');
                    int size = backpackS.size();

                    if (i >= 14 - size)
                    {
                        cn.getTextWindow().output(' ');
                        cn.getTextWindow().output(backpackS.peek());
                        cn.getTextWindow().output(' ');
                        tempStack.push(backpackS.pop());
                    }
                    else
                        cn.getTextWindow().output("   ");

                    cn.getTextWindow().output('|');
                    break;
                case 14:
                    cn.getTextWindow().output("+---+");
                    while(!tempStack.isEmpty())
                        backpackS.push(tempStack.pop());
                    break;
                case 15:
                    cn.getTextWindow().output("Backpack");
                    break;
                case 18:
                    cn.getTextWindow().output("Teleport :"+teleportRight);
                    break;
                case 20:
                    cn.getTextWindow().output("Score    :"+ score);
                    break;
                case 22:
                    cn.getTextWindow().output("Time     :"+ time / 1000);
            }

            cn.getTextWindow().output("\n");
        }
        cn.getTextWindow().output("#######################################################\n");

    }

    public int[] fillingArea() {
        placingWalls();
        placingEarths();
        placingBoulders();

        placingTreasures();
        placingEmptySquares();
        placingFirstRobots();
        return placingPlayer();
    }


    public void placingWalls() {
        for (int i = 0; i < 49; i++) {
            maze[7][i] = '#';
        }
        for (int i = 0; i < 49; i++) {
            maze[15][52 - i] = '#';
        }
    }

    public void placingEarths() {
        for (int i = 0; i < maze.length; i++)
            for (int j = 0; j < maze[0].length; j++)
                if (maze[i][j] != '#')
                    maze[i][j] = ':';
    }

    public void placingBoulders() {
        Random rnd = new Random();

        int boulderCount = 0;
        while (boulderCount < 180) {
            int row = rnd.nextInt(23);
            int col = rnd.nextInt(53);

            if (maze[row][col] != ':')
                continue;

            maze[row][col] = 'O';
            Boulder newBoulder=new Boulder(col,row);

            boulderCount++;
        }

    }

    public void placingTreasures() {
        Random rnd = new Random();

        int treasureCount = 0;
        while (treasureCount < 30) {
            int row = rnd.nextInt(23);
            int col = rnd.nextInt(53);

            if (maze[row][col] != ':')
                continue;

            int type = rnd.nextInt(1, 4);

            switch (type) {
                case 1:
                    maze[row][col] = '1';
                    break;
                case 2:
                    maze[row][col] = '2';
                    break;
                case 3:
                    maze[row][col] = '3';
                    break;
            }

            treasureCount++;
        }
    }

    public void placingEmptySquares() {
        Random rnd = new Random();
        int emptyCount = 0;
        while (emptyCount < 200) {
            int row = rnd.nextInt(23);
            int col = rnd.nextInt(53);

            if (maze[row][col] != ':')
                continue;

            maze[row][col] = ' ';
            emptyCount++;
        }
    }

    public void placingFirstRobots() {
        Random rnd = new Random();

        int robotCount = 0;
        while (robotCount < 7) { //normalde 7 olacak
            int row = rnd.nextInt(23);
            int col = rnd.nextInt(53);

            if (maze[row][col] != ':')
                continue;


            xRobots[row][col] = 1;

            maze[row][col] = 'X';
            robotCount++;
        }
    }

    public int[] placingPlayer() {
        Random rnd = new Random();


        int row;
        int col;
        boolean flag = true;
        do {
            row = rnd.nextInt(23);
            col = rnd.nextInt(53);

            if (maze[row][col] == ':') {
                maze[row][col] = 'P';
                flag = false;
            }
        } while (flag);

        int[] coordinate = {row, col};
        return coordinate;
    }

    public boolean inBound(int x, int y)
    {
        return x < maze[0].length && x > -1 && y < maze.length && y > -1;
    }

    public boolean isVisitable(int x, int y)
    {
        return maze[y][x] == ' ' || maze[y][x] == ':' || maze[y][x] == '1' || maze[y][x] == '2' || maze[y][x] == '3'
                || (x-1 > -1 && x + 1 < maze[0].length && maze[y][x-1] == 'P' && maze[y][x] == 'O' &&  maze[y][x+1] == ' ')
                || (x-1 > -1 && x + 1 < maze[0].length && maze[y][x-1] == ' ' && maze[y][x] == 'O' &&  maze[y][x+1] == 'P');
    }
    public void clear() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                cn.getTextWindow().output(' ');
            }
        }
    }
}



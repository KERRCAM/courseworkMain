package com.company;

/*
database
users table (userid, username)
game info table (userid, time, efficiency, money, food, cap1, cap2, cap3, cap4)
scores table  (userid, time, efficiency, combined)
*/

public class game {
    public static String gMapInPlay[][] = new String[30][200];
    public boolean gameRunning = true;
    public static int gameInfo[] = {0,0,100,100}; //time, efficiency, money, food
    public static int factionInfo[][] = {{0,0},{0,0},{0,0},{0,0}}; //(cap1, regionOccNum1),(cap2, regionOccNum2),(cap3, regionOccNum3),(cap4, regionOccNum4)
    public static long initialTime;
    public static long finalTime;

    public static void gameLoop(String startType){
        if (startType == "load") {
            gameStart.loadGame();
        }
        if (startType == "new") {
            gameStart.newGame();
        }
    }


    public void gameLoop(){
        while (gameRunning == true) { //repeats player turn and enemy turns until player either saves and exits, wins or loses
            playerTurn();
            enemyTurns();
        }
    }


    public void playerTurn(){
        troopPlace();
        boolean exit = false;
        while (exit == false) {
            String option = Main.getString("what would you like to (enter number of action): \n (1)-invade region-  \n (2)-move troops- \n (3)-make troops- \n (4)-special attacks- \n (5)-save and exit-");
            if (option.equals("1")) {
                invadeRegion();
            }
            if (option.equals("2")) {
                moveTroops();
            }
            if (option.equals("3")) {
                makeTroops();
            }
            if (option.equals("4")) {
                specialAttacks();
            }
            if (option.equals("5")) {
                saveGame();
                exit = true;
            }
        }
    }


    public static void enemyTurns(){
        System.out.println("");
    }


    public void saveGame(){
        int userID = loginFunctions.userIDfinder();
        String saveName = "gameSave" + String.valueOf(userID) + ".txt"; //just overwrites save if already exists
        finalTime = System.currentTimeMillis();
        float sessionTime = ((finalTime - initialTime) / 1000) + game.gameInfo[0];
        int sessionTimeInt = (int)sessionTime;
        gameInfo[0] = sessionTimeInt; //updates time on save with current saved time plus current session time
        gameRunning = false; //stops the game loop
    }


    public void invadeRegion(){
        System.out.println("");
    }


    public void moveTroops(){
        int exitRegion = Main.getInt("what region would you like to move troops from?", 0, 50);
        int troopNum = Main.getInt("how many troops would you like to move from the region", 0, Integer.valueOF(gMapInPlay[Main.regionArmPos[exitRegion][0]][Main.regionArmPos[exitRegion][1]]));
        int targetRegion = Main.getInt("what region would you like to move troops to?", 0, 50);
    }


    public void makeTroops(){
        System.out.println("");
    }


    public void specialAttacks(){
        System.out.println("");
    }


    public void troopPlace(){ //gets user input for target region and troop amount, loops until all new troops are placed
        int newTroops = factionInfo[0][1];
        while (newTroops > 0) {
            System.out.println("you have" + newTroops + "new troops left to place");
            int targetRegion = Main.getInt("what region would you like to place some new troops in?",0, 50);
            if (gMapInPlay[Main.regionOccPos[targetRegion][0]][Main.regionOccPos[targetRegion][1]].equals("PL")) {
                int troopNum = Main.getInt("how many troops would you like to place in this region?", 0, newTroops);
                gMapInPlay[Main.regionArmPos[targetRegion][0]][Main.regionArmPos[targetRegion][1]] = gMapInPlay[Main.regionArmPos[targetRegion][0]][Main.regionArmPos[targetRegion][1]] + troopNum; //adds troops to whatever is already there using the navigation arrays
            }else {
                System.out.println("you don't control this region");
            }
        }
    }


    public void passiveGain(){
        System.out.println("");
    }




}





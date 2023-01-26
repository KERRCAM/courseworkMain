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
    public static int gameInfo[] = new int[8]; //time, efficiency, money, food, cap1, cap2, cap3, cap4
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


    public static void playerTurn(){
            System.out.println("");
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

    }


    public void moveTroops(){

    }


    public void specialAttack(){

    }


   //etc




}





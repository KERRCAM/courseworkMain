package com.company;

/*
database
users table (userid, username)
game info table (userid, time, effic, money, food, cap1, cap2, cap3, cap4)
scores table  (userid, time, effic, combined)
*/

public class game {
    public static String gMapInPlay[][] = new String[30][200];
    public boolean gameRunning = true;

    public static void gameLoop(String startType){
        if (startType == "load") {
            gameStart.loadGame();
        }
        if (startType == "new") {
            gameStart.newGame();
        }
    }


    public void gameLoop(){
        while (gameRunning == true) {
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
        //blah save shit
        gameRunning = false;
    }

}





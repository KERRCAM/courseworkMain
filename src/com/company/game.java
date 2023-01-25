package com.company;

public class game {
    public static String gMapInPlay[][] = new String[30][200];

    public static void gameLoop(String startType){
        if (startType == "load") {
            gameStart.loadGame();
        }
        if (startType == "new") {
            gameStart.newGame();
        }
    }





}

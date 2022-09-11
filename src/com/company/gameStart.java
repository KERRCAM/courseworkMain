package com.company;

public class gameStart {


    public static void loadGame() {
        int userID = loginFunctions.userIDfinder();
        String saveName = "gameSave" + String.valueOf(userID) + ".txt";
        System.out.println(saveName);
        //Main.fileToMap(saveName); //uncomment when code is rdy
    }


    public static void newGame(){
        //all goes in game save function instead
        //int userID = loginFunctions.userIDfinder();
        //int saveNo = userID
        //String saveName = "gameSave" + String.valueOf(saveNo);
        Main.fileToMap("gMap1.txt");
    }


}

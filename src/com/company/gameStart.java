package com.company;

public class gameStart {


    public static void loadGame() {
        int userID = loginFunctions.userIDfinder();
        String saveName = "gameSave" + String.valueOf(userID) + ".txt";
        System.out.println(saveName);
        //Main.fileToMap(saveName); //uncomment when code is rdy
        //game.mainGameLoop //class game and method main game loop not made yet
    }


    public static void newGame(){
        //all goes in game save function instead
        //int userID = loginFunctions.userIDfinder();
        //int saveNo = userID
        //String saveName = "gameSave" + String.valueOf(saveNo);
        Main.fileToMap("gMap1.txt");
        Main.printMap(game.gMapInPlay, 30, 200);
        int startRegion = Integer.parseInt(Main.getString("what region would you like to start in?")) - 1;
        game.gMapInPlay[Main.regionOccPos[startRegion][0]][Main.regionOccPos[startRegion][1]] = "PL";
        //




        //game.mainGameLoop // call once game start processes are done

    }


}

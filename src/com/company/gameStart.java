package com.company;

public class gameStart {


    public static void loadGame() {
        int userID = loginFunctions.userIDfinder();
        String saveName = "gameSave" + String.valueOf(userID) + ".txt";
        System.out.println(saveName);
        //Main.fileToMap(saveName); //uncomment when code is rdy
        //game.gameLoop //class game and method main game loop not made yet
        //need to run loops for how many regions each player/ai controls so it doesnt have to be stored in database
        //need to check for save not existing so no crash
    }


    public static void newGame(){

        Main.fileToMap("gMap1.txt");
        Main.printMap(game.gMapInPlay, 30, 200);
        int startRegion = Integer.parseInt(Main.getString("what region would you like to start in?")) - 1;
        game.gMapInPlay[Main.regionOccPos[startRegion][0]][Main.regionOccPos[startRegion][1]] = "PL";
        //game.gameLoop //class game and method main game loop not made yet
        //




        //game.gameLoop // call once game start processes are done

    }


}

package com.company;

public class gameStart {


    public static void loadGame() {
        int userID = loginFunctions.userIDfinder();
        String saveName = "gameSave" + String.valueOf(userID) + ".txt";
        System.out.println(saveName);
        //Main.fileToMap(saveName); //uncomment when code is rdy
        regionOccCounter(); //counts number of regions controlled by each faction and adjusts game faction information array
        //need to check for save not existing so no crash - kick to menu if doesnt exist
        game.initialTime = System.currentTimeMillis();
        //game.gameLoop //class game and method main game loop not made yet
    }


    public static void newGame(){

        Main.fileToMap("gMap1.txt");
        Main.printMap(game.gMapInPlay, 30, 200);
        int startRegion = Integer.parseInt(Main.getString("what region would you like to start in?")) - 1;
        game.gMapInPlay[Main.regionOccPos[startRegion][0]][Main.regionOccPos[startRegion][1]] = "PL";
        game.initialTime = System.currentTimeMillis();
        //game.gameLoop //class game and method main game loop not made yet
        //




        //game.gameLoop // call once game start processes are done

    }


    public static void regionOccCounter(){
        for (int i = 0; i < 49; i++) {
            String occupation = game.gMapInPlay[Main.regionOccPos[i][0]][Main.regionOccPos[i][1]];
            if (occupation.equals("P1")) {
                game.factionInfo[0][0] = game.factionInfo[0][0] + 1;
            }
            if (occupation.equals("P2")) {
                game.factionInfo[1][0] = game.factionInfo[1][0] + 1;
            }
            if (occupation.equals("P3")) {
                game.factionInfo[2][0] = game.factionInfo[2][0] + 1;
            }
            if (occupation.equals("P4")) {
                game.factionInfo[3][0] = game.factionInfo[3][0] + 1;
            }
        }
    }

}

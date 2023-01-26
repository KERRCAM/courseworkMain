package com.company;

public class gameStart {


    public static void loadGame() {
        int userID = loginFunctions.userIDfinder();
        String saveName = "gameSave" + String.valueOf(userID) + ".txt";
        System.out.println(saveName);
        //Main.fileToMap(saveName); //uncomment when try catch for no existing file is done
        regionOccCounter(); //counts number of regions controlled by each faction and adjusts game faction information array
        //need to check for save not existing so no crash - kick to menu if doesnt exist or try catch (probably best)
        game.initialTime = System.currentTimeMillis();
        //game.gameLoop //uncomment when game loop is rdy
    }


    public static void newGame(){

        Main.fileToMap("gMap1.txt");
        Main.printMap(game.gMapInPlay, 30, 200);
        int startRegion = Integer.parseInt(Main.getString("what region would you like to start in?")) - 1;
        game.gMapInPlay[Main.regionOccPos[startRegion][0]][Main.regionOccPos[startRegion][1]] = "PL";
        game.initialTime = System.currentTimeMillis();
        //game.gameLoop //uncomment when game loop is rdy





    }


    public static void regionOccCounter(){
        //int count = 0; count and all related in this method is for test run of empty map
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
            /*
            if (occupation.equals("NC")) {
                count++;
                System.out.println("yes");
            }else{
                System.out.println("no");
            }
            */
        }
        //System.out.println(count);
    }


}

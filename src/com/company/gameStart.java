package com.company;

import java.util.Random;

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
        capitolAssignment();
        game.initialTime = System.currentTimeMillis();
        game.gameLoop(); //uncomment when game loop is rdy





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


    public static void capitolAssignment(){
        int p2Start = 0;
        int p3Start = 0;
        int p4Start = 0;
        Main.printMap(game.gMapInPlay, 30, 200);
        int startRegion = Main.getInt("what region would like to start in?", 0, 50);
        p2Start = uniqueRandomNum(startRegion, p2Start, p3Start, p4Start);
        p3Start = uniqueRandomNum(startRegion, p2Start, p3Start, p4Start);
        p4Start = uniqueRandomNum(startRegion, p2Start, p3Start, p4Start);
        game.gMapInPlay[Main.regionOccPos[startRegion - 1][0]][Main.regionOccPos[startRegion - 1][1]] = "P1";
        game.gMapInPlay[Main.regionOccPos[p2Start - 1][0]][Main.regionOccPos[p2Start - 1][1]] = "P2";
        game.gMapInPlay[Main.regionOccPos[p3Start - 1][0]][Main.regionOccPos[p3Start - 1][1]] = "P3";
        game.gMapInPlay[Main.regionOccPos[p4Start - 1][0]][Main.regionOccPos[p4Start - 1][1]] = "P4";
    }


    public static int uniqueRandomNum(int p1, int p2, int p3, int p4){
        int region = 0;
        boolean valid = false;
        Random random = new Random();
        while (valid == false){
            region = random.nextInt(49);
            if (region != p1 && region != p2 && region != p3 && region != p4){
                valid = true;
            }
        }
        return(region);
    }


}

package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class gameStart {


    //used to load saved games from text files and the database
    public static void loadGame() {
        boolean save = true; // will become false if a save doesnt exist preventing a false game start
        game.gameDone = false; //will become true when a game is won or lost
        game.gameRunning = true; //sets the game loop to be active
        int userID = loginFunctions.userIDfinder(); //gets user ID
        Main.fileContentsUsers.clear(); //have to clear the array list after to prevent errors
        String saveName = "gameSave" + String.valueOf(userID) + ".txt"; //game save names decided by ID
        System.out.println(saveName); //a check
        try {
            Main.fileToMap(saveName);
        } catch (Exception e) {
            System.out.println("you have no saved game");
            save = false;
            e.printStackTrace();
        }
        if (save == true){ //only runs if a save file was found
            regionOccCounter(); //counts number of regions controlled by each faction and adjusts game faction information array
            game.initialTime = System.currentTimeMillis(); //gets time of when the game starts
            Main.printMap(game.gMapInPlay, 30, 200);
            game.gameLoop(); //calls main game loop
        }
    }


    //used for starting any new games. note a user cant have multiple saves so this will overwrite any save they have
    public static void newGame(){
        game.gameDone = false;
        game.gameRunning = true;
        Main.fileToMap("gMap1.txt"); //loads the saved starting state map into the active map
        capitolAssignment(); //picks capitols
        game.initialTime = System.currentTimeMillis();
        game.gameLoop();
    }


    //used to count the number of regions the player and each enemy has in their control and updates faction info with this data
    public static void regionOccCounter(){
        //int count = 0; count and all related in this method is for test run of empty map
        for (int i = 0; i < 49; i++) {
            String occupation = game.gMapInPlay[Main.regionOccPos[i][0]][Main.regionOccPos[i][1]];
            if (occupation.equals("P1")) {
                game.factionInfo[0][1] = game.factionInfo[0][1] + 1;
            }
            if (occupation.equals("P2")) {
                game.factionInfo[1][1] = game.factionInfo[1][1] + 1;
            }
            if (occupation.equals("P3")) {
                game.factionInfo[2][1] = game.factionInfo[2][1] + 1;
            }
            if (occupation.equals("P4")) {
                game.factionInfo[3][1] = game.factionInfo[3][1] + 1;
            }
            /* //test print
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


    //used to pick out the capitols for each player / enemy at the start of a new game. Note capitols only dictate starting position due to changes in game design
    public static void capitolAssignment(){
        int p2Start = 0;
        int p3Start = 0;
        int p4Start = 0;
        Main.printMap(game.gMapInPlay, 30, 200);
        int startRegion = Main.getInt("what region would like to start in?", 0, 50); //gets the users input for their stating region
        p2Start = uniqueRandomNum(startRegion, p2Start, p3Start, p4Start); //randomly selects unique start region for the 3 enemies
        p3Start = uniqueRandomNum(startRegion, p2Start, p3Start, p4Start);
        p4Start = uniqueRandomNum(startRegion, p2Start, p3Start, p4Start);
        game.gMapInPlay[Main.regionOccPos[startRegion - 1][0]][Main.regionOccPos[startRegion - 1][1]] = "P1"; //changes the map to have the occupation of the capitols
        game.gMapInPlay[Main.regionOccPos[p2Start - 1][0]][Main.regionOccPos[p2Start - 1][1]] = "P2";
        game.gMapInPlay[Main.regionOccPos[p3Start - 1][0]][Main.regionOccPos[p3Start - 1][1]] = "P3";
        game.gMapInPlay[Main.regionOccPos[p4Start - 1][0]][Main.regionOccPos[p4Start - 1][1]] = "P4";
        game.gMapInPlay[Main.regionArmPos[startRegion - 1][0]][Main.regionArmPos[startRegion - 1][1]] = "05"; //changes the map to have 5 troops at each of the capitols
        game.gMapInPlay[Main.regionArmPos[p2Start - 1][0]][Main.regionArmPos[p2Start - 1][1]] = "05";
        game.gMapInPlay[Main.regionArmPos[p3Start - 1][0]][Main.regionArmPos[p3Start - 1][1]] = "05";
        game.gMapInPlay[Main.regionArmPos[p4Start - 1][0]][Main.regionArmPos[p4Start - 1][1]] = "05";
        game.factionInfo[0][0] = startRegion; //adds the capitols to faction info
        game.factionInfo[1][0] = p2Start;
        game.factionInfo[2][0] = p3Start;
        game.factionInfo[3][0] = p4Start;
        Main.printMap(game.gMapInPlay, 30, 200);
    }


    //used to ensure all 3 enemy starting locations are unique
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

package com.company;

/*
database
users table (userid, username)
game info table (userid, time, efficiency, money, food, cap1, cap2, cap3, cap4)
scores table  (userid, time, efficiency, combined)
*/

import java.util.Scanner;

public class game {
    public static String gMapInPlay[][] = new String[30][200];
    public boolean gameRunning = true;
    public static int gameInfo[] = {0,0,100,100}; //time, efficiency, money, food
    public static int factionInfo[][] = {{0,0},{0,0},{0,0},{0,0}}; //(cap1, regionOccNum1),(cap2, regionOccNum2),(cap3, regionOccNum3),(cap4, regionOccNum4)
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


    public void gameLoop(){ // avoids recursion as it could cause memory issues in a long-lasting game where the player has many turns
        while (gameRunning == true) { //repeats player turn and enemy turns until player either saves and exits, wins or loses
            playerTurn();
            enemyTurns();
        }
    }


    public void playerTurn(){
        passiveGain(); // start off each turn begins with placing the troops passively gained and also adding the money and food gained
        boolean exit = false;
        while (exit == false) {
            String option = Main.getString("what would you like to (enter number of action): \n (1)-invade region-  \n (2)-move troops- \n (3)-make troops- \n (4)-special attacks- \n (5)-save and exit-");
            if (option.equals("1")) {
                invadeRegion();
            }
            if (option.equals("2")) {
                moveTroops();
            }
            if (option.equals("3")) {
                makeTroops();
            }
            if (option.equals("4")) {
                specialAttacks();
            }
            if (option.equals("5")) {
                saveGame();
                exit = true;
            }
        }
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


    public void invadeRegion(){ // can only attack once a turn, and you must share a border
        int exitRegion = Main.getInt("what region would you like to move troops from?", 0, 50);
        int troopNum = Main.getInt("how many troops would you like to move from the region", 0, Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion][0]][Main.regionArmPos[exitRegion][1]])); //limit of current troop count of that region ensures there must be at least 1 troop left in the region
        int targetRegion = Main.getInt("what region would you like to move troops to?", 0, 50);
        checkRegionBorderValid(exitRegion, Main.regionBorderAmounts[exitRegion], "PL");
    }


    public void moveTroops(){ // can move troops as much as you want in a turn between any of your controlled regions
        int exitRegion = Main.getInt("what region would you like to move troops from?", 0, 50);
        int troopNum = Main.getInt("how many troops would you like to move from the region", 0, Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]])); //limit of current troop count of that region ensures there must be at least 1 troop left in the region
        int targetRegion = Main.getInt("what region would you like to move troops to?", 0, 50);
        gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]] = String.valueOf(Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]]) - troopNum); // takes troops of region they are being moved from
        gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]] = String.valueOf(Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]]) + troopNum); // adds troops taken off onto the region they are being moved to
    }


    public void makeTroops(){
        int limit = 0; // the limit is got from getting the limit of how many troops can be made from the current food and money the player has and taking the lower value
        System.out.println("you have " + gameInfo[2] + " money");
        System.out.println("you have " + gameInfo[3] + " food");
        int moneyLim = gameInfo[2] / 10;
        int foodLim = gameInfo[3] / 10;
        if (moneyLim < foodLim) {
            limit = moneyLim;
        }else {
            limit = foodLim;
        }
        int newTroops = Main.getInt("how many troops would you like to make?", 0, limit); // limit is then used on get int so player cant try get more than they can afford
        troopPlace(newTroops); // calls troop place with number of troops made
    }


    public void specialAttacks(){
        System.out.println("");
    }


    public void troopPlace(int troopsToPlace){ //gets user input for target region and troop amount, loops until all new troops are placed
        int newTroops = troopsToPlace;
        while (newTroops > 0) {
            System.out.println("you have " + newTroops + " new troops left to place");
            int targetRegion = Main.getInt("what region would you like to place some new troops in?",0, 50);
            if (gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]].equals("PL")) {
                int troopNum = Main.getInt("how many troops would you like to place in this region?", 0, newTroops);
                gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]] = String.valueOf(Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]]) + troopNum); //adds troops to whatever is already there using the navigation arrays
            }else {
                System.out.println("you don't control this region");
            }
        }
    }


    public void passiveGain(){ //gets the troops food and money gained passively from controlled regions
        System.out.println("");
    }


    public static int checkRegionBorderValid(int exitRegion, int borderNum, String attacker) { //checks for a valid attack target when invading
        boolean validity = false;

        return (vaildity);
    }


}





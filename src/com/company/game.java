package com.company;

/*
database
users table (userid, username)
game info table (userid, time, efficiency, money, food, cap1, cap2, cap3, cap4)
scores table  (userid, time, efficiency, combined)
*/

import java.util.ArrayList;

public class game {
    public static String gMapInPlay[][] = new String[30][200];
    public static boolean gameRunning = true;
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


    public static void gameLoop(){ // avoids recursion as it could cause memory issues in a long-lasting game where the player has many turns
        while (gameRunning == true) { //repeats player turn and enemy turns until player either saves and exits, wins or loses
            playerTurn();
            enemyTurns();
        }
    }


    public static void playerTurn(){
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
        //do passive gain for a region then invasion for region
        //needs checks for if player is actually still alive before calling their methods
        //simple run of occ counter in game start and 0 = out
        //all 0 then player has won
    }


    public static void enemyInvasion(String attacker){
        ArrayList<Integer> occRegions = new ArrayList<>(); //all regions attacker controls
        ArrayList<Integer> possibleTargetRegions = new ArrayList<>(); //all regions that border an occupied region
        ArrayList<Integer> validRangeTargetRegions = new ArrayList<>(); //all unique enemy regions that border an occupied region
        ArrayList<Integer> validTargetRegions = new ArrayList<>(); //all enemy regions bordering occupied region that can be invaded i.e. the occupied region has more troops than the target region
        for (int i = 0; i < 50; i++) {
            if (gMapInPlay[Main.regionOccPos[i][0]][Main.regionOccPos[i][1]].equals(attacker)){
                occRegions.add(i);
            }
        }
        for (int i = 0; i < occRegions.size(); i++) {
            for (int j = 0; j < Main.regionBorderAmounts[occRegions.get(i)]; j++) {
                if(!(gMapInPlay[Main.regionOccPos[Main.regionSharedBorders[occRegions.get(i)][j]][0]][Main.regionOccPos[Main.regionSharedBorders[occRegions.get(i)][j]][1]].equals(attacker))){
                    if (!(possibleTargetRegions.contains(Main.regionSharedBorders[occRegions.get(i)][j]))){
                        validRangeTargetRegions.add(Main.regionSharedBorders[occRegions.get(i)][j]);
                    }
                }
            }
        }
        //loop for last array list fill
        //calc resistance of each option
        //pick random lowest resistance option or pick random from common lowest
        //perform the invasion
    }


    public static void saveGame(){
        int userID = loginFunctions.userIDfinder();
        String saveName = "gameSave" + String.valueOf(userID) + ".txt"; //just overwrites save if already exists
        finalTime = System.currentTimeMillis();
        float sessionTime = ((finalTime - initialTime) / 1000) + game.gameInfo[0];
        int sessionTimeInt = (int)sessionTime;
        gameInfo[0] = sessionTimeInt; //updates time on save with current saved time plus current session time
        gameRunning = false; //stops the game loop
    }


    public static void invadeRegion(){ // can only attack once a turn, and you must share a border
        int exitRegion = Main.getInt("what region would you like to move troops from?", 0, 50);
        int troopNum = Main.getInt("how many troops would you like to move from the region", 0, Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]])); //limit of current troop count of that region ensures there must be at least 1 troop left in the region
        int targetRegion = Main.getInt("what region would you like to move troops to?", 0, 50);
        boolean valid = checkRegionBorderValid(exitRegion, targetRegion, Main.regionBorderAmounts[exitRegion - 1], "P1");
        if (valid == true && gMapInPlay[Main.regionOccPos[exitRegion - 1][0]][Main.regionOccPos[exitRegion - 1][1]].equals("P1") && troopNum > Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]])){
            String newExit = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]]) - troopNum);
            gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]] = newExit; // takes troops of region they are being moved from
            String newTarget = mapArmyAdj(troopNum - Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]]));
            gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]] = newTarget; // takes away teh number of troops at target region from the invading force
            gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]] = ("P1");
        }
        Main.printMap(game.gMapInPlay, 30, 200);
    }


    public static String mapArmyAdj(int newValue){ //used to adjust ary values below 10 like "4" into format "04"
        String army = String.valueOf(newValue);
        if (newValue < 10) {
            army = "0" + army;
        }
        return (army);
    }


    public static void moveTroops(){ // can move troops as much as you want in a turn between any of your controlled regions
        int exitRegion = Main.getInt("what region would you like to move troops from?", 0, 50);
        int troopNum = Main.getInt("how many troops would you like to move from the region", 0, Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]])); //limit of current troop count of that region ensures there must be at least 1 troop left in the region
        int targetRegion = Main.getInt("what region would you like to move troops to?", 0, 50);
        if (gMapInPlay[Main.regionOccPos[exitRegion - 1][0]][Main.regionOccPos[exitRegion - 1][1]].equals("PL") && gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]].equals("PL")){
            String newExit = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]]) - troopNum);
            gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]] = newExit; // takes troops of region they are being moved from
            String newTarget = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]]) + troopNum);
            gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]] = newTarget; // adds troops taken off onto the region they are being moved to
            }
        Main.printMap(game.gMapInPlay, 30, 200);
       }


    public static void makeTroops(){
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


    public static void specialAttacks(){
        System.out.println("");
    }


    public static void troopPlace(int troopsToPlace){ //gets user input for target region and troop amount, loops until all new troops are placed
        int newTroops = troopsToPlace;
        while (newTroops > 0) {
            System.out.println("you have " + newTroops + " new troops left to place");
            int targetRegion = Main.getInt("what region would you like to place some new troops in?",0, 50);
            if (gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]].equals("PL")) {
                int troopNum = Main.getInt("how many troops would you like to place in this region?", 0, newTroops);
                String newTarget = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]]) + troopNum);
                gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]] = newTarget; //adds troops to whatever is already there using the navigation arrays
            }else {
                System.out.println("you don't control this region");
            }
            Main.printMap(game.gMapInPlay, 30, 200);
        }
    }


    public static void passiveGain(){ //gets the troops food and money gained passively from controlled regions 1 troop 10 food money, x3 for holding a city farm mine
        System.out.println("");
    }


    public static boolean checkRegionBorderValid(int exitRegion, int targetRegion, int borderNum, String attacker) { //checks for a valid attack target when invading
        String occupation = gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]];
        boolean validity =  false;
        for (int i = 0; i < borderNum - 1; i++) {
            int current = Main.regionSharedBorders[exitRegion - 1][i];
            if (current == targetRegion && (occupation.equals("P1") || occupation.equals("P2") || occupation.equals("P3") || occupation.equals("P4") || occupation.equals("NC") && !(occupation.equals(attacker)))) {
                validity = true;
            }
        }
        if (validity == false){
            System.out.println("invalid attack target");
        }
        return (validity);
    }


}





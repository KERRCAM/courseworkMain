package com.company; //bugs to work out: region 49 invasions for player and enemy - map army adj for 10 is still adding 0?

/*
database
users table (userid, username)
game info table (userid, time, efficiency, money, food)
scores table  (userid, time, efficiency, combined)
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class game {
    public static String gMapInPlay[][] = new String[30][200];
    public static boolean gameRunning = true;
    public static int gameInfo[] = {0,0,100,100}; //time, efficiency, money, food
    public static int factionInfo[][] = {{0,0},{0,0},{0,0},{0,0}}; //(cap1, regionOccNum1),(cap2, regionOccNum2),(cap3, regionOccNum3),(cap4, regionOccNum4)
    public static long initialTime;
    public static long finalTime;
    public static boolean gameDone = false;

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
        gameInfo[1] = gameInfo[1] + 1;
        passiveGain(); // start off each turn begins with placing the troops passively gained and also adding the money and food gained
        boolean exit = false;
        while (exit == false) {
            String option = Main.getString("what would you like to (enter number of action): \n (1)-invade region-  \n (2)-move troops- \n (3)-make troops- \n (4)-special attacks- \n (5)-end turn- \n (6)-save and exit-");
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
                exit = true;
            }
            if (option.equals("6")) {
                saveGame();
                exit = true;
            }
        }
    }


    public static void enemyTurns(){
        gameStart.regionOccCounter();
        if (factionInfo[1][1] > 0){
            enemyPassiveGain("P2");
            enemyInvasion("P2");
        }
        if (factionInfo[2][1] > 0){
            enemyPassiveGain("P3");
            enemyInvasion("P3");
        }
        if (factionInfo[3][1] > 0){
            enemyPassiveGain("P4");
            enemyInvasion("P4");
        }
        Main.printMap(game.gMapInPlay, 30, 200);
        if (factionInfo[0][1] == 49){
            gameRunning = false;
            gameDone = true;
        }
    }


    public static void enemyInvasion(String attacker){
        ArrayList<Integer> occRegions = new ArrayList<>(); //all regions attacker controls
        ArrayList<Integer> possibleTargetRegions = new ArrayList<>(); //all regions that border an occupied region
        ArrayList<Integer> validRangeTargetRegions = new ArrayList<>(); //all unique enemy regions that border an occupied region
        ArrayList<Integer> validRangeExitRegions = new ArrayList<>(); //corresponding exit region for the target region
        ArrayList<Integer> validTargetRegions = new ArrayList<>(); //all enemy regions bordering occupied region that can be invaded i.e. the occupied region has more troops than the target region
        ArrayList<Integer> validExitRegions = new ArrayList<>(); //corresponding exit region for the target region
        ArrayList<Integer> armyDiff = new ArrayList<>(); //the difference between the troop numbers in the target and exit regions
        int target = 0; //index of target region
        Random random = new Random(); //fills all regions occupied by the attacker
        for (int i = 0; i < 49; i++) {
            if (gMapInPlay[Main.regionOccPos[i][0]][Main.regionOccPos[i][1]].equals(attacker)){
                occRegions.add(i);
            }
        }
        for (int i = 0; i < occRegions.size(); i++) { //fills all the regions that border the occupied regions
            for (int j = 0; j < Main.regionBorderAmounts[occRegions.get(i)]; j++) {
                if(!(gMapInPlay[Main.regionOccPos[Main.regionSharedBorders[occRegions.get(i)][j] - 1][0]][Main.regionOccPos[Main.regionSharedBorders[occRegions.get(i)][j] - 1][1]].equals(attacker))){
                    if (!(possibleTargetRegions.contains(Main.regionSharedBorders[occRegions.get(i)][j]))){
                        validRangeTargetRegions.add(Main.regionSharedBorders[occRegions.get(i)][j] - 1); //index adjust
                        //System.out.println(Main.regionSharedBorders[occRegions.get(i)][j]);
                        //System.out.println("");
                        validRangeExitRegions.add(occRegions.get(i));
                        //System.out.println(occRegions.get(i));
                    }
                }
            }
        }
        for (int i = 0; i < validRangeTargetRegions.size(); i++) { //fills all the regions that border the attacker and can be invaded
            if (Integer.parseInt(gMapInPlay[Main.regionArmPos[validRangeExitRegions.get(i)][0]][Main.regionArmPos[validRangeExitRegions.get(i)][1]]) - (Integer.parseInt(gMapInPlay[Main.regionArmPos[validRangeTargetRegions.get(i)][0]][Main.regionArmPos[validRangeTargetRegions.get(i)][1]]) + 1) > 0){
                validTargetRegions.add((validRangeTargetRegions.get(i)));
                validExitRegions.add(validRangeExitRegions.get(i));
                armyDiff.add(Integer.parseInt(gMapInPlay[Main.regionArmPos[validRangeExitRegions.get(i)][0]][Main.regionArmPos[validRangeExitRegions.get(i)][1]]) - Integer.parseInt(gMapInPlay[Main.regionArmPos[validRangeTargetRegions.get(i)][0]][Main.regionArmPos[validRangeTargetRegions.get(i)][1]]));
            }
        }
        if (armyDiff.size() > 0){ //only attempts to perform an invasion if there has been at least 1 valid target found
            for (int i = 0; i < armyDiff.size(); i++) { //fills the resistance values for each possible attack
                int lowestResistance = 0;
                if (armyDiff.get(i) > lowestResistance){
                    lowestResistance = armyDiff.get(i);
                    target = i;
                }if (armyDiff.get(i) == lowestResistance){
                    int ran = random.nextInt(2); //1 or 2
                    if (ran == 1){
                        target = i;
                    }
                }
            }
            //statements for performing the actual invasions once all checks are done and target is picked
            String newTarget = mapArmyAdj((Integer.parseInt(gMapInPlay[Main.regionArmPos[validExitRegions.get(target)][0]][Main.regionArmPos[validExitRegions.get(target)][1]]) - 1) - Integer.valueOf(gMapInPlay[Main.regionArmPos[validTargetRegions.get(target)][0]][Main.regionArmPos[validTargetRegions.get(target)][1]]));
            gMapInPlay[Main.regionArmPos[validTargetRegions.get(target)][0]][Main.regionArmPos[validTargetRegions.get(target)][1]] = newTarget;
            gMapInPlay[Main.regionOccPos[validTargetRegions.get(target)][0]][Main.regionOccPos[validTargetRegions.get(target)][1]] = attacker;
            gMapInPlay[Main.regionArmPos[validExitRegions.get(target)][0]][Main.regionArmPos[validExitRegions.get(target)][1]] = "01";
        }
    }


    public static void enemyPassiveGain(String playerNum){
        String newTroopNum = "";
        for (int i = 0; i < 49; i++) {
            String occupation = game.gMapInPlay[Main.regionOccPos[i][0]][Main.regionOccPos[i][1]];
            if (occupation.equals(playerNum)) {
                if (Main.regionExtraInfo[i][2] == 1) {
                    newTroopNum = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[i][0]][Main.regionArmPos[i][1]]) + 3);
                    gMapInPlay[Main.regionArmPos[i][0]][Main.regionArmPos[i][1]] = newTroopNum; //adds 3 troops to each controlled region containing a city
                } else {
                    newTroopNum = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[i][0]][Main.regionArmPos[i][1]]) + 1);
                    gMapInPlay[Main.regionArmPos[i][0]][Main.regionArmPos[i][1]] = newTroopNum; //adds 1 troop to each controlled region
                }
            }
        }
    }


    public static void saveGame(){
        int userID = loginFunctions.userIDfinder();
        String saveName = "gameSave" + String.valueOf(userID) + ".txt"; //just overwrites save if already exists
        finalTime = System.currentTimeMillis();
        float sessionTime = ((finalTime - initialTime) / 1000) + game.gameInfo[0];
        int sessionTimeInt = (int)sessionTime;
        gameInfo[0] = sessionTimeInt; //updates time on save with current saved time plus current session time
        gameRunning = false; //stops the game loop
        String DatabaseLocation = System.getProperty("user.dir") + "\\courseworkDatabase.accdb";
        try {
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://" + DatabaseLocation, "", "");
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            // also needs to be insert when user is made and then update here
            stmt.executeUpdate("UPDATE gameInfo set time = '"+gameInfo[0]+"', efficiency = '"+gameInfo[1]+"', money = '"+gameInfo[2]+"', food = '"+gameInfo[3]+"' WHERE ID = '"+userID+"'");
            con.close();
        } catch (Exception e) {
            System.out.println("Error in the SQL class: " + e);
        }
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
        System.out.println(loginFunctions.userIDfinder());
    }


    public static void troopPlace(int troopsToPlace){ //gets user input for target region and troop amount, loops until all new troops are placed
        int newTroops = troopsToPlace;
        while (newTroops > 0) {
            System.out.println("you have " + newTroops + " new troops left to place");
            int targetRegion = Main.getInt("what region would you like to place some new troops in?",0, 50);
            if (gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]].equals("P1")) {
                int troopNum = Main.getInt("how many troops would you like to place in this region?", 0, newTroops);
                String newTarget = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]]) + troopNum);
                gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]] = newTarget; //adds troops to whatever is already there using the navigation arrays
                newTroops = newTroops - troopNum;
            }else {
                System.out.println("you don't control this region");
            }
            Main.printMap(game.gMapInPlay, 30, 200);
        }
    }


    public static void passiveGain(){ //gets the troops food and money gained passively from controlled regions 1 troop 10 food money, x3 for holding a city farm mine
        int newTroops = 0;
        for (int i = 0; i < 49; i++) {
            String occupation = game.gMapInPlay[Main.regionOccPos[i][0]][Main.regionOccPos[i][1]];
            if (occupation.equals("P1")) {
                if (Main.regionExtraInfo[i][2] == 1){
                    newTroops = newTroops + 3; //adds 3 troops for each controlled region containing a city
                }else{
                    newTroops = newTroops + 1; //adds 1 troop for each controlled region
                }
                if (Main.regionExtraInfo[i][1] == 1){
                    gameInfo[3] = gameInfo[3] + 30; //adds 30 food for each controlled region containing a farm
                }else{
                    gameInfo[3] = gameInfo[3] + 10; //adds 10 food for each controlled region
                }
                if (Main.regionExtraInfo[i][4] == 1){
                    gameInfo[2] = gameInfo[3] + 30; //adds 30 money for each controlled region containing a mine
                }else{
                    gameInfo[2] = gameInfo[3] + 10; //adds 10 money for each controlled region
                }
            }
        }
        troopPlace(newTroops);
    }


    public static boolean checkRegionBorderValid(int exitRegion, int targetRegion, int borderNum, String attacker) { //checks for a valid attack target when invading
        String occupation = gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]]; //might need to add -1 back to target region
        boolean validity =  false;
        for (int i = 0; i < borderNum; i++) {
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





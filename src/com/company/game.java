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
    public static String gMapInPlay[][] = new String[30][200]; //the active map used for any ongoing games
    public static boolean gameRunning = true; //state of game running
    public static int gameInfo[] = {0,0,100,100}; //time, efficiency, money, food (players start with 100 money and food)
    public static int factionInfo[][] = {{0,0},{0,0},{0,0},{0,0}}; //(cap1, regionOccNum1),(cap2, regionOccNum2),(cap3, regionOccNum3),(cap4, regionOccNum4)
    public static long initialTime; //time when user starts their game
    public static long finalTime; //time when user ends their game
    public static boolean gameDone = false; //state of the game being complete (saving is not completing the game, only winning or losing)


    //initial call to decide if game is being started from new or loaded (directed to game start)
    public static void gameLoopStart(String startType){
        if (startType == "load") {
            gameStart.loadGame();
        }
        if (startType == "new") {
            gameStart.newGame();
        }
    }


    //the main game loop of the game
    public static void gameLoop(){ // avoids recursion as it could cause memory issues in a long-lasting game where the player has many turns
        while (gameRunning == true) { //repeats player turn and enemy turns until player either saves and exits, wins or loses
            playerTurn();
            enemyTurns();
        }
        if (gameDone == true){ //checks if game has been won or lost
            endGame();
        }
    }


    //called when gameDone becomes true meaning the game was either won or lost
    public static void endGame(){
        finalTime = System.currentTimeMillis(); //gets the time of when the game ended
        float sessionTime = (((finalTime - initialTime) / 1000) + gameInfo[0]); //calculates the time that the player was playing for
        gameInfo[0] = gameInfo[0] + (int)sessionTime; //updates the time score for game info before its put into the scores table on teh database
        if (factionInfo[0][1] == 0){//checks if game was lost
            System.out.println("ALL FRIENDLY REGIONS HAVE BEEN INVADED, YOU LOSE"); // if game was lost
        }
        if (factionInfo[0][1] == 49) {//checks if the game was won
            System.out.println("ALL REGIONS UNDER FRIENDLY CONTROL, YOU WIN"); // if game was won
            System.out.println("you won in " + gameInfo[0] + " seconds, " + gameInfo[1] + " moves and with " + gameInfo[2] + " money and " + gameInfo[3] + " food left"); //tells the user the stats they won the game with
            double combinedScoreDecimal = ((gameInfo[2] + gameInfo[3]) / (gameInfo[1] + (0.1 * gameInfo[0]))) * 1000; //formula for generating an overall score based on money food time and efficiency, money and food are equally weighted and efficiency is weighted more than time (for comparative scores)
            int combinedScore = (int)Math.rint(combinedScoreDecimal); //turns combined score from float into an integer
            String DatabaseLocation = System.getProperty("user.dir") + "\\courseworkDatabase.accdb";
            int userID = loginFunctions.userIDfinder() + 1; //gets user ID
            Main.fileContentsUsers.clear(); //clears users contents list
            try {
                Connection con = DriverManager.getConnection("jdbc:ucanaccess://" + DatabaseLocation, "", "");
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmt.executeUpdate("UPDATE scores set time = '"+gameInfo[0]+"', efficiency = '"+gameInfo[1]+"', combined = '"+combinedScore+"' WHERE ID = '"+userID+"'"); //sql update statement for updating scores table
                con.close();
            } catch (Exception e) {
                System.out.println("Error in the SQL class: " + e);
            }
        }
    }


    //main menu with all the move options available to the player during their turn
    public static void playerTurn(){
        gameInfo[1] = gameInfo[1] + 1; //iterate the players efficiency score
        passiveGain(); // start off each turn begins with placing the troops passively gained and also adding the money and food gained
        boolean exit = false;
        boolean invasion = false; //becomes true when the player does an invasion, this limits the player to one invasion per turn like the enemies
        while (exit == false) {
            String option = Main.getString("what would you like to (enter number of action): \n (1)-invade region-  \n (2)-move troops- \n (3)-make troops- \n (4)-special attacks (N/A)- \n (5)-end turn- \n (6)-save and exit-");
            if (option.equals("1")) { //checks if user has already done an invasion this turn
                if (invasion == true){
                    System.out.println("you can only invade once per turn");
                }else{
                    invadeRegion();
                    invasion = true;
                }
            }
            if (option.equals("2")) {
                moveTroops();
            }
            if (option.equals("3")) {
                makeTroops();
            }
            if (option.equals("4")) {//more of a check method used since special attacks didnt end up getting used
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


    //loop for all 3 enemy turns, also checks if the game is won or lost after a player turn and if an enemy has been knocked out (so that their turn is skipped)
    public static void enemyTurns(){
        gameStart.regionOccCounter();
        if (factionInfo[0][1] == 49 || factionInfo[0][1] == 0){//check for if game is won or lost
            gameRunning = false; //stops game loop
            gameDone = true; //sets game as complete
        }
        if (factionInfo[1][1] > 0){ //checks if P2 is eliminated
            enemyPassiveGain("P2");//does a passive gain on each enemy before their invasion attempt
            enemyInvasion("P2");//invasion attempt
        }
        if (factionInfo[2][1] > 0){ //checks if P3is eliminated
            enemyPassiveGain("P3");
            enemyInvasion("P3");
        }
        if (factionInfo[3][1] > 0){ //checks if P3 is eliminated
            enemyPassiveGain("P4");
            enemyInvasion("P4");
        }
        Main.printMap(game.gMapInPlay, 30, 200); //prints map so user can see what happened on the enemy turns
    }


    //very large method for running the many checks to determine if there is any valid invasion targets for an enemy
    //if there is a valid target or targets the option of the least resistance then needs to be selected
    //lastly if an invasion was found its performed and if not the enemy passes their turn
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
            if (gMapInPlay[Main.regionOccPos[i][0]][Main.regionOccPos[i][1]].equals(attacker)){ //checks if region is owned by attacker
                occRegions.add(i); //adds to list if so
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
        for (int i = 0; i < validRangeTargetRegions.size(); i++) { //fills all the regions that border the attacker and can be invaded (the exit region has more troops than the target region)
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
                    int ran = random.nextInt(2); //random number 1 or 2
                    if (ran == 1){
                        target = i;
                    }
                }
            }
            //statements for performing the actual invasions once all checks are done and target is picked
            String newTarget = mapArmyAdj((Integer.parseInt(gMapInPlay[Main.regionArmPos[validExitRegions.get(target)][0]][Main.regionArmPos[validExitRegions.get(target)][1]]) - 1) - Integer.valueOf(gMapInPlay[Main.regionArmPos[validTargetRegions.get(target)][0]][Main.regionArmPos[validTargetRegions.get(target)][1]]));
            gMapInPlay[Main.regionArmPos[validTargetRegions.get(target)][0]][Main.regionArmPos[validTargetRegions.get(target)][1]] = newTarget;
            gMapInPlay[Main.regionOccPos[validTargetRegions.get(target)][0]][Main.regionOccPos[validTargetRegions.get(target)][1]] = attacker;
            gMapInPlay[Main.regionArmPos[validExitRegions.get(target)][0]][Main.regionArmPos[validExitRegions.get(target)][1]] = "01"; //can be set to 1 as enemies will always invade at maximum force leaving only 1 troop behind
        }
    }


    //looks at all regions controlled by an enemy and increases their troop counts by 1 (or 3 if they contain a city)
    public static void enemyPassiveGain(String playerNum){
        String newTroopNum = "";
        for (int i = 0; i < 49; i++) {
            String occupation = game.gMapInPlay[Main.regionOccPos[i][0]][Main.regionOccPos[i][1]];
            if (occupation.equals(playerNum)) {
                if (Main.regionExtraInfo[i][2] == 1) { //check to see if the region contains a city
                    newTroopNum = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[i][0]][Main.regionArmPos[i][1]]) + 3);
                    gMapInPlay[Main.regionArmPos[i][0]][Main.regionArmPos[i][1]] = newTroopNum; //adds 3 troops to each controlled region containing a city
                } else {
                    newTroopNum = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[i][0]][Main.regionArmPos[i][1]]) + 1);
                    gMapInPlay[Main.regionArmPos[i][0]][Main.regionArmPos[i][1]] = newTroopNum; //adds 1 troop to each controlled region
                }
            }
        }
    }


    //used to save game data to the database and map data to a text file when the user wants to save a game part way through
    public static void saveGame(){
        int userID = loginFunctions.userIDfinder() + 1; //gets user ID
        Main.fileContentsUsers.clear(); //clears table
        String saveName = "gameSave" + String.valueOf(userID - 1) + ".txt"; //just overwrites save if already exists
        finalTime = System.currentTimeMillis(); //gets time of game ending
        float sessionTime = ((finalTime - initialTime) / 1000) + game.gameInfo[0]; //calculates session time
        int sessionTimeInt = (int)sessionTime; //converts session time from a float to an  integer
        gameInfo[0] = sessionTimeInt; //updates time on save with current saved time plus current session time
        gameRunning = false; //stops the game loop
        String DatabaseLocation = System.getProperty("user.dir") + "\\courseworkDatabase.accdb";
        try {
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://" + DatabaseLocation, "", "");
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate("UPDATE gameInfo set time = '"+gameInfo[0]+"', efficiency = '"+gameInfo[1]+"', money = '"+gameInfo[2]+"', food = '"+gameInfo[3]+"' WHERE ID = '"+userID+"'"); //SQl for updating gameInfo table oin the database
            con.close();
        } catch (Exception e) {
            System.out.println("Error in the SQL class: " + e);
        }
        Main.mapToFile(saveName, gMapInPlay, 30, 200); // saves active map to the users save, updating any pre-existing save or makes file if save deosnt exist
    }


    //method for getting users inputs for an invasion and doing all required checks that the invasion is valid before performing the invasion
    //exit region must hold more troops than the target region
    //exit region and target region must share a border
    //exit region must be player controlled
    //target region must not be player controlled
    public static void invadeRegion(){ // can only attack once a turn, and you must share a border
        boolean check = false;
        int exitRegion = 0;
        while (check == false){ //check for user controlling exit region here to ensure no hard lock loop for troop num
            exitRegion = Main.getInt("what region would you like to move troops from?", 0, 50);
            if (gMapInPlay[Main.regionOccPos[exitRegion - 1][0]][Main.regionOccPos[exitRegion - 1][1]].equals("P1")){ //checks player owns exit region
                check = true;
            }
        }
        int troopNum = Main.getInt("how many troops would you like to move from the region", 0, Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]])); //limit of current troop count of that region ensures there must be at least 1 troop left in the region
        int targetRegion = Main.getInt("what region would you like to move troops to?", 0, 50);
        boolean valid = checkRegionBorderValid(exitRegion, targetRegion, Main.regionBorderAmounts[exitRegion - 1], "P1"); //calls check for if target region is a valid target in terms of location and power
        if (valid == true && troopNum > Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]])){
            String newExit = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]]) - troopNum);
            gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]] = newExit; // takes troops of region they are being moved from
            String newTarget = mapArmyAdj(troopNum - Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]]));
            gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]] = newTarget; // takes away teh number of troops at target region from the invading force
            gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]] = ("P1"); //updates occupation of target region to the player (P1)
        }
        Main.printMap(game.gMapInPlay, 30, 200);//prints the map so the player can see the updated state of the map
    }


    //map print prints some colours based of a two digit piece of data being at any index location. this is fine with numbers at game starts
    //as they are in 05 format not 5 but as invasions take place there would be single digits in the wrong place.
    //these wouldn't get printed and would also scew the map when printed regardless so need to be correctly formatted into 0-5 format.
    public static String mapArmyAdj(int newValue){ //used to adjust ary values below 10 like "4" into format "04"
        String army = String.valueOf(newValue);
        if (newValue < 10) { //checks if the value is less than 10 as they're the values that need changed (single digit)
            army = "0" + army;
        }
        return (army);
    }


    //lets the player move their forces amongst all their controlled regions, you can do this multiple times in a turn
    public static void moveTroops(){
        boolean check = false;
        int exitRegion = 0;
        while (check == false){ //check for user controlling exit region here to ensure no hard lock loop for troop num
            exitRegion = Main.getInt("what region would you like to move troops from?", 0, 50);
            if (gMapInPlay[Main.regionOccPos[exitRegion - 1][0]][Main.regionOccPos[exitRegion - 1][1]].equals("P1")){ //checks user owns exit region
                check = true;
            }
        }
        int troopNum = Main.getInt("how many troops would you like to move from the region", 0, Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]])); //limit of current troop count of that region ensures there must be at least 1 troop left in the region
        int targetRegion = Main.getInt("what region would you like to move troops to?", 0, 50);
        if (gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]].equals("P1")){ //checks user owns target region
            String newExit = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]]) - troopNum);
            gMapInPlay[Main.regionArmPos[exitRegion - 1][0]][Main.regionArmPos[exitRegion - 1][1]] = newExit; // takes troops of region they are being moved from
            String newTarget = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]]) + troopNum);
            gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]] = newTarget; // adds troops taken off onto the region they are being moved to
            }
        Main.printMap(game.gMapInPlay, 30, 200);//prints the map so the user can see the updated map state
       }


    //used so the player can spend their money and food to make extra troops to reinforce their territories
    public static void makeTroops(){
        int limit = 0; // the limit is got from getting the limit of how many troops can be made from the current food and money the player has and taking the lower value
        System.out.println("you have " + gameInfo[2] + " money");
        System.out.println("you have " + gameInfo[3] + " food");
        int moneyLim = gameInfo[2] / 10; //converts money to smaller form (amount of troops that can be made with that amount)
        int foodLim = gameInfo[3] / 10; //converts food to a smaller form (amount of troops that can be made with that amount)
        if (moneyLim < foodLim) { //checks if money is the limiting factor
            limit = moneyLim; //sets a limit for how many troops the user can make since money is the limiting factor
        }else { //if money isnt the limiting  factor then food must be
            limit = foodLim; //sets a limit for how many troops the user can make since food is the limiting factor
        }
        int newTroops = Main.getInt("how many troops would you like to make?", 0, limit); // limit is then used on get int so player can't try to get more than they can afford
        troopPlace(newTroops); // calls troop place with number of troops made
    }


    //didn't go ahead making specials attacks but used method for real time testing purposes
    public static void specialAttacks(){
        System.out.println(loginFunctions.userIDfinder());
        Main.fileContentsUsers.clear();
    }


    //used to place the troops gained from passive gain and manual creation
    //troops to place can be put at any region and in any amount at each region
    //method loops until the player has placed all of their troops
    public static void troopPlace(int troopsToPlace){
        int newTroops = troopsToPlace; //number of troops the player needs to place
        while (newTroops > 0) { //checks if all troops have been placed
            System.out.println("you have " + newTroops + " new troops left to place"); //tells the player how many troops they have left to place
            int targetRegion = Main.getInt("what region would you like to place some new troops in?",0, 50);
            if (gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]].equals("P1")) { //checks target region is controlled by the player
                int troopNum = Main.getInt("how many troops would you like to place in this region?", 0, newTroops); //limit of the troop amount left to place
                String newTarget = mapArmyAdj(Integer.parseInt(gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]]) + troopNum);
                gMapInPlay[Main.regionArmPos[targetRegion - 1][0]][Main.regionArmPos[targetRegion - 1][1]] = newTarget; //adds troops to whatever is already there using the navigation arrays
                newTroops = newTroops - troopNum; //takes off troops just placed from total left to place
            }else {
                System.out.println("you don't control this region"); //print for if user selected an invalid region (one they don't control)
            }
            Main.printMap(game.gMapInPlay, 30, 200); //prints the map so the player can see the updated state of the map
        }
    }


    //gets the troops food and money gained passively from controlled regions
    //each controlled region yields 1 troop, 10 food and 10 money
    //or 3 troops if there is a city, 30 food if there is a farm and 30 money if there is a mine
    public static void passiveGain(){
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
        troopPlace(newTroops);//calls troop place method with number of new troops needing placed
    }


    //check used during player invasions to see if a selected target is a valid invasion option
    //valid being that the exit region borders the target region and the target region isn't controlled by the player
    public static boolean checkRegionBorderValid(int exitRegion, int targetRegion, int borderNum, String attacker) { //checks for a valid attack target when invading
        String occupation = gMapInPlay[Main.regionOccPos[targetRegion - 1][0]][Main.regionOccPos[targetRegion - 1][1]];
        boolean validity =  false; //validity state of the target
        for (int i = 0; i < borderNum; i++) {
            int current = Main.regionSharedBorders[exitRegion - 1][i];
            if (current == targetRegion && (occupation.equals("P1") || occupation.equals("P2") || occupation.equals("P3") || occupation.equals("P4") || occupation.equals("NC") && !(occupation.equals(attacker)))) {
                validity = true; //check for target not being controlled by the player
            }
        }
        if (validity == false){ //tells user if their target is invalid
            System.out.println("invalid attack target");
        }
        return (validity);//returns if the target is valid or not
    }


}





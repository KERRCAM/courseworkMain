package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class leaderboards {
    public static ArrayList<Integer> timeScores = new ArrayList<>();
    public static ArrayList<Integer> efficiencyScores = new ArrayList<>();
    public static ArrayList<Integer> combinedScores = new ArrayList<>();
    public static ArrayList<String> usernames = new ArrayList<>();


    public static void leaderboardMenu(){
        boolean exit = false;
        while (exit == false) {
            String action = Main.getString("what would you like to (enter number of action): \n (1)-view combined leaderboard- \n (2)-view timed leaderboard- \n (3)-view efficiency leaderboard- \n (4)-search username based on combined scores- \n (5)-exit- ");
            if (action.equals("1")) {
                combined();
            }
            if (action.equals("2")) {
                times();
            }
            if (action.equals("3")) {
                efficiency();
            }
            if (action.equals("4")) {
                usernameSearch();
            }
            if (action.equals("5")) {
                timeScores.clear();
                efficiencyScores.clear();
                combinedScores.clear();
                exit = true;
            }
        }
    }


    public static void mergeSort(ArrayList<Integer> array){ //using array-lists to more easily deal with unknown sizes of input
        int midPoint = (array.size() / 2); //finds a mid-point for the array
        ArrayList<Integer> leftHalfList = new ArrayList<>();
        ArrayList<Integer> rightHalfList = new ArrayList<>();
        if (array.size() < 2) { //doesn't run if the array is less than 2 size as it means its already split as far as it can go
            return; //does this rather than check for more than 1 so return is easier
        }
        for (int i = 0; i < midPoint; i++) { //adds arraylist up to mid-point into left half
            leftHalfList.add(array.get(i));
        }
        for (int i = midPoint; i < array.size(); i++) {//adds arraylist past mid-point into the right half
            rightHalfList.add(array.get(i));
        }
        mergeSort(leftHalfList); //recursion
        mergeSort(rightHalfList); // makes sure the array is split into individual pieces regardless of input size
        int leftPos = 0; //index trackers
        int rightPos = 0;
        int sortedPos = 0;
        while (leftPos < leftHalfList.size() && rightPos < rightHalfList.size()){ //checks pointers arnt at the end
            if (leftHalfList.get(leftPos) <= rightHalfList.get(rightPos)) { //if else sorts the split arraylists back into the main arraylist in the right order
                array.set(sortedPos, leftHalfList.get(leftPos)); //using set instead of adding so the list doesn't have to be cleared in odd ways = easier to handle one pointer instead
                leftPos++;
            }else{
                array.set(sortedPos, rightHalfList.get(rightPos));
                rightPos++;
            }
            sortedPos++;
        }
        while (leftPos < leftHalfList.size()){ //both while loops deal with if a pointer hasn't reached the end when the other has meaning all remaining data must be larger and already in order so can just be added to end of main arraylsit
            array.set(sortedPos, leftHalfList.get(leftPos));
            leftPos++;
            sortedPos++;
        }
        while (rightPos < rightHalfList.size()){
            array.set(sortedPos, rightHalfList.get(rightPos));
            rightPos++;
            sortedPos++;
        }
    }


    public static void loadScores(){
        String DatabaseLocation = System.getProperty("user.dir") + "\\courseworkDatabase.accdb";
        try{
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://" + DatabaseLocation, "", "");
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //String sql = "SELECT par2, par3 FROM testTab WHERE ID = '"+id+"'"; //for if where statement needed
            String sql = "SELECT time, efficiency, combined FROM scores"; //for no where statement needed
            ResultSet rs = stmt.executeQuery(sql); //executes the sql
            while(rs.next()) {
                timeScores.add(rs.getInt("time"));
                efficiencyScores.add(rs.getInt("efficiency"));
                combinedScores.add(rs.getInt("combined"));
            }


        }catch(Exception e){
            System.out.println("Error in the SQL class: " + e);
        }
    }


    public static void usernameSearch(){
        String DatabaseLocation = System.getProperty("user.dir") + "\\courseworkDatabase.accdb";
        int scoreToFind = Main.getInt("enter combined score you wan to find the username(s) for", 0, 1000000000); //gets target score from user
        try{
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://" + DatabaseLocation, "", "");
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "SELECT username FROM users, scores WHERE scores.ID = users.ID AND scores.combined = '"+scoreToFind+"'"; //searches for usernames in users that have a score = to the target, linking the tables with the userid primary keys
            ResultSet rs = stmt.executeQuery(sql); //executes the sql
            while(rs.next()) {
                System.out.println(rs.getString("username")); //prints all users found with a matching score
            }
        }catch(Exception e){
            System.out.println("Error in the SQL class: " + e);
        }
    }


    public static void combined(){
        int target = targetFinder();
        mergeSort(combinedScores);
        if (target == 0){
            for (int i = 0; i < 10; i++) {
                try{
                    System.out.println((i + 1) + ": " + combinedScores.get(i)); //prints top 10 scores
                } catch (Exception e){
                    System.out.println(i + ": "); //prints no score if they don't exist
                }
            }
        }else{
            for (int i = target - 5; i < target + 5; i++) { //makes sure not to print scores past top ie can't go 5 above if your rank 1
                if (i > 0){
                    try{
                        System.out.println(i + ": " + combinedScores.get(i - 1)); //prints surrounding 10 scores of user
                    } catch (Exception e){
                        System.out.println(i + ": ");
                    }
                }
            }
        }
    }


    public static void times(){ //same comments for combined
        int target = targetFinder();
        mergeSort(timeScores);
        if (target == 0){
            for (int i = 0; i < 10; i++) {
                try{
                    System.out.println((i + 1) + ": " + timeScores.get(i));
                } catch (Exception e){
                    System.out.println(i + ": ");
                }
            }
        }else{
            for (int i = target - 5; i < target + 5; i++) {
                if (i > 0){
                    try{
                        System.out.println(i + ": " + timeScores.get(i - 1));
                    } catch (Exception e){
                        System.out.println(i + ": ");
                    }
                }
            }
        }
    }


    public static void efficiency(){ //same comments for combined
        int target = targetFinder();
        mergeSort(efficiencyScores);
        if (target == 0){
            for (int i = 0; i < 10; i++) {
                try{
                    System.out.println((i + 1) + ": " + efficiencyScores.get(i));
                } catch (Exception e){
                    System.out.println(i + ": ");
                }
            }
        }else{
            for (int i = target - 5; i < target + 5; i++) {
                if (i > 0){
                    try{
                        System.out.println(i + ": " + efficiencyScores.get(i - 1));
                    } catch (Exception e){
                        System.out.println(i + ": ");
                    }
                }
            }
        }
    }


    public static int targetFinder(){ // gets input for if user wants to display the top 10 scores or the surrounding 10 scores of their own score
        int target = 0;
        String action = Main.getString("How would you like to see the leader board arranged? (enter number of action): \n (1)-top scores- \n (2)-your place on the leaderboard-");
        if (action.equals("2")){
            target = loginFunctions.userIDfinder();
            Main.fileContentsUsers.clear();
        }
        return (target);
    }


}

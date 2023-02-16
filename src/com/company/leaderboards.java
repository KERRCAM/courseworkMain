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


    public static void leaderboardMenu(){
        boolean exit = false;
        while (exit == false) {
            String action = Main.getString("what would you like to (enter number of action): \n (1)-view combined leaderboard- \n (2)-view timed leaderboard- \n (3)-view efficiency leaderboard- \n (4)-exit- ");
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
                exit = true;
            }
        }
    }


    public static void mergeSort(ArrayList<Integer> array){
        int midPoint = (array.size() / 2);
        ArrayList<Integer> leftHalfList = new ArrayList<>();
        ArrayList<Integer> rightHalfList = new ArrayList<>();
        if (array.size() < 2) {
            return;
        }
        for (int i = 0; i < midPoint; i++) {
            leftHalfList.add(array.get(i));
        }
        for (int i = midPoint; i < array.size(); i++) {
            rightHalfList.add(array.get(i));
        }
        mergeSort(leftHalfList);
        mergeSort(rightHalfList);
        int leftPos = 0;
        int rightPos = 0;
        int sortedPos = 0;
        while (leftPos < leftHalfList.size() && rightPos < rightHalfList.size()){
            if (leftHalfList.get(leftPos) <= rightHalfList.get(rightPos)) {
                array.set(sortedPos, leftHalfList.get(leftPos));
                leftPos++;
            }else{
                array.set(sortedPos, rightHalfList.get(rightPos));
                rightPos++;
            }
            sortedPos++;
        }
        while (leftPos < leftHalfList.size()){
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


    public static void combined(){
        int target = targetFinder();
        mergeSort(combinedScores);
        if (target == 0){
            for (int i = 0; i < 10; i++) {
                try{
                    System.out.println((i + 1) + ": " + combinedScores.get(i));
                } catch (Exception e){
                    System.out.println(i + ": ");
                }
            }
        }else{
            for (int i = target - 5; i < target + 5; i++) {
                if (i > 0){
                    try{
                        System.out.println(i + ": " + combinedScores.get(i - 1));
                    } catch (Exception e){
                        System.out.println(i + ": ");
                    }
                }
            }
        }
    }


    public static void times(){
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


    public static void efficiency(){
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


    public static int targetFinder(){
        int target = 0;
        String action = Main.getString("How wouold you like to see the leader board arranged? (enter number of action): \n (1)-top scores- \n (2)-your place on the leaderboard-");
        if (action.equals("2")){
            target = loginFunctions.userIDfinder();
        }
        return (target);
    }


}

package com.company;

import com.company.Objects.User;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static String userLoggedIn = "";
    public static String usersTable[][] = new String[10][4];
    public static File users = new File("users.txt");
    public static ArrayList<User> fileContentsUsers = new ArrayList<>();
    //new map is filled in with any new map that needs to made into a text file
    public static String newMap[][] = {{}};
    public static int regionOccPos[][] = {{3,5},{14,4},{24,3},{2,20},{8,15},{2,33},{6,24},{13,17},{19,15},{26,15},{23,22},{26,32},{13,38},{18,41},{8,51},{15,41},{21,53},{7,65},{13,68},{19,68},{9,82},{14,91},{13,108},{15,117},{2,92},{3,103},{4,120},{3,132},{2,151},{9,134},{6,149},{12,150},{3,164},{7,165},{15,166},{11,176},{4,182},{2,193},{27,86},{23,104},{27,103},{21,123},{27,122},{25,137},{27,145},{27,160},{26,173},{23,185},{27,191}}; // {2,5} = 3rd row down 6th column across // index pos for region id
    public static int regionArmPos[][] = {{4,5},{15,4},{25,3},{3,20},{9,15},{3,33},{7,24},{14,17},{20,15},{27,15},{24,22},{27,32},{14,38},{19,41},{9,51},{16,41},{22,53},{8,65},{14,68},{20,68},{10,82},{15,91},{14,108},{16,118},{3,92},{4,103},{5,120},{4,132},{3,151},{10,134},{7,149},{13,150},{4,164},{8,165},{16,166},{12,176},{5,182},{3,193},{28,86},{24,104},{28,103},{22,123},{28,122},{26,137},{28,145},{27,160},{27,173},{24,185},{28,191}}; // index pos for region troop number


    public static File gMap1File = new File("gMap1.txt");
    public static File tMap1File= new File("tMap1.txt");

    public static String tMapInPlay[][] = new String[13][38];





    public static void printMap(String mapName[][], int row, int column) {

        String ANSI_RESET = "\u001B[0m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_BLACK = "\u001B[30m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_PURPLE = "\u001B[35m";
        String ANSI_CYAN = "\u001B[36m";

        for (int i = 0; i < row; i++) {
            System.out.println("");
            for (int j = 0; j < column; j++) {
                if (mapName[i][j].equals("~")) {
                    System.out.print(ANSI_BLUE + mapName[i][j] + ANSI_RESET);
                }
                if (mapName[i][j].equals(".")) {
                    System.out.print(ANSI_GREEN + mapName[i][j] + ANSI_RESET);
                }
                if (mapName[i][j].equals("#")) {
                    System.out.print(ANSI_YELLOW + mapName[i][j] + ANSI_RESET);
                }
                if (mapName[i][j].equals("/")) {
                    System.out.print(ANSI_BLACK + mapName[i][j] + ANSI_RESET);
                }
                if (mapName[i][j].equals("+")) {
                    System.out.print(ANSI_RED + mapName[i][j] + ANSI_RESET);
                }
                if (mapName[i][j].length() == 2) {
                    System.out.print(ANSI_PURPLE + mapName[i][j] + ANSI_RESET);
                }
                if (mapName[i][j].equals("^") || mapName[i][j].equals("|") || mapName[i][j].equals("*") || mapName[i][j].equals("[") || mapName[i][j].equals("]") || mapName[i][j].equals("I") || mapName[i][j].equals("=")) {
                    System.out.print(ANSI_CYAN + mapName[i][j] + ANSI_RESET);
                }
            }
        }
    }


    public static void mapToFile(String fileName, String mapName[][], int row, int column) {
        try {
            FileWriter myWriter = new FileWriter(fileName, false); //Would need to change for each map, change name to be variable (done)
            //myWriter.write( + "\n"); // writes to file
            for (int i = 0; i < row; i++){
                for (int j = 0; j < column; j++) {
                    myWriter.write( mapName[i][j] + ",");
                }
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public static void fileToMap(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                List<String> splitLine = Arrays.asList(line.split(","));
                for (int j = 0; j < 200; j++) { //just need to change 200 and map in play for tutorial map version
                    game.gMapInPlay[i][j] = splitLine.get(j);
                    //System.out.print(splitLine.get(i)); //test print
                }
                //System.out.println(); //test print
                i++;
            }
        } catch (Exception e) {
            System.out.println("error");
            System.out.println(e);
        }
    }


    public static void newMap(){
        String newMapName = getString("what is the name of the new map");
        int rows = getPosInt("enter number of rows for new map (vertical height)");
        int columns = getPosInt("enter number of rows for new map (horizontal width)");
        mapToFile(newMapName,newMap, rows, columns);
    }




    public static String getString(String prompt) {
        Scanner input = new Scanner(System.in);
        String strInput = "";
        try {
            System.out.println(prompt);
            strInput = input.next();
        } catch (Exception e) {
            System.out.println("incorrect input");
            System.out.println(e);
        }
        return (strInput);
    }


    public static int getPosInt(String prompt) {
        Scanner input = new Scanner(System.in);
        int intInput = 0;
        while (intInput < 1) {
            System.out.println(prompt);
            intInput = input.nextInt();
        }
        return (intInput);
    }


    public static void adminMenu() {
        boolean exit = false;
        while (exit == false) {
            String action = getString("what would you like to (enter number of action): \n (1)-add map- \n (2)-exit- ");
            if (action.equals("1")) {

            }
            if (action.equals("2")) {
                exit = true;
            }
        }
    }


    public static void userMenu() {
        boolean exit = false;
        while (exit == false) {
            String option = getString("what would you like to (enter number of action): \n (1)-load game save-  \n (2)-start new game (warning new game will overwrite any saved game)- \n (3)-view tutorial- \n (4)-view leaderboards- \n (5)-exit-");
            if (option.equals("1")) {
                game.gameLoop("load");
            }
            if (option.equals("2")) {
                game.gameLoop("new");
            }
            if (option.equals("3")) {
            }
            if (option.equals("4")) {
            }
            if (option.equals("5")) {
                exit = true;
            }
        }
    }


    public static void main(String[] args) {

        //String testPass = accountCreationFunctions.passwordHash("456");
        //System.out.println(testPass);

        // gMap = game map
        // tMap = tutorial map

        // === = airbases
        // *** = farms
        // ||| = cities
        // ]]] = ports
        // ^^^ = mines
        // gmap1 only here temporarily - it doesnt need to be in the program to function - once map is made a text file it can be removed from code
        String gMap1[][] = {{"#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#"},
                            {"#",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".","04","",".",".",".",".",".",".",".","/",".",".",".","06","",".",".",".",".",".",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","25","",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".","29","",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".","38","",".",".",".",".","#"},
                            {"#",".",".",".",".","01","",".",".",".",".",".",".",".",".",".","/",".",".",".","ID","",".",".",".",".",".",".",".","/",".",".",".","ID","",".",".",".",".",".","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#",".",".",".",".",".",".",".",".",".",".",".",".","ID","",".",".",".",".","/",".",".",".",".","26","",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".","/",".",".",".",".",".",".",".","28","",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".","ID","",".",".",".",".",".",".","/",".",".",".",".","33","",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".","ID","",".",".",".",".","#"},
                            {"#",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".","/",".",".","00","",".",".",".",".","/","/","/","/",".",".",".","00","",".",".",".","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#",".",".",".",".",".",".","00","",".",".",".","/",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".","27","",".",".",".","/",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".","00","",".",".",".",".",".",".","/",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".","37","",".",".",".",".",".","/",".",".",".","00","",".",".","#","#","#"},
                            {"#",".",".",".",".","00","",".",".",".",".",".",".",".",".",".",".",".","/",".","/","/","/","/","/","/",".",".",".","/",".",".",".",".","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#",".",".",".",".",".","/",".",".",".",".",".","00","",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".","ID","",".",".",".","/",".",".",".",".",".",".","00","",".",".",".",".",".",".",".",".",".","/",".","/","/","/","/","/","/","/","/",".",".",".",".",".",".","/",".",".",".",".","00","",".","/","/","/","/","/","/","/","/","/","/",".",".",".",".",".","ID","",".",".",".",".",".",".","/",".",".",".",".",".",".","#","~","~"},
                            {"#",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/","/","/",".","/",".",".",".",".","07","",".",".",".",".","/",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#",".",".",".",".",".","/",".",".",".",".",".","#","#","#","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".","00","",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".","31","",".",".","/","/","/","/","/","/","/","/","/","/","/","/","/","/",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".","00","",".",".",".",".",".",".",".","/",".",".","#","#","#","~","~","~"},
                            {"#","/","/","/","/",".",".",".",".",".",".","/","/","/","/",".",".",".",".",".","/",".",".",".","ID","",".",".",".",".","/",".",".","#","+","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".","18","",".",".",".",".",".",".",".",".","#","#","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","#","#","#","#",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".","/",".","/","/","/","/","/","/","/","/",".",".",".",".",".",".",".","/",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".","/",".",".",".",".",".","34","",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","#","#","~","~","~","~","~","~"},
                            {"#",".",".","/",".","/","/","/","/","/","/",".",".",".",".","05","",".",".",".",".","/",".",".","00","",".",".",".",".","#","#","#","~","~","+","~","~","~","~","~","~","~","~","~","#",".",".",".",".",".","15","",".",".",".",".",".",".",".","/",".",".",".",".","ID","",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".","#","#","#","#","#","#","#","#","~","+","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#","#","#","#","#","#","#","#",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".","/","/","/","/","/","/","/",".",".",".",".",".",".","00","",".",".",".",".",".",".",".",".","/",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".","#","#","#","~","~","~","~","~","~","~","~"},
                            {"#",".",".","/",".",".",".",".",".",".",".",".",".",".",".","ID","",".",".",".",".",".","/",".",".",".",".","#","#","#","~","~","~","~","~","+","~","~","~","~","~","~","#","#","#",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".","/",".",".",".",".","00","",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".","21","",".",".",".",".",".",".",".","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#",".",".",".",".","/",".",".",".",".",".",".","30","",".",".",".",".",".",".",".","/","/","/","/","/",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".","00","",".",".",".",".",".",".",".",".","/","/","/","/","/","/",".",".",".",".",".",".","#","#","~","~","~","~","~","~","~","~","~","~","~"},
                            {"#",".",".",".","/",".",".",".",".",".",".",".",".",".",".","00","",".",".",".",".","/","/","/","#","#","#","~","~","~","~","~","~","~","~","~","+","~","~","#","#","#",".",".",".",".",".",".",".",".",".","00","",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".","#","#","#","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".",".",".","/","/","/","/","/","/",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".","/","/","/","/","/","/",".",".",".",".",".",".","/",".",".",".","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~"},
                            {"#",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".","/","/","/","/",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","+","#","#",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/","/",".",".",".",".",".","/","/","/","/","/","/","/","/",".",".",".",".","/",".",".",".","00","",".",".",".",".",".",".",".",".","/","/",".",".",".","#","~","~","~","~","~","~","~","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#",".","00","",".",".",".",".",".",".",".",".",".","/","/","/",".",".",".",".",".",".","/","/","/","/","/","/",".",".",".",".",".","/","/","/","/",".",".",".",".",".",".",".","36","",".",".",".",".","/","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~"},
                            {"#",".",".",".",".",".","/","/","/",".",".",".",".","/","/","/","/",".",".",".",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","#",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".","/","/","/","/","/","/",".",".","/","/","/","/","/",".",".",".",".",".",".",".",".","/","/","/","/","/","/",".",".",".",".",".",".",".",".",".","/","/","/",".",".",".",".",".","#","~","~","~","~","~","~","#",".",".",".",".",".","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#",".",".",".","/","/","/","/",".",".",".",".",".","32","",".",".",".",".",".",".",".",".","/","/","/","/","/",".",".",".",".",".",".",".",".",".",".",".","ID","",".",".",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~"},
                            {"#",".",".",".",".",".",".",".",".","/","/",".","/",".",".",".",".","08","",".",".",".","#","+","+","+","~","~","~","~","~","~","~","~","~","#",".",".","13","",".",".",".",".","/",".",".",".","/","/","/","/","/",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".","19","",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".","/","/","/",".",".",".",".",".",".",".",".","#","+","+","+","~","~","~","#",".",".",".","23","",".",".",".",".",".","/","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","#","#","#",".",".",".",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".",".",".",".","/","/","/","/","/","/","/",".",".",".",".","00","",".",".",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~"},
                            {"#",".",".",".","02","",".",".",".",".",".","/",".",".",".",".",".","ID","",".",".","#","~","~","~","~","+","+","+","~","~","~","~","~","#",".",".",".","ID","",".",".",".",".","/",".","/","/",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".",".","/",".",".","/","/",".",".",".",".",".","22","",".",".",".",".","#","~","~","~","+","+","#",".",".",".",".","ID","",".",".",".",".","/",".",".",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","#","#","#",".",".",".",".",".",".","00","",".",".",".",".",".",".",".",".",".","/","/","/","/",".",".",".",".",".",".",".","/","/","/","/",".",".",".",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~"},
                            {"#",".",".",".","ID","",".",".",".",".",".",".","/","/","/",".",".","00","",".",".","#","~","~","~","~","~","~","~","+","+","+","~","~","#",".",".",".","00","",".",".",".","/","/","/",".",".",".",".",".","16","",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".","00","",".",".",".",".",".",".",".",".",".",".",".",".","/","/",".",".",".",".",".",".",".","ID","",".",".",".","#","~","~","~","~","~","~","#",".",".",".",".","00","",".",".",".","/",".",".","24","",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","#","#","#","#",".",".",".",".",".",".",".",".","/","/","/","/","/",".",".",".",".",".","35","",".",".",".",".",".",".",".",".","/","/","/",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~"},
                            {"#",".",".",".","00","",".",".",".",".",".",".",".",".",".","/","/",".",".",".",".","#","~","~","~","~","~","~","~","~","~","~","+","+","#",".",".",".",".",".","/","/","/",".",".","/",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".","00","",".",".",".","#","~","~","~","~","~","~","~","#",".",".",".",".",".",".",".","/",".",".",".","ID","",".",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","#","#","#",".",".","/","/","/",".",".",".",".",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".",".","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~"},
                            {"#",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/","/","/","/","#","~","~","~","~","~","~","~","~","~","~","~","~","#",".",".","/","/","/",".","."," ",".",".","/",".",".",".",".",".","00","",".",".",".",".",".",".",".",".",".","/","/","/","/","/","/","/","/","/","/","/","/","/","/","/","/","/","/","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","#","~","~","~","~","~","~","~","~","#",".",".",".",".",".","/",".",".",".",".","00","",".",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#",".",".",".",".",".",".",".",".",".",".",".",".",".","00","",".",".",".",".",".",".",".",".","#","#","#","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~"},
                            {"#",".",".",".",".",".",".",".",".",".",".",".","/","/","/","/","/",".",".",".",".","#","~","~","~","~","~","~","~","~","~","~","~","~","~","#","/",".",".",".",".","14","",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","#","#","#","#","/",".",".",".",".",".",".",".","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","#","#","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~"},
                            {"#",".",".",".",".",".",".",".","/","/","/","/",".",".",".","09","",".",".",".",".","/","#","~","~","~","~","~","~","~","~","~","~","~","~","#",".",".",".",".",".","ID","",".",".",".","/",".",".",".",".",".",".",".","/","/","/","/","/","/","/","/","/",".",".",".",".",".","20","",".",".",".",".",".",".",".",".",".",".","/",".",".",".","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","#","#","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#",".",".",".",".",".",".","#","#","#","#","#","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#"},
                            {"#",".",".",".","/","/","/","/",".",".",".",".",".",".",".","ID","",".",".",".","/",".","#","~","~","~","~","~","~","~","~","~","~","~","~","#",".",".",".",".",".","00","",".",".",".","/","/","/","/","/","/","/","/",".",".",".",".",".",".",".",".","/",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","#","#","#",".","#"},
                            {"#","/","/","/",".",".",".",".","/",".",".",".",".",".",".","00","",".",".","/",".",".",".","#","#","~","~","~","~","~","~","~","~","~","~","~","#","#",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".","17","",".",".",".",".",".",".",".","/",".",".",".",".",".","00","",".","#","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","#","#","#","#","#","#","#",".",".",".",".","42","",".",".","#","#","#","#","~","~","~","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#","#","#","#","#",".",".",".",".","#"},
                            {"#",".",".",".",".",".",".",".",".","/","/","/","/",".",".",".",".",".","/",".",".",".",".",".",".","#","#","#","~","~","~","~","~","~","~","~","~","~","#","#","#","#",".",".",".",".","/",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".","/",".",".",".","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#","#","#","#",".",".",".",".",".",".",".",".",".",".",".","ID","",".",".",".",".",".",".","#","#","#","~","~","~","~","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","#"},
                            {"#",".",".",".",".",".",".",".",".",".",".",".",".","/","/",".",".","/",".",".",".",".","11","",".",".",".",".","#","#","~","~","~","~","~","~","~","~","+","~","~","~","#","#","#","#","/",".",".",".",".",".",".","00","",".",".",".",".",".",".",".","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#",".",".","40","",".",".",".",".",".",".","/","/","/","/",".",".",".",".",".",".",".","00","",".",".",".",".",".",".",".",".",".","#","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#",".",".",".","48","",".",".",".",".",".",".",".",".",".",".",".",".","#"},
                            {"#",".",".","03","",".",".",".",".",".",".",".",".",".",".","/","/",".",".",".",".",".","ID","",".",".",".",".",".",".","#","#","~","~","~","~","~","~","+","~","~","~","~","~","~","~","#","#","#","#",".",".",".",".",".",".",".","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".","/","/","/","/","/","/",".",".",".",".",".",".",".",".","/","/","/","/",".",".",".",".",".",".",".","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#","#",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".",".",".",".","#"},
                            {"#",".",".","ID","",".",".",".",".",".",".",".",".","/","/",".",".","/","/",".",".",".","00","",".",".",".",".",".",".","/",".","#","#","#","#","~","~","+","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#","~","+","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#",".",".",".",".",".",".",".",".",".",".","00","",".",".",".",".",".",".","/","/","/","/",".",".",".",".",".",".","/","/","/","/","/","/","/","/",".",".",".","/",".",".",".","44","",".",".","/",".",".","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#",".",".",".",".",".",".","/",".",".",".",".",".",".",".","00","",".",".",".",".",".","/","/","/","/","/","/","/","#"},
                            {"#",".",".","00","",".",".",".",".",".",".","/","/",".",".","10","",".",".","/","/",".",".",".",".",".",".",".",".","/",".",".","12","",".",".","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","+","+","+","+","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#",".",".",".",".",".","/","/","/","/","/","/","/","/","/","/","/","/","/","/","/","/","/","/","/",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".","ID","",".",".","/",".",".",".",".","#","#","#","#","#","~","~","~","~","~","~","#","#","#","#","#","#","#","#","#","#",".",".","/",".",".",".","47","",".",".",".","/",".",".",".",".",".",".",".",".","/","/","/","/","/",".",".",".",".",".",".",".","#"},
                            {"#",".",".",".",".",".",".",".","/","/","/",".",".",".",".","ID","",".",".",".",".","/","/","/",".",".",".",".",".","/",".",".","ID","",".",".",".",".",".",".","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","+","+","+","+","+","+","~","~","~","~","~","~","#","#","#","#",".",".",".",".",".","39","",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".","41","",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".","43","",".",".",".",".",".",".",".",".",".","/",".",".",".","00","",".",".","/",".",".",".","45","",".",".",".",".","#","#","#","#","#","#",".",".",".","46","",".",".",".",".",".",".",".","/",".",".",".","ID","",".",".",".",".","/",".",".",".",".","/","/","/",".",".",".",".","49","",".",".",".",".",".",".","#"},
                            {"#",".",".",".",".",".","/","/",".",".",".",".",".",".",".","00","",".",".",".",".",".",".",".","/","/","/",".","/",".",".",".","00","",".",".",".",".",".",".",".",".",".",".",".","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","+","+","+","#","#","#",".",".",".",".",".",".",".",".",".","ID","",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".","/",".",".",".",".","ID","",".",".",".",".",".",".","/",".",".",".",".",".",".","ID","",".",".",".",".",".",".",".","/",".",".",".","00","",".",".",".",".",".","/","/",".","/",".",".",".",".",".",".",".","ID","",".",".",".",".",".",".","#"},
                            {"#",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","/","/",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".",".","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#",".",".",".",".",".",".",".",".",".",".",".",".","00","",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".","00","",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".","00","",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".","/",".",".",".",".","00","",".",".",".",".",".",".","/",".",".",".",".",".",".","00","",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".",".",".",".",".","/",".",".",".",".",".",".",".",".","00","",".",".",".",".",".",".","#"},
                            {"#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#","#"}};




        //mapToFile("gMap1.txt",gMap1, 30, 200);
        //fileToMap("gMap1.txt"); // loads map in named text file to active gmap
        //printMap(gMap1, 30, 200); //prints active gmap

        String option = getString("would you like to (enter number of action): \n (1)-log in- \n (2)-sign up- \n (3)-exit-");
        boolean exit = false;
        while(exit == false) {
            if (option.equals("1")) {
                int logInStatus = loginFunctions.logIn();
                if (logInStatus == 0) {
                    System.out.println("username or password incorrect");
                }
                if (logInStatus == 1) {
                    System.out.println("user logged in");
                    userMenu();
                }
                if (logInStatus == 2) {
                    System.out.println("admin logged in");
                    adminMenu();
                }
            }
            if (option.equals("2")) {
                accountCreationFunctions.makeUser();
            }
            if (option.equals("3")) {
                exit = true;
            }
        }
    }


}

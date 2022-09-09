package com.company;

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


    public static void adminMenu() {
        boolean menu = true;
        while (menu == true) {
            String action = getString("what would you like to (enter number of action): \n (1)-- \n (2)-- ");
            if (action.equals("1")) {
            }
            String menuAgain = getString("would you like to perform another action Y or N?");
            if (menuAgain.equals("N")) {
                menu = false;
            }
        }
    }


    public static void userMenu() {
        boolean menu = true;
        while (menu == true) {
            String action = getString("what would you like to (enter number of action): \n (1)-load game save-  \n (2)-start new game- \n (3)-view leaderboards-");
            if (action.equals("1")) {
            }
            if (action.equals("2")) {
            }
            if (action.equals("3")) {
            }
            String menuAgain = getString("would you like to perform another action Y or N?");
            if (menuAgain.equals("N")) {
                menu = false;
            }
        }
    }


    public static void login(){

    }





    public static void main(String[] args) {
        String option = getString("would you like to (enter number of action): \n (1)-log in- \n (2)-sign up-");
        if (option.equals("1")) {
        }
        if (option.equals("2")) {
            accountCreationFunctions.makeAccount();
        }
    }


}

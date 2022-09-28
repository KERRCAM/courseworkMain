package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class accountCreationFunctions { //all needs to be converted from text file storage to database.


    public static void makeUser(){
        ArrayList<String> newUser = new ArrayList<>();
        newUser.add(userDetails("user"));
        try {
            FileWriter myWriter = new FileWriter(Main.users.getName(), true); //True means append to file contents, False means overwrite
            myWriter.write(newUser.get(0) + "\n"); // writes to file
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        String DatabaseLocation = System.getProperty("user.dir") + "\\courseworkDatabase.accdb";
        try {
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://" + DatabaseLocation, "", "");
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            stmt.executeUpdate("INSERT INTO users(username)" + "VALUES ("+newUser.get(0)+")");
            con.close();
        } catch (Exception e) {
            System.out.println("Error in the SQL class: " + e);
        }
    }
//"VALUES ("+newUser.get(0)+")");
    public static String userDetails(String authority) {
        String username = getUsername("enter username (must be at least 4 characters and contain no spaces)");
        String password = getPassword("enter enter password (must contain at least 1 letter and 1 number and contain no spaces and be at least 6 characters long)");
        String userAuthority = (authority);
        return (username + "," + password + "," + userAuthority);
    }


    public static String getUsername(String prompt) {
        Scanner input = new Scanner(System.in);
        String strInput = "";
        boolean validUsername = false;
        while (validUsername == false) {
            try {
                System.out.println(prompt);
                strInput = input.next();
            } catch (Exception e) {
                System.out.println("incorrect input");
                System.out.println(e);
            }
            boolean usernameCheck = checkUsername(strInput);
            if (usernameCheck == true) {
                validUsername = true;
            }else {
                System.out.println("invalid username entered");
            }
        }
        return (strInput);
    }


    public static boolean checkUsername(String username){
        boolean valid = false;
        int checks = 0;
        if (username.contains(" ")) {
        }else {
            checks++;
        }
        if (username.length() > 3) {
            checks++;
        }

        //unique name check temp check++ to satisfy if statment
        checks++;


        if (checks == 3) {
            valid = true;
        }
        return (valid);
    }



    public static String getPassword(String prompt) {
        Scanner input = new Scanner(System.in);
        String strInput = "";
        boolean validPassword = false;
        while (validPassword == false) {
            try {
                System.out.println(prompt);
                strInput = input.next();
            } catch (Exception e) {
                System.out.println("incorrect input");
                System.out.println(e);
            }
            boolean passwordCheck = checkPassword(strInput);
            if (passwordCheck == true) {
                validPassword = true;
            }else {
                System.out.println("invalid password entered");
            }
        }
        String hashedPassword = passwordHash(strInput);
        return(hashedPassword);
    }


    public static String passwordHash(String inputPassword){ // hash code in try catch got online
        String password = inputPassword;
        String hashedPassword = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return(hashedPassword);
    }


    public static boolean checkPassword(String password){
        boolean valid = false;
        char a;
        int checks = 0;
        for (a = 'a'; a <= 'z'; ++a) {
            String strA = String.valueOf(a);
            if (password.contains(strA)) {
                checks++;
                break;
            }
        }
        for (int i = 0; i < 10; i++) {
            String strA = String.valueOf(i);
            if (password.contains(strA)) {
                checks++;
                break;
            }
        }
        if (password.length() > 5) {
            checks++;
        }
        if (password.contains(" ")) {
        }else {
            checks++;
        }
        if (checks == 4) {
            valid = true;
        }
        return (valid);
    }


}

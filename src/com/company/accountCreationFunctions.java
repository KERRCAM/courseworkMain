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

public class accountCreationFunctions {
    public static String currentUsername;

    public static void makeUser(){
        ArrayList<String> newUser = new ArrayList<>(); //holds all details for a new account
        newUser.add(userDetails("user")); //default set to user authority
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

            stmt.executeUpdate("INSERT INTO users(username)" + "VALUES ('"+currentUsername+"')"); //SQL insert for the new user, auto generates user id in table
            con.close();
        } catch (Exception e) {
            System.out.println("Error in the SQL class: " + e);
        };
        try {
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://" + DatabaseLocation, "", "");
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate("INSERT INTO gameInfo(time, efficiency, money, food)" + "VALUES (0, 0, 0, 0)"); //initialises players game save
            con.close();
        } catch (Exception e) {
            System.out.println("Error in the SQL class: " + e);
        }
        try {
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://" + DatabaseLocation, "", ""); //initialises players scores on leaderboard
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate("INSERT INTO scores(time, efficiency, combined)" + "VALUES (0, 0, 0)");
            con.close();
        } catch (Exception e) {
            System.out.println("Error in the SQL class: " + e);
        }
    }


    //method that actually gets all the details for a new user
    public static String userDetails(String authority) { //prompts for new users username and password
        String username = getUsername("enter username (must be at least 4 characters and contain no spaces)");
        String password = getPassword("enter enter password (must contain at least 1 letter and 1 number and contain no spaces and be at least 6 characters long)");
        String userAuthority = (authority);
        currentUsername = username;
        return (username + "," + password + "," + userAuthority);
    }


    //gets the username input and calls on the checks
    public static String getUsername(String prompt) { //gets input for username and checks it passed parameters
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


    //checks if a given username is a valid input (valid being more than 4 characters, no spaces and must be unique)
    public static boolean checkUsername(String username){
        boolean valid = false; //validity state of the username
        int checks = 0;
        if (username.contains(" ")) { //spaces check
            System.out.println("you cant use spaces in your username");
        }else {
            checks++;
        }
        if (username.length() > 3) { //length check
            checks++;
        }else{
            System.out.println("username too short");
        }
        String DatabaseLocation = System.getProperty("user.dir") + "\\courseworkDatabase.accdb";
        try{
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://" + DatabaseLocation, "", "");
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "SELECT username FROM users"; //gets all usernames
            ResultSet rs = stmt.executeQuery(sql); //executes the sql
            while(rs.next()) {
                if (username.equals(rs.getString("username"))){ //checks if username is unique
                    System.out.println("this username is taken");
                    checks--;
                }
            }
        }catch(Exception e){
            System.out.println("Error in the SQL class: " + e);
        }
        checks++;
        if (checks == 3) { //looks to see if the username passed all checks
            valid = true;
        }
        return (valid); //returns if the username was valid or not (true = was valid, false = wasnt valid)
    }


    //gets password input form user checks it met parameters then sends it to be hashed so it can be compared to the stored value which is hashed
    public static String getPassword(String prompt) {
        Scanner input = new Scanner(System.in);
        String strInput = "";
        boolean validPassword = false; //validity state of password
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


    // puts passwords through MD5 hash so the text files dont contain the actual passwords to accounts
    public static String passwordHash(String inputPassword){
        String password = inputPassword;
        String hashedPassword = null;
        try //code for hash algorithm in try catch from online source not made personally * https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
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
        return(hashedPassword); //returns the password hashed
    }


    //the actual checks for a valid password (valid being no spaces, at least 6 characters and made of at least one letter and at least one number)
    public static boolean checkPassword(String password){
        boolean valid = false; //validity state of password
        char a; //temp char holder
        int checks = 0;
        for (a = 'a'; a <= 'z'; ++a) { //checking the password has at least one letter
            String strA = String.valueOf(a);
            if (password.contains(strA)) {
                checks++;
                break;
            }
        }
        for (int i = 0; i < 10; i++) { //checking the password has at least one number
            String strA = String.valueOf(i);
            if (password.contains(strA)) {
                checks++;
                break;
            }
        }
        if (password.length() > 5) { //checking password length
            checks++;
        }
        if (password.contains(" ")) { //checking password has no spaces
        }else {
            checks++;
        }
        if (checks == 4) { //looks to see if password passed all checks
            valid = true;
        }
        return (valid);
    }


}

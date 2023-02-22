package com.company;
import com.company.Objects.User;
import java.io.BufferedReader;
import java.io.FileReader;

public class loginFunctions {


    //splits user data from text file and puts into user object
    public static User commaSeperatedStringsSplitterUser(String userDetails) {
        String[] splitter = userDetails.split(",");
        User newUser = new User(splitter[0],splitter[1],splitter[2]); //puts into new user object
        return (newUser);
    }


    //used to put the users text file data into a list
    public static void fileToList(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(file.equals("users.txt")) {
                    Main.fileContentsUsers.add(commaSeperatedStringsSplitterUser(line));//Creates user objects and adds to Arraylist
                }
            }
        } catch (Exception e) {
            System.out.println("error");
            System.out.println(e);
        }
    }


    //gets the user ID of the current logged in user so it can be used to make or find game saves etc
    public static int userIDfinder(){
        fileToList("users.txt");
        int userIndexPos = 0;
        String username = Main.userLoggedIn;
        for (int i = 0; i < (Main.fileContentsUsers.size()); i++) {
            if (Main.fileContentsUsers.get(i).getUsername().equals(username)) { //finds index of currently logged in user
                userIndexPos = i;
            }
        }
        return(userIndexPos);
    }


    //main method for the login process that calls all other necessary methods
    public static int logIn(){
        int logInStatus = 0; // 0 = not logged in - 1 = user logged in - 2 = admin logged in
        fileToList("users.txt");
        boolean userFound = false;
        int userIndexPos = 0;
        String username = Main.getString("enter your user username");
        String password = accountCreationFunctions.passwordHash(Main.getString("enter your user password"));
        for (int i = 0; i < Main.fileContentsUsers.size(); i++) { //checks to see if it can find username matching the users input
            if (Main.fileContentsUsers.get(i).getUsername().equals(username)) {
                userIndexPos = i;
                userFound = true;
            }
        }
        if (userFound == true) {//if a matching username is found it then checks the password entered for that account is correct
            if (Main.fileContentsUsers.get(userIndexPos).getPassword().equals(password)) {
                logInStatus++;
            }
            if (Main.fileContentsUsers.get(userIndexPos).getAuthority().equals("admin")) { //checks authority level
                logInStatus++;
            }
        }
        if (logInStatus > 0) {
            Main.userLoggedIn = username;
        }
        Main.fileContentsUsers.clear();
        return(logInStatus); //if 0 is returned then login was unsuccessful, 1 a user is logged in and 2 an admin is logged in
    }//will take user to corresponding needed menu
}

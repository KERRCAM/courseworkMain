package com.company;
import com.company.Objects.User;
import java.io.BufferedReader;
import java.io.FileReader;

public class loginFunctions {


    public static User commaSeperatedStringsSplitterUser(String userDetails) {
        String[] splitter = userDetails.split(",");
        User newUser = new User(splitter[0],splitter[1],splitter[2]);
        return (newUser);
    }


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


    public static int userIDfinder(){
        fileToList("users.txt");
        int userIndexPos = 0;
        String username = Main.userLoggedIn;

        for (int i = 0; i < Main.fileContentsUsers.size(); i++) {
            if (Main.fileContentsUsers.get(i).getUsername().equals(username)) {
                userIndexPos = i;
            }
        }
        return(userIndexPos - 3);
    }


    public static int logIn(){
        int logInStatus = 0; // 0 = not logged in - 1 = user logged in - 2 = admin logged in
        fileToList("users.txt");
        boolean userFound = false;
        int userIndexPos = 0;
        String username = Main.getString("enter your user username");
        String password = accountCreationFunctions.passwordHash(Main.getString("enter your user password"));
        for (int i = 0; i < Main.fileContentsUsers.size(); i++) {
            if (Main.fileContentsUsers.get(i).getUsername().equals(username)) {
                userIndexPos = i;
                userFound = true;
            }
        }
        if (userFound == true) {
            if (Main.fileContentsUsers.get(userIndexPos).getPassword().equals(password)) {
                logInStatus++;
            }
            if (Main.fileContentsUsers.get(userIndexPos).getAuthority().equals("admin")) {
                logInStatus++;
            }
        }
        if (logInStatus > 0) {
            Main.userLoggedIn = username;
        }
        return(logInStatus);
    }

}

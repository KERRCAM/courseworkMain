package com.company;

import java.util.Scanner;

public class accountCreationFunctions {



    public static void makeAccount(){
        String username = getUsername("enter username (must be at least 4 characters and contain no spaces");
        String password = getPassword("enter enter password (must contain at least 1 letter and 1 number and contain no spaces and be at least 6 characters long");
        String authority = ("No");
    }


    public static String getUsername(String prompt) {
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
            boolean passwordCheck = checkUsername(strInput);
            if (passwordCheck == true) {
                validPassword = true;
            }else {
                System.out.println("invalid password entered");
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
        if (username.length() > 4) {
            checks++;
        }

        //unique name check


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
        return (strInput);
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
        if (password.length() > 6) {
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

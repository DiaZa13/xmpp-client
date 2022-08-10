package org.zclient;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // object to read from console
        Scanner read = new Scanner(System.in);
        Connection connection = new Connection("alumchat.fun");
        Authentication auth = new Authentication();

        System.out.println("Chat Sistemas Operativos 2.0");

        String username, password, email = "";
        String option, auth_opt;

        do {
            System.out.println("1 - Sing in");
            System.out.println("2 - Sing up");
            System.out.println("3 - Exit");

            option = read.nextLine();

            switch (option) {
                case "1" -> {
                    connection.start();
                    System.out.println("Username: ");
                    username = read.nextLine();
                    System.out.println("Password:");
                    password = read.nextLine();
                    User user = new User(username, password);
                    auth.singIn(connection.getStream(), user);

                    do {
                        System.out.println("1 - Users");
                        System.out.println("4 - Chat");
                        System.out.println("5 - Group chat");
                        System.out.println("6 - Edit profile");
                        System.out.println("8 - Files");
                        // If disconnect is selected it should return to the old menu
                        System.out.println("9 - Disconnect");
                        System.out.println("10 - Exit");

                        auth_opt = read.nextLine();

                        switch (auth_opt) {
                            case "1" -> {
                                System.out.println("2 - Add user to contacts");
                                System.out.println("3 - Details of a contact");
//                                Roster roster = Roster.getInstanceFor(connection);
//                                Collection<RosterEntry> entries = roster.getEntries();
//                                for (RosterEntry entry : entries) {
//                                    System.out.println(entry);
//                                }

                            }
                            case "2", "3", "5", "6", "7", "8" -> System.out.println("TODO");
                            case "4" -> {

                            }
                            case "9" -> connection.close();
                            case "10" -> auth_opt = "exit";
                            default -> System.out.println("Invalid option. Try again...");
                        }
                    } while (!"exit".equals(auth_opt));

                }
                case "2" -> {
                    connection.start();
                    System.out.println("Username: ");
                    username = read.nextLine();
                    System.out.println("Password:");
                    password = read.nextLine();
                    System.out.println("Email:");
                    email = read.nextLine();
                    User user = new User(username, password);
                    auth.singUp(connection.getStream(), user, email);

                    System.out.println("The account was created successfully");
                }
                case "3" -> System.exit(0);
                default -> System.out.println("Invalid option. Try again...");
            }
        } while (true);
    }

}

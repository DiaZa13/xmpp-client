package org.zclient;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // object to read from console
        Scanner read = new Scanner(System.in);
        Connection connection = new Connection("alumchat.fun");
        Authentication auth = new Authentication();

        String username, password, email = "";
        String option, auth_opt, user_opt;

        do {
            System.out.println("------***** Chat Sistemas Operativos 2.0 ******-------");
            System.out.println("1 - Sign in");
            System.out.println("2 - Sign up");
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
                        System.out.println("2 - Chat");
                        System.out.println("3 - Group chat");
                        System.out.println("4 - Edit profile");
                        System.out.println("5 - Files");
                        // If disconnect is selected it should return to the old menu
                        System.out.println("6 - Disconnect");
                        System.out.println("7 - Exit");

                        auth_opt = read.nextLine();

                        switch (auth_opt) {
                            case "1" -> {
                                Contacts contacts = new Contacts(connection.getStream());
                                do {
                                    System.out.println("1 - Show contacts");
                                    System.out.println("2 - Add contact");
                                    System.out.println("3 - Details of a contact");
                                    System.out.println("4 - Go back");

                                    user_opt = read.nextLine();

                                    switch (user_opt) {
                                        case "1" -> contacts.getContacts();
                                        case "2" -> {
                                            connection.start();
                                            System.out.println("Email: ");
                                            email = read.nextLine();
                                            System.out.println("Username:");
                                            username = read.nextLine();
                                            contacts.addContact(email, username);

                                        }
                                        case "3" ->{
                                            System.out.println("TODO");
                                        }
                                        case "4" -> user_opt = "exit";
                                        default -> System.out.println("Invalid option. Try again...");
                                    }
                                } while (!"exit".equals(user_opt));
                            }
                            case "2" -> {}
                            case "3" -> {}
                            case "4" -> {}
                            case "5" -> System.out.println("TODO");
                            case "6" -> connection.close();
                            case "7" -> auth_opt = "exit";
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

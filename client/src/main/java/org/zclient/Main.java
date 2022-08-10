package org.zclient;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // object to read from console
        Scanner read = new Scanner(System.in);
        Connection connection = new Connection("alumchat.fun");
        Authentication auth = new Authentication();

        String username, password, email, to_user, msg = "";
        String option, auth_opt, user_opt, comunication_opt;

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
                        System.out.println("2 - Messaging");
                        System.out.println("3 - Edit profile");
                        // If disconnect is selected it should return to the old menu
                        System.out.println("4 - Disconnect");
                        System.out.println("5 - Go back");

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
                            case "2" -> {
                                Contacts contacts = new Contacts(connection.getStream());
                                Communication communication = new Communication(connection.getStream());
                                communication.incomeListener();
                                do {
                                    // The message can also be a file
                                    System.out.println("1 - Direct Message");
                                    System.out.println("2 - Join a group");
                                    System.out.println("3 - Go back");

                                    comunication_opt = read.nextLine();

                                    switch (comunication_opt) {
                                        case "1" -> {
                                            System.out.println("To: ");
                                            to_user = read.nextLine();
                                            System.out.println("Message: ");
                                            msg = read.nextLine();
                                            communication.sendDirectmessage(to_user, msg);
                                        }
                                        case "2" -> {
                                            connection.start();
                                            System.out.println("Email: ");
                                            email = read.nextLine();
                                            System.out.println("Username:");
                                            username = read.nextLine();
                                            contacts.addContact(email, username);

                                        }
                                        case "3" -> comunication_opt = "exit";
                                        default -> System.out.println("Invalid option. Try again...");
                                    }
                                } while (!"exit".equals(comunication_opt));
                            }
                            case "3" -> {}
                            case "4" -> connection.close();
                            case "5" -> auth_opt = "exit";
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

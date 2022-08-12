package org.zclient;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // object to read from console
        Scanner read = new Scanner(System.in);
        Connection connection = new Connection("alumchat.fun");
        Authentication auth = new Authentication();

        String username, password, email, to_user, msg = "";
        String option, auth_opt, user_opt, comunication_opt;

        // revisar sino es mejor conectarme desde el constructor

        do {
            System.out.println("------***** Chat Sistemas Operativos 2.0 ******-------");
            System.out.println("1 - Sign in");
            System.out.println("2 - Sign up");
            System.out.println("3 - Exit");

            option = read.nextLine();

            switch (option) {
                case "1" -> {
                    connection.start();
                    System.out.print("Username: ");
                    username = read.nextLine();
                    System.out.print("Password:");
                    password = read.nextLine();
                    User user = new User(username, password);
                    auth.singIn(connection.getStream(), user);

                    // Cleaning the coso para hacer saber que inició sesión
                    System.out.printf(" %s  -> Available", username);

                    Contacts contacts = new Contacts(connection.getStream());
                    Communication communication = new Communication(connection.getStream());
                    // Listen for presences
                    connection.presenceListener();
                    // Listen for messages
                    connection.messageListener();
                    // TODO hacer algo para indicar que se ha loggeado correctamente
                    System.out.println("\n\nType -help to see available options... ");
                    do{
                        auth_opt = read.nextLine();
                        if (auth_opt.equals("-help")){
                                System.out.println("-users\t show users/contacts");
                                System.out.println("-add<username, email> \t add a user to contacts");
                                System.out.println("-details<user jid> \t details of a user");
                                System.out.println("-msg<user/group jid, msg/file> \t sends a message o file to a group or user");
                                System.out.println("-join<room@service/nickname> \t join a chat room");
                                System.out.println("-edit \t edit user profile");
                                // Buscar manera de hacer logout sin desconectar de server
                                System.out.println("-exit \t logout");
                        } else if (auth_opt.startsWith("-users")) {
                            contacts.getContacts();

                        } else if (auth_opt.startsWith("-add")) {
                            String data = auth_opt.substring(5,auth_opt.length()-1);
                            String[] parts = data.split(",");
                            username = parts[0];
                            email = parts[1];
                            contacts.addContact(email, username);

                        } else if (auth_opt.startsWith("-details")) {
                            // TODO
                            username = auth_opt.substring(9,auth_opt.length()-1);

                        } else if (auth_opt.startsWith("-msg")) {
                            String data = auth_opt.substring(5, auth_opt.length()-1);
                            String[] parts = data.split(",");
                            to_user = parts[0];
                            msg = parts[1];
                            communication.sendMessage(to_user, msg, true);
                            // TODO message to a group
                            // TODO send files

                        } else if (auth_opt.startsWith("-join")) {
                            // TODO
                            String group = auth_opt.substring(6, auth_opt.length()-1);
                            communication.joinGroup(group);

                        } else if (auth_opt.startsWith("-edit")) {
                           // Debe de seleccionar qué quiere editar

                        }
                    }while (!"-exit".equals(auth_opt));

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
                case "3" -> {
                    connection.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again...");
            }
        } while (true);
    }

}

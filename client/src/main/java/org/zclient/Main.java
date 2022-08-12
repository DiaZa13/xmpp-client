package org.zclient;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // object to read from console
        Scanner read = new Scanner(System.in);
        Connection connection = new Connection("alumchat.fun");

        String username, password, email, to_user, msg, data = "";
        String option, auth_opt;

        // revisar sino es mejor conectarme desde el constructor

        do {
            System.out.println("------***** Chat Sistemas Operativos 2.0 ******-------");
            System.out.println("1 - Sign in");
            System.out.println("2 - Sign up");
            System.out.println("3 - Exit");

            option = read.nextLine();

            switch (option) {
                case "1" -> {
                    // connects to the server
                    connection.start();
                    // allows to handle the actual logged in account
                    Authentication authentication = new Authentication(connection.getStream());

                    System.out.print("Username: ");
                    username = read.nextLine();
                    System.out.print("Password:");
                    password = read.nextLine();
                    User user = new User(username, password);
                    authentication.singIn(user);

                    // Cleaning the coso para hacer saber que inició sesión
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.printf(" %s  -> Available", username);

                    Contacts contacts = new Contacts(connection.getStream());
                    Communication communication = new Communication(connection.getStream());
                    // Listen for presences
                    connection.presenceListener(contacts);
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
                                System.out.println("-presence<new presence> \t edit user profile");
                                System.out.println("-delete \t delete the account");
                                System.out.println("-logout \t logout");
                        } else if (auth_opt.startsWith("-users")) {
                            contacts.getContacts();

                        } else if (auth_opt.startsWith("-add")) {
                            data = auth_opt.substring(5,auth_opt.length()-1);
                            String[] parts = data.split(",");
                            username = parts[0];
                            email = parts[1].trim();
                            contacts.addContact(email, username);

                        } else if (auth_opt.startsWith("-details")) {
                            username = auth_opt.substring(9,auth_opt.length()-1);
                            System.out.println(contacts.contactDetails(username));

                        } else if (auth_opt.startsWith("-msg")) {
                            data = auth_opt.substring(5, auth_opt.length()-1);
                            String[] parts = data.split(",");
                            to_user = parts[0];
                            msg = parts[1].trim();

                            if (to_user.contains("conference"))communication.groupMessage(to_user, msg);
                            else communication.directMessage(to_user, msg);

                            // TODO send files

                        } else if (auth_opt.startsWith("-join")) {
                            String group = auth_opt.substring(6, auth_opt.length()-1);
                            communication.joinGroup(group);

                        } else if (auth_opt.startsWith("-presence")) {
                            data = auth_opt.substring(10,auth_opt.length()-1);
                            user.editPresence(connection.getStream(), data);

                        } else if (auth_opt.startsWith("-delete")) {
                            authentication.deleteAccount();
                            auth_opt = "-logout";
                        } else if (auth_opt.equals("-logout")) {
                            connection.close();
                        }
                    }while (!"-logout".equals(auth_opt));

                }
                case "2" -> {
                    connection.start();
                    Authentication authentication = new Authentication(connection.getStream());
                    System.out.println("Username: ");
                    username = read.nextLine();
                    System.out.println("Password:");
                    password = read.nextLine();
                    System.out.println("Email:");
                    email = read.nextLine();
                    User user = new User(username, password);
                    authentication.singUp(user, email);

                    System.out.println("The account was created successfully\n");
                    connection.close();
                }
                case "3" -> {
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again...");
            }
        } while (true);
    }

}

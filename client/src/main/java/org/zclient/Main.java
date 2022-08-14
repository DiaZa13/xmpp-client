package org.zclient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            System.out.print(util.clearScreen());
            System.out.flush();
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
                    System.out.print("Password: ");
                    password = read.nextLine();
                    User user = new User(username, password);

                    if (!authentication.singIn(user)){
                        System.out.println("\033[0;37m** It was an error while trying to log in \033[0m");
                    }else {
                        LocalDateTime now = LocalDateTime.now();
                        // Cleaning the console to let the user know that its log in was successful
                        System.out.print(util.clearScreen());
                        System.out.flush();
                        System.out.printf("%s Z-Network                                                                    [XMPP-Client] [%sonline\033[0m%s]  %n\033[0m", util.BKG, util.BG, util.BKG);
                        // Saves the cursor position
                        System.out.print(util.cursorSave());
                        // Moving the cursor to the bottom
                        System.out.printf(util.cursorTo(21,1) + util.BKG + "[" + now.format(DateTimeFormatter.ofPattern("HH:mm:ss"))  + "] %s                                                                  [-help]  %n\033[0m",connection.getStream().getUser().asBareJid());
                        // Defines the scroll window
                        System.out.print(util.scrollSet(2,20));

                        Contacts contacts = new Contacts(connection.getStream());
                        Communication communication = new Communication(connection.getStream());
                        // Listen for messages
//                        connection.messageListener();
//                        // Listen for presences
//                        connection.presenceListener(contacts);
                        connection.addListener(contacts);

                        do{
                            System.out.print(util.cursorTo(22,1) + "\033[0K");
                            auth_opt = read.nextLine();
                            // Restore the cursor to the last saved position
                            System.out.print(util.cursorRestore());
                            if (auth_opt.equals("-help")){
                                System.out.println("\t\033[0;37m-users                            show users/contacts");
                                System.out.println("\t-add<email>                       add a user to contacts");
                                System.out.println("\t-details<email>                   details of a user");
                                System.out.println("\t-msg<user/group jid, msg/file>    sends a message o file to a group or user");
                                System.out.println("\t-join<room@service/nickname>      join a chat room");
                                System.out.println("\t-presence<new presence>           edit user profile");
                                System.out.println("\t-delete                           delete the actual logged account");
                                System.out.println("\t-logout                           logout \033[0m");

                                System.out.println(util.cursorSave());
                            } else if (auth_opt.startsWith("-users")) {
                                contacts.getContacts();
                                System.out.println(util.cursorSave());
                            } else if (auth_opt.startsWith("-add")) {
                                email = auth_opt.substring(5,auth_opt.length()-1);

                                if (contacts.addContact(email)){
                                    System.out.println("\033[0;37m**The friendship request was sent succesfully\033[0m");
                                }else
                                    System.out.println("\033[0;37m**It was an error sending the request\033[0m");

                                System.out.println(util.cursorSave());
                            } else if (auth_opt.startsWith("-details")) {
                                username = auth_opt.substring(9,auth_opt.length()-1);
                                String result = contacts.contactDetails(username);
                                if (result.equals("error")){
                                    System.out.printf("\033[0;37m**It was an error obtaining the details for %s \033[0m%n", username);
                                }else{
                                    System.out.println(result);
                                }
                                System.out.println(util.cursorSave());
                            } else if (auth_opt.startsWith("-msg")) {
                                data = auth_opt.substring(5, auth_opt.length()-1);
                                String[] parts = data.split(",");
                                to_user = parts[0];
                                msg = parts[1].trim();

                                if (to_user.contains("conference")) communication.groupMessage(to_user, msg);
                                else communication.directMessage(to_user, msg);

                                // TODO send files

                            } else if (auth_opt.startsWith("-join")) {
                                String group = auth_opt.substring(6, auth_opt.length()-1);

                                if (communication.joinGroup(group)){
                                    System.out.printf("\033[0;37m**Successfully joined the group %s\033[0m%n", group);
                                }else
                                    System.out.println("\033[0;37m**It was an error while trying to join the group\033[0m");
                                System.out.println(util.cursorSave());
                            } else if (auth_opt.startsWith("-presence")) {
                                data = auth_opt.substring(10,auth_opt.length()-1);

                                if (user.editPresence(connection.getStream(), data)){
                                    System.out.printf("\033[0;37m** Presence message successfully updated to %s\033[0m%n", data);
                                }else
                                    System.out.println("\033[0;37m** It was an error while trying to update the presence \033[0m");
                                System.out.println(util.cursorSave());
                            } else if (auth_opt.startsWith("-delete")) {
                                authentication.deleteAccount();
                                auth_opt = "-logout";
                                System.out.println(util.scrollScreen());

                            } else if (auth_opt.equals("-logout")) {
                                connection.close();
                                System.out.println(util.scrollScreen());
                            }else {
                                System.out.println("Invalid option. Type -help to see available options...");
                                System.out.println(util.cursorSave());
                            }
                        }while (!"-logout".equals(auth_opt));
                    }

                }
                case "2" -> {
                    connection.start();
                    Authentication authentication = new Authentication(connection.getStream());
                    System.out.print("Username: ");
                    username = read.nextLine();
                    System.out.print("Password: ");
                    password = read.nextLine();
                    System.out.print("Email: ");
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

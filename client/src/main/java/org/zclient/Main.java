package org.zclient;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.Console;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class  Main {

    public static void main(String[] args) throws XmppStringprepException {

        // object to read from console
        Scanner read = new Scanner(System.in);
        Connection connection = new Connection("alumchat.fun");
        Console console = System.console();

        String username, password, email, to_user, msg, data = "";
        String option, auth_opt;
        Map<BareJid, EntityFullJid> user_roster = new HashMap<BareJid, EntityFullJid>();
        Stack<Jid> requests = new Stack<>();
        LocalDateTime now;

        do {
            System.out.print(util.clearScreen());
            System.out.flush();
            System.out.println("\n\t------------- CHAT SISTEMAS OPERATIVOS 2.0 --------------\n");
            System.out.println("\t Sign in............................................ [1]");
            System.out.println("\t Sign up............................................ [2]");
            System.out.printf("\t Exit............................................... [3]%n\t> ");

            option = read.nextLine();

            switch (option) {
                case "1" -> {
                    // connects to the server
                    connection.start();
                    // allows to handle the actual logged in account
                    Authentication authentication = new Authentication(connection.getStream());
                    System.out.println("\n\t----------WELCOME BACK ----------------------------------");
                    System.out.print("\t Username: ");
                    username = read.nextLine();
                    System.out.print("\t Password: ");
                    password = new String(console.readPassword());
                    User user = new User(username, password, user_roster, requests);

                    if (!authentication.singIn(user)){
                        System.out.println("\033[0;37m** It was an error while trying to log in \033[0m");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    }else {
                        now = LocalDateTime.now();
                        // Cleaning the console to let the user know that its log in was successful
                        System.out.print(util.clearScreen());
                        System.out.flush();
                        System.out.printf("%s Z-Network                                                               [XMPP-Client] [%sonline\033[0m%s]  %n\033[0m", util.BKG, util.BG, util.BKG);
                        // Saves the cursor position
                        System.out.print(util.cursorSave());
                        // Moving the cursor to the bottom
                        System.out.printf(util.cursorTo(31,1) + util.BKG + "[" + now.format(DateTimeFormatter.ofPattern("HH:mm:ss"))  + "] %s                                                           [-help]  %n\033[0m",connection.getStream().getUser().asBareJid());
                        // Defines the scroll window
                        System.out.print(util.scrollSet(2,30));

                        Contacts contacts = new Contacts(connection.getStream());
                        Communication communication = new Communication(connection.getStream());
                        // Listen for income messages and presences
                        connection.addListener(user);
                        // Listen for income files
                        communication.receiveFile("..\\files/received");

                        do{
                            System.out.print(util.cursorTo(32,1) + "\033[0J");
                            auth_opt = read.nextLine();
                            // Restore the cursor to the last saved position
                            System.out.print(util.cursorRestore());
                            if (auth_opt.equals("-help")){
                                System.out.printf("\t%s-users                            show users/contacts%n", util.SM);
                                System.out.println("\t-request                          show pending subscription requests");
                                System.out.println("\t-add<jid>                         add a user to your contacts");
                                System.out.println("\t-remove<jid>                      removes a user from your contacts");
                                System.out.println("\t-details<jid>                     details of a user");
                                System.out.println("\t-msg<user/group jid, msg/file>    sends a message  to a group or user");
                                System.out.println("\t-file<user jid, file path>        sends a file to the specified user");
                                System.out.println("\t-join<room@service/nickname>      join a chat room");
                                System.out.println("\t-presence<new presence>           edit the message presence");
                                System.out.println("\t-show<new show>                   change the user presence show");
                                System.out.println("\t-delete                           delete the actual logged account");
                                System.out.println("\t-logout                           logout \033[0m");

                                System.out.print(util.cursorSave());

                            } else if (auth_opt.startsWith("-users")) {
                                System.out.printf("------USERS------------------------%n");
                                Map<RosterEntry, Presence> users = contacts.getContacts();
                                for (RosterEntry entry : users.keySet()) {
                                    String presence = users.get(entry).getType() == Presence.Type.available?util.BG:util.BR;
                                    System.out.printf("%s *%s%n", presence, entry.getJid());
                                }

                                System.out.print(util.cursorSave());

                            } else if (auth_opt.startsWith("-request")) {
                                String response;
                                if (user.requests().empty()){
                                    System.out.printf("%s** You have no more pending requests \033[0m%n",util.SM);

                                }else{
                                    Jid friend = user.requests().pop();
                                    System.out.print(util.cursorTo(32,1) + "\033[0K");
                                    System.out.printf(" -> %s wants to subscribe to your roster? [y/n] ", friend);
                                    response = read.nextLine();

                                    System.out.print(util.cursorRestore());
                                    if (response.equalsIgnoreCase("n")){
                                        contacts.declineSubscription(connection.getStream(), friend);
                                        System.out.printf("%s** Rejected request from %s \033[0m%n",util.SM, friend);

                                    }else{
                                        contacts.acceptSubscription(connection.getStream(), friend);
                                        System.out.printf("%s** Accepted request from %s\033[0m%n", util.SM, friend);
                                    }
                                    if (user.requests().size() > 0)
                                        System.out.printf("%s** You still have %d pending requests \033[0m%n", util.SM, user.requests().size());
                                }

                                System.out.print(util.cursorSave());

                            } else if (auth_opt.startsWith("-add")) {
                                email = auth_opt.substring(5,auth_opt.length()-1);

                                if (contacts.addContact(email)){
                                    System.out.printf("%s** The friendship request was sent succesfully\033[0m%n",util.SM);
                                }else
                                    System.out.printf("%s** It was an error sending the request\033[0m%n",util.SM);

                                System.out.print(util.cursorSave());

                            } else if (auth_opt.startsWith("-remove")) {
                                email = auth_opt.substring(8,auth_opt.length()-1);

                                if (contacts.deleteContact(email)){
                                    System.out.printf("%s** %s was removed successfully from your roster\033[0m%n",util.SM,email);
                                }else
                                    System.out.printf("%s** It was an error while deleting %s from your roster\033[0m%n",util.SM, email);

                                System.out.print(util.cursorSave());


                            } else if (auth_opt.startsWith("-details")) {
                                username = auth_opt.substring(9,auth_opt.length()-1);
                                String result = contacts.contactDetails(username);
                                System.out.println(result);

                                System.out.print(util.cursorSave());

                            } else if (auth_opt.startsWith("-msg")) {
                                data = auth_opt.substring(5, auth_opt.length()-1);
                                String[] parts = data.split(",");
                                to_user = parts[0];
                                msg = parts[1].trim();
                                now = LocalDateTime.now();

                                if (to_user.contains("conference")) communication.groupMessage(to_user, msg);
                                else communication.directMessage(to_user, msg);

                                // Shows en console the message that the user sent
                                System.out.printf("\033[0;93m[" + now.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "]%s to %s: \033[0;37m%s \033[0m%n", util.BW, to_user, msg);

                                System.out.print(util.cursorSave());

                            } else if (auth_opt.startsWith("-file")) {
                                data = auth_opt.substring(6, auth_opt.length()-1);
                                String[] parts = data.split(",");
                                to_user = parts[0];
                                msg = parts[1].trim();
                                Map<BareJid, EntityFullJid> roster = user.roster();
                                if(communication.sendFile(roster.get(JidCreate.entityBareFrom(to_user)), msg))
                                    System.out.printf("%s** The file was sent successfully\033[0m%n",util.SM);
                                else
                                    System.out.printf("%s** It was an error while trying to send the file\033[0m%n", util.SM);

                                System.out.print(util.cursorSave());

                            } else if (auth_opt.startsWith("-join")) {
                                String group = auth_opt.substring(6, auth_opt.length()-1);

                                if (communication.joinGroup(group)){
                                    System.out.printf("%s** Successfully joined the group %s\033[0m%n", util.SM, group);
                                }else
                                    System.out.printf("%s** It was an error while trying to join the group\033[0m", util.SM);

                                System.out.print(util.cursorSave());

                            } else if (auth_opt.startsWith("-presence")) {
                                data = auth_opt.substring(10,auth_opt.length()-1);

                                if (user.editPresence(connection.getStream(), data)){
                                    System.out.printf("%s** Presence message successfully updated to %s\033[0m%n", util.SM, data);
                                }else
                                    System.out.printf("%s** It was an error while trying to update the presence \033[0m%n", util.SM);

                                System.out.print(util.cursorSave());

                            } else if (auth_opt.startsWith("-delete")) {
                                authentication.deleteAccount();
                                auth_opt = "-logout";

                                System.out.println(util.scrollScreen());

                            } else if (auth_opt.equals("-logout")) {
                                connection.close();
                                System.out.println(util.scrollScreen());

                            }else {
                                System.out.printf("%s** Invalid option. Type -help to see available options...\033[0m%n", util.SM);
                                System.out.print(util.cursorSave());
                            }
                        }while (!"-logout".equals(auth_opt));
                    }

                }
                case "2" -> {
                    connection.start();
                    Authentication authentication = new Authentication(connection.getStream());
                    System.out.println("\n\t---------CREATE ACCOUNT ---------------------------------");
                    System.out.print("\t Username: ");
                    username = read.nextLine();
                    System.out.print("\t Password: ");
                    password = new String(console.readPassword());
                    System.out.print("\t Email: ");
                    email = read.nextLine();
                    User user = new User(username, password, user_roster, requests);
                    authentication.singUp(user, email);

                    System.out.println("The account was created successfully\n");
                    connection.close();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }
                }
                case "3" -> {
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again...");
            }
        } while (true);
    }

}

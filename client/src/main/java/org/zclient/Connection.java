package org.zclient;


import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Connection {
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String RED = "\033[1;31m";
    private String domain;
    private AbstractXMPPConnection stream;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    Scanner read = new Scanner(System.in);

    public Connection(String domain){
        this.domain = domain;
    }

    public void start() {
        try {
            XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                    .setXmppDomain(domain)
                    .setHost(domain)
                    // this tells the server that is ready to receive data
                    .setSendPresence(true)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();

            stream = new XMPPTCPConnection(configuration);
            stream.connect();

        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractXMPPConnection getStream() {
        return stream;
    }

    public void presenceListener(Contacts contact){
        StanzaListener listener = stanza -> {
            Presence presence = (Presence) stanza;
            if (!presence.getFrom().equals(stream.getUser())){
            if (presence.getType().equals(Presence.Type.subscribe)){
                new Thread(() -> {
                    System.out.printf("\033[1;33m** %s wants to subscribe to your roster \033[0m\n", presence.getFrom());
                    System.out.print("\033[0;37m   Do you accept the subscription?\n");
                    System.out.println("\t1 - Accept");
                    System.out.println("\t2 - Reject");
                    read.nextLine();
                    String response = read.nextLine();

                    contact.handleSubscription(stream, response, presence.getFrom());

                }).start();

            } else if (presence.getType().equals(Presence.Type.subscribed)) {
                System.out.printf("\033[1;33m** %s is subscribed to your roster \033[0m\n", presence.getFrom());
            } else if (presence.getType().equals(Presence.Type.unsubscribed)) {
                System.out.printf("\033[1;33m** %s is unsubscribed to your roster \033[0m\n", presence.getFrom());
            } else if (presence.getType().equals(Presence.Type.available)) {
                System.out.printf(GREEN_BOLD + "** %s is available \033[0m\n", presence.getFrom());
            } else if (presence.getType().equals(Presence.Type.unavailable)) {
                System.out.printf(RED + "** %s is unavailable \033[0m\n", presence.getFrom());
            } else{
                    System.out.printf("\033[1;33m** %s has updated its status to %s... \033[0m\n", presence.getFrom(),
                            presence.getStatus() != null?presence.getStatus():" - ");
                }
            }

        };

        StanzaFilter presence_filter = new StanzaTypeFilter(Presence.class);

        // Managing async the incoming presences
        stream.addStanzaListener(listener, presence_filter);

    }

    public void messageListener(){
        StanzaListener listener = stanza -> {
            Message message = (Message) stanza;
            if (message.getBody() != null){
                LocalDateTime now = LocalDateTime.now();
                if (message.getFrom().toString().contains("conference")){
                    System.out.printf("\033[0;93m[" + formatter.format(now) + "]\033[1;94m %s: \033[0;34m%s \033[0m\n", message.getFrom(), message.getBody());
                }else
                    System.out.printf("\033[0;93m[" + formatter.format(now) + "]\033[1;95m %s: \033[0;37m%s \033[0m\n", message.getFrom().asBareJid(), message.getBody());
            }
        };

        StanzaFilter message_filter = new StanzaTypeFilter(Message.class);

        // Managing async the incoming messages
        new Thread(() -> stream.addStanzaListener(listener, message_filter)).start();
    }


    // maybe i have to remove the listeners
    public void close(){
        stream.disconnect();
    }

}

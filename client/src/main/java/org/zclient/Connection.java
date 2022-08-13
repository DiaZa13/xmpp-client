package org.zclient;


import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Connection {
    public static final String GREEN = "\033[1;32m";
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
                switch (presence.getType()){
                    case subscribe -> {
                        // Request a subscription

                            System.out.printf("\n\033[1;33m** %s wants to subscribe to your roster \033[0m", presence.getFrom());
                            System.out.print("\n\033[0;37m   Do you accept the subscription?");
                            System.out.println("\n\t1 - Accept");
                            System.out.println("\n\t2 - Reject");
                            read.nextLine();
                            String response = read.nextLine();

                            contact.handleSubscription(stream, response, presence.getFrom());


                    }
                    case subscribed -> System.out.printf("\n\033[1;33m** %s is subscribed to your roster \033[0m", presence.getFrom());
                    case unsubscribed -> System.out.printf("\n\033[1;33m** %s is unsubscribed to your roster \033[0m", presence.getFrom());
                    case available -> {
                        if (presence.getStatus() != null)
                            System.out.printf("\n\033[1;33m** %s has updated its status to %s \033[0m", presence.getFrom(), presence.getStatus());
                        else
                            System.out.printf(GREEN + "\n** %s is available  \033[0m", presence.getFrom());
                    }
                    case unavailable -> {
                        if (presence.getStatus() != null)
                            System.out.printf("\n\033[1;33m** %s has updated its status to %s \033[0m", presence.getFrom(), presence.getStatus());
                        else
                            System.out.printf(RED + "\n** %s is unavailable \033[0m", presence.getFrom());
                    }
                    default -> System.out.printf("%n%s",presence);
                }
            }

        };

        StanzaFilter presence_filter = new StanzaTypeFilter(Presence.class);

        stream.addStanzaListener(listener, presence_filter);

    }

    public void messageListener(){
        StanzaListener listener = stanza -> {
            Message message = (Message) stanza;
            if (message.getBody() != null){
                LocalDateTime now = LocalDateTime.now();
                if (message.getFrom().toString().contains("conference")){
                    System.out.printf("\n\033[0;93m[" + formatter.format(now) + "]\033[1;94m %s: \033[0;34m%s \033[0m", message.getFrom(), message.getBody());
                }else
                    System.out.printf("\n\033[0;93m[" + formatter.format(now) + "]\033[1;95m %s: \033[0;37m%s \033[0m", message.getFrom().asBareJid(), message.getBody());
            }
        };

        StanzaFilter message_filter = new StanzaTypeFilter(Message.class);

        // Managing async the incoming messages
        stream.addStanzaListener(listener, message_filter);
    }


    // maybe i have to remove the listeners
    public void close(){
        stream.disconnect();
    }

}

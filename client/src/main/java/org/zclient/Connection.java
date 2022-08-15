package org.zclient;


import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.OrFilter;
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
    private final String domain;
    private AbstractXMPPConnection stream;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    Scanner read = new Scanner(System.in);

    public Connection(String domain){
        this.domain = domain;
    }

    public void start() {
        try {
            XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                    .setXmppDomain(domain)
                    .setHost(domain)
                    // sends presence to let know the server that is available
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

    public void handlerSubscription(Contacts contacts, Presence presence){
        System.out.print(util.cursorRestore());
        System.out.printf("\033[1;33m** %s wants to subscribe to your roster \033[0m%n", presence.getFrom());
        System.out.print(util.cursorSave());
        System.out.print(util.cursorTo(22, 1));
        System.out.print("\033[0;37mDo you want to accept the subscription? [Y/n] ");
        // the readers got confused i think idk
        String response = read.next();
        try {
            // Espera hasta obtener la respuesta
            Thread.currentThread().wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Handle request subscription
        if ("n".equalsIgnoreCase(response)) {
            contacts.declineSubscription(stream, presence.getFrom());

            System.out.print(util.cursorRestore());
            System.out.println("\033[0;37m** Rejected request \033[0m");
            System.out.print(util.cursorSave());
        } else {
            contacts.acceptSubscription(stream, presence.getFrom());

            System.out.print(util.cursorRestore());
            System.out.println("\033[0;37m** Accepted request \033[0m");
            System.out.print(util.cursorSave());
        }
        Thread.currentThread().notifyAll();
    }

    public void presenceListener(Presence presence){
        System.out.print(util.cursorRestore());
        switch (presence.getType()) {
            case subscribed -> {
                System.out.printf("\033[1;33m** %s is subscribed to your roster \033[0m%n", presence.getFrom());
                System.out.print(util.cursorSave());
            }
            case unsubscribed -> {
                System.out.printf("\033[1;33m** %s is unsubscribed to your roster \033[0m%n", presence.getFrom());
                System.out.print(util.cursorSave());
            }
            case available -> {
                if (presence.getStatus() != null)
                    System.out.printf("\033[1;33m** %s has updated its status to %s \033[0m%n", presence.getFrom(), presence.getStatus());
                else {
                    String[] data = presence.getStanzaId().split("-");
                    try{
                        int id = Integer.parseInt(data[1]);
                        if (id == 2)
                            System.out.printf(util.SG + "** %s is available  \033[0m%n", presence.getFrom());
                    }catch (Exception e){
                        System.out.printf(util.SG + "** %s is available  \033[0m%n", presence.getFrom());
                    }
                }
                System.out.print(util.cursorSave());
            }
            case unavailable -> {
                if (presence.getStatus() != null)
                    System.out.printf("\033[1;33m** %s has updated its status to %s \033[0m%n", presence.getFrom(), presence.getStatus());
                else
                    System.out.printf(util.SR + "** %s is unavailable \033[0m%n", presence.getFrom());

                System.out.print(util.cursorSave());
            }
            default -> {}
        }
        System.out.print(util.cursorTo(22, 1));
    }

    public void messageListener(Message message){
        LocalDateTime now = LocalDateTime.now();
        System.out.print(util.cursorRestore());

        if (message.getFrom().toString().contains("conference")){
            System.out.printf("\033[0;93m[" + formatter.format(now) + "]\033[1;94m %s: \033[0;34m%s \033[0m%n", message.getFrom(), message.getBody());
            System.out.print(util.cursorSave());
        }else{
            System.out.printf("\033[0;93m[" + formatter.format(now) + "]\033[1;95m %s: \033[0;37m%s \033[0m%n", message.getFrom().asBareJid(), message.getBody());
            System.out.print(util.cursorSave());
        }

        // TODO ver si puedo regresarlo a la posicion donde estaba escribiendo
        System.out.print(util.cursorTo(22,1));
    }

    public void addListener(Contacts contacts){

        // Filter the incoming stanzas
        StanzaFilter message_filter = new StanzaTypeFilter(Message.class);
        StanzaFilter presence_filter = new StanzaTypeFilter(Presence.class);
        OrFilter stanzaFilter = new OrFilter();
        stanzaFilter.addFilter(message_filter);
        stanzaFilter.addFilter(presence_filter);

        StanzaListener listener = stanza -> {
            String subclass = stanza.getClass().getName();
            if (subclass.contains("Message")){
                Message message = (Message) stanza;
                if (message.getBody() != null){
                    new Thread(() -> messageListener(message)).start();
                }
            }else {
                Presence presence = (Presence) stanza;
                if (!presence.getFrom().equals(stream.getUser())) {
                    if (presence.getType().equals(Presence.Type.subscribe)){
                        // If the presence is a request to subscribe to the user roster
                        // then it creates a new thread to handle it
                        new Thread(() -> handlerSubscription(contacts, presence)).start();
                    }else {
                        new Thread(() -> presenceListener(presence)).start();
                    }
                }
            }

        };

        stream.addStanzaListener(listener, stanzaFilter);
    }

    public void close(){
        stream.disconnect();
    }

}

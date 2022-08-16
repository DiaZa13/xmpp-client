package org.zclient;


import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.Console;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Connection {
    private final String domain;
    private AbstractXMPPConnection stream;
    static Semaphore mutex = new Semaphore(0);
    static Semaphore mutex1 = new Semaphore(1);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    final Scanner read = new Scanner(System.in);


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

    public synchronized void presenceListener(Presence presence, User user){

        System.out.print(util.cursorRestore());

        switch (presence.getType()) {
            case subscribe -> {
                System.out.printf("%s** %s sent you a subscription request \033[0m%n", util.SF, presence.getFrom());
                user.requests().push(presence.getFrom());
            }
            case unsubscribe -> System.out.printf("%s** %s unsubscribe from your roster \033[0m%n", util.SW, presence.getFrom());

            case subscribed -> System.out.printf("%s** %s is subscribed to your roster \033[0m%n", util.SS, presence.getFrom());

            case unsubscribed -> System.out.printf("%s** %s is unsubscribed to your roster \033[0m%n", util.SU, presence.getFrom());

            case available -> {
                if (presence.getStatus() != null)
                    System.out.printf("%s** %s has updated its status to %s \033[0m%n", util.SW, presence.getFrom(), presence.getStatus());
                else if (presence.getMode() != Presence.Mode.available && presence.getMode() != null) {
                    System.out.printf("%s** %s has updated its show to %s \033[0m%n", util.SW, presence.getFrom(), presence.getMode());
                } else {
                    String[] data = presence.getStanzaId().split("-");
                    try{
                        int id = Integer.parseInt(data[1]);
                        if (id == 2)
                            System.out.printf(util.SG + "** %s is available  \033[0m%n", presence.getFrom());
                    }catch (Exception e){
                        System.out.printf(util.SG + "** %s is available  \033[0m%n", presence.getFrom());
                    }
                }
                user.addUser2roster(presence.getFrom().asBareJid(), presence.getFrom().asEntityFullJidIfPossible());
            }
            case unavailable -> {
                if (presence.getStatus() != null)
                    System.out.printf("%s** %s has updated its status to %s \033[0m%n", util.SW, presence.getFrom(), presence.getStatus());
                else if (presence.getMode() != null && presence.getMode() != Presence.Mode.available) {
                    System.out.printf("%s** %s has updated its show to %s \033[0m%n", util.SW, presence.getFrom(), presence.getMode());
                }
                else
                    System.out.printf(util.SR + "** %s is unavailable \033[0m%n", presence.getFrom());

                user.addUser2roster(presence.getFrom().asBareJid(), presence.getFrom().asEntityFullJidIfPossible());
            }
            case error -> System.out.printf(util.SR + "** It has ocurred an error \033[0m%n");
            default -> System.out.printf("%s%n", presence);
        }

        System.out.print(util.cursorSave());

        System.out.print(util.cursorTo(22, 1));

    }

    public synchronized void messageListener(Message message, User user){
        LocalDateTime now = LocalDateTime.now();
        System.out.print(util.cursorRestore());

        if (message.getFrom().toString().contains("conference")){
            System.out.printf("\033[0;93m[" + formatter.format(now) + "]\033[1;94m %s: \033[0;34m%s \033[0m%n", message.getFrom(), message.getBody());
        }else{
            System.out.printf("\033[0;93m[" + formatter.format(now) + "]%s %s: %s%s \033[0m%n", util.DMU, message.getFrom().asBareJid(), util.DMM, message.getBody());

        }

        user.addUser2roster(message.getFrom().asBareJid(), message.getFrom().asEntityFullJidIfPossible());

        System.out.print(util.cursorSave());
        System.out.print(util.cursorTo(22,1));
    }

    public void addListener(User user){

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
                    new Thread(() -> messageListener(message, user)).start();
                }
            }else {
                Presence presence = (Presence) stanza;
                if (!presence.getFrom().equals(stream.getUser())) {
                        new Thread(() -> presenceListener(presence, user)).start();

                }
            }

        };

        stream.addStanzaListener(listener, stanzaFilter);
    }

    public void close(){
        stream.disconnect();
    }

}

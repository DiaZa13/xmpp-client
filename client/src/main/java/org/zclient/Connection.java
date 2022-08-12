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

public class Connection {
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String RED = "\033[0;31m";     // RED
    public static final String RESET = "\033[0m";  // Text Reset
    private String domain;
    AbstractXMPPConnection stream;

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

    public void presenceListener(){
        StanzaListener listener = stanza -> {
            Presence presence = (Presence) stanza;
            if (!presence.getFrom().equals(stream.getUser())){
                String color = presence.getType().equals(Presence.Type.available)?GREEN_BOLD:RED;
                System.out.printf(color + "📗 \033[0m%s %s... \n",
                        presence.getFrom(),
                        presence.getStatus() != null?"status is " + presence.getStatus():"has no status");
            }
        };


        StanzaFilter presence_filter = new StanzaTypeFilter(Presence.class);

        stream.addStanzaListener(listener, presence_filter);
    }

    // TODO add nickname to conference chats
    public void messageListener(){
        StanzaListener listener = stanza -> {
            Message message = (Message) stanza;
            if (message.getBody() != null){
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                System.out.printf("\033[0;93m[" + dtf.format(now) + "]\033[1;94m %s: \033[0;34m%s \033[0m\n", message.getFrom().asBareJid(), message.getBody());
            }
        };

        StanzaFilter message_filter = new StanzaTypeFilter(Message.class);

        stream.addStanzaListener(listener, message_filter);
    }

    // maybe i have to remove the listeners
    public void close(){
        stream.disconnect();
    }

}

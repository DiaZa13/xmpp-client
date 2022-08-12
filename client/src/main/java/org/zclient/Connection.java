package org.zclient;


import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

public class Connection {
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
                System.out.printf("%s presence: %s -> %s \n",presence.getFrom(), presence.getType(), presence.getStatus());
            }
        };


        StanzaFilter presence_filter = new StanzaTypeFilter(Presence.class);

        stream.addStanzaListener(listener, presence_filter);
    }

    public void messageListener(){
        StanzaListener listener = stanza -> {
            Message message = (Message) stanza;
            if (message.getBody().equals("null")){
                System.out.printf("%s:, %s \n",message.getFrom(), message.getBody());
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

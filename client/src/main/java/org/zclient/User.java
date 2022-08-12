package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jxmpp.jid.impl.JidCreate;

public record User(String username, String password) {
    public User {
        if (username.isEmpty() || password.isEmpty()) {
            throw new java.lang.IllegalArgumentException(
                    String.format("Invalid arguments: %s, %s", username, password));
        }
    }

    public void editPresence(AbstractXMPPConnection connection, String status){
        Presence presence = connection.getStanzaFactory()
                .buildPresenceStanza()
                .setStatus(status)
                .build();

        try {
            connection.sendStanza(presence);
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


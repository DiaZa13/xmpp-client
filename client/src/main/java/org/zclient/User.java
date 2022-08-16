package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityFullJid;

import java.util.HashMap;
import java.util.Map;

public record User(String username, String password, Map<BareJid, EntityFullJid> roster) {

    public User {
        if (username.isEmpty() || password.isEmpty()) {
            throw new java.lang.IllegalArgumentException(
                    String.format("Invalid arguments: %s, %s", username, password));
        }
    }

    public boolean editPresence(AbstractXMPPConnection connection, String status){
        Presence presence = connection.getStanzaFactory()
                .buildPresenceStanza()
                .setStatus(status)
                .build();

        try {
            connection.sendStanza(presence);
            return true;
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            return false;
        }
    }

    public void addUser2roster(BareJid email, EntityFullJid jid){
        roster.put(email, jid);
    }

}


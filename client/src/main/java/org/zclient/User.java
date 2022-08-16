package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;

import java.util.Map;
import java.util.Stack;

/**
* User
* Defines a basic record xmpp-user
* @author zaray
 *@version 1.0
**/

public record User(String username, String password, Map<BareJid, EntityFullJid> roster, Stack<Jid> requests) {

    public User {
        if (username.isEmpty() || password.isEmpty()) {
            throw new java.lang.IllegalArgumentException(
                    String.format("Invalid arguments: %s, %s", username, password));
        }
    }

    /**
     * Allows to edit the presence message (status) of a user
     * @param connection - the actual connection to a xmpp-server
     * @param status - defines the new presence message (status)
     * @return true if successfully edit the status, otherwise false
     **/
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

    /**
     * Handle the full JID of users in the roster
     * @param email - bareJid of the user
     * @param jid - full JID of the users (with resource part)
     **/
    public void addUser2roster(BareJid email, EntityFullJid jid){
        roster.put(email, jid);
    }

}


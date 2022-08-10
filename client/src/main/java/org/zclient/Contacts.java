package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Collection;
import java.util.List;

public class Contacts {

    private Roster roster;
    public Contacts(AbstractXMPPConnection connection){
        roster = Roster.getInstanceFor(connection);

    }

    public void getUsers(){

    }

    public void getContacts(){
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            Presence 	presence = roster.getPresence(entry.getJid());
            // delete this print of here
            System.out.printf("User: %s -> %s%n", entry.getJid(), presence.getType());
        }

    }

    public void addContact(String email, String username){
        try {
            BareJid jid = JidCreate.bareFrom(email);
            roster.createItemAndRequestSubscription(jid, username, null);
        } catch (XmppStringprepException | SmackException.NotLoggedInException | SmackException.NoResponseException |
                 XMPPException.XMPPErrorException | SmackException.NotConnectedException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void contactDetails(){

    }
}

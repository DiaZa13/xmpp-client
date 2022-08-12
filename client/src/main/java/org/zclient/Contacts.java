package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Collection;
import java.util.List;

public class Contacts {

    private Roster roster;
    public Contacts(AbstractXMPPConnection connection){
        roster = Roster.getInstanceFor(connection);
        final Roster.SubscriptionMode manual = Roster.SubscriptionMode.manual;


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

    public String contactDetails(String user){
        try {
            RosterEntry information = roster.getEntry(JidCreate.bareFrom(user));
            Presence presence = roster.getPresence(JidCreate.bareFrom(user));
            return String.format("🧑 %s%n\tName: %s%n\tPresence: %s%n\tStatus: %s%n\tShow: %s%n" ,
                    information.getJid(),
                    !information.getName().isEmpty()?information.getName():"-",
                    presence.getType(),
                    !presence.getStatus().isEmpty()?presence.getStatus():"-",
                    presence.getMode());

        } catch (XmppStringprepException e) {
            throw new RuntimeException(e);
        }

    }

    public void handleSubscription(Boolean response){
        SubscribeListener lister = new SubscribeListener() {
            @Override
            public SubscribeAnswer processSubscribe(Jid jid, Presence presence) {
                System.out.printf("%s wants to add you to its ");
                return null;
            }
        };

        roster.addSubscribeListener(lister);
    }
}

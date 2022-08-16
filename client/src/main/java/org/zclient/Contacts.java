package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Contacts {

    private final Roster roster;

    public Contacts(AbstractXMPPConnection connection){
        roster = Roster.getInstanceFor(connection);
        roster.setSubscriptionMode(Roster.SubscriptionMode.manual);

    }

    public Map<RosterEntry, Presence> getContacts(){
        Collection<RosterEntry> entries = roster.getEntries();
        Map<RosterEntry, Presence> users = new HashMap<RosterEntry, Presence>();
        for (RosterEntry entry : entries) {
            Presence presence = roster.getPresence(entry.getJid());
            users.put(entry, presence);
        }
        return users;
    }

    public boolean addContact(String email){
        try {
            BareJid jid = JidCreate.bareFrom(email);
            roster.createItemAndRequestSubscription(jid, String.valueOf(jid.getLocalpartOrNull()), null);
            return true;
        } catch (XmppStringprepException | SmackException.NotLoggedInException | SmackException.NoResponseException |
                 XMPPException.XMPPErrorException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean deleteContact(String email){
        try {
            RosterEntry user = roster.getEntry(JidCreate.bareFrom(email));
            roster.removeEntry(user);
            return true;
        } catch (SmackException.NotLoggedInException | SmackException.NoResponseException |
                 XMPPException.XMPPErrorException | SmackException.NotConnectedException | InterruptedException |
                 XmppStringprepException e) {
            return false;
        }
    }

    public String contactDetails(String user){
        try {
            RosterEntry information = roster.getEntry(JidCreate.bareFrom(user));
            if (information != null){
                Presence presence = roster.getPresence(JidCreate.bareFrom(user));
                return String.format("User JID: %s%n\tName: %s%n\tPresence: %s%n\tStatus: %s%n\tShow: %s%n\tContact: %s" ,
                        information.getJid(),
                        information.getName() != null?information.getName():"-",
                        presence.getType(),
                        presence.getStatus() != null?presence.getStatus():"-",
                        presence.getMode(),
                        roster.isSubscribedToMyPresence(JidCreate.bareFrom(user)));
            }
        } catch (XmppStringprepException e) {
            return util.SM + "** It was an error while obtaining the details for your contact";
        }
        return util.SM + "** It was an error while obtaining the details for your contact";
    }

    public void declineSubscription(AbstractXMPPConnection connection, Jid to){
        Presence presence_response = connection.getStanzaFactory()
                .buildPresenceStanza()
                .to(to)
                .ofType(Presence.Type.unsubscribed)
                .build();

        try {
            connection.sendStanza(presence_response);
        } catch (SmackException.NotConnectedException | InterruptedException ignored) {

        }
    }

    public void acceptSubscription(AbstractXMPPConnection connection, Jid to){
        Presence presence_response = connection.getStanzaFactory()
                .buildPresenceStanza()
                .to(to)
                .ofType(Presence.Type.subscribed)
                .build();

        // If the other user is not subscribed to the user roster it sends a request
        Presence request = connection.getStanzaFactory()
                .buildPresenceStanza()
                .to(to)
                .ofType(Presence.Type.subscribe)
                .build();

        try {
            connection.sendStanza(presence_response);
            // TODO Validate if the user is already on the roster
            connection.sendStanza(request);
        } catch (SmackException.NotConnectedException | InterruptedException ignored) {
        }

    }

}

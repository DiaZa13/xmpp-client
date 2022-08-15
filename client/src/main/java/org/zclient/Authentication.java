package org.zclient;


import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Authentication{

    private final AbstractXMPPConnection connection;
    private final AccountManager accountManager;

    public Authentication(AbstractXMPPConnection connection){
        this.accountManager = AccountManager.getInstance(connection);
        this.connection = connection;
    }

    public boolean singIn(User user){
        // If the connection is still active, then try to sign in
        if (connection.isConnected()){
            try {
                connection.login(user.username(), user.password());

                Presence presence = connection.getStanzaFactory()
                        .buildPresenceStanza()
                        .ofType(Presence.Type.available)
                        .build();

                connection.sendStanza(presence);
                System.out.println("Me no entender nada xd");

                return true;
            } catch (XMPPException | SmackException | InterruptedException | IOException e) {
                return false;
            }
        }

        return false;
    }

    public boolean singUp(User user, String email){

        Map<String, String> attributes = new HashMap<String, String>();
        // establish the rest of the attributes
        attributes.put("email", email);

        if (connection.isConnected()){
            try {
                // this allows to create an account over an insecure connection
                accountManager.sensitiveOperationOverInsecureConnection(true);
                accountManager.createAccount(Localpart.from(user.username()), user.password(), attributes);
                return true;
            } catch (XMPPException | SmackException | InterruptedException | IOException e) {
                return false;
            }
        }
        return false;
    }

    public void deleteAccount(){
        try {
            this.accountManager.deleteAccount();
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException |
                 SmackException.NotConnectedException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

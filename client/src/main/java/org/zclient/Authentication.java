package org.zclient;


import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.parts.Localpart;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication
 * Handles the login and registration process of a user
 * @author zaray
 * @version 1.0
 * */
public class Authentication{

    private final AbstractXMPPConnection connection;
    private final AccountManager accountManager;

    public Authentication(AbstractXMPPConnection connection){
        this.accountManager = AccountManager.getInstance(connection);
        this.connection = connection;
    }

    /**
     * Allows a user to log in to an existing xmpp-account
     * @param user - user that wants to log in
     * @return true if the user successfully log in, false otherwise
     * */
    public boolean singIn(User user){
        // If the connection is still active, then try to sign in
        if (connection.isConnected()){
            try {
                connection.login(user.username(), user.password());
                return true;
            } catch (XMPPException | SmackException | InterruptedException | IOException e) {
                return false;
            }
        }

        return false;
    }

    /**
     * Handles the registration of a new user to the xmmp-server
     * @param user - user that wants to register
     * @param email - email of the user <username@domain>
     * @return true if the operation was successful, false otherwise
     * */
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

    /**
     * Delete the current logged account from the xmpp-server
     * */
    public void deleteAccount(){
        try {
            this.accountManager.deleteAccount();
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException |
                 SmackException.NotConnectedException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

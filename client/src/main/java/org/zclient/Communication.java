package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

public class Communication {

    private final ChatManager chatManager;
    private String income_message;
    private final AbstractXMPPConnection connection;

    public Communication(AbstractXMPPConnection connection){
        chatManager = ChatManager.getInstanceFor(connection);
        this.connection = connection;
    }

    public void directMessage(String to, String msg){
        try {
            Chat chat = chatManager.chatWith(JidCreate.entityBareFrom(to));

            Message message = connection.getStanzaFactory()
                    .buildMessageStanza()
                    .ofType(Message.Type.chat)
                    .setBody(msg)
                    .build();
            chat.send(message);

        } catch (XmppStringprepException | SmackException.NotConnectedException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void groupMessage(String to, String msg){
        try {
            Message message = connection.getStanzaFactory()
                    .buildMessageStanza()
                    .ofType(Message.Type.groupchat)
                    .to(JidCreate.from(to))
                    .setBody(msg)
                    .build();

            connection.sendStanza(message);
        } catch (XmppStringprepException | org.jivesoftware.smack.SmackException.NotConnectedException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * @param jid → describes the jid in the form <room@service/nickname>
    */
    public void joinGroup(String jid){
        try {
            Presence presence = connection.getStanzaFactory()
                    .buildPresenceStanza()
                    .to(JidCreate.from(jid))
                    .build();

            connection.sendStanza(presence);
        } catch (XmppStringprepException | SmackException.NotConnectedException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

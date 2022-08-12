package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

public class Communication {

    private final ChatManager chatManager;
    private String income_message;
    private final AbstractXMPPConnection connection;
    private Boolean new_message = false;

    public Communication(AbstractXMPPConnection connection){
        chatManager = ChatManager.getInstanceFor(connection);
        this.connection = connection;
    }

    public void sendMessage(String to, String msg, Boolean group){
        try {

            Message message;
            if (!group){
                // Send a direct message
                Chat chat = chatManager.chatWith(JidCreate.entityBareFrom(to));
                 message = connection.getStanzaFactory()
                        .buildMessageStanza()
                        .ofType(Message.Type.chat)
                        .setBody(msg)
                        .build();
                chat.send(message);
            }else{
                // Send a group message
                message = connection.getStanzaFactory()
                        .buildMessageStanza()
                        .ofType(Message.Type.groupchat)
                        .to(JidCreate.from(to))
                        .setBody(msg)
                        .build();
                connection.sendStanza(message);
            }


            // TODO send files

        } catch (XmppStringprepException | SmackException.NotConnectedException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void setNew_message(Boolean new_message) {
        this.new_message = new_message;
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

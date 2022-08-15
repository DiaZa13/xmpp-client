package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.filetransfer.*;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.IOException;

public class Communication {

    private final ChatManager chatManager;
    private FileTransferManager fileManager;
    private String income_message;
    private final AbstractXMPPConnection connection;

    public Communication(AbstractXMPPConnection connection){
        chatManager = ChatManager.getInstanceFor(connection);
        fileManager = FileTransferManager.getInstanceFor(connection);
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
    public boolean joinGroup(String jid){
        try {
            Presence presence = connection.getStanzaFactory()
                    .buildPresenceStanza()
                    .to(JidCreate.from(jid))
                    .build();

            connection.sendStanza(presence);
            return true;
        } catch (XmppStringprepException | SmackException.NotConnectedException | InterruptedException e) {
            return false;
        }
    }

    public void sendFile(String jid, String path){

        try {
            EntityFullJid to_jid = JidCreate.entityFullFrom(jid);
            // stream negotiation based on XEP-0095
            OutgoingFileTransfer stream = fileManager.createOutgoingFileTransfer(to_jid);
            // when the stream negotiation is done know it can transfer files
            File file = new File(path);
            System.out.println(file);
            stream.sendFile(file, "file");
            System.out.println(stream.getProgress());
            System.out.println(stream.getStatus());
            System.out.println("Que ha pasado....");
        } catch (XmppStringprepException | SmackException e) {
            System.out.println(util.cursorTo(23,1));
            e.printStackTrace();
            connection.disconnect();
        }

    }

    public void receiveFile(String path){
        FileTransferListener listener = new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest fileTransferRequest) {
                System.out.println("Te han enviado un archivo");
                // path to save the receipt file
                File file = new File(path, fileTransferRequest.getFileName());
                try {
                    fileTransferRequest.accept().receiveFile(file);
                } catch (SmackException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        fileManager.addFileTransferListener(listener);
    }
}

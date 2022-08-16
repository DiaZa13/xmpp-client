package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.filetransfer.*;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Communication {

    private final ChatManager chatManager;
    private final FileTransferManager fileManager;

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

    public boolean sendFile(String jid, String path){
        // Use as a transport method In-Band Bytestreams XEP-0047
        FileTransferNegotiator.IBB_ONLY = true;
        try {
            EntityFullJid to_jid = JidCreate.entityFullFrom(jid);
            // stream negotiation based on XEP-0095
            OutgoingFileTransfer stream = fileManager.createOutgoingFileTransfer(to_jid);
            // when the stream negotiation is done know it can transfer files
            File file = new File(path);
            // It has an error with the file
            if (!file.canRead()) return false;

            stream.sendFile(file, "file");

            /* this allows to check the progress of the sending process
                while(!stream.isDone()){
                    System.out.println(stream.getProgress());
                    System.out.println(stream.getStatus());
                    System.out.println(stream.getBytesSent());
                }
            */

            return true;
        } catch (XmppStringprepException | SmackException e) {
            return false;
        }

}

    public void receiveFile(String path){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        FileTransferListener listener = new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest fileTransferRequest) {
                LocalDateTime now = LocalDateTime.now();
                System.out.println(util.cursorRestore());
                // TODO change colors
                System.out.printf("\033[0;93m[" + formatter.format(now) + "]\033[1;94m %s: \033[0;34m%s \033[0m%n", fileTransferRequest.getRequestor(), fileTransferRequest.getFileName());
                System.out.print(util.cursorSave());
                // path to save the receipt the file
                File file = new File(path, fileTransferRequest.getFileName());
                try {
                    System.out.println(fileTransferRequest.getFileName());
                    IncomingFileTransfer fileTransfer = fileTransferRequest.accept();
                    fileTransfer.receiveFile(file);

                    System.out.println(util.cursorRestore());
                    System.out.printf("** File successfully saved at %s",file.getAbsolutePath());
                    System.out.println(util.cursorSave());

                } catch (SmackException | IOException e) {

                    System.out.println(util.cursorRestore());
                    System.out.println("** It was an error while sending the file");
                    System.out.println(util.cursorSave());

                    try {
                        fileTransferRequest.reject();
                    } catch (SmackException.NotConnectedException | InterruptedException ex) {
                        System.out.println(util.cursorRestore());
                        System.out.println("** It was an error while trying to reject the file");
                        System.out.println(util.cursorSave());
                    }
                }
            }
        };

        fileManager.addFileTransferListener(listener);
    }
}

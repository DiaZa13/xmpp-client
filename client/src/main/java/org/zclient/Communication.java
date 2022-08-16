package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamSession;
import org.jivesoftware.smackx.filetransfer.*;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.*;
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

    public boolean sendFile(EntityFullJid jid, String filename){
        // Use as a transport method In-Band Bytestreams XEP-0047
        FileTransferNegotiator.IBB_ONLY = true;
        try {
            // stream negotiation based on XEP-0095
            OutgoingFileTransfer stream = fileManager.createOutgoingFileTransfer(jid);
            // when the stream negotiation is done know it can transfer files
            File file = new File("..\\files/send",filename);
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
        } catch (SmackException e) {
            return false;
        }

    }

    public void receiveFile(String path){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        FileTransferListener listener = new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest fileTransferRequest) {
                LocalDateTime now = LocalDateTime.now();
                System.out.print(util.cursorRestore());
                System.out.printf("\033[0;93m[" + formatter.format(now) + "]%s %s: %s sent you %s \033[0m%n", util.DMU, fileTransferRequest.getRequestor(), util.DMM, fileTransferRequest.getFileName());
                System.out.print(util.cursorSave());
                // path to save the receipt the file
                File file = new File(path, fileTransferRequest.getFileName());
                try {
                    IncomingFileTransfer fileTransfer = fileTransferRequest.accept();
                    fileTransfer.receiveFile(file);

                    System.out.print(util.cursorRestore());
                    System.out.printf("%s** File successfully saved at %s\033[0m%n",util.SM, file.getAbsolutePath());
                    System.out.print(util.cursorSave());


                    System.out.print(util.cursorTo(32,1));
                } catch (SmackException | IOException e) {

                    System.out.print(util.cursorRestore());
                    System.out.printf("%s** It was an error while receiving the file\033[0m%n", util.SM);
                    System.out.print(util.cursorSave());

                    try {
                        fileTransferRequest.reject();
                    } catch (SmackException.NotConnectedException | InterruptedException ex) {
                        System.out.print(util.cursorRestore());
                        System.out.printf("%s** It was an error while trying to reject the file\033[0m%n", util.SM);
                        System.out.print(util.cursorSave());
                    }
                    System.out.print(util.cursorTo(32,1));
                }
            }
        };

        fileManager.addFileTransferListener(listener);
    }
}

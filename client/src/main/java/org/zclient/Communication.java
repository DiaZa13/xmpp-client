package org.zclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.filetransfer.*;
import org.jivesoftware.smackx.jingleold.JingleManager;
import org.jivesoftware.smackx.jingleold.JingleSession;
import org.jivesoftware.smackx.jingleold.media.JingleMediaManager;
import org.jivesoftware.smackx.jingleold.mediaimpl.multi.MultiMediaManager;
import org.jivesoftware.smackx.jingleold.nat.BasicTransportManager;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

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

//            EntityFullJid to_jid = JidCreate.entityFullFrom(jid);
//            // stream negotiation based on XEP-0095
//            OutgoingFileTransfer stream = fileManager.createOutgoingFileTransfer(to_jid);
//            // when the stream negotiation is done know it can transfer files
//            File file = new File(path);
//            // It has an error with the file
//            if (!file.canRead()) return false;
//
//            stream.sendFile(file, "file");
//
//            while(!stream.isDone()){
//                System.out.println(stream.getProgress());
//                System.out.println(stream.getStatus());
//            }
//
//            return true;
//        } catch (XmppStringprepException | SmackException e) {
//            return false;
//        }


        public boolean sendFile(String jid, String path){

        System.out.print(util.cursorRestore());
//        try {
//            // Create a JingleMediaManager. In this case using Jingle Audio Media API
//            JingleMediaManager mediaManager = new MultiMediaManager(new BasicTransportManager());
//
//            // Create a JingleManager using a MultiMediaManager
//            JingleManager jm = new JingleManager(connection, Collections.singletonList(mediaManager));
//
//            // Create a new Jingle Call with a full JID
//            JingleSession js = jm.createOutgoingJingleSession(JidCreate.entityFullFrom(jid));
//            // Start the call
//            js.start();
//            Thread.sleep(10000);
//            js.terminate();
//            Thread.sleep(3000);
//            return true;
//        } catch (XMPPException | InterruptedException | XmppStringprepException | SmackException ex) {
//            ex.printStackTrace();
//            return false;
//
//        }
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

                while(!stream.isDone()){
                    System.out.println(stream.getProgress());
                    System.out.println(stream.getStatus());
                    System.out.println(stream.getBytesSent());
                }

                return true;
            } catch (XmppStringprepException | SmackException e) {
                return false;
            }

    }

    public void receiveFile(String path){
        FileTransferListener listener = new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest fileTransferRequest) {
                System.out.println("Te han enviado un archivo");
                // path to save the receipt the file
                String directoryName = System.getProperty("user.dir");
                File file = new File(directoryName, fileTransferRequest.getFileName());
                System.out.println(file.getPath());
                System.out.println(file.isDirectory());
                System.out.println(file.getAbsolutePath());
                try {
                    System.out.println(fileTransferRequest.getFileName());
                    IncomingFileTransfer fileTransfer = fileTransferRequest.accept();
                    fileTransfer.receiveFile(file);

                } catch (SmackException | IOException e) {
                    System.out.println("Ocurrió un error");
                    System.out.println(file.getPath());
                    System.out.println(file.isDirectory());
                    System.out.println(file.getAbsolutePath());
                    try {
                        fileTransferRequest.reject();
                    } catch (SmackException.NotConnectedException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        };

        fileManager.addFileTransferListener(listener);
    }
}

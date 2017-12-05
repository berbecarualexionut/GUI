/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.upb.etti.poo.chat_application.client;

import java.awt.TextArea;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import ro.upb.etti.poo.chat_application.structs.Message;
import ro.upb.etti.poo.chat_application.structs.PrivateMessage;

/**
 *
 * @author professor
 */
public class ClientPeer extends Thread {

    private final ObjectOutputStream mObjectOutputStream;
    private String mSender;
    public JTextArea mJt;
    private final Socket mSocket;

    public ClientPeer(String sender, Socket communicationSocket) throws IOException {
        mSender = sender;
        mSocket = communicationSocket;
        mObjectOutputStream = new ObjectOutputStream(communicationSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            ObjectInputStream stream = new ObjectInputStream(mSocket.getInputStream());

            while (true) {
                Message message = (Message) stream.readObject();
                setOutputPane(mJt);
                mJt.append(message.toString()+"\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientPeer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientPeer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sendMessage(String message) throws IOException {
        mObjectOutputStream.writeObject(new Message(mSender, message));
    }

    public void sendMessage(String recipient, String message) throws IOException {
        mObjectOutputStream.writeObject(new PrivateMessage(recipient, mSender, message));
    }

    public void setUsername(String user) {
        mSender = user;
    }

    public void setOutputPane(JTextArea textArea) {
        mJt = textArea;
    }

}

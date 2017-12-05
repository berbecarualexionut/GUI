/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.upb.etti.poo.chat_application.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import ro.upb.etti.poo.chat_application.structs.Message;

/**
 *
 * @author professor
 */
public class ServerPeer extends Thread {

    private final Socket mSocket;
    private final ObjectOutputStream mObjectOutputStream;
    private final Server mServer;
    private String mUsername;
    
    public ServerPeer(Server server, Socket communicationSocket) throws IOException {
        mSocket = communicationSocket;
        mObjectOutputStream = new ObjectOutputStream(communicationSocket.getOutputStream());
        mServer = server;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream stream = new ObjectInputStream(mSocket.getInputStream());

            while (true) {
                //ServerPeer peer = new ServerPeer();
                Message message = (Message) stream.readObject();
                mUsername = message.getSender();
                mServer.dispatch(message);
                //distribuie corect
                //System.out.println(stream.readObject().toString().trim());
            }
        } catch (EOFException ex) {
            // client disconnected gracefully so do nothing
        } catch (IOException ex) {
            System.err.println("Client connection reset: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println("Unknown object received.");
        }
    }
    
    public String getUsername()
    {
        return mUsername;
    }
    
    
    public void sendMessage(Message message) throws IOException
    {
        mObjectOutputStream.writeObject(message);
    }
    
    
}

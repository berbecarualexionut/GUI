package ro.upb.etti.poo.chat_application.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import ro.upb.etti.poo.chat_application.server.config.ServerConfig;
import ro.upb.etti.poo.chat_application.structs.Message;
import ro.upb.etti.poo.chat_application.structs.PrivateMessage;

public class Server {

    private final List<ServerPeer> mPeers;
    private final int mTCP_PORT;
    private final int mMAX_CLIENTS;
    private ServerSocket serverSocket;

    public Server(int TCP_PORT, int MAX_CLIENTS) throws IOException {
        mTCP_PORT = TCP_PORT;
        mMAX_CLIENTS = MAX_CLIENTS;

        mPeers = new ArrayList<>();
        serverSocket = new ServerSocket(mTCP_PORT);
    }

    public static void main(String[] args) {
        try {
            ServerConfig config = new ServerConfig();
            Server server = new Server(config.getTcpPort(), config.getMaxClients());
            server.listen();

        } catch (Throwable t) {
            System.err.println("Exception in thread main: " + t.getMessage());
        }
    }

    public void listen() throws IOException {

        while (true) {
            
            if(mPeers.size()< mMAX_CLIENTS) {
                ServerPeer peer = new ServerPeer(this, serverSocket.accept());
                mPeers.add(peer);
                peer.start();
                dispatch(new Message("SERVER","Someone just connected!"));
            }

        }
    }

    public synchronized void dispatch(Message message) throws IOException {
        if (message instanceof PrivateMessage) {
            PrivateMessage privateMessage = (PrivateMessage) message;
            for (ServerPeer peers : mPeers) {
                
                if(privateMessage.getRecipient().equals(peers.getUsername())
                        || privateMessage.getSender().equals(peers.getUsername()))
                peers.sendMessage(message);
            }
        }else{
            for(ServerPeer peers : mPeers)
                peers.sendMessage(message);
        }

    }

    public synchronized void removeClient(ServerPeer peer) {
        
        mPeers.remove(peer);
        

    }

}

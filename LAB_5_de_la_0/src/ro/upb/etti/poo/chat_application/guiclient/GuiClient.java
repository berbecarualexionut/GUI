/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.upb.etti.poo.chat_application.guiclient;

import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import ro.upb.etti.poo.chat_application.client.ClientPeer;

public class GuiClient extends JFrame{

    private Socket mSocket;
    private String mUser;
    private ClientPeer peer;
    
    private JLabel mSenderNameLabel;
    private JTextField mSenderTextField;
    private JButton mChangeSenderButton;
    private JTextArea mMessageDisplayTextArea;
    private JTextField mMessageTextField;
    private JButton mSendMessageButton;
    private JScrollPane mMessageAreaScrollPane;
    
    public GuiClient(Socket socket, String user) throws IOException
    {
        super("Chat");
        
        mSocket = socket;
        mUser = user;
        peer = new ClientPeer(user, socket);       
        initializeComponents();
        peer.setOutputPane(mMessageDisplayTextArea);
        
       
    }

    
    private void initializeComponents() {
       
        JPanel panel = new JPanel();
        
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        add(panel);
        
        mSenderNameLabel = new JLabel("Name: ");
        mSenderNameLabel.setPreferredSize(new Dimension(40,25));
        layout.putConstraint(SpringLayout.WEST, mSenderNameLabel, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, mSenderNameLabel, 8, SpringLayout.NORTH, panel);
        panel.add(mSenderNameLabel);
        
        mSenderTextField = new JTextField(15);
        mSenderTextField.setPreferredSize(new Dimension(30, 30));
        layout.putConstraint(SpringLayout.WEST, mSenderTextField, 60, SpringLayout.WEST, mSenderNameLabel); 
        layout.putConstraint(SpringLayout.NORTH, mSenderTextField, 5, SpringLayout.NORTH, panel);
        panel.add(mSenderTextField);
        
        
        mChangeSenderButton = new JButton("Change Name");
        mChangeSenderButton.setPreferredSize(new Dimension(120,27));
        layout.putConstraint(SpringLayout.WEST, mChangeSenderButton, 180, SpringLayout.WEST, mSenderTextField);
        layout.putConstraint(SpringLayout.NORTH, mChangeSenderButton, 5, SpringLayout.NORTH, panel);
        mChangeSenderButton.addActionListener(new ActionListener(){
                        
            @Override
            public void actionPerformed( ActionEvent e ){
                String temp="";
                temp = mSenderTextField.getText();
                peer.setUsername(temp);
                mMessageDisplayTextArea.append("Clientul si-a schimbat numele in: "+temp+"\n");            
            }
            
        });
        panel.add(mChangeSenderButton);
        
        
        mMessageDisplayTextArea = new JTextArea();
        mMessageDisplayTextArea.setEditable(false);
        mMessageDisplayTextArea.setPreferredSize(new Dimension(360,150));
        layout.putConstraint(SpringLayout.WEST, mMessageDisplayTextArea, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, mMessageDisplayTextArea, 40, SpringLayout.NORTH, panel);
        panel.add(mMessageDisplayTextArea);
        
        mMessageTextField = new JTextField(25);
        mMessageTextField.setPreferredSize(new Dimension(30, 30));
        layout.putConstraint(SpringLayout.WEST, mMessageTextField, 10, SpringLayout.WEST, panel); 
        layout.putConstraint(SpringLayout.NORTH, mMessageTextField, 200, SpringLayout.NORTH, panel);
        panel.add(mMessageTextField);
        
        mSendMessageButton = new JButton("Send");
        mSendMessageButton.setPreferredSize(new Dimension(80, 27));
        layout.putConstraint(SpringLayout.WEST, mSendMessageButton, 290, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, mSendMessageButton, 200, SpringLayout.NORTH, panel);
         mSendMessageButton.addActionListener(new ActionListener(){
                        
            @Override
            public void actionPerformed( ActionEvent e ){
                try{
                    String mesaj="";
                    mesaj = mMessageTextField.getText();
                if (mesaj.equals("/q")) {
                    mMessageDisplayTextArea.setText("Ai fost deconectat!");
                    mSocket.close(); 
                } else if (mesaj.matches("/w\\s+\\w+\\s+.+")) {
                    String[] messageParts = mesaj.split("\\s+", 3);
                    peer.sendMessage(messageParts[1], messageParts[2]);
                } else {
                    peer.sendMessage(mesaj);
                }
            }catch(IOException ex){
                //
            }      
            mMessageTextField.setText("");
            }
        });
        
        panel.add(mSendMessageButton);
        
        
    }   
       
    public void display()
    {
        peer.start();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,300);
        setResizable(false);
    }
    
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1",9000);
        GuiClient gui = new GuiClient(socket, "Batman");
        
        gui.display();
        
        
    }
    
    /*setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(400,300);
        setResizable(false);*/
}


package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ClientWithGUI extends javax.swing.JFrame 
{
    
	private static final long serialVersionUID = 1L;
	String username, address="192.168.1.8" ;
    @SuppressWarnings({ "unchecked", "rawtypes" })
	ArrayList<String> users = new ArrayList();
    int port = 6000;
    Boolean isConnected = false;
    double convert;

    public double getConvert() {
        return convert;
    }

    public void setConvert(double convert) {
        this.convert = convert;
    }
    BigInteger p, q, n, e, d, pEl, g,k;
    BigInteger a, a_inv, b;
    BigInteger x2, y2;
    String cipher = "";
    String cipher2 = "";
    String plain = "";
    String plain2 = "";
    Socket sock;
    BufferedReader reader;
    PrintWriter writer;

    public BigInteger getEl_pel() {
        return el_pel;
    }

    public void setEl_pel(BigInteger el_pel) {
        this.el_pel = el_pel;
    }

    public BigInteger getEl_x() {
        return el_x;
    }

    public void setEl_x(BigInteger el_x) {
        this.el_x = el_x;
    }

    public BigInteger getRsa_d() {
        return rsa_d;
    }

    public void setRsa_d(BigInteger rsa_d) {
        this.rsa_d = rsa_d;
    }

    public BigInteger getRsa_n() {
        return rsa_n;
    }

    public void setRsa_n(BigInteger rsa_n) {
        this.rsa_n = rsa_n;
    }
    
    
    BigInteger el_pel,el_x,rsa_d,rsa_n,el_a;

    public BigInteger getEl_a() {
        return el_a;
    }

    public void setEl_a(BigInteger el_a) {
        this.el_a = el_a;
    }

    public BigInteger getP() {
        return p;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    public BigInteger getQ() {
        return q;
    }

    public void setQ(BigInteger q) {
        this.q = q;
    }

    public BigInteger getN() {
        return n;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }

    public BigInteger getE() {
        return e;
    }

    public void setE(BigInteger e) {
        this.e = e;
    }

    public BigInteger getD() {
        return d;
    }

    public void setD(BigInteger d) {
        this.d = d;
    }

    public BigInteger getpEl() {
        return pEl;
    }

    public void setpEl(BigInteger pEl) {
        this.pEl = pEl;
    }

    public BigInteger getX2() {
        return x2;
    }

    public void setX2(BigInteger x2) {
        this.x2 = x2;
    }

    public BigInteger getY2() {
        return y2;
    }

    public void setY2(BigInteger y2) {
        this.y2 = y2;
    }
    public BigInteger getG() {
        return g;
    }

    public void setG(BigInteger g) {
        this.g = g;
    }
    ClientWithGUI(double convert, BigInteger p, BigInteger q, BigInteger n, BigInteger e, BigInteger d, BigInteger y, BigInteger pEl, BigInteger x, BigInteger g) {
        initComponents();
        this.setConvert(convert);
        this.setP(p);
        this.setQ(q);
        this.setN(n);
        this.setE(e);
        this.setD(d);
        this.setY2(y);
        this.setpEl(pEl);
        this.setX2(x);
        this.setG(g);
        k = new BigInteger(getpEl().bitLength() - 5, new Random());
        System.out.println("Nilai k : " + k);
        a = getG().modPow(k, getpEl());
        System.out.println("Nilai a : "+a);
    }  
    public void ListenThread() 
    {
         Thread IncomingReader = new Thread(new IncomingReader());
         IncomingReader.start();
    }
    
    public void userAdd(String data) 
    {
         users.add(data);
    }
    
    
    public void userRemove(String data) 
    {
       
         users.remove(data);
    
         
    } 
    public void sendDisconnect() 
    {
        String Disconnect = (username + ": :Disconnect");
        try
        {
            writer.println(Disconnect); 
            writer.flush(); 

        } catch (Exception e) 
        {
            ta_chat.append("Could not send Disconnect message.\n");
        }
    }

    
    public void Disconnect() 
    {
        try 
        {
            ta_chat.append("Disconnected.\n");
            isConnected = false;
            tf_username.setEditable(true);
        } catch(Exception ex) {
            ta_chat.append("Failed to disconnect. \n");
        }
        

    }
    
    public ClientWithGUI() 
    {
    	getContentPane().setForeground(new Color(0, 0, 0));
        initComponents();
        this.getContentPane().setBackground(new Color(211, 211, 211));
    }
    
    //--------------------------//
     // TODO add another field for the msg tone
    public class IncomingReader implements Runnable
    {
        
        @Override
        public void run() 
        {
            String[] data;
            String stream, connect = "Connect", disconnect = "Disconnect", chat = "Chat" , privatemsg = "private";

            try 
            {
                while ((stream = reader.readLine()) != null) 
                {
                     data = stream.split(":");

                     if (data[2].equals(chat)) 
                     {
                        ta_chat.append(data[0] + ": " + data[1] + "\n");
                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                     } 
                     else if (data[2].equals(connect))
                     {
                        ta_chat.removeAll();
                        userAdd(data[0]); // name of the client "data [0]"
                     } 
                     else if (data[2].equals(disconnect)) 
                     {
                         userRemove(data[0]);
                     } 
                     else if(data[2].equals(privatemsg)){
                         String saya = data[1];
                         dekripsi(saya);
                         ta_chat.append("Private Message from " + data[0] + " : " + plain2 +"\n");
                         ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                     }
                     else if(data[2].equals("request"))
                     {
                         ta_chat.append(" Server replied " + "\n" + data[1] +"\n");
                         ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                    }
                }
           }catch(Exception ex) { }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        b_connect = new javax.swing.JButton();
        b_disconnect = new javax.swing.JButton();
        lb_port = new javax.swing.JLabel();
        tf_port = new javax.swing.JTextField();
        btnonlineusers = new javax.swing.JButton();
        lb_address = new javax.swing.JLabel();
        tf_address = new javax.swing.JTextField();
        lb_username = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        tf_chat = new javax.swing.JTextField();
        b_send = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Client's frame");
        setBackground(java.awt.SystemColor.textHighlight);
        setForeground(new java.awt.Color(0, 51, 51));
        setName("client"); // NOI18N
        setResizable(false);

        b_connect.setText("Connect");
        b_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_connectActionPerformed(evt);
            }
        });

        b_disconnect.setText("Disconnect");
        b_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_disconnectActionPerformed(evt);
            }
        });

        lb_port.setText("Port             :");

        tf_port.setText("6000");
        tf_port.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_portActionPerformed(evt);
            }
        });

        btnonlineusers.setText("Online Users");
        btnonlineusers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnonlineusersActionPerformed(evt);
            }
        });

        lb_address.setText("Server IP     : ");

        tf_address.setText("192.168.1.8");
        tf_address.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_addressActionPerformed(evt);
            }
        });

        lb_username.setText("Client Name :");

        tf_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_usernameActionPerformed(evt);
            }
        });

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        b_send.setText("SEND");
        b_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_sendActionPerformed(evt);
            }
        });

        jButton1.setLabel("Private Chat");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(tf_chat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b_send, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lb_username, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lb_address, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lb_port))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tf_port, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tf_address, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                .addComponent(tf_username)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnonlineusers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_connect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_disconnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(57, 57, 57))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(b_disconnect)
                            .addComponent(b_connect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(btnonlineusers))
                        .addGap(48, 48, 48))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_address)
                            .addComponent(tf_address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_username))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tf_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_port))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_chat, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(b_send, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tf_addressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_addressActionPerformed
       
    }//GEN-LAST:event_tf_addressActionPerformed

    private void tf_portActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_portActionPerformed
   
    }//GEN-LAST:event_tf_portActionPerformed

    private void tf_usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_usernameActionPerformed
    
    }//GEN-LAST:event_tf_usernameActionPerformed

    private void b_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_connectActionPerformed
        if (isConnected == false) 
            {
        if(tf_username.getText().length()!=0){
            
           if(!users.contains(tf_username.getText())){
  
            username = tf_username.getText();
            tf_username.setEditable(false);
            tf_port.setEditable(false);
            tf_address.setEditable(false);
            ta_chat.setEditable(false);

            try 
            {
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                writer = new PrintWriter(sock.getOutputStream());
                writer.println(username + ": has connected :Connect");
                writer.flush(); 
                isConnected = true; 
                changetxtareafontsize(ta_chat) ;
            } 
            catch (Exception ex) 
            {
                ta_chat.append("Cannot Connect! Try Again. \n");
                tf_username.setEditable(true);
            }
            
            ListenThread();
            
            } else{
             JOptionPane.showMessageDialog(null, "Write another name for your Client, this name is already taken", "Duplicate name found !!!", JOptionPane.ERROR_MESSAGE);

                   }
        } else{         
                    JOptionPane.showMessageDialog(null, "Write a name for your Client first", "Missing Field !!!", JOptionPane.ERROR_MESSAGE);

           }
        
       }else if (isConnected == true) 
        {
            ta_chat.append("You are already connected. \n");
        }
        
    }//GEN-LAST:event_b_connectActionPerformed
   
    private void b_disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_disconnectActionPerformed
        sendDisconnect();
        Disconnect();
    }//GEN-LAST:event_b_disconnectActionPerformed

    private void b_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_sendActionPerformed
        
        send_it();
    }//GEN-LAST:event_b_sendActionPerformed

    private void btnonlineusersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnonlineusersActionPerformed
        ta_chat.append("\n Online users : \n");
        try {
               writer.println(username + ":" + "Request to know who is online " + ":" + "request" + ":" + username );
               writer.flush(); // flushes the buffer
            } catch (Exception ex) {
                ta_chat.append("Message was not sent. \n");
            }
        
    }//GEN-LAST:event_btnonlineusersActionPerformed
    public void enkripsi(String plaintext) {
        cipher = "";
        long start = System.nanoTime();
        
        //cipher2 = cipher2 + a + " ";
        cipher = cipher + a + " ";
        for (int i = 0; i < plaintext.length(); i++) {
            int M = (int) plaintext.charAt(i);
            System.out.println(M);
            BigInteger b = getY2().modPow(k, getpEl()).multiply(BigInteger.valueOf(M)).mod(getpEl()); 
            BigInteger C = b.modPow(getE(), getN());
            cipher = cipher + C + " ";
        }
        System.out.println("Nilai enkripsi kedua   : " + cipher);
        long waktu_enkripsi = System.nanoTime() - start;
        System.out.println("Waktu enkripsi: " + waktu_enkripsi + "NS");
        tf_chat.setText("");
    }
    public void dekripsi(String ciphertext) {
        long start = System.nanoTime();
        plain2 =" ";
        String data_cipher[] = ciphertext.split(" ");
        a_inv = getEl_a().modPow(getEl_pel().subtract(BigInteger.ONE).subtract(getEl_x()), getEl_pel());
        for (int i = 1; i < data_cipher.length; i++) {
            b = (new BigInteger(data_cipher[i])).modPow(getRsa_d(), getRsa_n());
            System.out.println(b);
            BigInteger M = b.multiply(a_inv).mod(getEl_pel()); 
            System.out.println("nilai m : " + M);
            byte ASCII = Byte.parseByte(M.toString());
            plain2 = plain2 + (char) ASCII;
        }
        System.out.println("plaintext :" + plain2);
        long waktu_dekripsi = System.nanoTime() - start;
        System.out.println("Waktu dekripsi : " + waktu_dekripsi);

    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //JOptionPane.showMessageDialog(null, "To send a private message to a particular person, write his name between double @ eg.  @NAME@  in the begining of the message",
                //"How to send a private message", JOptionPane.INFORMATION_MESSAGE);
        /*System.out.println("Nilai P : " + getP());
        System.out.println("Nilai Q : " + getQ());
        System.out.println("Nilai N : " + getN());
        System.out.println("Nilai e : " + getE());
        System.out.println("Nilai d : " + getD());
        System.out.println("Nilai y : " + getY2());
        System.out.println("Nilai PEL : " + getpEl());
        System.out.println("Nilai x : " + getX2());
        System.out.println("Nilai g : " + getG());*/
        if(el_a==null){
        el_a = new BigInteger(JOptionPane.showInputDialog("Enter Value of a :"));
        this.setEl_a(el_a);
        el_pel = new BigInteger(JOptionPane.showInputDialog("Enter Value of pEL :"));
        this.setEl_pel(el_pel);
        el_x   = new BigInteger(JOptionPane.showInputDialog("Enter Value of x :"));
        this.setEl_x(el_x);
        rsa_d  = new BigInteger(JOptionPane.showInputDialog("Enter Value of d :"));
        this.setRsa_d(rsa_d);
        rsa_n  = new BigInteger(JOptionPane.showInputDialog("Enter Value of N :"));
        this.setRsa_n(rsa_n);
        }else{
            JOptionPane.showMessageDialog(null, "Private key has been added");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    public void send_it(){
    	
    	String nothing = "";
        String mydata = tf_chat.getText() ;
        Pattern pattern = Pattern.compile("(@).*\\1");
        Matcher matcher = pattern.matcher(mydata);

        if ((tf_chat.getText()).equals(nothing)) {
            tf_chat.setText("");
            tf_chat.requestFocus();
        } 
        else if (matcher.find()) {
            String[] data = mydata.split("@");
            try {
               String hanya = "";
               hanya = data[2];
               enkripsi(hanya);
               writer.println(username + ":" + cipher + ":" + "private" + ":" + data[1] ); // data[2] is the txtmsg .. data[1] is the reciever of the private msg name 
               writer.flush(); // flushes the buffer
               ta_chat.append("You have sent {  "+data[2]+"  } as a private message to : " + "'"+data[1]+ "'" +"\n");
            } catch (Exception ex) {
                ta_chat.append("Message was not sent. \n"+"No online users found by that name  ");
            }
            
        }
        else {
            try {
               writer.println(username + ":" + tf_chat.getText() + ":" + "Chat");
               writer.flush(); // flushes the buffer
            } catch (Exception ex) {
                ta_chat.append("Message was not sent. \n");
            }
            tf_chat.setText("");
            tf_chat.requestFocus();
        }

        tf_chat.setText("");
        tf_chat.requestFocus();
    	
    }
   
    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new ClientWithGUI().setVisible(true);
            }
        });
    }
    
    public void changetxtareafontsize(JTextArea txtarea){
    	Font font1 = new Font("SansSerif", Font.BOLD, 11);
    	txtarea.setFont(font1);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_connect;
    private javax.swing.JButton b_disconnect;
    private javax.swing.JButton b_send;
    private javax.swing.JButton btnonlineusers;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_address;
    private javax.swing.JLabel lb_port;
    private javax.swing.JLabel lb_username;
    private javax.swing.JTextArea ta_chat;
    private javax.swing.JTextField tf_address;
    private javax.swing.JTextField tf_chat;
    private javax.swing.JTextField tf_port;
    private javax.swing.JTextField tf_username;
    // End of variables declaration//GEN-END:variables
}

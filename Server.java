// import java.net.*;
// import java.io.*;
// public class Server {

//   ServerSocket server; //Declares a ServerSocket object named server, which listens for incoming client connections.
//   Socket socket; //Declares a Socket object named socket that represents the connection between the server and a client.
//   BufferedReader br; // Declares a BufferedReader object named br for reading incoming data from the client.
//   PrintWriter out; //Declares a PrintWriter object named out for sending data back to the client

//   //constructor 
//   public Server(){ 
//   try {
//     server = new ServerSocket(7777);//Creates a ServerSocket on port 7777, which means the server will listen for client connections on this port.
//     System.out.println("Server is ready to acccept connection");
//     System.out.println("waiting...");
//     socket = server.accept(); // accpeting clients connection  and return the  object

//     br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//     out = new PrintWriter(socket.getOutputStream());

//     startReading();
//     startWritting();

//   } catch (Exception e) {
//     // TODO: handle exception
//     e.printStackTrace();
//   }

//   }

//   public void startReading(){
//     // thread read the data
//     Runnable r1=()->{ //Creates a new Runnable (a block of code that can run in a separate thread) to handle reading.
//       System.out.println("Reader started...");
//       try {
//       while (true) {
//         String msg = br.readLine();
//         if (msg.equals("exit")) {
//           System.out.println("client has stopped or client terminated the chat");

//           socket.close();
//           break;
//         }

//         System.out.println("Client - " + msg);
//       }

//       }
//         catch(Exception e){
//         // e.printStackTrace();
//         System.out.println("Connection closed");
//     }
     
//     };
//     new Thread(r1).start(); //Starts a new thread to run the reading process in parallel.

//   }
//   public void startWritting(){
//     // takesd data fronm user and sent to client
//     Runnable r2=()->{
//       System.out.println("Writer started...");
//       try {
//       while (!socket.isClosed()) {

//           BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
//           String content = br1.readLine();

          
//           out.println(content); //Sends the input content to the client.
//           out.flush(); //Ensures that the data is sent immediately.

//           if(content.equals("exit"))
//           {
//             socket.close();
//             break;
//           }
        
//       }
//     }catch(Exception e){
//       // e.printStackTrace();
//       System.out.println("Connection closed");
//     }
//   };

//     new Thread(r2).start();
// }
//   public static void main(String[] args) {
//     System.out.println("This is Server");
//     new Server(); //calling constructor
//   }
// }
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
  

public class Server {
  ServerSocket server;
  Socket socket;
  BufferedReader br;
  PrintWriter out;

  // Add these GUI components
  private JFrame frame;
  private JTextArea chatArea;
  private JTextField messageField;
  private JButton sendButton;
  private JLabel statusLabel;

  public Server() {
      try {
          // Create and setup the GUI first
          createGUI();

          server = new ServerSocket(7777);
          System.out.println("Server is ready to accept connection");
          statusLabel.setText("Waiting for client...");
          System.out.println("waiting...");
          socket = server.accept();
          statusLabel.setText("Client Connected!");

          br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          out = new PrintWriter(socket.getOutputStream());

          startReading();
          startWritting();

      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  // Add this method to create GUI
  private void createGUI() {
      // Create main window
      frame = new JFrame("Chat Server");
      frame.setSize(400, 500);
      frame.setLayout(new BorderLayout());

      // Create components
      chatArea = new JTextArea();
      chatArea.setEditable(false);
      JScrollPane scrollPane = new JScrollPane(chatArea);

      messageField = new JTextField();
      sendButton = new JButton("Send");
      statusLabel = new JLabel("Starting server...", SwingConstants.CENTER);

      // Create bottom panel for input
      JPanel bottomPanel = new JPanel(new BorderLayout());
      bottomPanel.add(messageField, BorderLayout.CENTER);
      bottomPanel.add(sendButton, BorderLayout.EAST);

      // Add components to frame
      frame.add(statusLabel, BorderLayout.NORTH);
      frame.add(scrollPane, BorderLayout.CENTER);
      frame.add(bottomPanel, BorderLayout.SOUTH);

      // Add button action
      sendButton.addActionListener(e -> {
          String msg = messageField.getText();
          if (!msg.isEmpty()) {
              chatArea.append("Server: " + msg + "\n");
              out.println(msg);
              out.flush();
              messageField.setText("");
              messageField.requestFocus();
          }
      });

      // Add window listener
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
  }

  // Modify your startReading method
  public void startReading() {
      Runnable r1 = () -> {
          System.out.println("Reader started...");
          try {
              while (true) {
                  String msg = br.readLine();
                  if (msg.equals("exit")) {
                      chatArea.append("Client has stopped the chat\n");
                      socket.close();
                      break;
                  }
                  chatArea.append("Client: " + msg + "\n");
              }
          } catch (Exception e) {
              System.out.println("Connection closed");
          }
      };
      new Thread(r1).start();
  }

  // Modify your startWritting method - this is optional now since we have GUI input
  public void startWritting() {
      // Keep this method for console input if needed
      Runnable r2 = () -> {
          try {
              while (!socket.isClosed()) {
                  BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                  String content = br1.readLine();
                  out.println(content);
                  out.flush();
                  if (content.equals("exit")) {
                      socket.close();
                      break;
                  }
              }
          } catch (Exception e) {
              System.out.println("Connection closed");
          }
      };
      new Thread(r2).start();
  }

  public static void main(String[] args) {
    System.out.println("This is Server");
    new Server(); //calling constructor
  }
}

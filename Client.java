// import java.awt.Font;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.net.Socket;

// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JTextArea;
// import javax.swing.JTextField;

// public class Client extends JFrame{
//   Socket socket;
//   BufferedReader br;
//   PrintWriter out;
//   //declare component
//   private JLabel heading = new JLabel("Client Area");
//   private JTextArea messageArea = new JTextArea();
//   private JTextField messageinput = new JTextField();
//   private Font font = new Font("Roboto",Font.PLAIN,20);

//   public Client()
//   {
//     try {
//       System.out.println("sending request to server");
//       socket = new Socket("192.168.29.164",7777);
//       System.out.println("connection done");


//       br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//     out = new PrintWriter(socket.getOutputStream());

//     createGUI();

//     startReading();
//     startWritting();

//     } catch (Exception e) {
//       // TODO: handle exception
//       System.out.println("connection closed");
//     }

//   }
//   private void createGUI(){

//   }
//   public void startReading(){
//     // thread read the data
//     Runnable r1=()->{
//       System.out.println("Reader started...");
//       try {
//       while (true) {
//         String msg = br.readLine();
//         if (msg.equals("exit")) {
//           System.out.println("server has stopped or server terminated the chat");

//           socket.close();
//           break;
//         }

//         System.out.println("Server - " + msg);
//       }

//       }
//         catch(Exception e){
//         // e.printStackTrace();
//         System.out.println("Connection closed");
//     }
     
//     };
//     new Thread(r1).start();
//   }
//   public void startWritting(){
//     // takesd data fronm user and sent to client
//     Runnable r2=()->{
//       System.out.println("Writer started...");
//       try {
//       while (!socket.isClosed()) {

//           BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
//           String content = br1.readLine();

          
//           out.println(content);
//           out.flush();

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
//     System.out.println("this is client...");
//     new Client();
//   }
// }

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
public class Client {
  Socket socket;
  BufferedReader br;
  PrintWriter out;

  // Add these GUI components
  private JFrame frame;
  private JTextArea chatArea;
  private JTextField messageField;
  private JButton sendButton;
  private JLabel statusLabel;

  public Client() {
      try {
          // Create and setup the GUI first
          createGUI();

          System.out.println("Sending request to server");
          statusLabel.setText("Connecting to server...");
          socket = new Socket("127.0.0.1", 7777);
          System.out.println("Connection done");
          statusLabel.setText("Connected to server!");

          br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          out = new PrintWriter(socket.getOutputStream());

          startReading();
          startWritting();

      } catch (Exception e) {
          statusLabel.setText("Connection failed!");
          System.out.println("connection closed");
      }
  }

  // Add this method to create GUI
  private void createGUI() {
      // Create main window
      frame = new JFrame("Chat Client");
      frame.setSize(400, 500);
      frame.setLayout(new BorderLayout());

      // Create components
      chatArea = new JTextArea();
      chatArea.setEditable(false);
      JScrollPane scrollPane = new JScrollPane(chatArea);

      messageField = new JTextField();
      sendButton = new JButton("Send");
      statusLabel = new JLabel("Starting client...", SwingConstants.CENTER);

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
              chatArea.append("You: " + msg + "\n");
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
                      chatArea.append("Server has stopped the chat\n");
                      socket.close();
                      break;
                  }
                  chatArea.append("Server: " + msg + "\n");
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
        System.out.println("this is client...");
        new Client();
  }
}
    
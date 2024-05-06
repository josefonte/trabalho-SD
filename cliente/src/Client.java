import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import org.zeromq.SocketType;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Socket socket ;
    private static PrintWriter out;
    private static BufferedReader in;




    public static void main(String[] args) throws IOException {

        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        frontPage();

    }



    public static void frontPage() {
        int returnValue = 1;
        try {
            while (returnValue==1) {
                System.out.println("Select an option:");
                System.out.println("1. Register");
                System.out.println("2. Authenticate");
                System.out.println("3. Exit");

                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        returnValue = register();
                        break;
                    case 2:
                        returnValue = authenticate();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        out.close();
                        socket.close();
                        returnValue = -1;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (returnValue==0) clientPage();
    }

    private static int register() throws IOException {
        System.out.println("Enter username:");
        String username = reader.readLine();
        System.out.println("Enter password:");
        String password = reader.readLine();
        out.println("create_account," + username + "," + password);
        String response = in.readLine();
        if (response.equals("Account created")) {
            System.out.println("Registration successful.");
            return 0;
        } else {
            System.out.println("Registration failed. Please try again." + response);
            return 1;
        }
    }

    private static int authenticate() throws IOException {
        System.out.println("Enter username:");
        String username = reader.readLine();
        System.out.println("Enter password:");
        String password = reader.readLine();
        out.println("login," + username + "," + password);
        String response = in.readLine();
        if (response.equals("Logged in")) {
            System.out.println("Authentication successful.");
            return 0;
        } else {
            System.out.println("Authentication failed. Please try again. "+ response);
            return 1;
        }
    }



    public static void clientPage() {

        try {
            while (true) {
                System.out.println("Select an option:");
                System.out.println("1. Create Album");
                System.out.println("2. Edit Album");
                System.out.println("3. Request Album Content");
                System.out.println("4. Exit");

                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        create_album();
                        break;
                    case 2:
                        edit_album();
                        break;
                    case 3:
                        request_album_content();
                        break;
                    case 4:
                        int result = logout();
                        if (result == 0){
                            System.out.println("Exiting...");
                            frontPage();
                            return;
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int logout() throws IOException {
        out.println("logout");
        String response = in.readLine();
        if (response.equals("Logged out")) {
            System.out.println("Logged out successfully.");
            return 0;
        } else {
            System.out.println("Logout failed. Please try again."+ response);
            return 1;
        }
    }

    private static int create_album() throws IOException {
        System.out.println("Enter album name:");
        String album_name = reader.readLine();
        out.println("create_album," + album_name);
        String response = in.readLine();
        if (response.equals("Album created")) {
            System.out.println("Album created successfully.");
        } else {
            System.out.println("Album creation failed. Please try again."+ response);
        }
        return 0;
    }

    private static Boolean canBeDelivered(Long pid, HashMap<Long, Integer> vv, HashMap<Long, Integer> vvm){
        if (vv.getOrDefault(pid,0)+1 == vvm.getOrDefault(pid,0)){
            for (HashMap.Entry<Long, Integer> entry : vvm.entrySet()) {
                if (entry.getKey() != pid) {
                    if (entry.getValue() > vv.getOrDefault((entry.getKey()),0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void handle_chat(String[] parts,HashMap<Long, Integer> selfVV,
                                      ArrayList<String> pending_messages, ArrayList<Long> pending_pids,
                                      ArrayList<HashMap<Long,Integer>> pending_vv, Long mypid) {
        // my message can either be of this format: album:chat:id,n:message
        // or this format: album:command:message
        String[] chat_parts = parts[2].split(";");
        Long id = Long.parseLong(chat_parts[0]);
        if (id.equals(mypid)){
            return;
        }
        HashMap vv = deserializeVersionVector(chat_parts[1]);
        System.out.println("Received this vv: " + vv);
        System.out.println("Current vv: " + selfVV);

        String message = parts[3];
        //if it is the first message, and the selfVV is empty, then we can deliver it and the selfVV isequal to vv
        if (selfVV.isEmpty()){
            selfVV.putAll(vv);
            System.out.println("Received: " + message);
        }
        else{
        if (canBeDelivered(id,selfVV,vv)){
            selfVV.put(id,selfVV.getOrDefault(id,0)+1);
            System.out.println("Received: " + message);
            for (int i = 0; i < pending_messages.size(); i++) {
                if (canBeDelivered(pending_pids.get(i),selfVV,pending_vv.get(i))){
                    System.out.println("Received: " + pending_messages.get(i));
                    pending_messages.remove(i);
                    pending_pids.remove(i);
                    pending_vv.remove(i);
                }
            }
        }
        else{
            pending_messages.add(message);
            pending_pids.add(id);
            pending_vv.add(vv);
        }}

    }


    // Helper method to serialize version vector to string
    private static String serializeVersionVector(HashMap<Long, Integer> versionVector) {
        StringBuilder sb = new StringBuilder();
        for (HashMap.Entry<Long, Integer> entry : versionVector.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
        }
        return sb.toString();
    }


    private static HashMap<Long, Integer> deserializeVersionVector(String vvString) {
        HashMap<Long, Integer> versionVector = new HashMap<>();
        String[] entries = vvString.split(",");
        for (String entry : entries) {
            String[] parts = entry.split("=");
            long pid = Long.parseLong(parts[0]);
            int seqNum = Integer.parseInt(parts[1]);
            versionVector.put(pid, seqNum);
        }
        return versionVector;
    }


    private static void chat(String album) throws IOException {
        try (ZContext context = new ZContext()) {
            // falta garantir entrega causal no serviço de chat dentro de cada grupo de edição
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            HashMap<Long, Integer> versionVector = new HashMap<>();
            long pid = ProcessHandle.current().pid();
            subscriber.connect("tcp://localhost:" + 5556);
            subscriber.subscribe(album.getBytes());

            publisher.connect("tcp://localhost:" + 5555);

            // Start a thread to handle incoming messages
            new Thread(() -> {
                try{
                    ArrayList<HashMap<Long,Integer>> pending_vv = new ArrayList<>();
                    ArrayList<Long> pending_pids = new ArrayList<>();
                    ArrayList<String> pending_messages = new ArrayList<>();
                    while (!Thread.currentThread().isInterrupted()) {
                    String receivedMessage = new String(subscriber.recv());
                    // my message can either be of this format: album:chat:id,n:message
                    // or this format: album:command:message
                    String[] parts = receivedMessage.split(":");
                    String type = parts[1];
                    String message;
                    if (type.equals("chat")) {
                        handle_chat(parts,versionVector,pending_messages,pending_pids,pending_vv,pid);
                    } else {
                        message = parts[2];
                        System.out.println("Received: " + message);
                    }

                }}
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            String command = reader.readLine();
            while (!command.startsWith("\\exit")){
                if (!command.startsWith("\\")){
                    int newSeqNum = versionVector.getOrDefault(pid,0) + 1;
                    versionVector.put(pid, newSeqNum);
                    String vv = serializeVersionVector(versionVector);
                    String new_message = String.format("%s:chat:%s;%s:%s", album, pid,vv,command);
                    publisher.send(new_message.getBytes());
                }
                else{
                    if (command.startsWith("\\add_file")) {
                        String[] parts = command.split(" ");
                        String file_name = parts[1];
                        String content = parts[2];
                    out.println("add_file," + album + "," + file_name + "," + content);
                    } else if (command.startsWith("\\remove_file")) {
                        String[] parts = command.split(" ");
                        String file_name = parts[1];
                        out.println("remove_file," + album + "," + file_name);
                    } else if (command.startsWith("\\rate_file")) {
                        String[] parts = command.split(" ");
                        String file_name = parts[1];
                        String rating = parts[2];
                        out.println("rate_file," + album + "," + file_name + "," + rating);
                    } else if (command.startsWith("\\add_user")) {
                        String[] parts = command.split(" ");
                        String username = parts[1];
                        out.println("add_user," + album + "," + username);
                    } else if (command.startsWith("\\remove_user")) {
                        String[] parts = command.split(" ");
                        String username = parts[1];
                        out.println("remove_user," + album + "," + username);
                    }
                    String response = album +":command: Response - " + in.readLine();
                    String request = album + ":command: Operation sent - " + command;
                    publisher.send(request.getBytes());
                    publisher.send(response.getBytes());

            }
                command = reader.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }


    private static int edit_album() throws IOException {
        /*
        CENAS QUE TEMOS DE TER EM CONSIDERAÇÃO (SDGE - requirements):
                - edição de álbuns descentralizada com CRDTs
                - interação peer-to-peer entre clientes com réplicas locais de álbuns
                - estruturas de dados com representação linear no número de utilizadores que editaram
           o álbum concorrentemente e não ao total de utilizadores do álbum
                - garantir entrega causal no serviço de chat dentro de cada grupo de edição
        */
        String command;
        String album_name;
        do{
            System.out.println("Enter album name (or enter to leave):");
            album_name = reader.readLine();
            out.println("check_user," + album_name );
            command = in.readLine();
            if (!command.equals("OK")){
                System.out.println("You are not in the album. Please try again.");
            }

        } while(!command.equals("OK") && !album_name.equals(""));

        if (album_name.equals("")){
            return 1;
        }

        System.out.println("""
                To use the chat functionality, simply type your message and hit enter. Your message will be sent to all users currently in the chat.
                                
                To perform editing operations on albums, start your input with a backslash "\\" followed by the command:
                                
                - To add a file to an album:
                  \\add_file <name_of_file> <content>
                                
                - To remove a file from an album:
                  \\remove_file <name_of_file>
                                
                - To rate a file in an album:
                  \\rate_file <name_of_file> <rating>
                                
                - To add a user to an album:
                  \\add_user <username>
                                
                - To remove a user from an album:
                  \\remove_user <username>
                
                To exit the chat, type "\\exit" and hit enter.               
                """);
        chat(album_name);
        return 1;
    }

    private static int request_album_content(){
        // primeiro pedir ao servidor central os metadados do album e depois pedir o conteudo ao servidor de dados
        return 1;
    }









}

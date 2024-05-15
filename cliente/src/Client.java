import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import org.zeromq.SocketType;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Socket socket ;
    private static PrintWriter out;
    private static BufferedReader in;

    private static String User = null;



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

                int choice;
                try {
                    choice = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }

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
            User = username;
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
                System.out.println("3. Request Album Metadata");
                System.out.println("4. Request File Data");
                System.out.println("5. Exit");
                int choice;
                try {
                    choice = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }

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
                        request_file_data();
                    case 5:
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
            User = null;
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




    private static Boolean canBeDelivered(Long pid, VersionVector vv, VersionVector vvm){
        if (vv.getOrDefault(pid,0)+1 == vvm.getOrDefault(pid,0)){
            for (HashMap.Entry<Long, Integer> entry : vvm.entrySet()) {
                if (!entry.getKey().equals(pid)) {
                    if (entry.getValue() > vv.getOrDefault((entry.getKey()),0)) {
                        return false;
                    }
                }
            }
            if (vv.lastReceived != null) {
                vv.lastDependency = new VersionVector(vv.lastReceived);
            }
            vv.lastReceived = new VersionVector(vvm);
            return true;
        }
        return false;
    }

    private static void checkMessagesPending(ArrayList<String> pending_messages, ArrayList<Long> pending_pids,
                                             ArrayList<VersionVector> pending_vv, VersionVector selfVV){
        boolean flag = true;
        while (flag && pending_messages.size() > 0){
            flag = false;
            for (int i = 0; i < pending_messages.size(); i++){
                String message = pending_messages.get(i);
                Long id = pending_pids.get(i);
                VersionVector vv = pending_vv.get(i);
                if (canBeDelivered(id,selfVV,vv)){
                    selfVV.put(id,selfVV.getOrDefault(id,0)+1);
                    System.out.println("Received: " + message);
                    pending_messages.remove(i);
                    pending_pids.remove(i);
                    pending_vv.remove(i);
                    flag = true;
                    break;
                }
            }
        }
    }

    private static void handle_chat(String[] parts,VersionVector selfVV,
                                      ArrayList<String> pending_messages, ArrayList<Long> pending_pids,
                                      ArrayList<VersionVector> pending_vv, Long mypid){

        String[] chat_parts = parts[2].split(";");
        Long id = Long.parseLong(chat_parts[0]);
        if (id.equals(mypid)){
            return;
        }
        VersionVector vv = VersionVector.deserializeVersionVector(chat_parts[1]);

        String message = parts[3];
        if (selfVV.firstMessage){
            selfVV.putAll(vv);
            System.out.println("Received: " + message);
            selfVV.lastReceived = new VersionVector(vv);
            selfVV.firstMessage = false;
        }
        else{
            if (canBeDelivered(id,selfVV,vv)){
                selfVV.put(id,selfVV.getOrDefault(id,0)+1);
                System.out.println("Received: " + message);
                checkMessagesPending(pending_messages,pending_pids,pending_vv,selfVV);
            }
            else{
                pending_messages.add(message);
                pending_pids.add(id);
                pending_vv.add(vv);
            }
        }

    }






    private static void chat(String album) {
        try (ZContext context = new ZContext()) {
            // falta garantir entrega causal no serviço de chat dentro de cada grupo de edição
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);

            long pid = ProcessHandle.current().pid();
            String pid_string = Long.toString(pid);

            // estado do chat
            ReentrantLock vvlock = new ReentrantLock();
            VersionVector versionVector = new VersionVector();

            //TODO estado do album ---> no inicio ir buscar lista de ficheiros e de utilizadores e o rate que este user deu
            // (NUNO)
            // out.println("get_album_info,"+album);
            // String response = in.readLine();
            ORSetCRDT utilizadores = new ORSetCRDT();
            ORSetCRDT ficheiros = new ORSetCRDT();
            HashMap<String,String> rates = new HashMap<>();





            subscriber.connect("tcp://localhost:" + 5556);
            subscriber.subscribe(album.getBytes());

            publisher.connect("tcp://localhost:" + 5555);

            // Start a thread to handle incoming messages
            new Thread(() -> {
                try{
                    ArrayList<VersionVector> pending_vv = new ArrayList<>();
                    ArrayList<Long> pending_pids = new ArrayList<>();
                    ArrayList<String> pending_messages = new ArrayList<>();
                    while (!Thread.currentThread().isInterrupted()) {
                    String receivedMessage = new String(subscriber.recv());
                    String[] parts = receivedMessage.split(":");
                    String type = parts[1];
                    if (type.equals("chat")) {
                        vvlock.lock();
                        handle_chat(parts,versionVector,pending_messages,pending_pids,pending_vv,pid);
                        vvlock.unlock();
                    } else {
                        String mpid = parts[2];
                        if (mpid.equals(Long.toString(pid))){
                            continue;
                        }
                        if (parts[3].equals("users")){
                            ORSetCRDT utilizadores_m = ORSetCRDT.deserialize(parts[4]);
                            utilizadores.join(utilizadores_m);
                        }
                        else if (parts[3].equals("files")){
                            System.out.println("Received: " + parts);
                            ORSetCRDT ficheiros_m = ORSetCRDT.deserialize(parts[4]);
                            ficheiros.join(ficheiros_m);
                        }
                    }

                }}
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            String command = reader.readLine();
            String message;
            String crdt_name;
            String crdt;
           // ORSetCRDT delta;
            while (!command.startsWith("\\exit")){
                if (!command.startsWith("\\")){
                    vvlock.lock();
                    int newSeqNum = versionVector.getOrDefault(pid,0) + 1;
                    versionVector.put(pid, newSeqNum);
                    String vv = versionVector.serializeVersionVector();
                    vvlock.unlock();
                    String new_message = String.format("%s:chat:%s;%s:%s", album, pid,vv,command);
                    publisher.send(new_message.getBytes());
                }
                else{
                    if (command.startsWith("\\add_file")) {
                        String[] parts = command.split(" ");
                        String file_name = parts[1];
                        //TODO - upload file
                        message = "add_file," + album + "," + file_name;
                        crdt_name = "files";
                        ficheiros.add(file_name,pid_string);
                        crdt = ficheiros.serialize();
                    } else if (command.startsWith("\\remove_file")) {
                        String[] parts = command.split(" ");
                        String file_name = parts[1];
                        //TODO - remove file
                        message = "remove_file," + album + "," + file_name;
                        crdt_name = "files";
                        ficheiros.remove(file_name,pid_string);
                        crdt = ficheiros.serialize();
                    } else if (command.startsWith("\\rate_file")) {
                        String[] parts = command.split(" ");
                        String file_name = parts[1];
                        String rating = parts[2];
                        if (rates.containsKey(file_name)){
                            System.out.println("You have already rated this file. Please try again.");
                            continue;
                        }
                        rates.put(file_name,rating);
                        continue;
                    } else if (command.startsWith("\\add_user")) {
                        String[] parts = command.split(" ");
                        String username = parts[1];
                        message = "add_user," + album + "," + username;
                        crdt_name = "users";
                        utilizadores.add(username,pid_string);
                        crdt = utilizadores.serialize();
                    } else if (command.startsWith("\\remove_user")) {
                        String[] parts = command.split(" ");
                        String username = parts[1];
                        message = "remove_user," + album + "," + username;
                        crdt_name = "users";
                        utilizadores.remove(username,pid_string);
                        crdt = utilizadores.serialize();
                    }
                    else {
                        System.out.println("Invalid command. Please try again.");
                        command = reader.readLine();
                        continue;
                    }
                    String new_message = String.format("%s:command:%s:%s:%s", album, pid, crdt_name,crdt);
                    publisher.send(new_message.getBytes());
                    //FIXME está a enviar pedido a pedido - enviar só no fim  (NUNO)
                    out.println(message);
                    String response = in.readLine();
                    System.out.println(response); // DEBUG

            }
                command = reader.readLine();
            }
            //TODO - enviar tudo de uma vez (NUNO)
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

    private static int request_album_content() throws IOException {

        String command;
        String album_name;
        System.out.println("Enter album name (or enter to leave):");
        album_name = reader.readLine();


        out.println("get_files,"+album_name);
        String response = in.readLine();
        //TODO - falta tratar a resposta
        System.out.println(response);
        return 1;
    }

    private static int request_file_data(){
        //TODO download de ficheiros
        return 1;
    }









}

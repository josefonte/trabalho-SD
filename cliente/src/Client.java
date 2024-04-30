import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import org.zeromq.SocketType;
import java.io.*;
import java.net.*;


import java.util.Scanner;


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

        int result = frontPage();
        if (result==-1) return;

        clientPage();

/*
        try (ZContext context = new ZContext()) {
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);


            subscriber.connect("tcp://localhost:"+ args[1]);
            subscriber.subscribe("".getBytes());
            // subscriber.subscribe(args[1].getBytes()); //room to subscribe


            publisher.connect("tcp://localhost:" + args[0]);

            // Start a thread to handle incoming messages
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    String receivedMessage = new String(subscriber.recv());
                    System.out.println("Received: " + receivedMessage);
                }
            }).start();

            // Main loop for sending messages
            Scanner scanner = new Scanner(System.in);

            // Prompt the user for input
            String message = scanner.nextLine(); // Read the input string


            // Close the scanner
            while (!message.equalsIgnoreCase("exit")) {

                publisher.send(message.getBytes());
                message = scanner.nextLine(); // Read the input string

            }
            scanner.close();

        }*/
    }



    public static int frontPage() {
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
                        returnValue = register(reader, out,in);
                        break;
                    case 2:
                        returnValue = authenticate(reader, out,in);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        out.close();
                        socket.close();
                        returnValue = -1;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    private static int register(BufferedReader reader, PrintWriter out,BufferedReader in) throws IOException {
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
            System.out.println("Registration failed. Please try again.");
            return 1;
        }
    }

    private static int authenticate(BufferedReader reader, PrintWriter out,BufferedReader in) throws IOException {
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
            System.out.println("Authentication failed. Please try again.");
            return 1;
        }
    }



    public static int clientPage() {
        int returnValue = 1;

        try {


            while (returnValue==1) {
                System.out.println("Select an option:");
                System.out.println("1. Create Album");
                System.out.println("2. Edit Album");
                System.out.println("3. Request Album Content");
                System.out.println("4. Exit");

                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        returnValue = create_album(reader, out,in);
                        break;
                    case 2:
                        returnValue = edit_album(reader, out,in);
                        break;
                    case 3:
                        returnValue = request_album_content(reader, out,in);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        out.close();
                        socket.close();
                        returnValue = -1;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    private static int create_album(BufferedReader reader, PrintWriter out,BufferedReader in) throws IOException {
        System.out.println("Enter album name:");
        String album_name = reader.readLine();
        out.println("create_album," + album_name);
        String response = in.readLine();
        if (response.equals("Album created")) {
            System.out.println("Album created successfully.");
            return 0;
        } else {
            System.out.println("Album creation failed. Please try again.");
            return 1;
        }
    }

    private static int edit_album(BufferedReader reader, PrintWriter out, BufferedReader in){
        /*
        CENAS QUE TEMOS DE TER EM CONSIDERAÇÃO (SDGE - requirements):
                - edição de álbuns descentralizada com CRDTs
                - interação peer-to-peer entre clientes com réplicas locais de álbuns
                - representação linear no número de utilizadores que editaram o álbum concorrentemente e não ao
           total de utilizadores do álbum
                - garantir entrega causal no serviço de chat dentro de cada grupo de edição

       O que pode ser feito:
            - editar album:
                            • adicionar novo ficheiro, com nome e conteúdo;
                            • remover ficheiro;
                            • adicionar utilizador ao grupo;
                            • remover utilizador do grupo;
                            • classificar ficheiros do álbum, com uma pontuação entre 0 e 5.
            - discussão em tempo real de cada album
        */
        return 1;
    }

    private static int request_album_content(BufferedReader reader, PrintWriter out, BufferedReader in){
        // primeiro pedir ao servidor central os metadados do album e depois pedir o conteudo ao servidor de dados
        return 1;
    }









}

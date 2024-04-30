import java.net.Socket;
import java.io.*;
public class Client {
    public static void main(String[] args) throws Exception {
        String id = args[0];
        String delay_write = args[1];
        String delay_read = args[2];

        try (Socket socket = new Socket("localhost", 12345);
             OutputStream os = new BufferedOutputStream(socket.getOutputStream());
             InputStream is = new BufferedInputStream(socket.getInputStream());) {
            try {
                Thread senderThread = new Thread(() -> {
                    try {

                        for (int i = 1; i <= 100; i++) {
                            String message = " | "+ id +" Message " + i + " | ";
                            os.write(message.getBytes());
                            os.flush();
                            System.out.println("Client: " + message);
                            try {
                                Thread.sleep(Long.parseLong(delay_write));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        os.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });


                Thread receiverThread = new Thread(() -> {
                    try {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            Thread.sleep(Long.parseLong(delay_read));
                            String serverResponse = new String(buffer, 0, bytesRead);
                            System.out.println("Server response: " + serverResponse);
                        }
                        is.close();
                    } catch (IOException e) {
                        System.err.println("Error reading from the server: " + e.getMessage());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                senderThread.start();
                receiverThread.start();

                // Wait for both threads to finish
                senderThread.join();
                receiverThread.join();
            } catch (Exception e) {
                System.out.println(e);
            }

        }catch (IOException e) {
            System.err.println( e.getMessage());
        }
    }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class termination {
    public static void main(String[] args) {
        // Porta que deseja verificar
        int port = 8000;

        try {
            // Executa o comando tasklist para listar todos os processos
            Process process = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(Integer.toString(port))) {
                    String[] parts = line.trim().split("\\s+");
                    String pid = parts[1];
                    // Encerra o processo pelo PID
                    Process killProcess = Runtime.getRuntime().exec("taskkill /PID " + pid + " /F");
                    System.out.println("Processo encerrado com sucesso.");
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


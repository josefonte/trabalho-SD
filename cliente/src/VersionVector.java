import java.util.HashMap;

public class VersionVector extends HashMap<Long, Integer> {

    public boolean firstMessage = true;
    public String serializeVersionVector() {
        StringBuilder sb = new StringBuilder();
        for (HashMap.Entry<Long, Integer> entry : this.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
        }
        return sb.toString();
    }


    public static VersionVector deserializeVersionVector(String vvString) {
        VersionVector versionVector = new VersionVector();
        String[] entries = vvString.split(",");
        for (String entry : entries) {
            String[] parts = entry.split("=");
            long pid = Long.parseLong(parts[0]);
            int seqNum = Integer.parseInt(parts[1]);
            versionVector.put(pid, seqNum);
        }
        return versionVector;
    }
}

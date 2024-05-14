
public class Dot {
    public String pid;
    public int seqNum;

    public Dot(String pid, int seqNum) {
        this.pid = pid;
        this.seqNum = seqNum;
    }

    public Dot(String dotString) {
        String[] parts = dotString.split(",");
        this.pid = parts[0];
        this.seqNum = Integer.parseInt(parts[1]);
    }



    public String serializeDot() {
        return pid + "," + seqNum;
    }

    public static Dot deserializeDot(String dotString) {
        return new Dot(dotString);
    }

    @Override
    public String toString() {
        return "(" + pid + "," + seqNum+")";
    }

    public boolean equals(Dot other) {
        return this.pid.equals(other.pid) && this.seqNum == other.seqNum;
    }
}

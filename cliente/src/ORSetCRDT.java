import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ORSetCRDT {
    VersionVector cc = new VersionVector();
    HashMap<String, HashSet<Dot>> m = new HashMap<>();

    public void add(String name, String pid) {
        Long pid_long = Long.parseLong(pid);
        int seqNum = cc.get(pid_long)+1;
        cc.put(pid_long,seqNum);
        Dot dot = new Dot(pid,seqNum);
        HashSet<Dot> dots = new HashSet<>();
        dots.add(dot);
        m.put(name, dots);
    }

    public void remove(String name, String pid) {
        m.remove(name);
    }

    public void merge(ORSetCRDT other){
        VersionVector cc_m = other.cc;
        HashMap<String, HashSet<Dot>> m_m = other.m;

        // (m, c) ⊔ (m′, c′) = ({k -→ v(k) | k ∈ dom m ∪ dom m′ ∧ v(k) ̸= ⊥}, )
        // where v(k) = fst((m(k), c) ⊔ (m′(k), c′))

        HashMap<String, HashSet<Dot>> new_m = new HashMap<>();
        HashSet<String> keys = new HashSet<>();
        keys.addAll(m.keySet());
        keys.addAll(m_m.keySet());
        for (String key : keys){
            // calcular v(k) = fst((m(k), c) ⊔ (m′(k), c′))
            // (s ∩ s′) ∪ (s \ c′) ∪ (s′\ c),
            HashSet<Dot> dots = new HashSet<>();
            HashSet<Dot> s = m.get(key);
            HashSet<Dot> s_m = m_m.get(key);
            HashSet<Dot> v_k = new HashSet(s); // use the copy constructor
            v_k.retainAll(s_m);
            for (Dot dot : s){
                Long pid = Long.parseLong(dot.pid);
                if (cc_m.get(pid) < dot.seqNum){
                    v_k.add(dot);
                }
            }
            for (Dot dot : s_m){
                Long pid = Long.parseLong(dot.pid);
                if (cc.get(pid) < dot.seqNum){
                    v_k.add(dot);
                }
            }
            if (!v_k.isEmpty()){
                new_m.put(key, v_k);
            }

        }
        // c ∪ c′
        cc.update(cc_m);
    }

    public String serialize(){
        StringBuilder sb = new StringBuilder();
        for (HashMap.Entry<String, HashSet<Dot>> entry : m.entrySet()) {
            sb.append(entry.getKey()).append("=");
            for (Dot dot : entry.getValue()){
                sb.append(dot.serializeDot()).append(",");
            }
            sb.append(";");
        }
        return sb.toString();
    }

    public static ORSetCRDT deserialize(String orSetString){
        ORSetCRDT orSet = new ORSetCRDT();
        String[] entries = orSetString.split(";");
        for (String entry : entries) {
            String[] parts = entry.split("=");
            String name = parts[0];
            HashSet<Dot> dots = new HashSet<>();
            String[] dotsString = parts[1].split(",");
            for (String dotString : dotsString){
                dots.add(Dot.deserializeDot(dotString));
            }
            orSet.m.put(name, dots);
        }
        return orSet;
    }



}

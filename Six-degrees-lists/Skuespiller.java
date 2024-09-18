import java.util.ArrayList;
import java.util.HashMap;

public class Skuespiller implements Comparable<Skuespiller> {
    String navn;
    String id;
    int size;
    int nummer;
    int distanse = Integer.MAX_VALUE;
    Skuespiller forrige;
    double vektet = Integer.MAX_VALUE;
    ArrayList<Film> filmer = new ArrayList<>();
    HashMap<Skuespiller, Film> naboer = new HashMap<>();
    ArrayList<Film> filmeneNabo = new ArrayList<>();
    public Skuespiller(String id, String navn){
        this.id = id;
        this.navn = navn;
    }

    public String toString() {
        return navn + " " + id;
    }

    @Override
    public int compareTo(Skuespiller o) {
        if(vektet < o.vektet){
            return 1;
        }
        if(vektet > o.vektet){
            return -1;
        }
        return 0;
    }

}
    


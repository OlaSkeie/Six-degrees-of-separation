import java.util.ArrayList;

public class Film {
    String id;
    String filmNavn;
    double rating;
    Film venstre;
    Film hoyre;
    int hoyde;
    ArrayList<Skuespiller> skuespillere = new ArrayList<>();
    public Film(String id, String fN, double r){
        this.id = id;
        filmNavn = fN;
        rating = r;
    }
    public String toString(){
        return "filmNavn: " + filmNavn + " Rating: " + rating;
    }
    
    
}

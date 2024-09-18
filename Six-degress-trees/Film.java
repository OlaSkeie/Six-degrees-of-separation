import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Film {
    String id;
    String filmNavn;
    double rating;
    double sRating;
    Skuespiller rot;
    Film venstre;
    int size;
    Film hoyre;
    Film sVenstre;
    Film sHoyre;
    int hoyde;
    int sHoyde;
    public Film(String id, String fN, double r){
        this.id = id;
        filmNavn = fN;
        rating = r;
        sRating = r;
    }
    
    public void les_fil(String filnavn){
        FilmGraf stt = new FilmGraf();
        BufferedReader b = null;
        try  {
            b = new BufferedReader(new FileReader("marvel_movies.tsv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s;
        try {
            while((s = b.readLine()) != null){
                s = s.strip();
                String[] st = s.split("\t");
                stt.rot = stt.insert(stt.rot, st[0], st[1], Double.parseDouble(st[2]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printUt(Skuespiller v) {
        if (v == null) {
            return;
        }
        System.out.println("" + v.navn + " " + "Hoyde: " + v.hoyde);
        printUt(v.venstre);
        printUt(v.hoyre);
    }

    public int balanceFactor(Skuespiller v) {
        if (v == null) {
            return 0;
        }
        return height(v.venstre) - height(v.hoyre);
    }

    public int height(Skuespiller v) {
        if (v == null) {
            return -1;
        }
        return v.hoyde;
    }


    int minHeight(Skuespiller v) {
        if (v == null) {
            return -1;
        }
        return 1 + Math.min(minHeight(v.venstre), minHeight(v.hoyre));
    }

    public Skuespiller balance(Skuespiller v) {
        if (balanceFactor(v) < -1) {
            if (balanceFactor(v.hoyre) > 0) {
                v.hoyre = hoyreRoter(v.hoyre);
            }
            return venstreRoter(v);
        }
        if (balanceFactor(v) > 1) {
            if (balanceFactor(v.venstre) < 0) {
                v.venstre = venstreRoter(v.venstre);
            }
            return hoyreRoter(v);
        }
        return v;

    }

    public Skuespiller venstreRoter(Skuespiller z) {
        Skuespiller y = z.hoyre;
        Skuespiller T1 = y.venstre;
        
        y.venstre = z;
        z.hoyre = T1;
        if (z.equals(rot)) {
            rot = y;
        }
        setHeight(z);
        setHeight(y);
        return y;
    }

    public Skuespiller hoyreRoter(Skuespiller z) {
        Skuespiller y = z.venstre;
        Skuespiller T2 = y.hoyre;
        
        y.hoyre = z;
        z.venstre = T2;
        if (z.equals(rot)) {
            rot = y;
        }
        setHeight(z);
        setHeight(y);
        return y;

    }
    public String toString(){
        return filmNavn + " " + hoyde;
    }

    public Skuespiller insert(Skuespiller v, Skuespiller s) {
        if (v == null) {
            return s;
        }
        if (s.id.compareTo(v.id) < 0) {
            v.venstre = insert(v.venstre, s);
        } else if (s.id.compareTo(v.id) > 0) {
            v.hoyre = insert(v.hoyre, s);
        } else {
            return v;
            
        }
        setHeight(v);
        return v;
        //return balance(v);
    }

    public void setHeight(Skuespiller v) {
        if (v == null) {
            return;
        }
        v.hoyde = max(height(v.venstre), (height(v.hoyre))) + 1;
    }

    public int max(int v, int h) {
        if (v > h)
            return v;
        return h;
    }

    public Skuespiller contains(Skuespiller v, String id) {
        if (v == null) {
            return null;
        }
        if (id.compareTo(v.id) == 0) {
            return v;
        }
        if (id.compareTo(v.id) < 0) {
            return contains(v.venstre,id);
        }
        if (id.compareTo(v.id) > 0) {
            return contains(v.hoyre, id);
        }
        return null;

    }

    public Skuespiller finnMinst(Skuespiller v) { 
        if (v.venstre == null) {
            return v;
        }
        return finnMinst(v.venstre);
    }

    public Skuespiller remove(Skuespiller v, String id) {
        if (v == null) {
            //size--;
            return v;
        }
        if(contains(v, id) == null){
            return null;
        }
        if (id.compareTo(v.id) < 0) {
            v.venstre = remove(v.venstre, id);
        } else if (id.compareTo(v.id) > 0) {
            v.hoyre = remove(v.hoyre, id);
        } else {
            if (v.venstre == null) {
                return v.hoyre;
            } else if (v.hoyre == null) {
                return v.venstre;
            }

            Skuespiller temp = finnMinst(v.hoyre);
            v.id = temp.id;
            v.hoyre = remove(v.hoyre, temp.id);
        }

        setHeight(v);
        return balance(v);
    }

    public int size() {
        return size;
    }

    
}

import java.util.ArrayList;

public class Skuespiller {
    Film filmRot;
    int size;
    int hoyde;
    String[] filmer;
    Skuespiller venstre;
    Skuespiller hoyre;
    String id;
    String navn;
    ArrayList<Film> a = new ArrayList<>();

    public Skuespiller(String id, String navn, String[] filmer) {
        this.filmer = filmer;
        this.id = id;
        this.navn = navn;
    }

    public String toString() {
        return navn + " " + id;
    }

    public int balanceFactor(Film v) {
        if (v == null) {
            return 0;
        }
        return height(v.sVenstre) - height(v.sHoyre);
    }

    public int height(Film v) {
        if (v == null) {
            return -1;
        }
        return v.sHoyde;
    }

    int minHeight(Film v) {
        if (v == null) {
            return -1;
        }
        return 1 + Math.min(minHeight(v.sVenstre), minHeight(v.sHoyre));
    }

    public Film balance(Film v) {
        if (balanceFactor(v) < -1) {
            if (balanceFactor(v.sHoyre) > 0) {
                v.sHoyre = hoyreRoter(v.sHoyre);
            }
            return venstreRoter(v);
        }
        if (balanceFactor(v) > 1) {
            if (balanceFactor(v.sVenstre) < 0) {
                v.sVenstre = venstreRoter(v.sVenstre);
            }
            return hoyreRoter(v);
        }
        return v;

    }

    public Film venstreRoter(Film z) {
        Film y = z.sHoyre;
        Film T1 = y.sVenstre;
        y.sVenstre = z;
        z.sHoyre = T1;
        setHeight(z);
        setHeight(y);
        return y;
    }

    public Film hoyreRoter(Film z) {
        Film y = z.sVenstre;
        Film T2 = y.sHoyre;
       System.out.println(size);
        y.sHoyre = z;
        z.sVenstre = T2;
        setHeight(z);
        setHeight(y);
        return y;

    }
    public void printUt(Film v) {
        if (v == null) {
            return;
        }
        System.out.println("" + v.filmNavn + " " + "Hoyde: " + v.sHoyde);
        printUt(v.sVenstre);
        printUt(v.sHoyre);
    }

    public Film settInn(Film v, Film f) {
        if(contains(v, f.id) != null){
            return null;
        }
        if (v == null) {
            size++;
            return f;
        } else if (f.id.compareTo(v.id) < 0) {
            v.sVenstre = settInn(v.sVenstre, f);
            
        } else if (f.id.compareTo(v.id) > 0) {
            v.sHoyre = settInn(v.sHoyre, f);
            
        } else {
            return v;

        }
        setHeight(v);
        return v;
        //return balance(v);
    }

    public void setHeight(Film v) {
        if (v == null) {
            return;
        }
        v.sHoyde = max(height(v.sVenstre), (height(v.sHoyre))) + 1;
    }

    public int max(int v, int h) {
        if (v > h)
            return v;
        return h;
    }

    public Film contains(Film v, String id) {
        if (v == null) {
            return null;
        }
        if (id.compareTo(v.id) == 0) {
            return v;
        }
        if (id.compareTo(v.id) < 0) {
            //System.out.println(v.sVenstre);
            return contains(v.sVenstre, id);
        }
        if (id.compareTo(v.id) > 0) {
            return contains(v.sHoyre, id);
        }
        return null;

    }

    public Film finnMinst(Film v) {
        if (v.sVenstre == null) {
            return v;
        }
        return finnMinst(v.sVenstre);
    }

    public Film remove(Film v, String id) {
        if (v == null) {
            //size--;
            return v;
        }
        if(contains(v, id) == null){
            return null;
        }
        if (id.compareTo(v.id) < 0) {
            v.sVenstre = remove(v.sVenstre, id);
        } else if (id.compareTo(v.id) > 0) {
            v.sHoyre = remove(v.sHoyre, id);
        } else {
            if (v.sVenstre == null) {
                return v.sHoyre;
            } else if (v.sHoyre == null) {
                return v.sVenstre;
            }

            Film temp = finnMinst(v.sHoyre);
            v.sRating = temp.sRating;
            v.sHoyre = remove(v.sHoyre, temp.id);
        }

        setHeight(v);
        return balance(v);
    }

    public int size() {
        return size;
    }
}

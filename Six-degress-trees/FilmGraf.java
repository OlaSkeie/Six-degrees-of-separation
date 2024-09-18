import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class FilmGraf {
    Film rot;
    int size;
    static int noder;
    static int kanter;

    public static void main(String[] args) {
        FilmGraf f = new FilmGraf();
        f.les_fil_filmer("marvel_movies.tsv", f);
        //printUt(f.rot);
        f.les_fil_skuespillere("marvel_actors.tsv", f);


    }

    public void les_fil_skuespillere(String filnavn, FilmGraf fg) {
        BufferedReader b = null;
        try {
            b = new BufferedReader(new FileReader(filnavn));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s;

        try {
            while ((s = b.readLine()) != null) {
                //System.out.println(s);
                String[] st = s.split("\t");
                String[] filmer = new String[st.length - 2];
                for (int i = 0; i < st.length - 2; i++) {
                    filmer[i] = st[i + 2];
                }
                Skuespiller ss = new Skuespiller(st[0], st[1], filmer);
                for (int j = 0; j < filmer.length; j++) {
                    
                    Film v = contains(fg.rot, filmer[j]);
                    if (v != null) {
                        ss.a.add(v);
                        v.rot = v.insert(v.rot, ss);
                    }
                    System.out.println(ss);
                    System.out.println();
                    System.out.println(v);
                    v.printUt(v.rot);
                    System.out.println();

                }
                
                //System.out.println(ss);
                
                // ss.printUt(ss.filmRot);
                
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void les_fil_filmer(String filnavn, FilmGraf fg) {
        BufferedReader b = null;
        try {
            b = new BufferedReader(new FileReader(filnavn));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s;
        try {
            while ((s = b.readLine()) != null) {

                String[] st = s.split("\t");
                fg.rot = fg.insert(fg.rot, st[0], st[1], Double.parseDouble(st[2]));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printUt(Film v) {
        if (v == null) {
            return;
        }
        System.out.println("" + v.filmNavn + " " + "Hoyde: " + v.hoyde);
        printUt(v.venstre);
        printUt(v.hoyre);
    }

    public int balanceFactor(Film v) {
        if (v == null) {
            return 0;
        }
        return height(v.venstre) - height(v.hoyre);
    }

    public int height(Film v) {
        if (v == null) {
            return -1;
        }
        return v.hoyde;
    }

    int minHeight(Film v) {
        if (v == null) {
            return -1;
        }
        return 1 + Math.min(minHeight(v.venstre), minHeight(v.hoyre));
    }

    public Film balance(Film v) {
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

    public Film venstreRoter(Film z) {
        Film y = z.hoyre;
        Film T1 = y.venstre;
        if (z.equals(rot)) {
            rot = y;
        }
        y.venstre = z;
        z.hoyre = T1;
        setHeight(z);
        setHeight(y);
        return y;
    }

    public Film hoyreRoter(Film z) {
        Film y = z.venstre;
        Film T2 = y.hoyre;
        if (z.equals(rot)) {
            rot = y;
        }
        y.hoyre = z;
        z.venstre = T2;
        setHeight(z);
        setHeight(y);
        return y;

    }

    public Film insert(Film v, String id, String fN, double rating) {
        if (v == null) {
            // size++;
            return new Film(id, fN, rating);
        }
        if (id.compareTo(v.id) < 0) {
            v.venstre = insert(v.venstre, id, fN, rating);
        } else if (id.compareTo(v.id) > 0) {
            v.hoyre = insert(v.hoyre, id, fN, rating);
        } else {
            return v;

        }
        setHeight(v);
        return balance(v);
    }

    public void setHeight(Film v) {
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

    public Film contains(Film v, String id) {
        if (v == null) {
            return null;
        }
        if (id.compareTo(v.id) == 0) {
            return v;
        }
        if (id.compareTo(v.id) < 0) {
            return contains(v.venstre, id);
        }
        if (id.compareTo(v.id) > 0) {
            return contains(v.hoyre, id);
        }
        return null;

    }

    public Film finnMinst(Film v) {
        if (v.venstre == null) {
            return v;
        }
        return finnMinst(v.venstre);
    }

    public Film remove(Film v, String id) {
        if (v == null) {
            // size--;
            return v;
        }
        if (contains(v, id) == null) {
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

            Film temp = finnMinst(v.hoyre);
            v.rating = temp.rating;
            v.hoyre = remove(v.hoyre, temp.id);
        }

        setHeight(v);
        return balance(v);
    }

    public int size() {
        return size;
    }
}

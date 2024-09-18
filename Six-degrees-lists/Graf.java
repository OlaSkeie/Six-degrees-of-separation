import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graf {
    ArrayList<Kant> kanter = new ArrayList<>();
    static int h = 0;
    ArrayList<Film> filmer = new ArrayList<>();
    ArrayList<Skuespiller> skuespillere = new ArrayList<>();
    HashMap<String, ArrayList<Skuespiller>> filmene = new HashMap<>();
    HashMap<Integer, Integer> komponenter = new HashMap<>();
    boolean[] besokt;
    public Graf(String filmerFil, String skuespillerFil){
        les_film(filmerFil);
        les_skuespillere(skuespillerFil);
        lagKanter();
        besokt = new boolean[kanter.size()];

    }

    public static void main(String[] args) {
        Graf g = new Graf(args[0], args[1]);
        // g.komponentC();
        //System.out.println("Komponenter: " + g.komponenter);
        g.printUt(g.contains("nm2704448"), g.contains("nm0161247"));
    }
    public int komponentC(){
        int c = 0;
        for(int i = 0; i < skuespillere.size(); i++){
            if(!besokt[skuespillere.get(i).nummer]){
                int k = DFS(skuespillere.get(i), 0); 
                h = 0;
                if(komponenter.containsKey(k)){  // Hvis k er i mapet, oppdaterer jeg valuen til k med +1
                    int j = komponenter.get(k) + 1;
                    komponenter.put(k, j);
                    
                }
                else{
                    komponenter.put(k, 1); // Hvis ikke putter jeg en ny k inn i mapet, med value 1
                }
                
                c++;
            }
        }
        return c;

    }

    public int DFS(Skuespiller s, int i){
        h++;
        besokt[s.nummer] = true;
        for(Skuespiller p : s.naboer.keySet()){
            if(!besokt[p.nummer]){
                DFS(p, h); // Får stackoverflowerror når det er rundt 20000 skuespillere. Vet ikke om det er noe
                // feil med koden min, eller om stacken bare blir for stor.
            }
        }
        return h;
    }

    public Skuespiller contains(String id) {
        for (Skuespiller s : skuespillere) {
            if (s.id.equals(id)) {
                return s;
            }
        }
        return null;
    }

    public int antallKanterHverFilm(int i) {
        int k = 0;
        for (int j = i; j > 0; j--) {
            k += j;
        }
        return k;
    }

    public void stiVektet(Skuespiller k, Skuespiller s) {
        HashMap<Skuespiller, Double> jk = dijkstra(k); // Kaller dijkstra slik at jeg får avstanden til alle fra k
        Skuespiller tmp = s;
        int j = 0;  //Index sjekker 
        int i = 0;
        Skuespiller t = null;
        int index = Integer.MAX_VALUE;
        boolean[] besokt = new boolean[skuespillere.size()]; //Sjekke hvilke skuespillere som er besøkt, alle har et nummer
        while (!tmp.id.equals(k.id)) {
            besokt[tmp.nummer] = true;

            for (Skuespiller h : tmp.naboer.keySet()) {
                if (!besokt[h.nummer]) {
                    if (jk.get(h) < index) {
                        index = h.distanse;
                        t = h;
                        i = j;
                    }
                }
                if (h.id.equals(k.id)) {
                    System.out.println(tmp + "\n ==");
                    Film v = tmp.filmeneNabo.get(i);
                    System.out.println(v + "==> ");
                    System.out.println(h + "\n");
                    return;
                }

                j++;
            }
            System.out.println(tmp + "\n ==");

            Film v = tmp.filmeneNabo.get(i);
            System.out.println(v + "==> ");
            tmp = t;
            System.out.println(tmp + "\n");
            index = Integer.MAX_VALUE;
            i = 0;
            j = 0;

        }
    }

    public void printUt(Skuespiller en, Skuespiller to) {
        System.out.println("Total vekt: " + dijkstra(en).get(to)); // Kaller dijkstra slik at jeg får avstanden til alle fra en
        Skuespiller t = to;
        ArrayList<Film> f = new ArrayList<>();
        ArrayList<Skuespiller> s = new ArrayList<>();
        
        while(!t.equals(en)){
            f.add(t.forrige.naboer.get(t));
            s.add(t);
            t = t.forrige;
        }
        s.add(t);
        System.out.println(s.get(s.size() - 1));
        int i = f.size() - 1;
        int j = s.size() - 2;
        
        while(i != -1 || j != -1){
            if(i != -1){
                System.out.println(f.get(i) + " ==> ");
                i--;
            }
            if(j != -1){
                System.out.println(s.get(j));
                System.out.println();
                System.out.println("== " + s.get(j));
                j--;

            }
            
            
        }
    }

    public double get(HashMap<Skuespiller, Double> h, Skuespiller s) {
        Double d;
        if ((d = h.get(s)) == null) {
            return Double.MAX_VALUE;
        }
        return d;
    }

    public HashMap<Skuespiller, Double> dijkstra(Skuespiller s) {
        HashMap<Skuespiller, Double> dist = new HashMap<>();
        PriorityQueue<Skuespiller> q = new PriorityQueue<>(Comparator.comparing(dist :: get)); //Fikk denne fra ChatGpt da jeg spurte om måter til å optimalisere koden
        
        dist.put(s, 0.0);
        q.add(s);
        while (!q.isEmpty()) {
            Skuespiller tmp = q.poll();
            for (Skuespiller p : tmp.naboer.keySet()) {
                double ds = get(dist, tmp) + (10 - tmp.naboer.get(p).rating);
                if (ds < get(dist, p)) {
                    dist.put(p, ds);
                    p.forrige = tmp;
                    q.add(p);
                }
            }
        }
        return dist;
    }

    public void sti(Skuespiller k, Skuespiller s) { //Printer ut stien for uvektede grafer
        Skuespiller tmp = s;
        int j = 0;
        int i = 0;
        Skuespiller t = null;
        int index = Integer.MAX_VALUE;
        boolean[] besokt = new boolean[skuespillere.size()];

        // besokt[tmp.nummer] = true;
        while (!tmp.id.equals(k.id)) {
            besokt[tmp.nummer] = true;
            for (Skuespiller h : tmp.naboer.keySet()) {
                if (!besokt[h.nummer]) {
                    if (h.distanse < index) {
                        index = h.distanse;
                        t = h;
                        i = j;
                    }
                }
                
                if (h.id.equals(k.id)) {
                    System.out.println(tmp + "\n ==");
                    Film v = tmp.filmeneNabo.get(i);
                    System.out.println(v + "==> ");
                    System.out.println(h + "\n");
                    return;
                }

                j++;
            }
            System.out.println(tmp + "\n ==");
            Film v = tmp.filmeneNabo.get(i);
            System.out.println(v + "==> ");
            tmp = t;
            System.out.println(tmp + "\n");
            index = Integer.MAX_VALUE;
            i = 0;
            j = 0;

        }

    }

    public int tellNoder() {
        return skuespillere.size();
    }

    public int tellKanter() {
        return kanter.size();
    }

    public void kortVei(Skuespiller k, Skuespiller s) { // sti for færrest noder fra k til s
        boolean[] besokt = new boolean[skuespillere.size()];
        Queue<Skuespiller> queue = new LinkedList<>();
        k.distanse = 0;
        k.vektet = 0;
        queue.add(k);
        while (!queue.isEmpty()) {

            Skuespiller tmp = queue.poll();
            for (Skuespiller skuespiller : tmp.naboer.keySet()) {
                if (skuespiller.equals(s)) {
                    System.out.println(tmp.distanse + 1);
                    return;

                }
                if (!besokt[skuespiller.nummer]) {
                    besokt[skuespiller.nummer] = true;
                    queue.add(skuespiller);
                    skuespiller.distanse = tmp.distanse + 1;
                    skuespiller.vektet = 10 - (tmp.naboer.get(skuespiller).rating) + tmp.vektet;

                }

            }

        }

    }

    public void lagKanter() {
        for (int i = 0; i < filmene.size(); i++) {
            Film v = filmer.get(i);
            int j = antallKanterHverFilm(filmene.get(v.id).size() - 1);
            int teller = 0;
            int index1 = 0;
            int index2 = 1;
            while (teller < j) {
                Kant kant = new Kant(v);
                kant.start = filmene.get(v.id).get(index1);
                kant.slutt = filmene.get(v.id).get(index2);
                if (kant.start.naboer.containsKey(kant.slutt)) {
                    if (v.rating > kant.start.naboer.get(kant.slutt).rating) {
                        kant.start.naboer.put(kant.slutt, v);
                    }
                } else {
                    kant.start.naboer.put(kant.slutt, v);
                }
                if (kant.slutt.naboer.containsKey(kant.start)) {
                    if (v.rating > kant.slutt.naboer.get(kant.start).rating) {
                        kant.slutt.naboer.put(kant.start, v);
                    }
                } else {
                    kant.slutt.naboer.put(kant.start, v);
                }
                kant.slutt.filmeneNabo.add(v);
                kant.start.filmeneNabo.add(v);
                teller++;
                kanter.add(kant);

                if (filmene.get(v.id).get(index2).equals(filmene.get(v.id).get(filmene.get(v.id).size() - 1))) {
                    index1++;
                    index2 = index1;
                }
                index2++;
            }
        }

    }

    public void les_skuespillere(String filnavn) {
        BufferedReader b = null;
        try {
            b = new BufferedReader(new FileReader(filnavn));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s;
        int teller = 0;
        try {
            while ((s = b.readLine()) != null) {
                String[] st = s.split("\t");
                Skuespiller ss = new Skuespiller(st[0], st[1]);
                ss.nummer = teller;
                //sId.put(ss.nummer, ss);
                teller++;
                skuespillere.add(ss);
                for (int i = 0; i < st.length - 2; i++) {
                    if (filmene.containsKey(st[i + 2])) {
                        filmene.get(st[i + 2]).add(ss);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void les_film(String filnavn) {
        BufferedReader b = null;
        try {
            b = new BufferedReader(new FileReader(filnavn));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s;
        try {
            while ((s = b.readLine()) != null) {
                String[] st = s.split("\t");
                Film f = new Film(st[0], st[1], Double.parseDouble(st[2]));
                filmer.add(f);
                filmene.put(st[0], new ArrayList<Skuespiller>());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class Kant {
        Skuespiller start;
        Skuespiller slutt;
        Film film;
        Kant neste;

        public Kant(Film film) {
            this.film = film;
        }

        public String toString() {
            return start.navn + " =====> " + slutt.navn + " Film: " + film.filmNavn;
        }
    }
}
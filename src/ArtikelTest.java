public class ArtikelTest {

    public static void main(String[] args) {
        Forlag forlag = new Forlag("University press", "Denmark");

        Tidsskrift t1 = new Tidsskrift("Journal of Logic");
        Tidsskrift t2 = new Tidsskrift("Brain");
        t1.setForlag(forlag);
        t2.setForlag(forlag);

        String  f1[] = new String[]{"A. Abe", "A. Turing"};
        String  f2[] = new String[]{"B. Bim"};
        Artikel a1 = new Artikel(f1, "A", t1);
        Artikel a2 = new Artikel(f2, "B", t1);

        // Første artikel har reference til den anden
        a1.setReferenceliste(new Artikel[]{a2});

        System.out.println(a1.toString() + "\n");
        System.out.println(a2.toString());

    }
}

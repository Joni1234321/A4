import java.util.Arrays;

public class Artikel {

    Artikel referenceliste[];
    Tidsskrift tidsskrift;
    String forfattere[], titel;

    public Artikel (String forfattere[], String titel, Tidsskrift tidsskrift) {
        this.forfattere = forfattere;
        this.titel = titel;
        this.tidsskrift = tidsskrift;
    }

    public void setReferenceliste(Artikel referenceliste[]){
        for (int i = 0; i < referenceliste.length; i++)
            if (referenceliste[i].equals(this))
                throw new IllegalArgumentException("Recursive reference for artikel: " + referenceliste[i]);   // Recursive

        this.referenceliste = referenceliste;
    }

    public String toString() {
        String res = " ";
        if (forfattere != null)
            for (int i = 0; i < forfattere.length; i++) {
                if (i != 0) res += " & ";
                res += forfattere[i];
            }
        res += ": '" + titel + "' . " + tidsskrift.toString();

        if (referenceliste != null && referenceliste.length != 0) {
            res += ". Artiklen refererer til følgende ";
            res += (referenceliste.length == 1) ? "artikel" : referenceliste.length + " artikler";
            res += "\n";
            for (int i = 0; i < referenceliste.length; i++) {
                if (i != 0) res += "\n";
                res += (i + 1) + ": ";
                res += referenceliste[i].toString();
            }
        }
        return res.trim();
    }
}

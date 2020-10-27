public class Tidsskrift {

    Forlag forlag;
    String titel, issn;

    public Tidsskrift (String titel) {
        this.titel = titel;
    }

    public void setForlag (Forlag forlag){
        this.forlag = forlag;
    }
    public void setIssn (String issn) {
        this.issn = issn;
    }

    public String toString() {
        String res = "";
        if (titel != null)
            res += titel + " ";
        if (forlag != null)
            res += "af " + forlag.toString() + " ";
        if (issn != null)
            res += issn + " ";

        return res.trim();
    }
}

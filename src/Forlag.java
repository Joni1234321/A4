public class Forlag {
    String navn, sted;

    public Forlag (String navn, String sted) {
        this.navn = navn;
        this.sted = sted;
    }

    public String toString() {
        return "forlaget '" + navn + "'" + " i '" + sted + "'";
    }
}

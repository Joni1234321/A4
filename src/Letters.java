
public class Letters {
    public static void main(String[] args) {
        int length = 0;
        for (int i = 0; i < args.length; i++) {
            length += args[i].length();
        }
        System.out.println(length);
    }
}

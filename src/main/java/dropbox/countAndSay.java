package dropbox;

public class countAndSay {

    public static void main(String[] args) {
        int n = 4;
        countAndSay proc = new countAndSay();
        System.out.println(proc.countAndSay(n));
    }

    public String countAndSay(int n) {
        StringBuilder sb = new StringBuilder();
        sb.append(1);
        if (n == 1) {
            return sb.toString();
        }

        int count = 0;
        char ch = '#';

        for (int i = 2; i <= n; ++i) {
            String prev = sb.toString();
            sb.delete(0, sb.length());
            for (int j = 0; j < prev.length(); ++j) {
                if (prev.charAt(j) != ch) {
                    if (count != 0) {
                        sb.append(count);
                        sb.append(ch);
                    }
                    count = 1;
                    ch = prev.charAt(j);
                } else {
                    count++;
                }
            }

            sb.append(count);
            sb.append(ch);
            count = 0;
            ch = '#';

        }
        return sb.toString();
    }
}


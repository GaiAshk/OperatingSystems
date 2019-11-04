import java.util.List;

public class Worker implements Runnable {
    private final List<String> lines;

    public Worker(List<String> lines) {
        this.lines = lines;
    }

    public void run() {
        int numLettersFound = 0;
        for (String lines : lines) {
            numLettersFound += getNumLettersFound(lines);

        }
        System.out.println("number of letters found: " + numLettersFound);
    }

    private int getNumLettersFound(String string) {
        int numLettersFound = 0;
        String lowerCasedLine = string.toLowerCase();
        for (int i = 0; i < lowerCasedLine.length(); i++) {
            char c = lowerCasedLine.charAt(i);
            if (c == 'o' || c == 'p' || c == 'e' || c == 'r' || c == 'a' ||
                    c == 't' || c == 'i' || c == 'n' || c == 'g'
                    || c == 's' || c == 'y' || c == 'm') {
                numLettersFound += 1;

            }
        }
        return numLettersFound;
    }
}

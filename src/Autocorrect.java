import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author Lucas Ying
 */
public class Autocorrect {

    public static void main(String[] args) {
        // Load dictionary from "dictionaries/english.txt"
        String[] words = loadDictionary("large");
        // Create an Autocorrect instance with threshold 3
        Autocorrect autocorrect = new Autocorrect(words, 3);
        Scanner s = new Scanner(System.in);

        System.out.println("Type your words below we will correct them! (type 'exit' to quit)");

        // Continuous loop for user input
        while (true) {
            System.out.print("Enter a word: ");
            String typed = s.nextLine().trim();

            if (typed.equals("exit")) {
                System.out.println("Quitting now.");
                break;
            }

            // If it's a valid word in the dictionary, do nothing
            if (Arrays.binarySearch(words, typed) >= 0) {
                System.out.println("Word found in the dictionary, looks like you spelled it right!");
                System.out.println("");
                continue;
            }

            // Get suggested matches using runTest
            String[] suggestions = autocorrect.runTest(typed);

            // Print suggestions or message if none found
            if (suggestions.length == 0) {
                System.out.println("No matches found.");
            } else {
                System.out.println("Did you mean: ");
                for (int i = 0; i < Math.min(5, suggestions.length); i++) {
                    System.out.println(" - " + suggestions[i]);
                }
                System.out.println("");
            }
        }
    }

    private final String[] dictionaryWords;
    private final int threshold;

    /**
     * Constructs an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */
    public Autocorrect(String[] words, int threshold) {
        this.dictionaryWords = words;
        this.threshold = threshold;
    }

    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {
        ArrayList<String> matches = new ArrayList<>();

        for (String word : dictionaryWords) {
            int distance = ed(typed, word);

            if (distance <= threshold) {
                matches.add(word);
            }
        }
        // Sort first by edit distance, then alphabetically
        matches.sort(Comparator.comparingInt((String word) -> ed(typed, word)).thenComparing(String::compareTo));

        return matches.toArray(new String[0]);

    }

    // Computes the Levenshtein edit distance between two words using tabulation.
    public int ed(String typed, String word) {
        if (word.isEmpty()) return -1;

        int ed = 0;

        int length1 = typed.length();
        int length2 = word.length();
        int[][] matrix = new int[length1 + 1][length2 + 1];

        // Initialize first column and row
        for (int j = 0; j <= length1 ; j++) {
            matrix[j][0] = j;
        }
        for (int k = 0; k <= length2; k++) {
            matrix[0][k] = k;
        }

        // Fill matrix
        for (int j = 1; j <= length1; j++) {
            for (int k = 1; k <= length2; k++) {
                if (typed.charAt(j-1) == word.charAt(k-1)){
                    matrix[j][k] = matrix[j-1][k-1];
                } else {
                    matrix[j][k] = 1 + Math.min(Math.min(matrix[j-1][k], matrix[j][k-1]), matrix[j-1][k-1]);
                }
            }
        }
        ed = matrix[length1][length2];
        return ed;
    }


    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary)  {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;
            }
            return words;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
    static int pollySyllables = 0;

    public static void main(String[] args) {
        // read from command line
        String pathToFile = args[0];
        String text = readFileAsString(pathToFile);
        assert text != null;
        int numberOFSentences = getNumberOFSentence(text);
        int numberOfWords = getNumberOFWords(text);
        int numberOfCharacters = getNumberOFCharacters(text);
        int syllables = getSyllables(text);
        System.out.printf("The text is:%n%s%n%nWords: %d%nSentences: %d%nCharacters: %d%nSyllables: %d%nPolysyllables: %d%n",
                text, numberOfWords, numberOFSentences, numberOfCharacters, syllables, pollySyllables);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        Scanner scanner = new Scanner(System.in);
        String indexChoice = scanner.nextLine();
        switch (indexChoice.toUpperCase()) {
            case "ARI":
                double ari = getReadabilityIndex(numberOFSentences, numberOfWords, numberOfCharacters);
                System.out.printf("Automated Readability Index: %.2f (about %s year olds).", ari, calculateAgeFromIndex(ari));
                break;
            case " FK":
                double fk = getFleschKincaid(numberOFSentences, numberOfWords, syllables);
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s year olds).", fk, calculateAgeFromIndex(fk));
                break;
            case "SMOG":
                double smog = getSmog(numberOFSentences, pollySyllables);
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s year olds).", smog, calculateAgeFromIndex(smog));
                break;
            case "CL":
                double cl = getColemanLiau(numberOFSentences, numberOfWords, numberOfCharacters);
                System.out.printf("Coleman–Liau index: %.2f (about %s year olds).", cl, calculateAgeFromIndex(cl));
                break;
            case "ALL":
                ari = getReadabilityIndex(numberOFSentences, numberOfWords, numberOfCharacters);
                System.out.printf("Automated Readability Index: %.2f (about %s year olds).%n", ari, calculateAgeFromIndex(ari));
                fk = getFleschKincaid(numberOFSentences, numberOfWords, syllables);
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s year olds).%n", fk, calculateAgeFromIndex(fk));
                smog = getSmog(numberOFSentences, pollySyllables);
                System.out.printf("Simple Measure of Gobbledygook %.2f (about %s year olds).%n", smog, calculateAgeFromIndex(smog));
                cl = getColemanLiau(numberOFSentences, numberOfWords, numberOfCharacters);
                System.out.printf("Coleman–Liau index: %.2f (about %s year olds).%n", cl, calculateAgeFromIndex(cl));
                break;


            default:
//                System.out.println("This text should be understood by %s year olds.%n");
        }
    }

    public static String readFileAsString(String pathToFile) {
        try {
            return new String(Files.readAllBytes(Paths.get(pathToFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getNumberOFSentence(String text) {
        return text.split("[.!?]").length;
    }

    public static int getNumberOFWords(String text) {
        return text.split("\\s").length;
    }

    public static int getNumberOFCharacters(String text) {
        return text.replaceAll("\\s", "").length();
    }

    public static double getReadabilityIndex(int numbOfSentences, int numberOfWords, int numberOfCharacters) {
        return 4.71 * (double) numberOfCharacters / numberOfWords + 0.5 * numberOfWords / numbOfSentences - 21.43;
    }

    public static double getFleschKincaid(int numberOfSentences, int numberOfWords, int syllables) {
        return 0.39 * (double) numberOfWords / numberOfSentences + 11.8 * syllables / numberOfWords - 15;
    }

    public static double getSmog(int numberOfSentences, int pollySyllables) {
        return 1.043 * Math.sqrt((double) pollySyllables * 30 / numberOfSentences) + 3.1291;
    }

    public static double getColemanLiau(int numberOfSentences, int numberOfWords, int numberOfCharacters) {
        double l = (double) numberOfCharacters / numberOfWords * 100;
        double s = (double) numberOfSentences / numberOfWords * 100;
        return 0.0588 * l - 0.296 * s - 15.8;
    }

    public static String calculateAgeFromIndex(double score) {
        String[] age = {"6", "7", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "24", "24"};
        return age[(int) Math.round(score) - 1];
    }

    public static int getSyllables(String text) {
        String[] words = text.split("\\s");
        int syllables = 0;
        String regex = "[aeiou]";
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll(".$", "k");
            int vowels = 0;
            for (int j = 0; j < words[i].length(); j++) {
                if (Character.toString(words[i].charAt(j)).matches(regex)) {
                    vowels += 1;
                    if (j < (words[i].length() - 1) && Character.toString(words[i].charAt(j + 1)).matches(regex)) {
                        vowels -= 1;
                    }
                }
            }
            if (vowels > 2) {
                pollySyllables++;
            }
            syllables += Math.max(vowels, 1);
        }
        return syllables;
    }
}
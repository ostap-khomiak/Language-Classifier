import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;

public class Main {

    static final String[] LANGUAGES = {"Polish", "English", "Spanish"};
    static final int ALPHABETLENGTH = 26;


    static double[] frequencyList = new double[ALPHABETLENGTH];
    public static void main(String[] args) {

        Perceptron polishPerceptron = new Perceptron(ALPHABETLENGTH);
        Perceptron englishPerceptron = new Perceptron(ALPHABETLENGTH);
        Perceptron spanishPerceptron = new Perceptron(ALPHABETLENGTH);

        trainPerceptrons(polishPerceptron, englishPerceptron, spanishPerceptron);

        System.out.println("---------------------------------");
        testPerceptrons(polishPerceptron, englishPerceptron, spanishPerceptron);

        System.out.println("---------------------------------");


        Scanner sc = new Scanner(System.in);
        System.out.println("Choose mindfully:");
        System.out.println("1. Enter filepath.");
        System.out.println("2. Enter string.");
        int choice = sc.nextInt();
        if (choice == 1) {
            System.out.println("Enter absolute filepath:");
            String path = sc.next();
            readFile(new File(path));


            System.out.println(guessLanguage(polishPerceptron, englishPerceptron, spanishPerceptron));

        }  else if (choice == 2) {
            System.out.println("Enter string:");
            String string = sc.next();
            string = normalize(string);
            int[] tempList = new int[ALPHABETLENGTH];
            for(char c : string.toCharArray()) {
                tempList[c - 'a']++;
            }
            frequencyList = calculateFrequency(tempList);

            System.out.println(guessLanguage(polishPerceptron, englishPerceptron, spanishPerceptron));

        }

    }

    public static String normalize(String s) {
        return s.toLowerCase()
                .replaceAll("[ìíî]", "i")
                .replaceAll("[ąàá]", "a")
                .replaceAll("ć", "c")
                .replaceAll("[èéę]", "e")
                .replaceAll("ś", "s")
                .replaceAll("ł", "l")
                .replaceAll("ń", "n")
                .replaceAll("òó", "o")
                .replaceAll("[ùúü]", "u")
                .replaceAll("[żź]", "z")
                .replaceAll("[^a-z]", "");
    }

    public static double[] calculateFrequency(int[] list) {
        double sum = 0;
        for(double elem : list) {
            sum += elem;
        }
        double[] frequency = new double[ALPHABETLENGTH];
        for(int i = 0; i < frequency.length; i++) {
            frequency[i] = list[i] / sum;
        }
        return frequency;
    }

    public static void readFile(File file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int[] tempList = new int[ALPHABETLENGTH];
            while( (line = br.readLine()) != null) {
                line = normalize(line);
                for(char c : line.toCharArray()) {
                    tempList[c - 'a']++;
                }
            }

            frequencyList = calculateFrequency(tempList);
            fr.close();
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void trainPerceptrons(Perceptron polishPerceptron, Perceptron englishPerceptron, Perceptron spanishPerceptron) {

        for (int i = 0; i < 111; i++) {

            for(int lang = 0; lang < LANGUAGES.length; lang++) {

                int[] targets = new int[3];
                targets[lang] = 1;

                for (int number = 1; number <= 3; number++) {
                    String path = "Languages/" + LANGUAGES[lang] + "/" + number + ".txt";
                    readFile(new File(path));

                    polishPerceptron.learn(frequencyList, targets[0]);
                    englishPerceptron.learn(frequencyList, targets[1]);
                    spanishPerceptron.learn(frequencyList, targets[2]);
                }
            }
        }
    }

    public static String guessLanguage(Perceptron polishPerceptron, Perceptron englishPerceptron, Perceptron spanishPerceptron) {
        int pl = polishPerceptron.calculate(frequencyList);
        int en = englishPerceptron.calculate(frequencyList);
        int sp = spanishPerceptron.calculate(frequencyList);

//        System.out.println(pl + " " + en + " " + sp);

        String result;

        if (sp == 1) {
            result = "Spanish";
        } else if (en == 1) {
            result = "English";
        } else if (pl == 1) {
            result = "Polish";
        } else {
            result = "Unknown";
        }

        return result;

    }

    public static void testPerceptrons(Perceptron pl, Perceptron en, Perceptron sp) {

        for (String lang : LANGUAGES) {
            readFile(new File("Languages/" + lang + "/test.txt"));
            System.out.println(guessLanguage(pl, en, sp));
        }
    }



}

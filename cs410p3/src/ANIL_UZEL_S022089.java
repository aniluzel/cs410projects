import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ANIL_UZEL_S022089 {
    public static void main(String[] args) throws IOException {

        /** CHANGE TURING FILE NAME FROM HERE **/
        String input_file = "Input_ANIL_UZEL_S022089.txt";

        /** DEFAULT FILE READ METHOD. INPUT.TXT AND JAVA CLASS MUST BE IN THE SAME FOLDER **/
        URL path = ANIL_UZEL_S022089.class.getResource(input_file);
        if(path==null) {
            System.out.println("File not found. 'Input_ANIL_UZEL_S022089.txt' files must be in the same folder as 'ANIL_UZEL_S022089.java'. You can also try the other method.");
        }
        File f = new File(path.getFile());

        /** IF IT DOESN'T WORK COMMENT OUT LINES 16-20 AND TRY THIS **/
        // File f = new File(input_file);

        /** OR THIS IF YOU WANT TO GIVE IT A FULL FILE PATH **/
        // String fullpath = "ENTER PATH HERE";
        // File f = new File(fullpath);

        Scanner reader = new Scanner(new FileReader(f));

        reader.nextLine(); // skip BLANK SYMBOL
        String blank_symbol = reader.nextLine();

        reader.nextLine(); // skip STATES
        String[] split = reader.nextLine().split("\\s+");
        ArrayList<String> states = new ArrayList<>(Arrays.asList(split));

        reader.nextLine(); // skip START STATE
        String start_state = reader.nextLine();

        reader.nextLine(); // skip ACCEPT STATE
        String accept_state = reader.nextLine();

        reader.nextLine(); // skip REJECT STATE
        String reject_state = reader.nextLine();
        reader.nextLine(); // skip TRANSITIONS

        ArrayList<String> transitions = new ArrayList<>();
        String line = reader.nextLine();
        while(!line.equals("STRING TO BE DETECTED")) {
            transitions.add(line);
            line = reader.nextLine();
        } // skip STRING TO BE DETECTED

        split = reader.nextLine().split("");
        ArrayList<String> tape = new ArrayList<>(Arrays.asList(split));
        tape.add(blank_symbol);

        reader.close(); // end of file

        int tape_index = 0;
        String current_state = start_state;
        String current_tape = tape.get(tape_index);
        String rout = "";
        ArrayList<String> loop_check = new ArrayList<>();

        while(true) {
            // unique identifier for each transition to detect if machine enters a loop
            String current_pair = current_state + current_tape + tape_index + tape;

            if (loop_check.contains(current_pair)) {
                System.out.println("ROUT: " + rout);
                System.out.println("RESULT: looped");
                break;
            }
            loop_check.add(current_pair);

            rout += current_state + " ";

            if (current_state.equals(accept_state)) {
                System.out.println("ROUT: " + rout);
                System.out.println("RESULT: accepted");
                break;
            } else if (current_state.equals(reject_state)) {
                System.out.println("ROUT: " + rout);
                System.out.println("RESULT: rejected");
                break;
            } else {
                for (String transition : transitions) {
                    split = transition.split("\\s+");
                    if (split[0].equals(current_state) && split[1].equals(current_tape)) {
                        tape.set(tape_index, split[2]);
                        if (split[3].equals("R")) {
                            if (tape_index + 1 == tape.size()) {
                                tape.add(blank_symbol);
                                if (tape.get(tape.size() - 1).equals(blank_symbol) && tape.get(tape.size() - 2).equals(blank_symbol)) {
                                    tape_index--;
                                    tape.remove(tape.size() - 1);
                                }
                            }
                            tape_index++;
                        } else if (split[3].equals("L") && tape_index != 0) {
                            tape_index--;
                        }

                        current_state = split[4];
                        current_tape = tape.get(tape_index);
                        break;
                    }
                }
            }
        }
    }
}

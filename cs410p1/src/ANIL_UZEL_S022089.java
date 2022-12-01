import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ANIL_UZEL_S022089 {
    public static void main(String[] args) throws IOException {

            /** CHANGE NFA FILE NAME FROM HERE **/
            String input = "NFA1.txt";

            /** DEFAULT FILE READ METHOD. NFA.TXT AND JAVA CLASS MUST BE IN THE SAME FOLDER **/
            URL path = ANIL_UZEL_S022089.class.getResource(input);
            if(path==null) {
                System.out.println("File not found. 'NFA.txt' files must be in the same folder as 'ANIL_UZEL_S022089.java'. You can also try the other method.");
            }
            File f = new File(path.getFile());

            /** IF IT DOESN'T WORK COMMENT OUT LINES 16-20 AND TRY THIS **/
            // File f = new File(input);

            /** OR THIS IF YOU WANT TO GIVE IT A FULL FILE PATH **/
            // String fullpath = "ENTER PATH HERE";
            // File f = new File(fullpath);

            Scanner reader = new Scanner(new FileReader(f));

            // Get line 'ALPHABET'
            String line = reader.nextLine();

            ArrayList<String> alphabets = new ArrayList<>();
            ArrayList<String> states = new ArrayList<>();

            if (line.equals("ALPHABET")) {

                // Skip line 'ALPHABET'
                line = reader.nextLine();

                while (reader.hasNextLine()) {
                    if (line.equals("STATES")) {
                        // Get line 'STATES'
                        line = reader.nextLine();
                        break;
                    } else {
                        // Add ALPHABET lines to ArrayList alphabets
                        alphabets.add(line);
                        line = reader.nextLine();
                    }
                }

                while (reader.hasNextLine()) {
                    if (line.equals("START")) {
                        // Get line 'START'
                        line = reader.nextLine();
                        break;
                    } else {
                        // Add STATE lines to ArrayList states
                        states.add(line);
                        line = reader.nextLine();
                    }
                }

                // Create 2D String Array NFA table from state and alphabet sizes
                String[][] nfa = new String[states.size()+1][alphabets.size()+1];

                // NFA[0][0] is not used for states and alphabets
                nfa[0][0] = "NFA_TABLE";

                // Assign alphabets to NFA[][alphabets]
                for (int i = 1; i <= alphabets.size(); i++) {
                    nfa[0][i] = alphabets.get(i-1);
                }

                // Assign states to NFA[][states]
                for (int i = 1; i <= states.size(); i++) {
                    nfa[i][0] = states.get(i-1);
                }

                // Assign START state
                String start_state = line;
                line = reader.nextLine();
                line = reader.nextLine();

                // Assign FINAL state
                String final_state = "";
                while (reader.hasNextLine()) {
                    if (line.equals("TRANSITIONS")) {
                        line = reader.nextLine();
                        break;
                    } else {
                        final_state += line;
                        line = reader.nextLine();
                    }
                }

                String[] final_state_split = final_state.split("");

                // Read and assign TRANSITIONS
                while (reader.hasNextLine()) {
                    if (line.equals("END")) {
                        // End of file
                        reader.close();
                        break;
                    } else {
                        // Split TRANSITION lines into an array
                        String[] transition = line.split("\\s+");

                        // Assign TRANSITION values into NFA array
                        for (int i = 0; i <= states.size(); i++) {
                            if (nfa[i][0] != null && nfa[i][0].equals(transition[0])) {
                                for (int j = 0; j <= alphabets.size(); j++) {
                                    if (nfa[0][j] != null && nfa[0][j].equals(transition[1]) ) {
                                        if (nfa[i][j] == null) {
                                            nfa[i][j] = transition[2];
                                        } else {
                                            nfa[i][j] += transition[2];
                                        }
                                    }
                                }
                            }
                        }

                        line = reader.nextLine();
                    }
                }

                // Fill null values with '-' which indicates an empty state
                for (int i = 0; i <= states.size(); i++) {
                    if (nfa[i][0] != null) {
                        for (int j = 0; j <= alphabets.size(); j++) {
                            if (nfa[i][j] == null) {
                                nfa[i][j] = "-";
                            }
                        }
                    }
                }

                // Created 2 ArrayLists for states of DFA and transitions of DFA
                // They will be used in creation of 2D String DFA later
                ArrayList<String> dfa_states = new ArrayList<>();
                ArrayList<String> dfa_transitions = new ArrayList<>();

                dfa_states.add("DFA_TABLE");

                // Adds first state of NFA to DFA
                dfa_states.add(nfa[1][0]);

                // Checks if the first state of NFA contains an empty state '-'
                // If it exists marks them with 'q' which indicates a dead state
                for (int i = 0; i < alphabets.size(); i++) {
                    if (nfa[1][i+1].equals("-")){
                        dfa_transitions.add("q");
                    } else {
                        dfa_transitions.add(nfa[1][i+1]);
                    }
                }

                int transition_counter = alphabets.size();

                // Start converting NFA states and transitions into DFA states and transitions
                for(int i = 2; i <= dfa_states.size(); i++) {

                    // Check transitions of DFA
                    // If a state inside a transition doesn't exist in DFA states, adds it
                    for (String dfa_transition : dfa_transitions) {
                        if (!dfa_states.contains(dfa_transition)) {
                            dfa_states.add(dfa_transition);
                            break;
                        }
                    }

                    // If a new DFA state is added, checks unions of combined states
                    if (i != dfa_states.size()) {
                        String[] split_transition = dfa_states.get(i).split("");
                        String[] temp_transition_arr = new String[alphabets.size()];

                        for (int x = 0; x < alphabets.size(); x++) {
                            dfa_transitions.add("");
                            temp_transition_arr[x] = "";
                        }

                        for (int j = 0; j < states.size(); j++) {
                            for (int k = 0; k < split_transition.length; k++) {
                                if (nfa[j+1][0].contains(split_transition[k])) {
                                    for (int x = 0; x < alphabets.size(); x++) {
                                        if (!nfa[j + 1][x+1].equals("-") && !temp_transition_arr[x].contains(nfa[j + 1][x+1])) {
                                            String[] temp_transition_split = nfa[j + 1][x+1].split("");
                                            Arrays.sort(temp_transition_split);

                                            for (int l = 0; l < temp_transition_split.length; l++) {
                                                if (!temp_transition_arr[x].contains(temp_transition_split[l])) {
                                                    temp_transition_arr[x] += temp_transition_split[l];
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Adds unions of combined states into DFA transitions
                        for (int x = 0; x < alphabets.size(); x++) {
                            char[] temp_transition_chars = temp_transition_arr[x].toCharArray();
                            Arrays.sort(temp_transition_chars);
                            String sorted_transition = new String(temp_transition_chars);

                            dfa_transitions.set(transition_counter + x, sorted_transition);

                            // Adds dead state transitions
                            if (dfa_transitions.get(transition_counter + x).equals("")) {
                                dfa_transitions.set(transition_counter + x, "q");
                            }
                        }

                        transition_counter += alphabets.size();
                    }
                }

                // Create 2D String Array DFA table from DFA states and alphabet sizes
                String[][] dfa = new String[dfa_states.size()][alphabets.size()+1];
                transition_counter = 0;

                // DFA[0][0] is not used for states and alphabets
                dfa[0][0] = "DFA_TABLE";

                // Assign alphabets to DFA[][alphabets]
                for (int i = 0; i < alphabets.size(); i++) {
                    dfa[0][i+1] = alphabets.get(i);
                }

                // Assign states to NFA[states][]
                for (int i = 1; i < dfa_states.size(); i++) {
                    dfa[i][0] = dfa_states.get(i);
                    for (int j = 0; j < alphabets.size(); j++) {
                        dfa[i][j+1] = dfa_transitions.get(transition_counter);
                        transition_counter++;
                    }
                }

                // DFA output to console
                System.out.println("ALPHABET");
                for (int i = 0; i < alphabets.size(); i++) {
                    System.out.println(dfa[0][i+1]);
                }
                System.out.println("STATES");
                for (int i = 0; i < dfa.length-1; i++) {
                    System.out.println(dfa[i+1][0]);
                }
                System.out.println("START");
                System.out.println(start_state);
                System.out.println("FINAL");
                for (int i = 0; i < dfa.length-1; i++) {
                    for (String state : final_state_split) {
                        if(dfa[i+1][0].contains(state)) {
                            System.out.println(dfa[i+1][0]);
                            break;
                        }
                    }
                }
                System.out.println("TRANSITIONS");

                for (int i = 0; i < dfa.length-1; i++) {
                    for (int k = 0; k < alphabets.size(); k++) {
                        System.out.print(dfa[i+1][0] + " " + dfa[0][k+1] + " " + dfa[i+1][k+1]);
                        System.out.println("");
                    }
                }

                System.out.print("END");

            } else {
                System.out.println("Text file does not match the NFA text format.");
            }
    }
}

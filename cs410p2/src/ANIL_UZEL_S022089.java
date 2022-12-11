import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ANIL_UZEL_S022089 {
    public static void main(String[] args) throws IOException {

        /** CHANGE CFG FILE NAME FROM HERE **/
        String input = "G3.txt";

        /** DEFAULT FILE READ METHOD. CFG.TXT AND JAVA CLASS MUST BE IN THE SAME FOLDER **/
        URL path = ANIL_UZEL_S022089.class.getResource(input);
        if(path==null) {
            System.out.println("File not found. 'CFG.txt' files must be in the same folder as 'ANIL_UZEL_S022089.java'. You can also try the other method.");
        }
        File f = new File(path.getFile());

        /** IF IT DOESN'T WORK COMMENT OUT LINES 16-20 AND TRY THIS **/
        // File f = new File(input);

        /** OR THIS IF YOU WANT TO GIVE IT A FULL FILE PATH **/
        // String fullpath = "ENTER PATH HERE";
        // File f = new File(fullpath);

        Scanner reader = new Scanner(new FileReader(f));

        ArrayList<String> nonterminal = new ArrayList<>();
        ArrayList<String> terminal = new ArrayList<>();
        ArrayList<String> temp_rules = new ArrayList<>();

        // Get line 'NON-TERMINAL'
        String line = reader.nextLine();

        if (line.equals("NON-TERMINAL")) {

            // Skip line 'NON-TERMINAL'
            line = reader.nextLine();

            while (reader.hasNextLine()) {
                if (line.equals("TERMINAL")) {
                    // Get line 'TERMINAL'
                    line = reader.nextLine();
                    break;
                }
                else {
                    // Add NON-TERMINAL lines to ArrayList nonterminal
                    nonterminal.add(line);
                    line = reader.nextLine();
                }
            }

            while (reader.hasNextLine()) {
                if (line.equals("RULES")) {
                    // Get line 'RULES'
                    line = reader.nextLine();
                    break;
                }
                else {
                    // Add TERMINAL lines to ArrayList terminal
                    terminal.add(line);
                    line = reader.nextLine();
                }
            }

            while (reader.hasNextLine()) {
                if (line.equals("START")) {
                    // Get line 'START'
                    line = reader.nextLine();
                    break;
                }
                else {
                    // Add RULES lines to ArrayList temp_rules
                    temp_rules.add(line);
                    line = reader.nextLine();
                }
            }

            // Assign START symbol
            String start_symbol = line;

            // End of file
            reader.close();

            // Elimination of start symbol on the right-hand side
            // If start symbol is found on right-hand side, new start symbol will be assigned to '$'
            for (int i = 0; i < temp_rules.size(); i++) {
                if (temp_rules.get(i).split(":")[1].contains(start_symbol)){
                    String new_start_symbol = "$";
                    nonterminal.add(0, new_start_symbol);
                    temp_rules.add(0, new_start_symbol + ":" + start_symbol);
                    start_symbol = new_start_symbol;
                    break;
                }
            }

            // Refactor Rules
            // Add all rules of a non-terminal to a single string
            ArrayList<String> rules = new ArrayList<>();
            for (int i = 0; i < nonterminal.size(); i++) {
                rules.add("");
                for (int k = 0; k < temp_rules.size(); k++) {
                    if (temp_rules.get(k).split(":")[0].equals(nonterminal.get(i))) {
                        String temp = rules.get(i);
                        if (!temp.equals("")) {
                            temp += "|";
                        }
                        temp += temp_rules.get(k).split(":")[1];
                        rules.set(i, temp);
                    }
                }
            }

            // Removal of useless productions
            ArrayList<Integer> removalIndexes = new ArrayList<>();
            for (int i = 0; i < nonterminal.size(); i++) {
                boolean isUseless = true;
                String selected_symbol = nonterminal.get(i);
                if (!selected_symbol.equals(start_symbol)) {
                    for (int k = 0; k < rules.size(); k++) {
                        for (String rule : rules.get(k).split("\\|")) {
                            if (rule.contains(selected_symbol) && i != k) {
                                isUseless = false;
                                break;
                            }
                        }
                    }
                    if(isUseless) {
                        removalIndexes.add(i);
                    }
                }
            }
            if (removalIndexes.size() != 0) {
                for (int i = 0; i < removalIndexes.size(); i++) {
                    int index = removalIndexes.get(i);
                    index -= i;
                    nonterminal.remove(index);
                    rules.remove(index);
                }
            }

            // Removal of null productions
            for (int i = 0; i < rules.size(); i++) {
                if (rules.get(i).contains("e")) {
                    String null_symbol = nonterminal.get(i);

                    // Remove the rule with 'e' from non-terminal
                    String temp = "";
                    for (String s : rules.get(i).split("\\|")) {
                        if(!s.equals("e")){
                            temp += (s + "|");
                        }
                    }
                    rules.set(i, temp.substring(0,temp.length()-1));

                    // Remove non-terminal with epsilon occurrences from each rule
                    for (int j = 0; j < rules.size(); j++) {
                        if (rules.get(j).contains(null_symbol)) {
                            ArrayList<String> splitRule = new ArrayList<>();
                            splitRule.addAll(List.of(rules.get(j).split("\\|")));
                            for (int ruleIndex = 0; ruleIndex < splitRule.size(); ruleIndex++) {
                                if(splitRule.get(ruleIndex).contains(null_symbol)){
                                    for (int k = 0; k < splitRule.get(ruleIndex).length(); k++) {
                                        String compare = String.valueOf(splitRule.get(ruleIndex).charAt(k));
                                        if (compare.equals(null_symbol)) {
                                            StringBuilder sb = new StringBuilder(splitRule.get(ruleIndex));
                                            if (String.valueOf(sb).equals(null_symbol)) {
                                                sb.setCharAt(0, 'e');
                                            }
                                            else {
                                                sb.deleteCharAt(k);
                                            }
                                            if (!splitRule.contains(String.valueOf(sb))) {
                                                splitRule.add(String.valueOf(sb));
                                                ruleIndex = 0;
                                            }
                                        }
                                    }
                                }
                            }
                            String combineRule = "";
                            for (int combineIndex = 0; combineIndex < splitRule.size(); combineIndex++) {
                                if (combineIndex + 1 == splitRule.size()) {
                                    combineRule += splitRule.get(combineIndex);
                                }
                                else {
                                    combineRule += splitRule.get(combineIndex) + "|";
                                }
                            }
                            rules.remove(j);
                            rules.add(j, combineRule);
                            i = 0;
                        }
                    }
                }
            }

            // Removal of unit productions
            // Might need to re-run more than once depending on the CFG
            boolean run = true;
            while (run) {
                run = false;
                for (int i = 0; i < nonterminal.size(); i++) {
                    String unit_symbol = nonterminal.get(i);
                    for (int j = 0; j < rules.size(); j++) {
                        ArrayList<String> splitRule = new ArrayList<>();
                        splitRule.addAll(List.of(rules.get(j).split("\\|")));
                        if (splitRule.contains(unit_symbol)) {
                            ArrayList<String> splitUnitRule = new ArrayList<>();
                            splitUnitRule.addAll(List.of(rules.get(i).split("\\|")));
                            for (int k = 0; k < splitRule.size(); k++) {
                                if (splitRule.get(k).equals(unit_symbol)) {
                                    splitRule.remove(k);
                                    if (i!=j) {
                                        for (String rule : splitUnitRule) {
                                            if (!splitRule.contains(rule)) {
                                                splitRule.add(rule);
                                            }
                                        }
                                    }
                                }
                            }
                            String combineRule = "";
                            for (int combineIndex = 0; combineIndex < splitRule.size(); combineIndex++) {
                                if (combineIndex + 1 == splitRule.size()) {
                                    combineRule += splitRule.get(combineIndex);
                                }
                                else {
                                    combineRule += splitRule.get(combineIndex) + "|";
                                }
                            }
                            rules.remove(j);
                            rules.add(j, combineRule);
                            run = true;
                        }
                    }
                }
            }

            // Removal of productions with more than non-single terminals
            // Might need to re-run in case rules contain more than one terminal
            run = true;
            while (run) {
                run = false;
                for (int i = 0; i < rules.size(); i++) {
                    ArrayList<String> splitRule = new ArrayList<>();
                    splitRule.addAll(List.of(rules.get(i).split("\\|")));
                    for (int k = 0; k < splitRule.size(); k++) {
                        if (splitRule.get(k).length() > 1 && terminal.stream().anyMatch(splitRule.get(k)::contains)) {
                            StringBuilder sb = new StringBuilder(splitRule.get(k));
                            for (int l = 0; l < sb.length(); l++) {
                                if (nonterminal.contains(String.valueOf(sb.charAt(l)))){
                                    sb.deleteCharAt(l);
                                    l--;
                                }
                            }
                            while (sb.length() > 1) {
                                sb.deleteCharAt(0);
                                run = true;
                            }
                            if (!rules.contains(String.valueOf(sb))) {
                                for (int charIndex = 65; charIndex <= 90; charIndex++) {
                                    String new_symbol = Character.toString(charIndex);
                                    if (!nonterminal.contains(new_symbol)) {
                                        nonterminal.add(new_symbol);
                                        rules.add(String.valueOf(sb));
                                        break;
                                    }
                                }
                            }
                            int symbolIndex = rules.indexOf(String.valueOf(sb));
                            splitRule.set(k, splitRule.get(k).replace(String.valueOf(sb), nonterminal.get(symbolIndex)));
                            String combineRule = "";
                            for (int combineIndex = 0; combineIndex < splitRule.size(); combineIndex++) {
                                if (combineIndex + 1 == splitRule.size()) {
                                    combineRule += splitRule.get(combineIndex);
                                }
                                else {
                                    combineRule += splitRule.get(combineIndex) + "|";
                                }
                            }
                            if (!nonterminal.get(i).equals(combineRule)) {
                                rules.remove(i);
                                rules.add(i, combineRule);
                            }
                        }
                    }
                }
            }

            // Removal of productions with more than two variables
            // Might need to re-run depending on the length of rules
            run = true;
            while (run) {
                for (int i = 0; i < rules.size(); i++) {
                    run = false;
                    ArrayList<String> splitRule = new ArrayList<>();
                    splitRule.addAll(List.of(rules.get(i).split("\\|")));
                    for (int k = 0; k < splitRule.size(); k++) {
                        if (splitRule.get(k).length() > 2 && splitRule.get(k).toUpperCase().equals(splitRule.get(k))) {
                            run = true;
                            String temp = splitRule.get(k);
                            temp = temp.substring(0,2);
                            if (!rules.contains(temp)) {
                                for (int charIndex = 65; charIndex <= 90; charIndex++) {
                                    String new_symbol = Character.toString(charIndex);
                                    if (!nonterminal.contains(new_symbol)) {
                                        nonterminal.add(new_symbol);
                                        rules.add(temp);
                                        break;
                                    }
                                }
                            }
                            int symbolIndex = rules.indexOf(temp);
                            splitRule.set(k, splitRule.get(k).replace(temp, nonterminal.get(symbolIndex)));
                            String combineRule = "";
                            for (int combineIndex = 0; combineIndex < splitRule.size(); combineIndex++) {
                                if (combineIndex + 1 == splitRule.size()) {
                                    combineRule += splitRule.get(combineIndex);
                                }
                                else {
                                    combineRule += splitRule.get(combineIndex) + "|";
                                }
                            }
                            if (!nonterminal.get(i).equals(combineRule)) {
                                rules.remove(i);
                                rules.add(i, combineRule);
                            }
                            k = 0;
                        }
                    }
                }
            }

            // CNF output to console
            System.out.println("NON-TERMINAL");
            for (int i = 0; i < nonterminal.size(); i++) {
                System.out.println(nonterminal.get(i));
            }
            System.out.println("TERMINAL");
            for (int i = 0; i < terminal.size(); i++) {
                System.out.println(terminal.get(i));
            }
            System.out.println("RULES");
            for (int i = 0; i < rules.size(); i++) {
                ArrayList<String> splitRule = new ArrayList<>();
                splitRule.addAll(List.of(rules.get(i).split("\\|")));
                for (String s : splitRule) {
                    System.out.println(nonterminal.get(i) + ":" + s);
                }
            }
            System.out.println("START");
            System.out.println(start_symbol);

        }
        else {
            System.out.println("Text file does not match the CFG text format.");
        }
    }
}

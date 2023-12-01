import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class tomasulo {
    // arraylist of instructions to add instrucyions read from file
    public ArrayList<instruction> instructions = new ArrayList<instruction>();

    public void readInstructions() {
        String fileName = "src\\instructions.txt";
        String line = null;

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                String[] tokens = line.split(" ");
                String type = tokens[0];
                String i = tokens[1];

                if (type.equals("L.D") || type.equals("S.D")) {
                    int value = Integer.parseInt(tokens[2]);
                    instruction inst = new instruction(type, i, value);
                    instructions.add(inst);
                } else if (type.equals("ADDI") || type.equals("SUBI")) {
                    String j = tokens[2];
                    int value = Integer.parseInt(tokens[3]);
                    instruction inst = new instruction(type, i, j, value);
                    // TODO: Add the instruction to the appropriate reservation station
                } else {
                    String j = tokens[2];
                    String k = tokens[3];
                    instruction inst = new instruction(type, i, j, k);
                    // TODO: Add the instruction to the appropriate reservation station
                }
            }

            bufferedReader.close();
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }

    public static void main(String[] args) throws Exception {
        readInstructions();
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class tomasulo {
    // arraylist of instructions to add instrucyions read from file
    public ArrayList<instruction> program = new ArrayList<instruction>();
    int cycle = 0;
    int addLatency = 5;
    int subLatency = 5;
    int mulLatency = 5;
    int divLatency = 5;
    int ldLatency = 5;
    int sdLatency = 5;
    int numaddBuffers = 5;
    int nummulBuffers = 5;
    int numLoadBuffers = 5;
    int numStoreBuffers = 5;
    reservationStation[] addBuffers;
    reservationStation[] mulBuffers;
    loadBuffer[] loadBuffers;
    storeBuffer[] storeBuffers;
    registerFile registerFile = new registerFile();
    int ADDI = 1;
    int SUBI = 1;
    int BNEZ = 1;
    int cycleCount = 0;

    public void readInstructions() {
        String fileName = "src\\instructions.txt";
        String line = null;

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(" ");
                String type = tokens[0];
                String i = tokens[1];

                if (type.equals("L.D") || type.equals("S.D")) {
                    int value = Integer.parseInt(tokens[2]);
                    instruction inst = new instruction(type, i, value);
                    program.add(inst);
                } else if (type.equals("ADDI") || type.equals("SUBI")) {
                    String j = tokens[2];
                    int value = Integer.parseInt(tokens[3]);
                    instruction inst = new instruction(type, i, j, value);
                    program.add(inst);
                } else {
                    String j = tokens[2];
                    String k = tokens[3];
                    instruction inst = new instruction(type, i, j, k);
                    program.add(inst);
                }
            }
            bufferedReader.close();
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }

    public tomasulo() {
        readInstructions();
        // Scanner sc = new Scanner(System.in);
        // System.out.println("Enter the latency of ADD instruction: ");
        // int addLatency = sc.nextInt();
        // this.addLatency = addLatency;
        // System.out.println("Enter the latency of SUB instruction: ");
        // int subLatency = sc.nextInt();
        // this.subLatency = subLatency;
        // System.out.println("Enter the latency of MUL instruction: ");
        // int mulLatency = sc.nextInt();
        // this.mulLatency = mulLatency;
        // System.out.println("Enter the latency of DIV instruction: ");
        // int divLatency = sc.nextInt();
        // this.divLatency = divLatency;
        // System.out.println("Enter the latency of LD instruction: ");
        // int ldLatency = sc.nextInt();
        // this.ldLatency = ldLatency;
        // System.out.println("Enter the latency of SD instruction: ");
        // int sdLatency = sc.nextInt();
        // this.sdLatency = sdLatency;
        // System.out.println("Enter the number of ADD reservation stations: ");
        // int numaddBuffers = sc.nextInt();
        // this.numaddBuffers = numaddBuffers;
        this.addBuffers = new reservationStation[numaddBuffers];
        for (int i = 0; i < numaddBuffers; i++) {
            addBuffers[i] = new reservationStation();
            addBuffers[i].tag = "A" + i + 1;
        }
        // System.out.println("Enter the number of MUL reservation stations: ");
        // int nummulBuffers = sc.nextInt();
        // this.nummulBuffers = nummulBuffers;
        this.mulBuffers = new reservationStation[nummulBuffers];
        for (int i = 0; i < nummulBuffers; i++) {
            mulBuffers[i] = new reservationStation();
            mulBuffers[i].tag = "M" + i + 1;
        }
        // System.out.println("Enter the number of LD reservation stations: ");
        // int numLoadBuffers = sc.nextInt();
        // this.numLoadBuffers = numLoadBuffers;
        this.loadBuffers = new loadBuffer[numLoadBuffers];
        for (int i = 0; i < numLoadBuffers; i++) {
            loadBuffers[i] = new loadBuffer();
            loadBuffers[i].tag = "L" + i + 1;
        }
        // System.out.println("Enter the number of SD reservation stations: ");
        // int numStoreBuffers = sc.nextInt();
        // this.numStoreBuffers = numStoreBuffers;
        this.storeBuffers = new storeBuffer[numStoreBuffers];
        for (int i = 0; i < numStoreBuffers; i++) {
            storeBuffers[i] = new storeBuffer();
            storeBuffers[i].tag = "S" + i + 1;
        }
    }

    public void printTomasuloDetails() {
        System.out.println("ADD Latency: " + addLatency);
        System.out.println("SUB Latency: " + subLatency);
        System.out.println("MUL Latency: " + mulLatency);
        System.out.println("DIV Latency: " + divLatency);
        System.out.println("LD Latency: " + ldLatency);
        System.out.println("SD Latency: " + sdLatency);
        System.out.println("Number of ADD reservation stations: " + numaddBuffers);
        System.out.println("Number of MUL reservation stations: " + nummulBuffers);
        System.out.println("Number of LD reservation stations: " + numLoadBuffers);
        System.out.println("Number of SD reservation stations: " + numStoreBuffers);
    }

    public void issue() {
        if (program.size() == 0) {
            return;
        }
        instruction inst = program.get(0);
        String type = inst.type;
        if (type.equals("ADD.D") || type.equals("SUB.D") || type.equals("ADDI") || type.equals("SUBI")
                || type.equals("DADD") || type.equals("DSUB")
                || type.equals("DADDI") || type.equals("DSUBI")) {
            for (int i = 0; i < numaddBuffers; i++) {
                if (addBuffers[i].isempty()) {
                    if (!type.equals("DADDI") && !type.equals("DSUBI")) {
                        String firstOperand = inst.j;
                        String secondOperand = inst.k;
                        int firstOperandIndex = Integer.parseInt(firstOperand.substring(1));
                        int secondOperandIndex = Integer.parseInt(secondOperand.substring(1));
                        int resultIndex = Integer.parseInt(inst.i.substring(1));
                        if (registerFile.registers[firstOperandIndex].busy == false
                                && registerFile.registers[secondOperandIndex].busy == false) {

                                    //todo: check the register file for the value of the operands and if it is bust i will put the tag of the reservation station in the Qi and Qj
                            addBuffers[i].opcode = type;
                            addBuffers[i].Qi = null;
                            addBuffers[i].Qj = null;
                            addBuffers[i].busy = true;
                            addBuffers[i].Vi = Integer.parseInt(registerFile.registers[firstOperandIndex].Qi);
                            addBuffers[i].Vj = Integer.parseInt(registerFile.registers[secondOperandIndex].Qi);
                            registerFile.registers[resultIndex].Qi = addBuffers[i].tag;
                            registerFile.registers[resultIndex].busy = true;
                            inst.issue = cycle;
                            return;
                        }
                    } else {
                        // ADDI or SUBI
                        String firstOperand = inst.j;
                        int firstOperandIndex = Integer.parseInt(firstOperand.substring(1));
                        int resultIndex = Integer.parseInt(inst.i.substring(1));
                        int value = Integer.parseInt(inst.k);
                        if (registerFile.registers[firstOperandIndex].busy == false) {
                            addBuffers[i].opcode = type;
                            addBuffers[i].Qi = null;
                            addBuffers[i].Qj = null;
                            addBuffers[i].busy = true;
                            addBuffers[i].Vi = Integer.parseInt(registerFile.registers[firstOperandIndex].Qi);
                            addBuffers[i].Vj = value;
                            registerFile.registers[resultIndex].Qi = addBuffers[i].tag;
                            inst.issue = cycle;
                            registerFile.registers[resultIndex].busy = true;

                            return;
                        }
                    }
                }
                program.remove(0);
            }
        }

    }
}

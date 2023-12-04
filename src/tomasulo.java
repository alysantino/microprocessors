import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class tomasulo {
    // arraylist of instructions to add instrucyions read from file
    public ArrayList<instruction> program = new ArrayList<instruction>();
    ArrayList<instruction> issued = new ArrayList<instruction>();
    ArrayList<instruction> toBeExecuted = new ArrayList<instruction>();
    ArrayList<instruction> executed = new ArrayList<instruction>();
    ArrayList<instruction> toBeWritten = new ArrayList<instruction>();

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
    cache cache;
    registerFile registerFile = new registerFile();
    int ADDILatency = 1;
    int BNEZLatency = 1;
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
        cache = new cache(100);
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

    // Your simulator should include ALU ops (FP adds, subs, multiply,
    // divide), (integer ADDI or SUBI needed for loops), loads and stores
    // and branches (BNEZ is enough).
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
                        if (registerFile.registers[firstOperandIndex].busy == false) {
                            addBuffers[i].Qi = null;
                            addBuffers[i].Vi = Integer.parseInt(registerFile.registers[firstOperandIndex].Qi);
                        } else {
                            addBuffers[i].Qi = registerFile.registers[firstOperandIndex].Qi;
                            addBuffers[i].Vi = 0;
                        }
                        if (registerFile.registers[secondOperandIndex].busy == false) {
                            addBuffers[i].Qj = null;
                            addBuffers[i].Vj = Integer.parseInt(registerFile.registers[secondOperandIndex].Qi);
                        } else {
                            addBuffers[i].Qj = registerFile.registers[secondOperandIndex].Qi;
                            addBuffers[i].Vj = 0;
                        }
                        addBuffers[i].opcode = type;
                        addBuffers[i].busy = true;
                        // make conditional statment for type if DADD make the addbuffer[i].time =
                        // addlatnecy if SUBI make the addbuffer[i].time = sublatency
                        addBuffers[i].time = type.equals("DADD") ? addLatency : subLatency;
                        registerFile.registers[resultIndex].Qi = addBuffers[i].tag;
                        registerFile.registers[resultIndex].busy = true;
                        inst.issue = cycle;
                        inst.tag = addBuffers[i].tag;
                    } else {
                        // ADDI or SUBI
                        String firstOperand = inst.j;
                        int firstOperandIndex = Integer.parseInt(firstOperand.substring(1));
                        int resultIndex = Integer.parseInt(inst.i.substring(1));
                        int value = Integer.parseInt(inst.k);
                        if (registerFile.registers[firstOperandIndex].busy == false) {
                            addBuffers[i].Qi = null;
                            addBuffers[i].Vi = Integer.parseInt(registerFile.registers[firstOperandIndex].Qi);
                        } else {
                            addBuffers[i].Qi = registerFile.registers[firstOperandIndex].Qi;
                            addBuffers[i].Vi = 0;
                        }
                        addBuffers[i].opcode = type;
                        addBuffers[i].Qj = null;
                        addBuffers[i].Vj = value;
                        registerFile.registers[resultIndex].Qi = addBuffers[i].tag;
                        addBuffers[i].busy = true;
                        addBuffers[i].time = ADDILatency;
                        registerFile.registers[resultIndex].busy = true;
                        inst.issue = cycle;
                        inst.tag = addBuffers[i].tag;
                    }
                } else {
                    System.out.println("add buffer is full");
                    return;
                }
            }
        }
        if (type.equals("MUL.D") || type.equals("DIV.D")) {
            for (int i = 0; i < nummulBuffers; i++) {
                if (mulBuffers[i].isempty()) {
                    String firstOperand = inst.j;
                    String secondOperand = inst.k;
                    int firstOperandIndex = Integer.parseInt(firstOperand.substring(1));
                    int secondOperandIndex = Integer.parseInt(secondOperand.substring(1));
                    int resultIndex = Integer.parseInt(inst.i.substring(1));
                    if (registerFile.registers[firstOperandIndex].busy == false) {
                        mulBuffers[i].Qi = null;
                        mulBuffers[i].Vi = Integer.parseInt(registerFile.registers[firstOperandIndex].Qi);
                    } else {
                        mulBuffers[i].Qi = registerFile.registers[firstOperandIndex].Qi;
                        mulBuffers[i].Vi = 0;
                    }
                    if (registerFile.registers[secondOperandIndex].busy == false) {
                        mulBuffers[i].Qj = null;
                        mulBuffers[i].Vj = Integer.parseInt(registerFile.registers[secondOperandIndex].Qi);
                    } else {
                        mulBuffers[i].Qj = registerFile.registers[secondOperandIndex].Qi;
                        mulBuffers[i].Vj = 0;
                    }
                    mulBuffers[i].opcode = type;
                    mulBuffers[i].busy = true;
                    registerFile.registers[resultIndex].Qi = mulBuffers[i].tag;
                    registerFile.registers[resultIndex].busy = true;
                    mulBuffers[i].time = type.equals("MUL.D") ? mulLatency : divLatency;
                    inst.issue = cycle;
                    inst.tag = mulBuffers[i].tag;
                } else {
                    System.out.println("mul buffer is full");
                    return;
                }
            }
        }
        if (type.equals("L.D")) {
            for (int i = 0; i < numLoadBuffers; i++) {
                if (loadBuffers[i].isempty()) {
                    String firstOperand = inst.i;
                    int address = inst.value;
                    loadBuffers[i].address = address;
                    loadBuffers[i].busy = true;
                    loadBuffers[i].time = ldLatency;
                    registerFile.registers[Integer.parseInt(firstOperand.substring(1))].Qi = loadBuffers[i].tag;
                    registerFile.registers[Integer.parseInt(firstOperand.substring(1))].busy = true;
                    inst.issue = cycle;
                    inst.tag = loadBuffers[i].tag;
                } else {
                    System.out.println("load buffer is full");
                    return;
                }
            }
        }
        if (type.equals("S.D")) {
            for (int i = 0; i < numStoreBuffers; i++) {
                if (storeBuffers[i].isempty()) {
                    String firstOperand = inst.i;
                    int address = inst.value;
                    storeBuffers[i].address = address;
                    storeBuffers[i].busy = true;
                    storeBuffers[i].time = sdLatency;
                    if (registerFile.registers[Integer.parseInt(firstOperand.substring(1))].busy == true) {
                        storeBuffers[i].Q = registerFile.registers[Integer.parseInt(firstOperand.substring(1))].Qi;
                    } else {
                        storeBuffers[i].V = Integer
                                .parseInt(registerFile.registers[Integer.parseInt(firstOperand.substring(1))].Qi);
                    }
                    inst.issue = cycle;
                    inst.tag = storeBuffers[i].tag;
                } else {
                    System.out.println("store buffer is full");
                    return;
                }
            }
        }
        // todo: BNEZ
        program.remove(0);
        issued.add(inst);
    }

    public void execute() {
        for (reservationStation buffer : addBuffers) {
            if (buffer.busy == true) {
                if (buffer.time == 1) {
                    // get the instruction from the executed array
                    for (instruction instruction : executed) {
                        if (instruction.tag.equals(buffer.tag)) {
                            instruction.executionComplete = cycle;
                            logicExecute(buffer, instruction);
                            buffer.time--;
                        }
                    }
                    buffer.busy = false;
                    buffer.deleteStation();
                } else {
                    if (buffer.Qi == null && buffer.Qj == null) {
                        buffer.time--;
                    }
                }
            }
        }
        for (reservationStation buffer : mulBuffers) {
            if (buffer.busy == true) {
                if (buffer.time == 1) {
                    // get the instruction from the executed array
                    for (instruction instruction : executed) {
                        if (instruction.tag.equals(buffer.tag)) {
                            instruction.executionComplete = cycle;
                            logicExecute(buffer, instruction);
                            buffer.time--;
                        }
                    }
                    buffer.busy = false;
                    buffer.deleteStation();
                } else {
                    if (buffer.Qi == null && buffer.Qj == null) {
                        buffer.time--;
                    }
                }
            }
        }
        for (loadBuffer buffer : loadBuffers) {
            if (buffer.busy == true) {
                if (buffer.time == 1) {
                    // get the instruction from the executed array
                    for (instruction instruction : executed) {
                        if (instruction.tag.equals(buffer.tag)) {
                            instruction.executionComplete = cycle;
                            instruction.writeResult = cycle+1;
                            instruction.value = cache.read(buffer.address);
                            buffer.time--;
                        }
                    }
                    buffer.busy = false;
                    buffer.deleteBuffer();
                } else {
                    buffer.time--;
                }
            }
        }

        for (storeBuffer buffer : storeBuffers) {
            if (buffer.busy == true) {
                if (buffer.time == 1) {
                    // get the instruction from the executed array
                    for (instruction instruction : executed) {
                        if (instruction.tag.equals(buffer.tag)) {
                            instruction.executionComplete = cycle;
                            instruction.writeResult = cycle+1;
                            cache.write(buffer.address, buffer.V);
                            buffer.time--;
                        }
                    }
                    buffer.busy = false;
                    buffer.deleteBuffer();
                } else {
                    buffer.time--;
                }
            }
        }
    }

    public void writeBack() {
        // loop on all buffers if the time is 0 then i will remove it from the array and
        // add it to the to be written array and publish its result to the register file
        // and the bus
    }

    private void logicExecute(reservationStation s, instruction inst) {
        // reservationStation is respsnisble for executing the instruction ADD.D or
        // SUB.D or ADDI or SUBI or DADD or DSUB or DADDI or DSUBI or DIV.D or MUL.D
        // so i need to check on the opcode and then i will know what to do on the vi
        // and vj and the time
        if (s.opcode.equals("ADD.D")) {
            inst.value = s.Vi + s.Vj;
        } else if (s.opcode.equals("SUB.D")) {
            inst.value = s.Vi - s.Vj;
        } else if (s.opcode.equals("ADDI")) {
            inst.value = s.Vi + s.Vj;
        } else if (s.opcode.equals("SUBI")) {
            inst.value = s.Vi - s.Vj;
        } else if (s.opcode.equals("DADD")) {
            inst.value = s.Vi + s.Vj;
        } else if (s.opcode.equals("DSUB")) {
            inst.value = s.Vi - s.Vj;
        } else if (s.opcode.equals("DADDI")) {
            inst.value = s.Vi + s.Vj;
        } else if (s.opcode.equals("DSUBI")) {
            inst.value = s.Vi - s.Vj;
        } else if (s.opcode.equals("DIV.D")) {
            inst.value = s.Vi / s.Vj;
        } else if (s.opcode.equals("MUL.D")) {
            inst.value = s.Vi * s.Vj;
        }

    }
    //in the running function i need to check on all instructions time and check with clock if it need to be executed or not
}

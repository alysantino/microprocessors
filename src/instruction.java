public class instruction {
    public String type;
    public String i;
    public String j;
    public String k;
    public int issue;
    public int executionComplete;
    public int writeResult;
    public int value;

    public instruction(String type, String i, String j, String k) {
        this.type = type;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public instruction(String type, String i, String j, int value) {
        this.type = type;
        this.i = i;
        this.j = j;
        this.value = value;
    }

    public instruction(String type, String i, int value) {
        this.type = type;
        this.i = i;
        this.value = value;

    }
    //toString method for debugging
    @Override
    public String toString() {
        //print appriopriate instruction based on type
        if (type.equals("L.D") || type.equals("S.D")) {
            return type + " " + i + " " + value;
        } else if (type.equals("ADDI") || type.equals("SUBI")) {
            return type + " " + i + " " + j + " " + value;
        } else {
            return type + " " + i + " " + j + " " + k;
        }
    }

}

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

}

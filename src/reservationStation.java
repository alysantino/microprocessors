public class reservationStation {
    public String opcode;
    public int Vi;
    public int Vj;
    public String Qi;
    public String Qj;
    public String tag;
    public boolean busy;

    public reservationStation(String opcode, int Vi, int Vj, String Qi, String Qj, String tag, boolean busy) {
        this.opcode = opcode;
        this.Vi = Vi;
        this.Vj = Vj;
        this.Qi = Qi;
        this.Qj = Qj;
        this.tag = tag;
        this.busy = busy;
    }

    public reservationStation() {
        this.opcode = null;
        this.Vi = 0;
        this.Vj = 0;
        this.Qi = null;
        this.Qj = null;
        this.tag = null;
        this.busy = false;
    }

    public void deleteStation() {
        this.opcode = null;
        this.Vi = 0;
        this.Vj = 0;
        this.Qi = null;
        this.Qj = null;
        this.tag = null;
        this.busy = false;
    }
    @Override
    public String toString() {
        return "addStation{" +
                "opcode='" + opcode + '\'' +
                ", Vi=" + Vi +
                ", Vj=" + Vj +
                ", Qi='" + Qi + '\'' +
                ", Qj='" + Qj + '\'' +
                ", tag=" + tag +
                ", busy=" + busy +
                '}';
    }

    public boolean isempty() {
        if (this.busy == false) {
            return true;
        } else {
            return false;
        }
    }
}

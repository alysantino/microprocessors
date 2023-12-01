public class storeBuffer {
    public int address;
    public int V;
    public boolean busy;
    public int tag;
    public String Q;

    public storeBuffer(int address, int V, boolean busy, int tag, String Q) {
        this.address = address;
        this.V = V;
        this.busy = busy;
        this.tag = tag;
        this.Q = Q;
    }

    public void deleteBuffer() {
        this.address = 0;
        this.V = 0;
        this.busy = false;
        this.tag = 0;
        this.Q = null;
    }

    public int getAddress() {
        return address;
    }

    public int getV() {
        return V;
    }

    public boolean isBusy() {
        return busy;
    }

    public int getTag() {
        return tag;
    }

    public String getQ() {
        return Q;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setV(int V) {
        this.V = V;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setQ(String Q) {
        this.Q = Q;
    }

    @Override
    public String toString() {
        return "storeBuffer{" +
                "address=" + address +
                ", V=" + V +
                ", busy=" + busy +
                ", tag=" + tag +
                ", Q='" + Q + '\'' +
                '}';
    }

}

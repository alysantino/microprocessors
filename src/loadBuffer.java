
public class loadBuffer {
    public int address;
    public boolean busy;
    public int tag;

    public loadBuffer(int address) {
        this.address = address;
        this.busy = true;
    }

    public void deleteBuffer() {
        this.address = 0;
        this.busy = false;

    }

    public int getAddress() {
        return address;
    }

    public boolean isBusy() {
        return busy;
    }

    public int getTag() {
        return tag;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }



    // toString method for debugging
    @Override
    public String toString() {
        return "loadBuffer{" +
                "address=" + address +
                ", busy=" + busy +
                ", tag=" + tag +
                '}';
    }
}

public class cache {
    public cacheCell[] cacheCells;

    public class cacheCell {
        public int address;
        public int value;
        public boolean busy;

        public cacheCell(int address, int value) {
            this.address = address;
            this.value = value;
        }
    }

    public cache(int n) {
        this.cacheCells = new cacheCell[n];
        for (int i = 0; i < n; i++) {
            cacheCells[i] = new cacheCell(i, 1);
        }
    }

    public int read(int address) {
        for (int i = 0; i < cacheCells.length; i++) {
            if (cacheCells[i].address == address) {
                return cacheCells[i].value;
            }
        }
        return -1;
    }

    public void write(int address, int value) {
        for (int i = 0; i < cacheCells.length; i++) {
            if (cacheCells[i].address == address) {
                cacheCells[i].value = value;
            }
        }
    }

    public void makeBusy(int address) {
        for (int i = 0; i < cacheCells.length; i++) {
            if (cacheCells[i].address == address) {
                cacheCells[i].busy = true;
            }
        }
    }

    public void makeNotBusy(int address) {
        for (int i = 0; i < cacheCells.length; i++) {
            if (cacheCells[i].address == address) {
                cacheCells[i].busy = false;
            }
        }
    }

    public boolean isBusy(int address) {
        for (int i = 0; i < cacheCells.length; i++) {
            if (cacheCells[i].address == address) {
                return cacheCells[i].busy;
            }
        }
        return false;
    }

}

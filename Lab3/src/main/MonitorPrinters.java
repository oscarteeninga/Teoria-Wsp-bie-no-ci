package main;

public class MonitorPrinters {
    private int size;
    private BoundedBuffer buffer;

    public MonitorPrinters(int size, BoundedBuffer buffer) {
        this.size = size;
        this.buffer = buffer;
    }

    public int order() {
        try {
            return (int) buffer.take();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public void give(int printer_nr) {
        try {
            buffer.put(printer_nr);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

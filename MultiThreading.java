class NumPrinter {
    public void printZero() {
        System.out.print(0);
    }
    public void printEven(int number) {
        System.out.print(number);
    }
    public void printOdd(int number) {
        System.out.print(number);
    }
}
class ThreadController {
    private int n;
    private int state = 0; 
    private final Object lock = new Object();

    public ThreadController(int n) {
        this.n = n;
    }
    public void printZero(NumPrinter printer) {
        for (int i = 1; i <= n; i++) {
            synchronized (lock) {
                while (state != 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                printer.printZero();
                state = (i % 2 == 0) ? 2 : 1; 
                lock.notifyAll();
            }
        }
    }
    public void printEven(NumPrinter printer) {
        for (int i = 2; i <= n; i += 2) {
            synchronized (lock) {
                while (state != 2) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                printer.printEven(i);
                state = 0; 
                lock.notifyAll();
            }
        }
    }
    public void printOdd(NumPrinter printer) {
        for (int i = 1; i <= n; i += 2) {
            synchronized (lock) {
                while (state != 1) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                printer.printOdd(i);
                state = 0; 
                lock.notifyAll();
            }
        }
    }
}
public class MultiThreading {
    public static void main(String[] args) {
        int n = 5;
        NumPrinter printer = new NumPrinter();
        ThreadController controller = new ThreadController(n);
        Thread zeroThread = new Thread(() -> controller.printZero(printer));
        Thread evenThread = new Thread(() -> controller.printEven(printer));
        Thread oddThread = new Thread(() -> controller.printOdd(printer));
        zeroThread.start();
        evenThread.start();
        oddThread.start();
    }
}


// 1. Lazy Initialization
class LazySingleton {
    
    private static LazySingleton instance;
    private LazySingleton() {
        System.out.println("Instance created by: " + Thread.currentThread().getName());
    }

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}

// 2. Thread Safe
class ThreadSafeSingleton {

    private static ThreadSafeSingleton instance;
    private ThreadSafeSingleton() {
        System.out.println("Instance created by: " + Thread.currentThread().getName());
    }

    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }

}

// 3. Double Checked Singleton
class DoubleCheckedSingleton {
    private static volatile DoubleCheckedSingleton instance;

    private DoubleCheckedSingleton() {
        System.out.println("Thread created by: " + Thread.currentThread().getName());
    }

    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) {
            synchronized(DoubleCheckedSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
}

// 4. Eager Initialization
class EagerSingleton {
    private static final EagerSingleton instance = new EagerSingleton();
    
    private EagerSingleton() {
        System.out.println("Thread created by: " + Thread.currentThread().getName());
    }

    public static EagerSingleton getInstance() {
        return instance;
    }
}

// 5. Bill Pugh
class BillPughSigleton {
    private BillPughSigleton() {
        System.out.println("Thread creaded by: " + Thread.currentThread().getName());
    }

    private static class Holder {
        private static final BillPughSigleton HOLDER = new BillPughSigleton();
    }

    public static BillPughSigleton getInstance() {
        return Holder.HOLDER;
    }
}

public class SingletonDesignPattern {
    
    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            BillPughSigleton.getInstance();
        };

        int THREAD_COUNT = 20;
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(task, "Thread-" + (i + 1));
            threads[i].start();
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }

        System.out.println("Main thread finished after all threads completed...");
    }
}

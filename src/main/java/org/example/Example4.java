package org.example;

//Есть объект, в котором 2 счетчика: 1 - long, 2 - Integer. Оба на старте = 0.
//Задача - любыми средствами реализовать инкремент каждой величины в 3 потока по 1000 раз.
//В результате должен получиться объект с 3000 в каждом из полей


public class Example4 {
    public static void main(String[] args) throws InterruptedException {
        //Создаем 3 потока с объектами класса MyRunnable, у которого есть синхронизированный метод increment()
        MyRunnable runnable = new MyRunnable();
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);

        //Запускаем потоки
        thread1.start();
        thread2.start();
        thread3.start();

        //Для продолжения работы main определяем, что ждем, когда потоки завершат свою работу
        thread1.join();
        thread2.join();
        thread3.join();

        //Выводим конечный результат работы
        System.out.println("countInteger = " + Counter.countInteger + ", countLong = " + Counter.countLong);
    }
}

    //Основной статический класс для хранения 2-х переменных countInteger и countLong
    class Counter {
        static Integer countInteger = 0;    //(нужно ли в случае синхронизации по методу использовать ключ слово volatile?)
        static long countLong = 0;          //(нужно ли в случае синхронизации по методу использовать ключ слово volatile?)
    }

    //Класс, который имплементирует интерфейс Runnable и имеет синхронизированный метод
    class MyRunnable implements Runnable {
        //синхронизированный метод изменения переменных countInteger и countLong
        public synchronized void increment() {
            Counter.countInteger++;
            Counter.countLong++;
        }
        //переопределенный метод run
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                increment();
            }
        }
    }
// 1) если ключевое слово synchronized указано, как модификатор метода,
//      то синхронизация идет по монитору экземпляра объекта (this)




//Другой вариант реализации (без отдельного класса)
//public class Example4 {
//    static Integer countInteger = 0;
//    static long countLong = 0;
//
//    public static synchronized void increment() {
//        countInteger++;
//        countLong++;
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        Thread thread1 = new Thread(new MyRunnable());
//        Thread thread2 = new Thread(new MyRunnable());
//        Thread thread3 = new Thread(new MyRunnable());
//
//        thread1.start();
//        thread2.start();
//        thread3.start();
//
//        thread1.join();
//        thread2.join();
//        thread3.join();
//
//        System.out.println("countInteger = " + countInteger + ", countLong = " + countLong);
//
//    }
//}
//class MyRunnable implements Runnable {
//    //переопределенный метод run
//    @Override
//    public void run() {
//        for (int i = 0; i < 1000; i++) {
//            Example4.increment();
//        }
//    }
//}







package org.example;
import static org.example.Example1.ex;

//Создать класс, в котором будет три метода: void first() {sout("first")}, по аналогии second и third.
//Запустить 3 потока, которые по очереди вызывали бы соответствующие своему порядку инициализации методы.
//Работу организовать так, чтобы в консоли был вывод: Third, Second, First

public class Example1 {
    //создаем объект класса Exam, для которого будем вызывать 3 метода (first, second, third)
    public static Exam ex = new Exam();
    public static void main(String[] args) throws InterruptedException {
        //создаем Потоки для объектов классов
        Thread thread1 = new Thread(new MyThead1());
        Thread thread2 = new Thread(new MyThead2());
        Thread thread3 = new Thread(new MyThead3());

        //Вариант 1. Просто запуск в порядке, который нужно и ожидание, пока отработается предыдущий поток, перед запуском следующего
        System.out.println("Вариант 1. Просто запуск в порядке, который нужно и ожидание, " +
                "\nпока отработается предыдущий поток, перед запуском следующего (контролирует запуск метод main):");

        thread3.start();
        thread3.join();

        thread2.start();
        thread2.join();

        thread1.start();
        thread1.join();
        System.out.println();

        //Вариант 2. Присвоение приоритета потокам (Thread.MIN_PRIORITY, Thread.MАХ_PRIORITY, Thread.NORM_PRIORITY)
        //в данном случае потоки должны запускаться в порядке приоритетности вне зависимости, в каком порядке они запущены
        System.out.println("Вариант 2: С присвоением приоритета и запуском в порядке написания в методе." +
                "\n(срабатывает не каждый раз)");

        //Создаем новые объекты Потоков, т.к. нельзя повторно использовать ранее вызванные потоки
        Thread thread4 = new Thread(new MyThead1());
        Thread thread5 = new Thread(new MyThead2());
        Thread thread6 = new Thread(new MyThead3());

        thread4.setPriority(Thread.MIN_PRIORITY);
        thread5.setPriority(Thread.NORM_PRIORITY);
        thread6.setPriority(Thread.MAX_PRIORITY);

        thread4.start();
        thread5.start();
        thread6.start();

    }
}

    class MyThead1 implements Runnable {
        @Override
        public void run() {
            ex.first();
        }
    }
    class MyThead2 implements Runnable {
        @Override
        public void run() {
            ex.second();
        }
    }
    class MyThead3 implements Runnable {
        @Override
        public void run() {
            ex.third();
        }
    }

    class Exam {
        public void first() {System.out.println("First");}
        public void second() {System.out.println("Second");}
        public void third() {System.out.println("Third");}
    }




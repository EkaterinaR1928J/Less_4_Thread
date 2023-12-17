package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

//12 грузовиков подъезжают к КПП для проверки веса автомобиля.
//Всего въездов, весов, и выездов по 5 штук
//(обратить внимание, что лочиться весы и семафор, который отвечает за въезд/выезд, могут по-разному).
//Требуется создать приложение, эмулирющее проведение взвешивания

//В данном решении использованы 2 Семафора: 1 - на въезд/выезд, 2 - на весы

public class Example2 {
    public static void main(String[] args) throws InterruptedException {
        //Создаем 2 семафора: на заезд на КПП и на весы
        Semaphore kpp = new Semaphore(5);
        Semaphore scales = new Semaphore(5);

        //Создаем коллекцию объектов класса Truck (коллекция из 12-ти грузовиков).
        //В аргументы передаем 2 семафора
        List<Truck> truckList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            truckList.add(new Truck(i, kpp, scales));
        }

        //У каждого объекта коллекции грузовиков запускаем метод truck.join(), чтобы main ждал завершения всех потоков
        truckList.stream().forEach(truck -> {
            try {
                truck.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        //После завершения всех потоков выводим итог, что все грузовики проехали
        System.out.println("\nВсе грузовики проехали");
    }
}

//класс Грузовик, который наследует класс Thread (Поток), поэтому надо переопределить метод run
class Truck extends Thread{
    int number;
    private Semaphore kpp;
    private Semaphore scales;

    //в конструкторе сразу создаем объект и запускаем его методом start (вызывает метод run)
    public Truck(int number, Semaphore kpp, Semaphore scales) {
        this.number = number;
        this.kpp = kpp;
        this.scales = scales;
        this.start();           //запускаем поток
    }
    //переопределяем метод run
    public void run() {
        try {
            System.out.println("Грузовик № " + number + " ждет перед КПП");
            kpp.acquire();      //метод acquire() получает разрешение от семафора "КПП". Он заблокирует поток, пока ресурс будет недоступен
            System.out.println("\tГрузовик № " + number + " начал заезд на КПП");
            sleep(1000);        //время на заезд на КПП
            try {
                scales.acquire();        //запрос к семафору "Весы"
                System.out.println("\tГрузовик № " + number + " начал взвешиваться");
                sleep((int) Math.random()*2000);        //время на взвешивание
            } finally {
                scales.release();   //отпускаем семафор "Весы"
            }
            System.out.println("\t\tГрузовик № " + number + " завершил взвешивание и выехал с КПП");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            kpp.release();      //отпускаем семафор "КПП"
        }
    }
}

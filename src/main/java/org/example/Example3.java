package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

//Задача с очередью пациентов: реализовать чтение из файла, сбор объекта и передачу в список объектов класса Patient

public class Example3 {
    //Задаем путь к файлу с данными в строках
    static String file = "src/main/dump.txt";
    //Объявляем объект класса DequeWrapper для создания очереди ArrayDeque<> из строк, прочитанных из файла
    static DequeWrapper newPatientStrings = new DequeWrapper();
    //Объявляем коллекцию для хранения списка пациентов
    static List<Patient> patients = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        //Создаем 2 потока для создания очереди ArrayDeque<> строк из файла и создания коллекции пациентов
        Thread thread1 = new Thread(new PatientString());
        Thread thread2 = new Thread(new Patients());

        //Запускаем потоки
        thread1.start();
        thread2.start();

        //Для выполнения последующих действий в main ждем, когда потоки отработают полностью
        thread1.join();
        thread2.join(5000);     //устанавливаем параметр таймаута в 5 с. для принудительного выхода из программы

        thread1.interrupt();
        thread2.interrupt();

        //Для проверки выведем на экран результаты (длину коллекции пациентов и саму коллекцию):
        System.out.println("Длина списка пациентов = " + patients.size());
        patients.forEach(System.out::println);
    }


    //Класс для создания очереди ArrayDeque<> из строк
    static class PatientString implements Runnable {
        //Т.к. класс имплементирует интерфейс Runnable, надо переопределить метод запуска Потока run
        @Override
        public void run() {

            try (Scanner scanner = new Scanner(new FileReader(file))) {     //создаем объект класса Scanner для чтения
                String newPatientString;
                while (scanner.hasNext()) {                     //Работаем, пока сканер находит следующую строку
                    newPatientString = scanner.nextLine();      //В строковую переменную newPatientString считываем следующую строку
                    newPatientStrings.add(newPatientString);    //Добавляем строку в очередь newPatientString
                    //(при этом метод .add переопределяется в классе DequeWrapper)
                }
                scanner.close();
            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
                System.out.println("Ошибка. Не найден файл.");
            }

        }
    }

    //Класс для создания объекта класса Patient и добавления его в коллекцию patients
    static class Patients implements Runnable {
        @Override
        public void run() {
            while (true) {
                String temp;    //Временная переменная типа String для хранения строки,
                try {           //которая используется конструктором класса Patient
                    temp = newPatientStrings.pop()      //из очереди newPatientStrings методом .pop возвращается с удалением
                            .replaceAll(",", "")    //элемент (у нас - строка) из начала очереди
                            .replaceAll("'", "")    //к возвращенному элементу применяются replaceAll
                            .replace("(", "")       //для удаления лишних символов
                            .replace(")", "");
                    Patient newPatient = new Patient(temp);     //итоговую строку передаем в конструктор класса Patient
                    patients.add(newPatient);                   //новый объект класса Patient добавляем в коллекцию patients
                } catch (InterruptedException e) {
                    System.out.println("Поток чтения из очереди прерван");
                }
            }
        }
    }
}

//Класс для переопределения части методов очереди ArrayDeque<> newPatientStrings
class DequeWrapper {
    final Deque<String> newPatientStrings;

    public DequeWrapper() {
        this.newPatientStrings = new ArrayDeque<>();
    }

    //Синхронизированный метод .add для добавления в очередь ArrayDeque<> newPatientStrings новой строки
    //Метод оповещает другую очередь (в нашем случае очередь на заполнение коллекции пациентов),
    //что строка добавлена
    public synchronized void add(String st) {
        newPatientStrings.add(st);      //добавляет
        notify();                       //оповещает
    }

    //Синхронизированный метод .pop для возвращения с удалением первого элемента очереди для дальнейшей работы
    public synchronized String pop() throws InterruptedException {
        while (newPatientStrings.isEmpty()) {
            wait();                             //Ждет, если в очереди newPatientStrings нет записей, когда они появятся
        }
        return newPatientStrings.pop();         //возвращает элемент из очереди
    }

    public synchronized boolean isEmpty() {     //Метод не используется в текущей реализации
        return newPatientStrings.isEmpty();
    }

}






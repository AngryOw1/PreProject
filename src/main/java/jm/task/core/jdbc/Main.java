package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.service.UserServiceImpl;


public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserServiceImpl usi = new UserServiceImpl();
        usi.createUsersTable();

        //добавляем 4-ех юзеров
        usi.saveUser("Глеб", "Емельянов", (byte) 23);
        usi.saveUser("Алексей", "Самарин", (byte) 32);
        usi.saveUser("Никита", "Бакулин", (byte) 25);
        usi.saveUser("Ильхам", "Салькаев", (byte) 30);

        //Получение всех User из базы и вывод в консоль
        System.out.println(usi.getAllUsers());

        //Удаляем юзера с id = 2 и выводим всех юзеров с учетом изменений
        usi.removeUserById(2);

        System.out.println(usi.getAllUsers());

        //Очищаем таблицу
        usi.cleanUsersTable();

        System.out.println(usi.getAllUsers());
        usi.dropUsersTable();
    }
}

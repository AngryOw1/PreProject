package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.PropertiesUtil;
import jm.task.core.jdbc.util.Util;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection connection = Util.open();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String createTable = """
                CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY ,
                name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                age SMALLINT CHECK (age >0 AND age < 128));
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTable);
        } catch (SQLException e) {
            System.out.println("createUsersTable " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String dropTable = """
                DROP TABLE IF EXISTS users;              
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(dropTable);
        } catch (SQLException e) {
            System.out.println("dropUsersTable " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String insertUser = "INSERT INTO users(name, last_name, age) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUser)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            System.out.printf("User с именем – %S добавлен в базу данных \n", name);
        } catch (SQLException e) {
            System.out.println("saveUser " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String removeById = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(removeById)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("removeById " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        String selectAll = """
                SELECT * 
                FROM users                
                """;

        List<User> userList = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectAll);
            while (resultSet.next()){
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("last_name");
                byte age = resultSet.getByte("age");

                User user = new User(name, lastName, age);
                user.setId(id);
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("getAllUsers " + e.getMessage());
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        String cleanTable = """
                TRUNCATE TABLE users;
                
                ALTER SEQUENCE users_id_seq RESTART WITH 1;
                UPDATE users SET id=nextval('users_id_seq');
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(cleanTable);
        } catch (SQLException e) {
            System.out.println("cleanUsersTable " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

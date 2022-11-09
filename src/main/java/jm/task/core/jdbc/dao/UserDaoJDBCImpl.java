package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection = Util.connectToDB();

    public UserDaoJDBCImpl() {

    }
    @Override
    public void createUsersTable() {
        String tableProp = """
                    create table if not exists user (
                    id int auto_increment primary key,
                    firstName varchar(30),
                    lastName varchar(30),
                    age tinyint
                    );
                    """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(tableProp);
            System.out.println("Successfully created the table");
        } catch (SQLException e) {
            System.out.println("Failed creation the table");
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("drop table if exists User;");
            System.out.println("Successfully removed the table");
        } catch (SQLException e) {
            System.out.println("Failed to remove the table");
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String saveUser = "insert into User (firstName, lastName, age) values (?,?,?);";

        try (PreparedStatement pStatement = connection.prepareStatement(saveUser)) {
            pStatement.setString(1, name);
            pStatement.setString(2, lastName);
            pStatement.setByte(3, age);
            pStatement.execute();
            System.out.println("User " + name + " successfully added");
        } catch (SQLException e) {
            System.out.println("Failed to add the user");
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        String removeUserById= "delete from user where id = ?";

        try (PreparedStatement pStatement = connection.prepareStatement(removeUserById)) {
            pStatement.setLong(1, id);
            pStatement.execute();
            System.out.println("Successfully removed user " + id);
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Failed to remove the user " + id);
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try (ResultSet resultSet = connection.createStatement().executeQuery("select * from User;")) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
                System.out.println(user);
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Failed to obtain users");
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("truncate table User;");
            System.out.println("Successfully clean the table");
        } catch (SQLException e) {
            System.out.println("Failed to clean the table");
            e.printStackTrace();
        }
    }
}

package com.akadatsky.dao;

import com.akadatsky.model.User;
import com.akadatsky.model.UserList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static UserDao instance;
    final String path = "/home/antn/IdeaProjects/userApiSample/src/main/java/com/akadatsky/dao/text.txt";
    File file = new File(path);
    private Gson gson = new Gson();
    private List<User> users = new ArrayList<>();

    private UserDao() {
    }

    public static synchronized UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    public void addUser(User user) {
        users.add(user);
        reWrite();

    }

    public void reWrite() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.append(gson.toJson(users));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public UserList getUsersFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "UTF-8"));
        String line = br.readLine();
        if (line != null) {
            try (final Reader reader = new FileReader(path)) {
                users = gson.fromJson(reader, new TypeToken<List<User>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return new UserList(users);
        }
        return getUsers();

    }


    public UserList getUsers() {
        return new UserList(users);
    }


    public boolean remove(String name) {
        for (User user : users) {
            if (user.getFirstName().equalsIgnoreCase(name)) {
                users.remove(user);
                reWrite();
                return true;
            }
        }
        return false;
    }

    public boolean update(User testUser) {
        for (User user : users) {
            if (user.getFirstName().equalsIgnoreCase(testUser.getFirstName())) {
                user.setAge(testUser.getAge());
                reWrite();
                return true;
            }
        }
        return false;
    }

}

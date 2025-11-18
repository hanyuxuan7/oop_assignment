package data;

import entity.*;
import java.util.Collection;

public interface UserRepository {
    void addUser(User user);
    User getUser(String userID);
    Collection<User> getAllUsers();
    void removeUser(String userID);
    void saveUsers(String filePath);
    void loadUsers(String filePath);
}

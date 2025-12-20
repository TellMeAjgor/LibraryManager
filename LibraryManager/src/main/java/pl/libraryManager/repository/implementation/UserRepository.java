package pl.libraryManager.repository.implementation;

import pl.libraryManager.model.User;
import pl.libraryManager.repository.IUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }

    @Override
    public void save(User user) {
        users.add(user);
    }
}
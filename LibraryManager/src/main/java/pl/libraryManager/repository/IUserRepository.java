package pl.libraryManager.repository;

import pl.libraryManager.model.User;
import java.util.Optional;

public interface IUserRepository {
    Optional<User> findByUsername(String username);
    void save(User user);
}

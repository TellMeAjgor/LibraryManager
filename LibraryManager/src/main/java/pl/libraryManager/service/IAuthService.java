package pl.libraryManager.service;

import pl.libraryManager.model.User;

public interface IAuthService {
    boolean login(String username, String password);
    void logout();
    User getCurrentUser();
}

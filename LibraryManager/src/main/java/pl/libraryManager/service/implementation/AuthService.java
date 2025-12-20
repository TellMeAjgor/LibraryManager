package pl.libraryManager.service.implementation;

import pl.libraryManager.model.User;
import pl.libraryManager.repository.IUserRepository;
import pl.libraryManager.service.IAuthService;
import pl.libraryManager.utils.SecurityUtils;

import java.util.Optional;

public class AuthService implements IAuthService {
    private final IUserRepository userRepository;
    private User currentUser;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            String inputHash = SecurityUtils.hashPassword(password);
            if (userOpt.get().getPasswordHash().equals(inputHash)) {
                this.currentUser = userOpt.get();
                return true;
            }
        }
        return false;
    }

    @Override
    public void logout() {
        this.currentUser = null;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }
}

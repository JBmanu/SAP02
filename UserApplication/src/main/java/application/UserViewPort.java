package application;

import java.util.Optional;

public interface UserViewPort {
    void showError(Optional<ErrorApplication> error);
}

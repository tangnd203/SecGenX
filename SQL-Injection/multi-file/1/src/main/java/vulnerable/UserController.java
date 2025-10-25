package vulnerable;

import java.util.Optional;

// Lớp 1: Nhận input từ "bên ngoài" (ở đây là từ hàm main)
public class UserController {
    private final UserService userService = new UserService();

    public void findUser(String username) {
        System.out.println("[Controller] Nhận được yêu cầu tìm người dùng: " + username);
        Optional<User> user = userService.findUserByName(username);

        if (user.isPresent()) {
            System.out.println("[Controller] Tìm thấy: " + user.get());
        } else {
            System.out.println("[Controller] Không tìm thấy người dùng.");
        }
    }
}
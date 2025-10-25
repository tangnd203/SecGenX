package fixed;

import java.util.Optional;

// Lớp 1: Vai trò không đổi, gọi đến Service đã được sửa lỗi
public class UserControllerFixed {
    private final UserServiceFixed userService = new UserServiceFixed();

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
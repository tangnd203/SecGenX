package fixed;

import java.util.Optional;

// Lớp 2: Vai trò không đổi, gọi đến Repository đã được sửa lỗi
public class UserServiceFixed {
    private final UserRepositoryFixed userRepository = new UserRepositoryFixed();

    public Optional<User> findUserByName(String username) {
        System.out.println("[Service] Đang xử lý logic cho người dùng: " + username);
        return userRepository.queryUser(username);
    }
}
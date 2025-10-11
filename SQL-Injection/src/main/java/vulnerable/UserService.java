package vulnerable;

import java.util.Optional;

// Lớp 2: Xử lý logic nghiệp vụ, chỉ đơn giản là chuyển tiếp yêu cầu
public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public Optional<User> findUserByName(String username) {
        System.out.println("[Service] Đang xử lý logic cho người dùng: " + username);
        // Dữ liệu không an toàn tiếp tục được truyền đi
        return userRepository.queryUser(username);
    }
}
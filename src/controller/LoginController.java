package controller;

import service.LoginService;
import utils.DateHandler;
import view.Register;
import view.Login;
import view.LoginManager;
import view.ViewManager; // <-- Thêm import cho ViewManager
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.awt.EventQueue; // <-- Thêm import cho EventQueue

public class LoginController {
    private Login login;
    private LoginManager loginManager;
    private Register register;
    private LoginService loginService;

    // Constructor cho Login (giữ nguyên)
    public LoginController(Login login, LoginService loginService) {
        this.login = login;
        this.loginService = loginService;
        login.getLoginButton().addActionListener(e -> handleLogin());
        // Thêm listener cho nút đăng ký và quên mật khẩu nếu cần
        // login.getRegisterButton().addActionListener(...)
        // login.getForgotPasswordButton().addActionListener(...)
    }

    // Constructor cho LoginManager (giữ nguyên)
    public LoginController(LoginManager loginManager, LoginService loginService) {
        this.loginManager = loginManager;
        this.loginService = loginService;
        loginManager.getLoginButton().addActionListener(e -> handleManagerLogin());
    }

    // Constructor cho Register (giữ nguyên)
    public LoginController(Register register, LoginService loginService) {
        this.register = register;
        this.loginService = loginService;
        register.getRegisterButton().addActionListener(e -> handleRegister());
    }

    // handleLogin() (giữ nguyên)
    private void handleLogin() {
        String username = login.getUsername().trim();
        String password = login.getPassword().trim();

        if (username.isEmpty()) {
            login.showMessage("Tên đăng nhập đang trống!");
            return;
        }
        if (password.isEmpty()) {
            login.showMessage("Mật khẩu đang trống!");
            return;
        }

        try {
            int customerID = loginService.login(username, password);
            if (customerID > 0) {
                login.showMessage("Đăng nhập thành công!");
                login.navigateToBuy(customerID); // Navigate to Customer view
            } else {
                login.showMessage("Đăng nhập thất bại. Vui lòng kiểm tra lại tên đăng nhập và mật khẩu!");
            }
        } catch (SQLException e) {
            login.showMessage("Lỗi khi đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // handleManagerLogin() (CHỈNH SỬA Ở ĐÂY)
    private void handleManagerLogin() {
        String userManager = loginManager.getUsername().trim();
        String userPasswordManager = loginManager.getPassword().trim();

        if (userManager.isEmpty()) {
            loginManager.showMessage("Tên đăng nhập không thể để trống!");
            return;
        }
        if (userPasswordManager.isEmpty()) {
            loginManager.showMessage("Mật khẩu không được để trống!");
            return;
        }

        try {
            boolean success = loginService.loginManager(userManager, userPasswordManager);
            if (success) {
                loginManager.showMessage("Đăng nhập thành công!");

                // --- BẮT ĐẦU THAY ĐỔI ---
                // Tạo và hiển thị ViewManager trên Event Dispatch Thread
                EventQueue.invokeLater(() -> {
                    try {
                        ViewManager viewManagerFrame = new ViewManager();
                        viewManagerFrame.setVisible(true);
                    } catch (Exception ex) {
                        // Hiển thị lỗi nếu không thể tạo ViewManager
                         JOptionPane.showMessageDialog(null, "Lỗi khi mở giao diện quản lý: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                         ex.printStackTrace();
                    }
                });

                // Đóng cửa sổ LoginManager (giải phóng tài nguyên)
                loginManager.dispose(); // Thay vì loginManager.hideWindow();
                // --- KẾT THÚC THAY ĐỔI ---

            } else {
                loginManager.showMessage("Đăng nhập thất bại. Vui lòng kiểm tra lại tên đăng nhập và mật khẩu!");
            }
        } catch (SQLException e) {
            loginManager.showMessage("Lỗi khi đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // handleRegister() (giữ nguyên)
    private void handleRegister() {
        String fullName = register.getFullName().trim();
        String phoneStr = register.getPhone().trim();
        String userAddress = register.getUserAddress().trim();
        String userName = register.getUserName().trim();
        String userPassword = register.getUserPassword().trim();
        String confirmPassword = register.getConfirmPassword().trim();
        String gmail = register.getGmail().trim();
        int day = register.getDay();
        int month = register.getMonth();
        int year = register.getYear();
        String userSex = register.getUserSex().trim(); // Lấy giới tính

        // --- Các bước kiểm tra dữ liệu (giữ nguyên) ---
        if (userSex.isEmpty()) {
            register.showMessage("Bạn chưa lựa chọn giới tính", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!DateHandler.validateDate(day, month, year)) {
            register.showMessage("Ngày tháng năm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!userPassword.equals(confirmPassword)) {
            register.showMessage("Mật khẩu không khớp", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fullName.isEmpty() || phoneStr.isEmpty() || userAddress == null || userAddress.isEmpty() || userName.isEmpty() || userPassword.isEmpty() || gmail.isEmpty()) {
             register.showMessage("Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
             return;
        }

        if (!fullName.matches("[\\p{L}\\s]+")) { // Cho phép dấu cách
            register.showMessage("Tên chỉ được chứa chữ cái và khoảng trắng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phoneStr.matches("^0\\d{9,10}$")) { // Sửa regex phone Việt Nam (10-11 số)
            register.showMessage("Số điện thoại không hợp lệ! Phải bắt đầu bằng 0 và có 10 hoặc 11 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", gmail)) {
             register.showMessage("Gmail không đúng định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
             return;
        }
        // --- Hết kiểm tra ---

        try {
            // Sửa tham số userAddress khi gọi register
            boolean result = loginService.register(userName, userPassword, phoneStr, fullName, userAddress, userSex, day, month, year, gmail);
            if (result) {
                register.showMessage("Đăng ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                register.navigateToLogin(); // Chuyển về màn hình Login sau khi đăng ký
            } else {
                // Service nên ném lỗi cụ thể nếu có thể
                register.showMessage("Đăng ký thất bại. Tên đăng nhập, SĐT hoặc Email có thể đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            // Hiển thị thông báo lỗi SQL thân thiện hơn
            String errorMessage = "Lỗi khi đăng ký: ";
            if (e.getMessage().contains("duplicate key")) { // Kiểm tra lỗi trùng khóa
                 errorMessage += "Tên đăng nhập, số điện thoại hoặc email đã tồn tại.";
            } else {
                 errorMessage += e.getMessage();
            }
            register.showMessage(errorMessage, "Lỗi Cơ Sở Dữ Liệu", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
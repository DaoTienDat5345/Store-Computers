package controller;

import service.LoginService;
import utils.DateHandler;
import view.Register;
import view.Login;
import view.LoginManager;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class LoginController {
    private Login login;
    private LoginManager loginManager;
    private Register register;
    private LoginService loginService;

    public LoginController(Login login, LoginService loginService) {
        this.login = login;
        this.loginService = loginService;
        login.getLoginButton().addActionListener(e -> handleLogin());
    }

    public LoginController(LoginManager loginManager, LoginService loginService) {
        this.loginManager = loginManager;
        this.loginService = loginService;
        loginManager.getLoginButton().addActionListener(e -> handleManagerLogin());
    }

    public LoginController(Register register, LoginService loginService) {
        this.register = register;
        this.loginService = loginService;
        register.getRegisterButton().addActionListener(e -> handleRegister());
    }

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
                login.navigateToBuy(customerID);
            } else {
                login.showMessage("Đăng nhập thất bại. Vui lòng kiểm tra lại tên đăng nhập và mật khẩu!");
            }
        } catch (SQLException e) {
            login.showMessage("Lỗi khi đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
                loginManager.hideWindow();
            } else {
                loginManager.showMessage("Đăng nhập thất bại. Vui lòng kiểm tra lại tên đăng nhập và mật khẩu!");
            }
        } catch (SQLException e) {
            loginManager.showMessage("Lỗi khi đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
        String userSex = register.getUserSex().trim();

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

        if (fullName.isEmpty() || phoneStr.isEmpty() || userAddress.isEmpty() || userName.isEmpty() || userPassword.isEmpty() || gmail.isEmpty()) {
            register.showMessage("Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!fullName.matches("[\\p{L}\\s]+")) {
            register.showMessage("Tên chỉ được chứa chữ cái!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phoneStr.matches("^0\\d{8,11}$")) {
            register.showMessage("Số điện thoại không hợp lệ! Phải bắt đầu bằng 0 và có từ 9 đến 12 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", gmail)) {
            register.showMessage("Gmail không đúng định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean result = loginService.register(userName, userPassword, phoneStr, fullName, userAddress, userSex, day, month, year, gmail);
            if (result) {
                register.showMessage("Đăng ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                register.navigateToLogin();
            } else {
                register.showMessage("Đăng ký thất bại. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            register.showMessage("Lỗi khi đăng ký: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
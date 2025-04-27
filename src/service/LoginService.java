package service;

import java.sql.SQLException;
import repository.CustomerRepository;

public class LoginService {
    private final CustomerRepository customerRepository;

    public LoginService() {
        this.customerRepository = new CustomerRepository();
    }

    public boolean check(String userName) throws SQLException {
        return customerRepository.checkUserName(userName);
    }

    public boolean checkPhone(String phone) throws SQLException {
        return customerRepository.checkPhone(phone);
    }

    public boolean checkEmail(String email) throws SQLException {
        return customerRepository.checkEmail(email);
    }

    public boolean register(String userName, String userPassword, String phone, String fullName, String userAddress, String userSex, int day, int month, int year, String userEmail) throws SQLException {
        if (check(userName)) {
            throw new SQLException("Tên đăng nhập đã tồn tại!");
        }
        if (checkPhone(phone)) {
            throw new SQLException("Số điện thoại đã được sử dụng!");
        }
        if (checkEmail(userEmail)) {
            throw new SQLException("Gmail đã được sử dụng!");
        }

        return customerRepository.register(userName, userPassword, phone, fullName, userAddress, userSex, day, month, year, userEmail);
    }

    public int login(String userName, String userPassword) throws SQLException {
        return customerRepository.login(userName, userPassword);
    }

    public boolean loginManager(String userManager, String userPasswordManager) throws SQLException {
        return customerRepository.loginManager(userManager, userPasswordManager);
    }

    public String verifyForgotPassword(String email, String phone) throws SQLException {
        return customerRepository.verifyForgotPassword(email, phone);
    }

    public boolean changePassword(String email, String personalCode, String newPassword) throws SQLException {
        return customerRepository.changePassword(email, personalCode, newPassword);
    }
}
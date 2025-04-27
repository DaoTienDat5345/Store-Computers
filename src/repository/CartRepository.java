package repository;

import database.QLdatabase;
import model.OrderDetail;
import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class CartRepository {

    public List<Object[]> getCartItems(int customerID) throws SQLException {
        List<Object[]> cartItems = new ArrayList<>();
        try (Connection con = QLdatabase.getConnection()) {
            if (con == null) throw new SQLException("Không thể kết nối đến database!");

            String sql = "SELECT p.productName, p.price " +
                        "FROM Cart c JOIN Products p ON c.productID = p.productID " +
                        "WHERE c.customerID = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, customerID);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = new Object[2];
                        row[0] = rs.getString("productName");
                        row[1] = rs.getDouble("price");
                        cartItems.add(row);
                    }
                }
            }
        }
        return cartItems;
    }

    public void deleteProductFromCart(int customerID, String productName) throws SQLException {
        try (Connection con = QLdatabase.getConnection()) {
            if (con == null) throw new SQLException("Không thể kết nối đến database!");

            String sql = "DELETE FROM Cart WHERE customerID = ? AND productID IN (SELECT productID FROM Products WHERE productName = ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, customerID);
                ps.setString(2, productName);
                ps.executeUpdate();
            }
        }
    }

    public Object[] getProductInfo(String productName) throws SQLException {
        try (Connection con = QLdatabase.getConnection()) {
            if (con == null) throw new SQLException("Không thể kết nối đến database!");

            String sql = "SELECT productID, Quantity FROM Products WHERE productName = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, productName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Object[]{rs.getString("productID"), rs.getInt("Quantity")};
                    }
                }
            }
        }
        return null;
    }

    public boolean checkProductInCart(int customerID, String productID) throws SQLException {
        try (Connection con = QLdatabase.getConnection()) {
            if (con == null) throw new SQLException("Không thể kết nối đến database!");

            String sql = "SELECT * FROM Cart WHERE customerID = ? AND productID = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, customerID);
                ps.setString(2, productID);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }

    public void updateCartQuantity(int customerID, String productID) throws SQLException {
        try (Connection con = QLdatabase.getConnection()) {
            if (con == null) throw new SQLException("Không thể kết nối đến database!");

            String sql = "UPDATE Cart SET Quantity = Quantity + 1 WHERE customerID = ? AND productID = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, customerID);
                ps.setString(2, productID);
                ps.executeUpdate();
            }
        }
    }

    public void insertIntoCart(int customerID, String productID) throws SQLException {
        try (Connection con = QLdatabase.getConnection()) {
            if (con == null) throw new SQLException("Không thể kết nối đến database!");

            String sql = "INSERT INTO Cart (customerID, productID, Quantity) VALUES (?, ?, 1)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, customerID);
                ps.setString(2, productID);
                ps.executeUpdate();
            }
        }
    }

    public List<Object[]> getCheckoutItems(int customerID, List<String> productNames) throws SQLException {
        List<Object[]> checkoutItems = new ArrayList<>();
        try (Connection con = QLdatabase.getConnection()) {
            if (con == null) throw new SQLException("Không thể kết nối đến database!");

            String sql = "SELECT p.productID, p.productName, p.price, c.Quantity as cartQuantity " +
                        "FROM Products p JOIN Cart c ON p.productID = c.productID " +
                        "WHERE p.productName = ? AND c.customerID = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (String productName : productNames) {
                    ps.setString(1, productName);
                    ps.setInt(2, customerID);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Object[] item = new Object[4];
                            item[0] = rs.getString("productID");
                            item[1] = rs.getString("productName");
                            item[2] = rs.getDouble("price");
                            item[3] = rs.getInt("cartQuantity");
                            checkoutItems.add(item);
                        }
                    }
                }
            }
        }
        return checkoutItems;
    }
}
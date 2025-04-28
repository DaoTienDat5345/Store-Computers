package service;

import model.Products;
import repository.ProductManagerRepository;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ProductManagerService {

    private final ProductManagerRepository productManagerRepository;

    public ProductManagerService() {
        this.productManagerRepository = new ProductManagerRepository();
    }

    public List<Products> getAllProducts() {
        try {
            return productManagerRepository.getAllProductsForManager();
        } catch (SQLException e) {
            // Log lỗi hoặc xử lý theo cách phù hợp
            System.err.println("Service Error: Lỗi khi tải sản phẩm cho Manager - " + e.getMessage());
            // Trả về danh sách rỗng thay vì null để tránh NullPointerException ở tầng View/Controller
            return Collections.emptyList();
        }
    }

    public boolean addProduct(String id, String name, String categoryID, String description,
                              double price, double priceCost, String imagePath, int quantity) {
        try {
            // Có thể thêm logic kiểm tra dữ liệu đầu vào ở đây nếu cần
            return productManagerRepository.addProductManager(id, name, categoryID, description, price, priceCost, imagePath, quantity);
        } catch (SQLException e) {
            System.err.println("Service Error: Lỗi khi thêm sản phẩm - " + e.getMessage());
            return false;
        }
    }

    public boolean updateProduct(String productID, String name, String categoryID, String description,
                                 double price, double priceCost, String imagePath, int quantity) {
        try {
            // Logic kiểm tra dữ liệu
            return productManagerRepository.updateProductManager(productID, name, categoryID, description, price, priceCost, imagePath, quantity);
        } catch (SQLException e) {
            System.err.println("Service Error: Lỗi khi cập nhật sản phẩm - " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(String productID) {
        try {
            // Có thể kiểm tra logic nghiệp vụ trước khi xóa (ví dụ: không xóa nếu còn tồn kho?)
            return productManagerRepository.deleteProductManager(productID);
        } catch (SQLException e) {
            System.err.println("Service Error: Lỗi khi xóa sản phẩm - " + e.getMessage());
             // Có thể phân tích SQLException để biết lỗi cụ thể (vd: ràng buộc khóa ngoại)
            return false;
        }
    }
}
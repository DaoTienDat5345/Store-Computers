package model;

import java.util.ArrayList;
import java.util.Objects;

public class Address {
    private int matinh;
    private String tenTinh;

    public Address(int matinh, String tenTinh) {
        this.matinh = matinh;
        this.tenTinh = tenTinh;
    }

    public int getMatinh() {
        return matinh;
    }

    public void setMatinh(int matinh) {
        this.matinh = matinh;
    }

    public String getTenTinh() {
        return tenTinh;
    }

    public void setTenTinh(String tenTinh) {
        this.tenTinh = tenTinh;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matinh, tenTinh);
    }

    @Override
    public String toString() {
        return tenTinh;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Address other = (Address) obj;
        return matinh == other.matinh && Objects.equals(tenTinh, other.tenTinh);
    }

    public static ArrayList<Address> getDSTinh() {
        String[] arr_tinh = {
                "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bắc Ninh",
                "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau", "Cần Thơ", "Cao Bằng",
                "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang",
                "Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên",
                "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An",
                "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình",
                "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh",
                "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "TP Hồ Chí Minh",
                "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
        };

        ArrayList<Address> listTinh = new ArrayList<>();
        for (int i = 0; i < arr_tinh.length; i++) {
            listTinh.add(new Address(i + 1, arr_tinh[i])); 
        }
        return listTinh;
    }
}

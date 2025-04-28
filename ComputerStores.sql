
CREATE DATABASE ComputerStores
COLLATE Vietnamese_CI_AS;
GO
USE ComputerStores;
GO

-- Tạo bảng Manager
CREATE TABLE Manager(
    managerID INT IDENTITY(1,1) PRIMARY KEY,
    userManager VARCHAR(50) NOT NULL,
    userPasswordManager VARCHAR(50),
    imageManager VARCHAR(255),
    ManagerName NVARCHAR(20),
    ManagerAge INT,
    ManagerPhone VARCHAR(15),
    ManagerDate DATE
);

-- Tạo bảng Customer
CREATE TABLE Customer(
    customerID INT IDENTITY(1,1) PRIMARY KEY,
    userName NVARCHAR(50) NOT NULL,
    userPassword VARCHAR(50) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    fullName NVARCHAR(50) NOT NULL,
    userAddress NVARCHAR(50) NOT NULL,
    userSex NVARCHAR(4),
    userDate DATE,
    userEmail VARCHAR(100) NOT NULL CHECK (userEmail LIKE '%@%.%'),
    PersonalCode AS ('AB' + CAST(customerID AS VARCHAR(10))) PERSISTED, -- Mã cá nhân mặc định
    CONSTRAINT UQ_Customer_userEmail UNIQUE (userEmail),
    CONSTRAINT UQ_Customer_userName UNIQUE (userName),
    CONSTRAINT UQ_Customer_phone UNIQUE (phone)
);

-- Tạo bảng Employees
CREATE TABLE Employees(
    employeesID INT IDENTITY(1,1) PRIMARY KEY,
    position NVARCHAR(50) NOT NULL,
    salary DECIMAL(15,2) NOT NULL,
    employeesName NVARCHAR(50) NOT NULL,
    employeesPhone VARCHAR(50) NOT NULL,
    employeesSex NVARCHAR(4) NOT NULL
);

-- Tạo bảng Categories
CREATE TABLE Categories (
    categoryID VARCHAR(20) PRIMARY KEY,
    categoryCode VARCHAR(2) NOT NULL UNIQUE,
    CategoryName NVARCHAR(50) NOT NULL
);

-- Tạo bảng Products
CREATE TABLE Products (
    productID VARCHAR(20) PRIMARY KEY,
    productName NVARCHAR(100) NOT NULL,
    categoryID VARCHAR(20) NOT NULL,
    Description NVARCHAR(50),
    price DECIMAL(10,2) NOT NULL,
    priceCost DECIMAL(10,2) NOT NULL,
    imagePath VARCHAR(255) NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity >= 0),
    Status AS (CASE WHEN Quantity > 0 THEN N'Còn hàng' ELSE N'Hết hàng' END),
    FOREIGN KEY (categoryID) REFERENCES Categories(categoryID)
);

-- Tạo bảng Cart
CREATE TABLE Cart (
    cartID INT IDENTITY(1,1) PRIMARY KEY, 
    customerID INT NOT NULL,
    productID VARCHAR(20) NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity >= 0), 
    FOREIGN KEY (customerID) REFERENCES Customer(customerID), 
    FOREIGN KEY (productID) REFERENCES Products(productID) 
);

-- Tạo bảng Orders
CREATE TABLE Orders(
    orderID VARCHAR(20) PRIMARY KEY,
    orderDate DATE NOT NULL,
    TotalAmount DECIMAL(15,2) NOT NULL,
    customerID INT NOT NULL,
    FOREIGN KEY (customerID) REFERENCES Customer(customerID)
);

-- Tạo bảng OrderDetails
CREATE TABLE OrderDetails (
    orderDetailsID VARCHAR(20) PRIMARY KEY,
    orderID VARCHAR(20) NOT NULL,
    productID VARCHAR(20) NOT NULL,
    Quantity INT NOT NULL,
    Subtotal DECIMAL(15,2) NOT NULL,
    warrantyType NVARCHAR(20) NOT NULL DEFAULT N'Thường' CHECK (warrantyType IN (N'Thường', N'Vàng')), 
    warrantyPrice DECIMAL(15,2) NOT NULL DEFAULT 0,
    warrantyStartDate DATE NOT NULL,
    warrantyEndDate DATE NOT NULL,
    note NVARCHAR(255) NULL, 
    FOREIGN KEY (orderID) REFERENCES Orders(orderID),
    FOREIGN KEY (productID) REFERENCES Products(productID)
);

-- Tạo bảng Shifts
CREATE TABLE Shifts (
    shiftID VARCHAR(10) PRIMARY KEY,
    shiftName NVARCHAR(50), 
    shiftDay NVARCHAR(50)
);

-- Tạo bảng WorkShiftSchedule
CREATE TABLE WorkShiftSchedule (
    employeesID INT,
    shiftID VARCHAR(10),
    PRIMARY KEY (employeesID, shiftID),
    FOREIGN KEY (employeesID) REFERENCES Employees(employeesID),
    FOREIGN KEY (shiftID) REFERENCES Shifts(shiftID)
);

-- Trigger cho Orders
GO
CREATE OR ALTER TRIGGER trg_AutoGenerateOrderID
ON Orders
INSTEAD OF INSERT
AS
BEGIN
    DECLARE @newID VARCHAR(20), @maxID INT;
    SELECT @maxID = COALESCE(MAX(CAST(SUBSTRING(orderID, 4, 3) AS INT)), 0) + 1 
    FROM Orders WITH (UPDLOCK, SERIALIZABLE);
    SET @newID = 'ORD' + RIGHT('000' + CAST(@maxID AS VARCHAR(3)), 3);
    INSERT INTO Orders (orderID, orderDate, TotalAmount, customerID)
    SELECT @newID, orderDate, TotalAmount, customerID FROM inserted;
END;
GO

-- Trigger cho OrderDetails
GO
CREATE OR ALTER TRIGGER trg_AutoGenerateOrderDetailsID
ON OrderDetails
INSTEAD OF INSERT
AS
BEGIN
    DECLARE @newDetailID VARCHAR(20), @count INT;
    SELECT @count = COUNT(*) + 1 FROM OrderDetails;
    SET @newDetailID = 'ORDD' + FORMAT(@count, '000');
    INSERT INTO OrderDetails (orderDetailsID, orderID, productID, Quantity, Subtotal, warrantyType, warrantyPrice, warrantyStartDate, warrantyEndDate, note)
    SELECT @newDetailID, orderID, productID, Quantity, Subtotal, warrantyType, warrantyPrice, warrantyStartDate, warrantyEndDate, note FROM inserted;
END;
GO

-- Trigger cho Products
GO
CREATE OR ALTER TRIGGER before_insert_product
ON Products
INSTEAD OF INSERT
AS
BEGIN
    INSERT INTO Products (productID, productName, categoryID, Description, price, priceCost, imagePath, Quantity)
    SELECT 
        c.categoryCode + CAST(
            COALESCE(
                MAX(CAST(SUBSTRING(p.productID, LEN(c.categoryCode) + 1, LEN(p.productID)) AS INT)), 
                100
            ) + ROW_NUMBER() OVER (PARTITION BY i.categoryID ORDER BY (SELECT NULL)) AS VARCHAR),
        i.productName,
        i.categoryID,
        i.Description,
        i.price,
        i.priceCost,
        i.imagePath,
        i.Quantity
    FROM inserted i
    JOIN Categories c ON i.categoryID = c.categoryID
    LEFT JOIN Products p ON p.categoryID = i.categoryID AND p.productID LIKE c.categoryCode + '%'
    GROUP BY 
        i.productName, i.categoryID, i.Description, i.price, i.priceCost, i.imagePath, i.Quantity, c.categoryCode;
END;
GO

-- Xóa trigger trg_UpdateProductQuantity vì đã xử lý trong Java
DROP TRIGGER IF EXISTS trg_UpdateProductQuantity;
GO

-- Chèn dữ liệu mẫu
INSERT INTO Manager (userManager, userPasswordManager, imageManager, ManagerName, ManagerAge, ManagerPhone, ManagerDate) 
VALUES 
    ('manager1', 'pass123', 'pic/MacBook-Pro.png', N'Nguyễn Văn A', 35, '0912345678', '2024-01-15');

INSERT INTO Categories (categoryID, categoryCode, CategoryName)
VALUES 
    ('L001', 'L', N'Laptop'),
    ('P001', 'PC', N'PC'),
    ('C001', 'CP', N'CPU'),
    ('R001', 'RA', N'RAM'),
    ('S001', 'SS', N'SSD'),
    ('G001', 'GP', N'GPU'),
    ('N001', 'PS', N'Nguồn');

INSERT INTO Products (productName, categoryID, Description, price, priceCost, imagePath, Quantity)
VALUES 
    -- Laptop
    (N'MacBook Pro M2', 'L001', N'MacBook M2 chip mạnh, màn Retina', 35000000, 30000000, 'pic/MacBook-Pro.png', 20),
    (N'Asus ROG Strix G16', 'L001', N'Laptop gaming RTX 3060, 144Hz', 25000000, 22000000, 'pic/ASUS.png', 20),
    (N'Dell XPS 15', 'L001', N'Laptop OLED 15.6", i7, mỏng nhẹ', 30000000, 27000000, 'pic/DELL.jpg', 20),
    (N'HP Spectre x360', 'L001', N'Laptop 2-trong-1, màn cảm ứng', 28000000, 25000000, 'pic/HP.jpg', 20),
    (N'Lenovo Legion 5', 'L001', N'Laptop gaming Ryzen 7, 165Hz', 23000000, 20000000, 'pic/LENOVO.jpg', 20),
    -- PC
    (N'PC Gaming RTX 4070', 'P001', N'PC RTX 4070, i9, RGB đẹp', 40000000, 35000000, 'pic/PCAMD.jpg', 20),
    (N'PC Văn Phòng Intel i5', 'P001', N'PC i5-12400, 8GB, nhỏ gọn', 15000000, 12000000, 'pic/PCVP.jpg', 20),
    (N'PC AMD Ryzen 9', 'P001', N'PC Ryzen 9 7950X, đồ họa mạnh', 50000000, 45000000, 'pic/PCAMDRyzen9.jpg', 20),
    (N'PC Mini ITX', 'P001', N'PC nhỏ gọn, i5, tiết kiệm diện tích', 20000000, 18000000, 'pic/PCMiniITX.jpg', 20),
    (N'PC Workstation Xeon', 'P001', N'PC Xeon, 64GB RAM, chuyên nghiệp', 60000000, 55000000, 'pic/PCWorkstationXeon.jpg', 20),
    -- CPU
    (N'Intel Core i9-13900K', 'C001', N'CPU 24 nhân, xung 5.8GHz', 12000000, 11000000, 'pic/IntelCorei9-13900K.jpg', 20),
    (N'AMD Ryzen 9 7950X', 'C001', N'CPU 16 nhân, hiệu suất đỉnh', 13000000, 12500000, 'pic/AMDRyzen97950X.jpg', 20),
    (N'Intel Core i7-13700K', 'C001', N'CPU 16 nhân, chơi game tốt', 9000000, 8500000, 'pic/IntelCorei7-13700K.jpg', 20),
    (N'AMD Ryzen 7 7800X', 'C001', N'CPU 8 nhân, tối ưu gaming', 8500000, 8000000, 'pic/AMDRyzen77800X.jpg', 20),
    (N'Intel Core i5-13600K', 'C001', N'CPU 14 nhân, giá trị cao', 7000000, 6500000, 'pic/IntelCorei5-13600K.jpg', 20),
    -- RAM
    (N'Corsair Vengeance 32GB', 'R001', N'RAM DDR5 32GB, tốc độ 5600MHz', 4000000, 3500000, 'pic/CorsairVengeance32GB.jpg', 20),
    (N'G.Skill Trident Z RGB 16GB', 'R001', N'RAM 16GB RGB, DDR4 3200MHz', 3000000, 2800000, 'pic/GSkillTridentZRGB16GB2.jpg', 20),
    (N'Kingston Fury Beast 32GB', 'R001', N'RAM DDR4 32GB, 3600MHz', 3800000, 3400000, 'pic/KingstonFuryBeast32GB.jpg', 20),
    (N'Teamgroup T-Force 16GB', 'R001', N'RAM 16GB, giá rẻ, 3200MHz', 2700000, 2500000, 'pic/TeamgroupT-Force16GB1.jpg', 20),
    (N'Crucial Ballistix 64GB', 'R001', N'RAM 64GB, DDR4, đa nhiệm tốt', 7500000, 7000000, 'pic/CrucialBallistix64GB.jpg', 20),
    -- SSD
    (N'Samsung 980 Pro 1TB', 'S001', N'SSD 1TB, PCIe 4.0, 7000MB/s', 3500000, 3200000, 'pic/Samsung980Pro1TB.jpg', 20),
    (N'WD Black SN850X 2TB', 'S001', N'SSD 2TB NVMe, tốc độ cao', 6000000, 5500000, 'pic/WDBlackSN850X2TB.jpg', 20),
    (N'Kingston KC3000 1TB', 'S001', N'SSD 1TB, PCIe 4.0, bền bỉ', 3200000, 2800000, 'pic/KingstonKC30001TB.jpg', 20),
    (N'Crucial P5 Plus 2TB', 'S001', N'SSD 2TB, gaming, 6600MB/s', 5800000, 5200000, 'pic/CrucialP5Plus2TB.png', 20),
    (N'ADATA XPG Gammix S70', 'S001', N'SSD 1TB, tản nhiệt, 7400MB/s', 3600000, 3300000, 'pic/AData.jpg', 20),
    -- GPU
    (N'NVIDIA RTX 4090', 'G001', N'GPU 24GB, 4K gaming đỉnh cao', 50000000, 48000000, 'pic/NVIDIARTX4090.jpg', 20),
    (N'AMD Radeon RX 7900XTX', 'G001', N'GPU 24GB, hiệu suất vượt trội', 40000000, 38000000, 'pic/AMDRadeonRX7900XTX.jpg', 20),
    (N'NVIDIA RTX 4070 Ti', 'G001', N'GPU 12GB, chơi game 1440p', 25000000, 23000000, 'pic/NVIDIARTX4070Ti.jpg', 20),
    (N'AMD Radeon RX 6800', 'G001', N'GPU 16GB, giá trị tốt', 20000000, 18000000, 'pic/AMDRadeonRX6800.jpg', 20),
    (N'NVIDIA RTX 3060 Ti', 'G001', N'GPU 8GB, gaming 1080p mượt', 12000000, 11000000, 'pic/NVIDIARTX3060Ti.jpg', 20),
    -- Nguồn (PSU)
    (N'Corsair RM850x', 'N001', N'Nguồn 850W, 80+ Gold, êm ái', 3500000, 3200000, 'pic/CorsairRM850x.jpg', 20),
    (N'Seasonic Focus GX-750', 'N001', N'Nguồn 750W, 80+ Gold, bền', 3000000, 2800000, 'pic/SeasonicFocusGX-750.jpg', 20),
    (N'ASUS ROG Thor 1000W', 'N001', N'Nguồn 1000W, RGB, 80+ Platinum', 6000000, 5500000, 'pic/ASUSROGThor1000W.jpg', 20),
    (N'MSI MPG A850G', 'N001', N'Nguồn 850W, gaming, 80+ Gold', 4000000, 3700000, 'pic/MSIMPGA850G1.jpg', 20),
    (N'Gigabyte P650B', 'N001', N'Nguồn 650W, 80+ Bronze, giá rẻ', 2000000, 1800000, 'pic/GigabyteP650B.jpg', 20);

INSERT INTO Employees (position, salary, employeesName, employeesPhone, employeesSex)
VALUES 
    (N'Nhân viên Kế toán', 15000000.00, N'Nguyễn Văn A', '0901234567', N'Nam'),
    (N'Nhân viên Kỹ thuật', 18000000.00, N'Trần Thị B', '0912345678', N'Nữ'),
    (N'Nhân viên Bán hàng', 10000000.00, N'Lê Văn C', '0923456789', N'Nam'),
    (N'Nhân viên Bán hàng', 10500000.00, N'Phạm Thị D', '0934567890', N'Nữ'),
    (N'Nhân viên Bán hàng', 11000000.00, N'Hoàng Văn E', '0945678901', N'Nam'),
    (N'Nhân viên Bán hàng', 9500000.00, N'Đặng Thị F', '0956789012', N'Nữ'),
    (N'Nhân viên Bán hàng', 9800000.00, N'Ngô Văn G', '0967890123', N'Nam'),
    (N'Nhân viên Bán hàng', 10200000.00, N'Bùi Thị H', '0978901234', N'Nữ'),
    (N'Nhân viên Bán hàng', 9700000.00, N'Dương Văn I', '0989012345', N'Nam'),
    (N'Nhân viên Bán hàng', 9900000.00, N'Vũ Thị J', '0990123456', N'Nữ');

INSERT INTO Shifts (shiftID, shiftName, shiftDay)
VALUES
    ('T2S', N'SÁNG', N'THỨ 2'),
    ('T2C', N'CHIỀU', N'THỨ 2'),
    ('T2T', N'TỐI', N'THỨ 2'),
    ('T3S', N'SÁNG', N'THỨ 3'),
    ('T3C', N'CHIỀU', N'THỨ 3'),
    ('T3T', N'TỐI', N'THỨ 3'),
    ('T4S', N'SÁNG', N'THỨ 4'),
    ('T4C', N'CHIỀU', N'THỨ 4'),
    ('T4T', N'TỐI', N'THỨ 4'),
    ('T5S', N'SÁNG', N'THỨ 5'),
    ('T5C', N'CHIỀU', N'THỨ 5'),
    ('T5T', N'TỐI', N'THỨ 5'),
    ('T6S', N'SÁNG', N'THỨ 6'),
    ('T6C', N'CHIỀU', N'THỨ 6'),
    ('T6T', N'TỐI', N'THỨ 6'),
    ('T7S', N'SÁNG', N'THỨ 7'),
    ('T7C', N'CHIỀU', N'THỨ 7'),
    ('T7T', N'TỐI', N'THỨ 7'),
    ('CNS', N'SÁNG', N'CHỦ NHẬT'),
    ('CNC', N'CHIỀU', N'CHỦ NHẬT'),
    ('CNT', N'TỐI', N'CHỦ NHẬT');

INSERT INTO WorkShiftSchedule (employeesID, shiftID)
VALUES 
    (1, 'T2S'), 
    (2, 'T2C');
USE TechEx;

CREATE TABLE IF NOT EXISTS User(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    Username            VARCHAR(255) UNIQUE NOT NULL,
    Email               VARCHAR(255) UNIQUE NOT NULL,
    PasswordHash        VARCHAR(255) NOT NULL,
    Name                VARCHAR(255) NOT NULL,
    Surname             VARCHAR(255) NOT NULL,
    BirthDate           DATE NOT NULL,
    Phone               VARCHAR(15), -- (opzionale)
    Role                ENUM ('Customer', 'Admin') DEFAULT 'Customer'
);

CREATE TABLE IF NOT EXISTS Address(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    Street              TEXT NOT NULL,
    AdditionalInfo      TEXT,
    City                VARCHAR(100) NOT NULL,
    PostalCode          VARCHAR(20) NOT NULL,
    Region              VARCHAR(100), -- (opzionale)
    Country             VARCHAR(100) NOT NULL,
    Name                VARCHAR(255) NOT NULL,
    Surname             VARCHAR(255) NOT NULL,
    Phone               VARCHAR(15), -- (opzionale)
    AddressType         ENUM('Shipping', 'Billing') NOT NULL
);

CREATE TABLE IF NOT EXISTS UserAddress(
    AddressID           INT,
    UserID              INT,
    IsDefault           BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (AddressID) REFERENCES Address(ID),
    FOREIGN KEY (UserID) REFERENCES User(ID),
    PRIMARY KEY (AddressID, UserID)
);

CREATE TABLE IF NOT EXISTS PaymentMethod(
    ID                  INT AUTO_INCREMENT PRIMARY KEY ,
    UserID              INT NOT NULL,
    Number              VARCHAR(19) NOT NULL,
    Expiration          DATE NOT NULL,
    Name                VARCHAR(255) NOT NULL,
    IsDefault           BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (UserID) REFERENCES User(ID)
);
-- Aggiunta metodi di pagamento alternativi

CREATE TABLE IF NOT EXISTS OrderAddress(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    Street              TEXT NOT NULL,
    City                VARCHAR(100) NOT NULL,
    PostalCode          VARCHAR(20) NOT NULL,
    Region              VARCHAR(100), -- (opzionale)
    Country             VARCHAR(100) NOT NULL,
    Name                VARCHAR(255) NOT NULL,
    Surname             VARCHAR(255) NOT NULL,
    Phone               VARCHAR(15), -- (opzionale)
    AddressType         ENUM('Shipping', 'Billing') NOT NULL
);

CREATE TABLE IF NOT EXISTS `Order`(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    UserID              INT NOT NULL,
    OrderDate           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    OrderStatus         ENUM ('Pending', 'Processing', 'Shipped', 'Delivered', 'Cancelled') DEFAULT 'Pending',
    DeliveryDate        DATE,
    TotalAmount         DECIMAL(10, 2) NOT NULL,
    ShippingAddressID     INT NOT NULL,
    BillingAddressID      INT NOT NULL,
    FOREIGN KEY (UserID)  REFERENCES User(ID),
    FOREIGN KEY (ShippingAddressID) REFERENCES OrderAddress(ID),
    FOREIGN KEY (BillingAddressID)  REFERENCES OrderAddress(ID)
);

CREATE TABLE IF NOT EXISTS OrderItem(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    OrderID             INT NOT NULL,
    ItemName            VARCHAR(255) NOT NULL,
    ItemDescription     TEXT,
    ItemBrand           VARCHAR(255) NOT NULL,
    ItemPrice           DECIMAL(10, 2) NOT NULL,        -- prezzo senza IVA
    ItemCategory        ENUM('Display', 'Camera', 'Battery', 'Microphone', 'Speaker', 'Case', 'Button', 'Sensor') NOT NULL,
    ItemGrade           ENUM('Original', 'Excellent', 'Great', 'Good') NOT NULL,
    ItemQuantity        INT NOT NULL DEFAULT 0 CHECK (ItemQuantity >= 0),
    ItemVAT             DECIMAL(5, 2) DEFAULT 22.00 NOT NULL,          -- es. 22.00 -> 22%
    FOREIGN KEY (OrderID) REFERENCES `Order`(ID)
);

CREATE TABLE IF NOT EXISTS Product(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    Name                VARCHAR(255) NOT NULL,
    Description         TEXT,
    Brand               VARCHAR(255) NOT NULL,
    Price               DECIMAL(10, 2) NOT NULL,        -- prezzo senza IVA
    Category            ENUM('Display', 'Camera', 'Battery', 'Microphone', 'Speaker', 'Case', 'Button', 'Sensor') NOT NULL,
    Grade               ENUM('Original', 'Excellent', 'Great', 'Good') NOT NULL,
    StockQuantity       INT NOT NULL DEFAULT 0 CHECK (StockQuantity >= 0),
    VAT                 DECIMAL(5, 2) DEFAULT 22.00 NOT NULL          -- es. 22.00 -> 22%
);

CREATE TABLE IF NOT EXISTS Review(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    UserID              INT NOT NULL,
    ProductID           INT NOT NULL,
    Title               VARCHAR(255) NOT NULL,
    Description         TEXT,
    Rating              TINYINT NOT NULL CHECK (Rating BETWEEN 1 AND 5),
    FOREIGN KEY (UserID) REFERENCES User(ID),
    FOREIGN KEY (ProductID) REFERENCES Product(ID)
);

CREATE TABLE IF NOT EXISTS Cart(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    UserID              INT UNIQUE NOT NULL,
    FOREIGN KEY (UserID) REFERENCES User(ID)
);

CREATE TABLE IF NOT EXISTS CartItem(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    CartID              INT NOT NULL,
    ProductID           INT NOT NULL,
    Quantity            INT NOT NULL CHECK ( Quantity > 0 ),
    FOREIGN KEY (CartID) REFERENCES Cart(ID),
    FOREIGN KEY (ProductID) REFERENCES Product(ID)
);

CREATE TABLE IF NOT EXISTS Wishlist(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    UserID              INT UNIQUE NOT NULL,
    FOREIGN KEY (UserID) REFERENCES User(ID)
);

CREATE TABLE IF NOT EXISTS WishlistItem(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    WishlistID          INT NOT NULL,
    ProductID           INT NOT NULL,
    FOREIGN KEY (WishlistID) REFERENCES Wishlist(ID),
    FOREIGN KEY (ProductID) REFERENCES Product(ID)
);
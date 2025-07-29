USE TechEx;

CREATE TABLE IF NOT EXISTS `User`(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    Username            VARCHAR(255) NOT NULL ,
    Email               VARCHAR(255) UNIQUE NOT NULL,
    PasswordHash        VARCHAR(255) NOT NULL,
    Role                ENUM ('Customer', 'Admin') DEFAULT 'Customer'
);

CREATE TABLE IF NOT EXISTS Address(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    Street              TEXT NOT NULL,
    AdditionalInfo      TEXT,
    City                VARCHAR(100) NOT NULL,
    PostalCode          VARCHAR(20) NOT NULL,
    Region              VARCHAR(100),
    Country             VARCHAR(100) NOT NULL
);

CREATE TABLE UserAddress(
    AddressID           INT,
    UserID              INT,
    AddressType         ENUM('Shipping', 'Billing') NOT NULL,
    Name                VARCHAR(255) NOT NULL,
    Surname             VARCHAR(255) NOT NULL,
    Phone               VARCHAR(20) NOT NULL,
    IsDefault           BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (AddressID) REFERENCES Address(ID),
    FOREIGN KEY (UserID) REFERENCES User(ID),
    PRIMARY KEY (AddressID, UserID, AddressType)
);

CREATE TABLE PaymentMethod(
    ID                  INT AUTO_INCREMENT PRIMARY KEY ,
    UserID              INT NOT NULL,
    Number              VARCHAR(4) NOT NULL,
    Expiration          DATE NOT NULL,
    Name                VARCHAR(255) NOT NULL,
    IsDefault           BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (UserID) REFERENCES User(ID)
);
-- Aggiunta metodi di pagamento alternativi

CREATE TABLE OrderAddress(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    Street              TEXT NOT NULL,
    City                VARCHAR(100) NOT NULL,
    PostalCode          VARCHAR(20) NOT NULL,
    Region              VARCHAR(100),
    Country             VARCHAR(100) NOT NULL,
    AddressType         ENUM('Shipping', 'Billing') NOT NULL
);

CREATE TABLE `Order`(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    UserID              INT NOT NULL,
    OrderDate           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    OrderStatus         ENUM ('Pending', 'Processing', 'Shipped', 'Delivered', 'Cancelled') DEFAULT 'Pending',
    DeliveryDate        DATE,
    TotalAmount         DECIMAL(10, 2) NOT NULL,
    ShippingAddressID     INT NOT NULL,
    BillingAddressID      INT NOT NULL,
    FOREIGN KEY (UserID) REFERENCES User(ID),
    FOREIGN KEY (ShippingAddressID) REFERENCES OrderAddress(ID),
    FOREIGN KEY (BillingAddressID) REFERENCES OrderAddress(ID)
);

CREATE TABLE OrderItem(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    OrderID             INT NOT NULL,
    ItemName            VARCHAR(255) NOT NULL,
    ItemDescription     TEXT,
    ItemBrand           VARCHAR(255) NOT NULL,
    ItemPrice           DECIMAL(10, 2) NOT NULL,        -- prezzo senza IVA
    ItemCategory        VARCHAR(255) NOT NULL,
    ItemSeller          VARCHAR(255) NOT NULL,
    ItemQuantity        INT NOT NULL DEFAULT 0 CHECK (ItemQuantity >= 0),
    ItemVAT             DECIMAL(5, 2) DEFAULT 22.00 NOT NULL,          -- es. 22.00 -> 22%
    FOREIGN KEY (OrderID) REFERENCES `Order`(ID)
);

CREATE TABLE Product(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    Name                VARCHAR(255) NOT NULL,
    Description         TEXT,
    Brand               VARCHAR(255) NOT NULL,
    Price               DECIMAL(10, 2) NOT NULL,        -- prezzo senza IVA
    Category            VARCHAR(255) NOT NULL,
    Seller              VARCHAR(255) NOT NULL,
    StockQuantity       INT NOT NULL DEFAULT 0 CHECK (StockQuantity >= 0),
    VAT                 DECIMAL(5, 2) DEFAULT 22.00 NOT NULL          -- es. 22.00 -> 22%
);
-- Tabella per la Categoria

CREATE TABLE Review(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    UserID              INT NOT NULL,
    ProductID           INT NOT NULL,
    Title               VARCHAR(255) NOT NULL,
    Description         TEXT,
    Rating              TINYINT NOT NULL CHECK (Rating BETWEEN 1 AND 5),
    FOREIGN KEY (UserID) REFERENCES User(ID),
    FOREIGN KEY (ProductID) REFERENCES Product(ID)
);

CREATE TABLE Cart(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    UserID              INT UNIQUE NOT NULL,
    FOREIGN KEY (UserID) REFERENCES User(ID)
);

CREATE TABLE CartItem(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    CartID              INT NOT NULL,
    ProductID           INT NOT NULL,
    Quantity            INT NOT NULL CHECK ( Quantity > 0 ),
    FOREIGN KEY (CartID) REFERENCES Cart(ID),
    FOREIGN KEY (ProductID) REFERENCES Product(ID)
);

CREATE TABLE Wishlist(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    UserID              INT UNIQUE NOT NULL,
    FOREIGN KEY (UserID) REFERENCES User(ID)
);

CREATE TABLE WishlistItem(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    WishlistID          INT NOT NULL,
    ProductID           INT NOT NULL,
    FOREIGN KEY (WishlistID) REFERENCES Wishlist(ID),
    FOREIGN KEY (ProductID) REFERENCES Product(ID)
);
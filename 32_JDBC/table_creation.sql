CREATE USER 'evg'@'localhost' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON *.* TO 'evg'@'localhost' WITH GRANT OPTION;

CREATE TABLE Company (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    contact_email VARCHAR(255),
    phone_number VARCHAR(20)
);

CREATE TABLE Products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    company_id INT,
    FOREIGN KEY (company_id) REFERENCES Company(company_id)
);

-- Insert data into the Company table
INSERT INTO Company (company_name, address, contact_email, phone_number)
VALUES
    ('Amazone', '123 Main St, CityA', 'info@abccorp.com', '123-456-7890'),
    ('Google', '456 Elm St, CityB', 'contact@xyzinc.com', '987-654-3210'),
    ('IBM', '789 Oak St, CityC', 'info@lmnent.com', '555-123-4567'),
    ('PQR Industries', '567 Oak St, CityD', 'info@pqrindustries.com', '555-987-6543'),
    ('Enterprise', '890 Elm St, CityE', 'contact@lmkent.com', '123-555-7890'),
    ('NASA', '234 Maple St, CityF', 'info@nopco.com', '999-333-2222'),
    ('Oracle', '678 Pine St, CityG', 'contact@qrsltd.com', '111-777-4444'),
    ('Macdonald', '345 Cedar St, CityH', 'info@xyztech.com', '777-222-1111');
    
    ALTER TABLE Products 
    MODIFY COLUMN product_id INT AUTO_INCREMENT;
    
INSERT INTO Products (product_name, price, company_id)
VALUES
    ('Product A1', 49.99, 1),
    ('Product A2', 29.99, 1),
    ('Product A3', 39.99, 1),
    ('Product A4', 19.99, 1),
    ('Product A5', 59.99, 1);
    
  INSERT INTO Products (product_name, price, company_id)
VALUES
    ('Product B1', 199.99, 2),
    ('Product B2', 149.99, 2),
    ('Product B3', 99.99, 2),
    ('Product B4', 249.99, 2),
    ('Product B5', 179.99, 2);  
    

    
   INSERT INTO Products (product_name, price, company_id)
VALUES
    ('Product D1', 3.99, 3),
    ('Product D2', 3453.99, 3),
    ('Product D3', 44.99, 3),
    ('Product D4', 345.99, 3),
    ('Product D5', 345.99, 3); 
    
       INSERT INTO Products (product_name, price, company_id)
VALUES
    ('Product X1', 32.99, 4),
    ('Product X2', 353.99, 4),
    ('Product X3', 464.99, 4),
    ('Product X4', 3345.99, 4),
    ('Product X5', 355.99, 4); 
    
           INSERT INTO Products (product_name, price, company_id)
VALUES
    ('Product J1', 32.99, 5),
    ('Product J2', 454.99, 5),
    ('Product J3', 3.99, 5),
    ('Product J4', 4.99, 5),
    ('Product J5', 45.99, 5); 

    
               INSERT INTO Products (product_name, price, company_id)
VALUES
    ('Product J1', 32.99, 5),
    ('Product J2', 454.99, 5),
    ('Product J3', 3.99, 5),
    ('Product J4', 4.99, 5),
    ('Product J5', 45.99, 5); 
    
        
        INSERT INTO Products (product_name, price, company_id)
VALUES
    ('Product F1', 299.99, 6),
    ('Product F2', 499.99, 6),
    ('Product F3', 199.99, 6),
    ('Product F4', 999.99, 6),
    ('Product F5', 349.99, 6);
    
               INSERT INTO Products (product_name, price, company_id)
VALUES
    ('Product P1', 32.99, 7),
    ('Product P2', 454.99, 7),
    ('Product P3', 3.99, 7),
    ('Product P4', 4.99, 7),
    ('Product P5', 45.99, 7); 
    
                   INSERT INTO Products (product_name, price, company_id)
VALUES
    ('Product U1', 32.99, 8),
    ('Product U2', 454.99, 8),
    ('Product U3', 3.99, 8),
    ('Product U4', 4.99, 8),
    ('Product U5', 45.99, 8); 
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    



/******************************************************/

ALTER TABLE Laptop 
ADD
CONSTRAINT PK_laptop
PRIMARY KEY (code);

ALTER TABLE PC 
ADD
CONSTRAINT PK_pc
PRIMARY KEY (code);

ALTER TABLE Printer
ADD
CONSTRAINT PK_printer
PRIMARY KEY (code);

ALTER TABLE Product
ADD
CONSTRAINT PK_product
PRIMARY KEY (model);

/***************************************************************/

ALTER TABLE Laptop ADD
CONSTRAINT FK_laptop_product 
FOREIGN KEY (model)
REFERENCES Product (model);

ALTER TABLE PC
ADD
CONSTRAINT FK_pc_product 
FOREIGN KEY (model)
REFERENCES Product (model);

ALTER TABLE Printer
ADD
CONSTRAINT FK_printer_product
FOREIGN KEY (model)
REFERENCES Product (model);

/***************************************************************/
1)• Find the model number, speed and hard drive capacity for all the PCs with prices below $500. Result set: model, speed, hd

SELECT model, speed, hd
FROM PC
WHERE price < 500;

2)List all printer manufacturers. Result set: manufacturer

SELECT DISTINCT maker 
FROM Product
WHERE type = 'Printer';

3)Find the model number, RAM and screen size of the laptops with prices over $1000

SELECT model, ram, screen
FROM Laptop
WHERE price > 1000;

4)⚫ List all of the color printers models

SELECT model
FROM Printer
WHERE color = 'y';

5)• Find the model number, speed and hard drive capacity of the PCs having 12x CD and prices less than $600 or having 24x CD and prices less than $600

SELECT model, speed, hd
FROM PC
WHERE (cd  = '12x' AND price < 600)
OR (cd = '24x' AND price < 600);
 

6)Point out the manufacturer and speed of the laptops that have a hard drive capacity more than or equal to 10 GB

SELECT p.maker, l.speed
FROM Laptop l
JOIN Product p ON l.model = p.model
WHERE l.hd >= 10;

7)Find out the models and prices for all the products (of any type) produced by manufacturer B

WITH subQ AS
(
	SELECT model
	FROM Product
	WHERE maker = 'B'
)
SELECT price, model
FROM Printer
WHERE model IN (SELECT model FROM subQ)
UNION ALL
SELECT price, model
from PC
Where model IN (SELECT model FROM subQ)
UNION ALL
SELECT price, model
from Laptop
Where model IN (SELECT model FROM subQ);

8)Find out which manufacturers sell PCs but not laptops

SELECT DISTINCT p1.maker
FROM Product p1
WHERE p1.type = 'PC'
AND p1.maker NOT IN 
(
	SELECT DISTINCT p2.maker
	FROM Product p2
	WHERE p2.type = 'Laptop'
);

9)Find the manufacturers of the PCs that have a speed of not less than 450MHz. Result set: manufacturer

SELECT DISTINCT p.maker
FROM Product p
JOIN PC pc ON p.model = pc.model
WHERE pc.speed >= 450;


10)Find the models of printers that have the highest price. Result set: model, price

SELECT model, price
FROM Laptop
ORDER BY price DESC
LIMIT 1; // Bad solution if we have number of products with the same price

SELECT model, price
FROM Laptop
WHERE price = (SELECT MAX(price) FROM Laptop);

11)Find out the average speed of PCs

SELECT AVG (price) AS avg_price
FROM PC;

12)Find out the average speed of the laptops priced over $1000

SELECT AVG (price) AS avg_price
FROM Laptop
WHERE price > 1000;

13)Find out the average speed of the PCs produced by manufacturer A

SELECT AVG(pc.speed) AS avg_speed
FROM PC pc
JOIN Product p ON p.model = pc.model
WHERE p.maker = 'A';

14)Find out manufacturers who make two or more versions of a model of PC

SELECT maker
FROM Product
WHERE type = 'PC'
GROUP BY maker
HAVING COUNT(*) > 1;

15)Find the hard drive sizes that are equal among two or more PCs

SELECT hd
FROM PC
GROUP BY hd
HAVING COUNT(*) > 1;

16)Find the pairs of PC models having identical speeds and RAM. As a result, each pair is shown only once, l.e., (i, j) but not (j, i). Result set model with higher number, model with lower number, speed, and RAM

SELECT 
p1.model AS model_1, p2.model AS model_2,
p1.speed AS speed, p1.ram AS ram
FROM PC p1
JOIN PC p2 ON p1.model < p2.model
AND p1.speed = p2.speed
AND p1.ram = p2.ram;

17)Find the laptop models whose speed is lower than the slowest PC

SELECT model
FROM Laptop 
WHERE speed < (
	SELECT MIN(speed) FROM PC 
	); 

18)Find the manufacturers of the cheapest color printers. Result set: manufacturer, price

SELECT p.maker, pri.price
FROM Product p
JOIN Printer pri ON p.model = pri.model
WHERE pri.color = 'y' 
AND pri.price = (
	SELECT MIN(price) 
	FROM Printer WHERE color = 'y'
	);

19)For each manufacturer that has models in the Laptop table, find the average laptop screen size produced. Result set: manufacturer, average screen size

SELECT p.maker, AVG(screen) as scren_avg
FROM Laptop l
JOIN Product p ON p.model = l.model
GROUP BY p.maker;

20)Find the manufacturers producing at least three distinct models of PCs

SELECT p.maker, COUNT(pc.model) AS n_models
FROM Product p
JOIN PC pc ON pc.model = p.model
GROUP BY p.maker
HAVING n_models > 2;

SELECT maker, COUNT(DISTINCT model) AS n_models
FROM Product
WHERE type = 'PC'
GROUP BY maker
HAVING n_models > 2;

21)List the highest priced PCs for each manufacturer

SELECT p.maker, MAX(pc.price) AS max_price
FROM Product p
JOIN PC pc ON pc.model = p.model
GROUP BY p.maker;

22)For each PC whose speed that exceeds 600 MHz, define the average price of the PCs with identical speeds

SELECT speed, AVG(price) AS avg_price
FROM PC 
WHERE speed > 600
GROUP BY speed;

23)Find the manufacturers that produce both a PC with speed not less than 750 MHz and a laptop with speed not less than 750 MHz. Result set: manufacturer

SELECT p.maker
FROM Product p
JOIN PC pc ON pc.model = p.model
WHERE pc.speed >= 750
UNION
SELECT p.maker
FROM Product p
JOIN Laptop l ON l.model = p.model
WHERE l.speed >= 750;

24)Find a manufacturer who produce printers and also PCs with the highest speed among the PCs with the lowest RAM

SELECT p.maker
FROM Product p
JOIN PC pc ON pc.model = p.model
WHERE ram = (SELECT MIN(ram) FROM PC)
  AND speed = (SELECT MAX(speed) FROM PC WHERE ram = (SELECT MIN(ram) FROM PC))
  INTERSECT
  SELECT maker 
  FROM Product
  WHERE type = 'Printer';
  
25)Define the average price of the PCs and laptops produced by manufacturer A. Result set: single total price

WITH subQuery AS 
(
	SELECT l.price
	FROM Laptop l
	JOIN Product p ON l.model = p.model
	WHERE p.maker = 'A'
	UNION ALL
	SELECT pc.price
	FROM PC pc
	JOIN Product p ON pc.model = p.model
	WHERE p.maker = 'A'
)
SELECT AVG(price) 
FROM subQuery;


26)Define the average size of the PC hard drive for each manufacturer that also produces printers. Result set: manufacturer, average capacity of HD


SELECT p.maker, AVG(hd) AS hd_avg
FROM Product p
JOIN PC pc ON p.model = pc.model
WHERE maker IN 
(
	SELECT maker 
	FROM Product
	WHERE type = 'Printer'
)
GROUP BY p.maker;


27)For each group of laptops with the identical model number, add the following record into the PC table:
version: minimum code of laptops in the group +20 
model: laptops model number +1000
speed: maximum speed of laptops in the group
ram: maximum ram size of laptops in the group *2
hd: maximum hd capacity of laptops in the group "2
cd: default value
price: maximum price of laptops in the group divided by 1.5

ALTER TABLE PC
ALTER COLUMN cd
SET DEFAULT '10x';

INSERT INTO PC code, model, speed, ram, hd, price 
SELECT MIN(code) + 20,
 model + 1000,
 MAX(speed),
  MAX(ram) * 2,
   MAX(hd) * 2,
     MAX(price) / 1.5 
     FROM Laptop 
     GROUP BY model;
     
     
28)Delete all of the computers that have the minimum HDD size or minimum RAM size from the PC table ? 
    
DELETE p1
FROM PC p1
JOIN (SELECT MIN(hd) AS min_hd, MIN(ram) AS min_ram FROM PC ) p2
ON p1.hd = p2.min_hd OR p1.ram = p2.min_ram;

     

























































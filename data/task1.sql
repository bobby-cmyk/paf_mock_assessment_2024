-- Write your Task 1 answers in this file
CREATE DATABASE bedandbreakfast;

USE bedandbreakfast;

CREATE TABLE users (
	email VARCHAR(128) NOT NULL,
	name VARCHAR(128),
	
	CONSTRAINT pk_email PRIMARY KEY(email)
);

CREATE TABLE bookings (
	booking_id CHAR(8) NOT NULL,
	listing_id VARCHAR(20),
	duration INT,
	email VARCHAR(128),
	
	CONSTRAINT pk_booking_id PRIMARY KEY(booking_id),
	CONSTRAINT fk_email FOREIGN KEY(email) REFERENCES users(email)
);

CREATE TABLE reviews (
	id INT NOT NULL AUTO_INCREMENT,
	date TIMESTAMP,
	listing_id VARCHAR(20),
	reviewer_name VARCHAR(64),
	comments TEXT,
	
	CONSTRAINT pk_reviews_id PRIMARY KEY(id)
);

SHOW VARIABLES LIKE 'local_infile';

LOAD DATA LOCAL INFILE  
'/Users/aikenong/vttp/paf_assessment_template/data/users.csv'
INTO TABLE users 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(email ,name);
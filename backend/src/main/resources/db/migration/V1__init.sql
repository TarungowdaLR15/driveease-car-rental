CREATE EXTENSION IF NOT EXISTS btree_gist;
CREATE TABLE pricing_tiers(code text PRIMARY KEY, discount_pct numeric(5,2) NOT NULL CHECK(discount_pct BETWEEN 0 AND 100));
INSERT INTO pricing_tiers VALUES('STANDARD',0),('SILVER',5),('GOLD',10),('CORPORATE',15);
CREATE TABLE vehicles(id bigserial PRIMARY KEY, reg_no text UNIQUE NOT NULL, make text NOT NULL, model text NOT NULL,
  category text NOT NULL, daily_price numeric(12,2) NOT NULL, active boolean NOT NULL DEFAULT true);
INSERT INTO vehicles(reg_no,make,model,category,daily_price) VALUES
('KA01AB1001','Hyundai','Creta','SUV',3200),('KA01AB1002','Mahindra','XUV700','SUV',4600),
('KA01AB1003','Honda','City','Sedan',2800),('KA01AB1004','BMW','3 Series','Sedan',7900),
('KA01AB1005','Tata','Nexon EV','Electric',3600),('KA01AB1006','Porsche','911','Sports',21000);
CREATE TABLE bookings(id bigserial PRIMARY KEY, reference text UNIQUE NOT NULL, vehicle_id bigint NOT NULL REFERENCES vehicles(id),
  customer_email text NOT NULL, pickup_at timestamptz NOT NULL, return_at timestamptz NOT NULL,
  status text NOT NULL DEFAULT 'PENDING' CHECK(status IN('PENDING','CONFIRMED','ACTIVE','COMPLETED','CANCELLED','REJECTED','OVERDUE')),
  total numeric(12,2) NOT NULL, deposit numeric(12,2) NOT NULL, created_at timestamptz NOT NULL DEFAULT now(),
  CHECK(return_at > pickup_at),
  -- database-level double-booking guard on half-open intervals [pickup, return)
  CONSTRAINT no_overlap EXCLUDE USING gist (vehicle_id WITH =, tstzrange(pickup_at,return_at,'[)') WITH &&)
    WHERE (status IN ('PENDING','CONFIRMED','ACTIVE','OVERDUE')));

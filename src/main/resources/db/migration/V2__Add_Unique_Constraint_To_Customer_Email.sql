ALTER TABLE customer.public.customer
ADD CONSTRAINT customer_email_unique UNIQUE (email);
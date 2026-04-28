INSERT INTO public.product (id, amount, product_name, description, status)
VALUES
    ('1cf53eb4-8853-49bc-8b39-a7132e1f1cc9', 89.90, 'Camiseta Java', 'Camiseta preta com estampa Java', 'ACTIVE'),
    ('4449f3a2-ea5c-411d-87dc-ab6d52ff5bee', 149.90, 'Mochila Dev', 'Mochila com divisórias para laptop e gadgets', 'ACTIVE'),
    ('830efa01-fc8a-4df1-83cc-50d9dde425c0', 59.90, 'Caneca Spring Boot', 'Caneca branca com logo Spring Boot', 'ACTIVE')
-- agrega más registros aquí
    ON CONFLICT (id) DO UPDATE
                            SET amount = EXCLUDED.amount,
                            product_name = EXCLUDED.product_name,
                            description = EXCLUDED.description,
                            status = EXCLUDED.status;
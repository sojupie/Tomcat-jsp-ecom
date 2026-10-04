INSERT INTO category (name, description) VALUES
    ('Skafferi', 'Råvaror för vardagens måltider'),
    ('Färdigmat', 'Smidiga favoriter att värma och servera'),
    ('Fryst', 'Frysta råvaror för när det passar')
ON CONFLICT (name) DO UPDATE SET description = EXCLUDED.description;

INSERT INTO product (category_id, sku, name, description, price)
SELECT category.id, sample.sku, sample.name, sample.description, sample.price
FROM (VALUES
    ('Skafferi', 'SMK-1001', 'Tahini, len sesampasta', 'Krämig sesampasta med nötig smak.', 49.90),
    ('Färdigmat', 'SMK-1002', 'Falafel, klassisk', 'Små kikärtsbollar med örter och kryddor.', 59.90),
    ('Färdigmat', 'SMK-1003', 'Hummus, original', 'Len kikärtsröra med tahini och citron.', 39.90),
    ('Skafferi', 'SMK-1004', 'Dadelsirap', 'Mjuk sötma från solmogna dadlar.', 54.90),
    ('Skafferi', 'SMK-1005', 'Basmatiris', 'Långkornigt ris med fin doft.', 69.90),
    ('Fryst', 'SMK-1006', 'Gröna bondbönor', 'Varsamt frysta bönor, redo för grytan.', 44.90),
    ('Fryst', 'SMK-1007', 'Babyokra', 'Mild okra som passar i mustiga rätter.', 42.90),
    ('Skafferi', 'SMK-1008', 'Rostade kikärtor', 'Krispigt snacks med lätt sälta.', 29.90)
) AS sample(category_name, sku, name, description, price)
JOIN category ON category.name = sample.category_name
ON CONFLICT (sku) DO UPDATE SET
    category_id = EXCLUDED.category_id,
    name = EXCLUDED.name,
    description = EXCLUDED.description,
    price = EXCLUDED.price;

INSERT INTO inventory (product_id, quantity)
SELECT product.id, sample.quantity
FROM (VALUES
    ('SMK-1001', 18), ('SMK-1002', 12), ('SMK-1003', 0), ('SMK-1004', 24),
    ('SMK-1005', 9), ('SMK-1006', 15), ('SMK-1007', 6), ('SMK-1008', 30)
) AS sample(sku, quantity)
JOIN product ON product.sku = sample.sku
ON CONFLICT (product_id) DO NOTHING;

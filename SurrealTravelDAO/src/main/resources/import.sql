INSERT INTO account (PASSWORD, USERNAME) VALUES ('$2a$12$qH7ZI7na0DDS/b5Z8JWyZ.R8mmt7uLyImpU2Vox9xTl50bKtbG6ay', 'root');
INSERT INTO account_roles VALUES ((SELECT id FROM account WHERE username='root'), 'ROLE_USER');
INSERT INTO account_roles VALUES ((SELECT id FROM account WHERE username='root'), 'ROLE_STAFF');
INSERT INTO account_roles VALUES ((SELECT id FROM account WHERE username='root'), 'ROLE_ADMIN');

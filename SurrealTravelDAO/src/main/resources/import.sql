INSERT INTO account (PASSWORD, USERNAME) VALUES ('$2a$12$pkpPekRM8ViLOmCRONtFae7zf1LJ.utIXW7UgVYUw8MKW1MYgljEO', 'pa165');
INSERT INTO account_roles VALUES ((SELECT id FROM account WHERE username='pa165'), 'ROLE_USER');
INSERT INTO account_roles VALUES ((SELECT id FROM account WHERE username='pa165'), 'ROLE_STAFF');
INSERT INTO account_roles VALUES ((SELECT id FROM account WHERE username='pa165'), 'ROLE_ADMIN');

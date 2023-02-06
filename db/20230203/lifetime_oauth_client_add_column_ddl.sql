alter table lifetime_oauth_client add COLUMN domain_name VARCHAR(120) DEFAULT NULL;
alter table lifetime_oauth_client add COLUMN client_secret_bak VARCHAR(120) DEFAULT NULL;

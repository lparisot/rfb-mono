##

## Some configuration

You must create a file ~/.rfb_pass.txt which contains your ansible vault password.

You must create a new vault file in group_vars/all/vault, which contains
```
---
vault_db_user_password: yourdatabasepassword

vault_social_google_client_id: yourgoogleclientid
vault_social_google_client_secret: yourgoogleclientsecret

vault_social_twitter_client_id: yourtwitterclientid
vault_social_twitter_client_secret: yourtwitterclientsecret

vault_social_facebook_client_id: yourfacebookclientid
vault_social_facebook_client_secret: yourfacebookclientsecret
```
You must change all keys with yours and encrypt the file with:
```
$ ansible-vault encrypt group_vars/all/vault
```
The file will be encrypt without asking any password, using the one found in ~/.rfb_pass.txt file.

You must also change group_vars/all/vars file.
The servername with your domain name.
The db_name and db_user_name with your values.

## Installation of our application in AWS

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

vault_aws_access_key_id: yourawskeyid
vault_aws_secret_access_key: yourawsaccesskey
```
To create your AWS access key, see https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html#Using_CreateAccessKey

You must change all keys with yours then encrypt the file with:
```
$ ansible-vault encrypt group_vars/all/vault
```
The file will be encrypt without asking any password, using the one found in ~/.rfb_pass.txt file (this filename is defined in ansible.cfg file).

You must also change group_vars/all/vars file:
* The servername with your domain name.
* The db_name and db_user_name with your values.
* the ec2_vpc_default with your AWS vpc.
* the ec2_subnet with your subnet.
* the ec2_region with your region.

## Ansible playbooks

These playbooks will create two instances in AWS. We use the CentOS 7 (x86_64) found in AWS market place. Instance type is t2.micro with a volume type gp2 with 8GB. With these values we are free tier eligible.

So we have rfb-app01 which is the webserver and rfb-db01 which is the database server.

They will also create two security groups, one for the webserver (opening port 22 and 80), and the other for the database server, (opening port 22, 3306 for the webserver and 3306 for the local PC where you launch the playbooks).

And install all required applications.

The webserver will contains:
* an apache server waiting on port 80 and redirecting to port 8080
* docker
* the docker container of the application wainting on port 8080

The database server will contains:
* mariadb waiting on port 3306

At last, you can connect via your browser to the public DNS of the webserver and see the little web application.

You can also ssh directly to the servers, using ssh rfb-app01 or rfb-db01.

This is done by the private access key generated to access the instances and stored in your ~/.ssh folder (rfb-staging-private.pem), and the ~/.ssh/config file which is modify to map the rfb-app01 and rfb-db01 name on the AWS public DNS names.

On the webserver you can check the application by doing:
```
$ sudo docker ps
$ sudo docker logs <dockerid>
$ sudo less /var/log/httpd/access_log
$ sudo less /var/log/httpd/error_log
```
On the database server you can check the database via:
```
$ systemctl status mariadb
$ sudo less /var/log/mariadb/mariadb.log
```

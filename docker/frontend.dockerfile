FROM httpd:2.4

# Add the compiled Angular application
ADD frontend/dist/openrunning/* /usr/local/apache2/htdocs/
# Configuration files of the httpd server
ADD docker/files/frontend-htaccess /usr/local/apache2/htdocs/.htaccess
ADD docker/files/frontend-httpd.conf /usr/local/apache2/conf/httpd.conf

FROM httpd:2.4

ADD frontend/dist/openrunning/* /usr/local/apache2/htdocs/

Sitemaster
======

Yves Hwang
06.03.2015

Setting up your own dev-machine
-------------------------------
You will need:

* Gradle 2.1
* openjdk-8
* docker if you so desires
* Vagrant & virtualbox

production environment
----------------------
* Ubuntu trusty
* OpenJDK8

To build
--------
gradle build
gradle fatJar

Unit test
---------
gradle check

Integration test
----------------
gradle integrationTest [parameters]

To load test data
-----------------
gradle loadTestData [parameters]
(recreates schema by default)

To run
------
Gradle:
* gradle run -Dhibernate.connection.url=jdbc:postgresql://HOST:PORT/DB_NAME
(implies: -contentRoot src/ui/build & -disableAuth)

Standalone jar:
* java -Dhibernate.connection.url=jdbc:postgresql://HOST:PORT/DB_NAME -jar build/libs/siterumasteru-all-....jar  [arguments]

Parameters
----------
Application arguments:
* -contentRoot - specifies which directory should be used to serve static (default: jar:/content/)
* -disableAuth - disables Authentication Filter, restricted resources can be accessed by anyone

System parameters:
* hibernate.connection.url - specifies jpa connection (example "-Dhibernate.connection.url=jdbc:postgresql://HOST:PORT/DB_NAME")
* hibernate.connection.username - username for jpa db
* hibernate.connection.password - password for jpa db
* hibernate.hbm2ddl.auto - specifies what to do with schema, possible values: 'validate'(default), update, create, create-drop-core
* jde.url - JDE WebService - URL
* jde.username - JDE WebService - Username
* jde.password - JDE WebService - Password

--------
This is the common api component that is published in the SFR Bintray account.

All RESTful API project and products will utilise this library. Naturally each project is free to pin it to a specific verison of the the api-core library of their desire.

see https://github.com/statoilfuelretail/api-core

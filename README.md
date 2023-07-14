# CCoins-ms

* Pre-requirements:
  - Install Docker: https://www.docker.com/products/docker-desktop/
  - Clone this repository in a folder with git clone
  - Enter CCoins_Bff and execute on console: "./clone_repos.sh" to clone full-program repository

* To run all microservices, step on CCoins_Bff and execute:
$ ./run.sh

* To open Owner side, use:
http://localhost:4200/admin

* Once you login and create a Bar in your account, you have to create 1 or more tables.
Each table has a QR code to enter into Client side (Recommend to download QR image and use https://scanqr.org/ to scan it in another browser)


---------------------------------------------------------------
To take server down, execute: docker-compose down

# CCoins-ms

* Pre-requirements:
  - Install Docker: https://www.docker.com/products/docker-desktop/
  - Install Git: https://git-scm.com/book/en/v2/Getting-Started-Installing-Git
  
*How to Run:

  *First time? Use this steps to download and run project, else you can jump this step 
  
  - HIGH RECOMMENDED: Have a SSH on GitHub: https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent
	(or else clone repositories manually and ignore this steps)
  - Enter the following link and download file "clone_and_run.sh": https://github.com/Bercovein/CCoins_Bff/blob/develop/clone_and_run.sh
  - Step on download folder, open a bash console and run: "./clone_and_run.sh"


  *If project is already cloned then step on CCoins_Bff and execute:
  $ ./run.sh

* To open Owner side, use:
http://localhost:4200/admin

* Once you login and create a Bar in your account, you have to create 1 or more tables.
Each table has a QR code to enter into Client side (Require to download QR image and use https://scanqr.org/ in another browser to scan)


---------------------------------------------------------------
To take server down, execute: docker-compose down

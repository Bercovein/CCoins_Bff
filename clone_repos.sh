#!/bin/bash

# Repositorios a clonar
repos=(
    "https://github.com/MarcosBertolotti/CCoins"
    "https://github.com/Bercovein/CCoins_Coins"
    "https://github.com/Bercovein/CCoins_Prizes"
    "https://github.com/Bercovein/CCoins_Bars"
    "https://github.com/Bercovein/CCoins_Users"
)

# va hacia atras en el repositorio
cd ../

# Iterar sobre los repositorios y clonarlos
for repo in "${repos[@]}"
do
    git clone "$repo"
done
repos=(
    "./CCoins/":"https://github.com/MarcosBertolotti/CCoins"
	"./CCoins_Bff/":"https://github.com/Bercovein/CCoins_Bff"
	"./CCoins_Coins/":"https://github.com/Bercovein/CCoins_Coins"
    "./CCoins_Prizes/":"https://github.com/Bercovein/CCoins_Prizes"
    "./CCoins_Bars/":"https://github.com/Bercovein/CCoins_Bars"
    "./CCoins_Users/":"https://github.com/Bercovein/CCoins_Users"
)

# Iterar sobre los repositorios y clonarlos
for element in "${repos[@]}"
do
	IFS=":" read -r directory repository <<< "$element"
	
	if [ -d "$directory" ]
    then
		echo  "$repository" + " Sí, sí existe."
	else
		echo "$repository" + " No, no existe"
		git clone "$repository"
	fi
done

./CCoins_Bff/run.sh
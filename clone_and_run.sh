repos=(
    "./CCoins/":"https://github.com/MarcosBertolotti/CCoins"
	"./CCoins_Bff/":"https://github.com/Bercovein/CCoins_Bff"
	"./CCoins_Coins/":"https://github.com/Bercovein/CCoins_Coins"
    "./CCoins_Prizes/":"https://github.com/Bercovein/CCoins_Prizes"
    "./CCoins_Bars/":"https://github.com/Bercovein/CCoins_Bars"
    "./CCoins_Users/":"https://github.com/Bercovein/CCoins_Users"
)

folder="./Chopp_Coins/"
echo "Verificando carpeta Chopp_Coins."
if [ ! -d "$folder" ]; then
	echo "Creando carpeta Chopp_Coins."
    mkdir "$folder"
fi

cd "$folder"

# Iterar sobre los repositorios y clonarlos
echo "Verificando repositorios."
for element in "${repos[@]}"
do
	IFS=":" read -r directory repository <<< "$element"
	echo "Verificando repositorio: " + "$directory"
	if [ ! -d "$directory" ]
    then
		echo "Clonando repositorio: " + "$directory"
		git clone "$repository"
	fi
done

echo "Programa instalado correctamente!"

# Pregunta al usuario

while true; do
	read -p "¿Quiere ejecutarlo ahora? (yes/no): " response

	# Comprueba la respuesta del usuario
	if [[ "$response" == "yes" || "$response" == "y" ]]; then
		cd ./CCoins_Bff/
		./run.sh
		break
	elif [[ "$response" == "no" || "$response" == "n"  ]]; then
		echo "Para ejecutarlo luego, puede utilizar el comando ./run.sh dentro de la carpeta 'CCoins_Bff'"
		break
	else
		echo "Respuesta inválida. Por favor, ingresa 'yes' o 'no'."
	fi
done


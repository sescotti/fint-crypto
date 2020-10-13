function log_error(){
	echo -e "||\x1B[31mERROR\x1B[0m|| $1"
}

function log_info(){
	echo -e "||\x1B[94mINFO\x1B[0m|| $1"
}

function log_warn(){
	echo -e "||\x1B[93mWARN\x1B[0m|| $1"
}

function verify_installs {

	echo -e -n "||\x1B[94mINFO\x1B[0m|| Verifying prerequisites..."
	CAN_EXECUTE=true

	for var in $@; do

		if [ -z $(which "$var") ]; then
			echo ""
			log_error "Fatal error: $var not found. Please install it to run this script"
			CAN_EXECUTE=false
		fi

	done

	if [ $CAN_EXECUTE == false ]; then
		echo ""
		log_error "Please first fix the errors above and try again"
		exit 1
	else
		echo "All good"
	fi

}

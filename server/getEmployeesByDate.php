<?php
//debug options, using hardcoded date
//in addition to hardcoded date, we can get more specific info about error
//when we visit page directly in the browser
//probably more features will be added for debug mode in the future
$debug = FALSE;


if($debug){
	//set debug date. if we don't have date specified in _GET array
	//then we will use '2013-12-16' as default date
	//some records have been prepared for this debug date
	//above date shouldn't give us null response. if it does, then we have
	//broken database
	$debug_date = isset($_GET['date']) ? $_GET['date'] : '2013-12-16';
};


//some initial data for response
//if anything goes wrong we have success set to false by default
$response = array(
	'message' => 'No message',
	'success' => FALSE
);


//if debug is turned on, GET array will be used to fetch date
//if no debug_date is provided in GET array, then default date will be used
if($debug){
	$date = $debug_date;
	echo 'Debug mode is enabled, random errors may (and will) occur <br />';
	echo '_GET date is set to '.$_GET['date'].'<br/>';
	echo 'Current debug date is '.$date.'<br />';
} else {
	//get date from _POST and set is as our paremeter for DB
	if(isset($_POST['date']) && $_POST['date']!=NULL) {
		$date = $_POST['date'];
	}
	else {
		$response['message'] = 'No date provided.';
	}

}
//if we have date set up, then proceed with fetching data
if($date){

	try {
		require_once __DIR__.'/config.php';
		$db_connection = new PDO('mysql:host='.DB_HOST.';dbname='.DB_NAME, DB_USERNAME, DB_PASSWORD);
	} catch (PDOException $e) {

		if($debug) {
			echo $e->getMessage();
		} else {
			$response["message"] = "Cannot connect to MySQL database. " +
			"Error info contained in 'error' field.";
			$response["error"] = $e->getMessage();
		}
	}

	$sql = 'SELECT DISTINCT godziny_pracy.pracownikID, pracownicy.nazwisko, pracownicy.imie
			FROM godziny_pracy
			INNER JOIN pracownicy
			ON godziny_pracy.pracownikID = pracownicy.ID
			WHERE godziny_pracy.data = :date';
	try {
		$statement = $db_connection->prepare($sql);

		if($statement){
			//FIXME: add handler for empty response
			$sql_response = $statement->execute(array(
					'date' => $date
			));

			if($debug){
				echo '<br />';
				print_r($sql_response);
				echo '<br/>';
			}

			if($sql_response){

				$result = $statement->fetchAll(PDO::FETCH_ASSOC);

				if($debug){
					echo 'Content of $result <br/>';
					echo '<pre>';
					print_r($result);
					echo '<br/>';
					echo '</pre>';
				}

				if($result){
					$response['employees'] = $result;
					$response['message'] = 'Success, data contained in "employees" field';
					$response['success'] = TRUE;
				} else {
					$response['message'] = 'No data fetched';
					$response['success'] = FALSE;
				}
			} else {
				$error = $statement->errorInfo();
				$response['error'] = $error;
				$response['message'] = "No response fetched. See 'error' for more details ";
			}

			if($debug){
				echo 'Content of $response Array';
				echo '<pre>';
				print_r($response);
				echo '</pre>';
			}
		}

	} catch (PDOException $e) {
		echo $e->getMessage();
	}
}

if(!$debug){
	header('Content type: application/json');
}

echo json_encode($response, TRUE);
//echo json_encode($_POST, TRUE);

?>

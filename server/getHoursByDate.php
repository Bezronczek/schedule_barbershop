<?php
//debug options, using hardcoded date
$debug = FALSE;


if($debug){
	$debug_date = isset($_GET['date']) ? $_GET['date'] : '2013-12-16';
};

$response = array(
	'message' => 'No message',
	'success' => FALSE
);

if($debug){
	$date = $debug_date;
	echo 'Debug mode is enabled, random errors may (and will) occur <br />';
	echo '_GET date is set to '.$_GET['date'].'<br/>';
	echo 'Current debug date is '.$date.'<br />';
	//echo 'To disable debug mode, please change debug variable to FALSE <br />';
} else {
	if(isset($_POST['date']) && $_POST['date']!=NULL) {
		$date = $_POST['date'];
	}
	else {
		$response['message'] = 'No date provided.';
	}

}
if($date){

	try {
		require_once __DIR__.'/config.php';
		$db_connection = new PDO('mysql:host='.DB_HOST.';dbname='.DB_NAME, DB_USERNAME, DB_PASSWORD);
	} catch (PDOException $e) {
		echo 'Cannot connect to MySQL database';
		echo $e->getMessage();
	}

	$sql = 'SELECT DISTINCT godziny_pracy.godzina
			FROM godziny_pracy
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
					$response['hours'] = $result;
					$response['message'] = 'Success, data contained in "hours" field';
					$response['success'] = TRUE;
				} else {
					$response['message'] = 'No data fetched';
					$response['success'] = FALSE;
				}
			} else {
				$error = $statement->errorInfo();
				$response['message'] = $error;
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
?>
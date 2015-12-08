<?php
//$debug = true;


try {
	require_once __DIR__.'/config.php';
	$db_connection = new PDO('mysql:host='.DB_HOST.';dbname='.DB_NAME, DB_USERNAME, DB_PASSWORD);
} catch (PDOException $e) {
	echo 'Cannot connect to MySQL database';
	echo $e->getMessage();
}
	$sql = 'SELECT ID, nazwa
			FROM kategorie';
	try {
	$statement = $db_connection->prepare($sql);
		if($statement){
		//FIXME: add handler for empty response
		$sql_response = $statement->execute();
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
				$response['services'] = $result;
				$response['message'] = 'Success, data contained in "services" field';
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
header('Content type: application/json');
echo json_encode($response);
?>
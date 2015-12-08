<?php

$response = array(
	'message' => 'No message',
	'success' => FALSE
);

if((isset($_POST['name']) && $_POST['name'] != NULL) &&
	(isset($_POST['surname']) && $_POST['surname'] != NULL) &&
	(isset($_POST['phoneNumber']) && $_POST['phoneNumber'] != NULL) &&
	//(isset($_POST['email']) && $_POST['email'] != NULL)&&
	(isset($_POST['date']) && $_POST['date'] != NULL) &&
	(isset($_POST['time']) && $_POST['time'] != NULL) &&
	(isset($_POST['service']) && $_POST['service'] != NULL) &&
	(isset($_POST['worker']) && $_POST['worker'] != NULL)) {
	
	$name = $_POST['name'];
	$surname = $_POST['surname'];
	$phoneNumber = $_POST['phoneNumber'];
	$email = isset($_POST['email']) ? $_POST['email'] : NULL;
	$date = $_POST['date'];
	$time = $_POST['time'];
	$serviceID = $_POST['service'];
	$employeeID = $_POST['worker'];
	
	try {
		require_once __DIR__.'/config.php';
		$db_connection = new PDO('mysql:host='.DB_HOST.';dbname='.DB_NAME, DB_USERNAME, DB_PASSWORD);
	} catch (PDOException $e) {
		echo 'Cannot connect to MySQL database';
		echo $e->getMessage();
	}
	
	$sql_query = 'INSERT INTO wizyty (`data`, `godzina`, `zabiegID`, `potwierdzona`, `pracownikID`, `telefon`, `email`, `nazwisko`, `imie`, `userID`)
				  VALUES(:date, :hour, :service, TRUE, :employeeID, :phoneNumber, :email, :surname, :name, 1);
				  UPDATE godziny_pracy 
				  SET zajety = TRUE
				  WHERE pracownikID = :employeeID AND data = :date AND godzina = :hour;';
	
	try {
		$statement = $db_connection->prepare($sql_query);
		
		if($statement){
			
			$params = array(
				'date' => $date,
				'hour' => $time,
				'service' => $serviceID,
				'employeeID' => $employeeID,
				'phoneNumber' => $phoneNumber,
				'email' => $email,
				'surname' => $surname,
				'name' => $name
			);
			
			$sql_response = $statement->execute($params);

			if($sql_response){
				$result = $statement->fetchAll(PDO::FETCH_ASSOC);
				$response['message'] = 'Dane zostaly dodane';
				$response['success'] = TRUE;
				if($result){
					$response['result'] = $result;
					$response['message'] = 'Dane zosatly dodane';
					$response['success'] = TRUE;
				}
			} else {
				$error = $statement->errorInfo();
				$response['message'] = $error;
			}
		}
		
	} catch (PDOException $e) {
		echo $e->getMessage();
	}
		
} else {
	$response['message'] = "Wystapil blad. Prosze sprawdzic dane i sprobowac ponownie";
}
header('Content type: application/json');
echo json_encode($response);
?>
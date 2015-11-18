<?php
	// Check to see if user is logged in
	$sessionKey = $_SESSION['key'];
	$username = $_SESSION['username'];
    
	$tbl_name = "users";

	//Connect to MySQl
	$mysqli = new mysqli($db['host'], $db['user'], $db['pass'], $db['name']);
	if ($mysqli->connect_errno) {
	  echo "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error;
	}
    
	$result = $mysqli->query("SELECT * FROM $tbl_name WHERE email='$username'");
	
	if(!$result->num_rows){
		header("Location: ../");
	}else{

		$row = $result->fetch_array(MYSQLI_ASSOC);
    
		$key = $row['session_id'];
	
		if($key != $sessionKey){
			header("Location: ?p=logout");
		//	echo "Session IDs Do Not Match: ".$key." : ".$sessionKey;
		}
	
		if(!isset($_SESSION['user'])){
			header("Location: ?p=logout");
		}else{
			$user = $_SESSION['user'];
		}
	}
?>

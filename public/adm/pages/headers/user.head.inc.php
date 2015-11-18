<?php
	if(isset($_POST['submit'])){
		$username = $_POST['email'];
		$fname = $_POST['fname'];
		$lname = $_POST['lname'];
		$pass1 = password_hash($_POST['pass'], PASSWORD_BCRYPT, $options);
		
		$tbl_name = "users";
			
		//Connect to MySQl
		$mysqli = new mysqli($db['host'], $db['user'], $db['pass'], $db['name']);
			if ($mysqli->connect_errno) {
				echo "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error;
			}

		$result = $mysqli->query("SELECT * FROM $tbl_name WHERE email='$username'");

		
		if ($result->num_rows){
			$output = "<span id='error'>That user already exists!</span>";
		}else{
			$mysqli->query("INSERT INTO $tbl_name (id, email, password, salt, first_name, last_name, last_login, last_ip, session_id) VALUES('', '$username', '$pass1', '$options[salt]', '$fname', '$lname', '', '', '')");
			$mysqli->close();
			$output = "Successfully created new user account!";
		}
	}
?>
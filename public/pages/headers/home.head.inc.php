<?php
	if(isset($_POST['submit'])){
		//Table name
		$tbl_name = "users";

		//Connect to MySQl
		$mysqli = new mysqli($db['host'], $db['user'], $db['pass'], $db['name']);
		if ($mysqli->connect_errno) {
			echo "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error;
		}
		
		//Gather the username and password sent from the login page
		$login_username = $_POST['username'];
    
		//Protect MySQL injection
		$login_username = stripslashes($login_username);
		$login_username = mysqli_real_escape_string($mysqli, $login_username);
		
		//Grab the salt for the username given and encrypt the password.
		$result = $mysqli->query("SELECT * FROM $tbl_name WHERE email='$login_username'");

		if(!$result->num_rows){
			$error = "<div align='center' id='error'>email or password is incorrect.</div>";
		}else{
			$row = $result->fetch_array(MYSQLI_ASSOC);

			$name = $row['first_name'];
			$key = $row['session_id'];

			$options['salt'] = $row['salt'];

			$login_pass = password_hash($_POST['pass'], PASSWORD_BCRYPT, $options);

			//Check to make sure that it is correct.
			if($login_pass != $row['password']){
				//An error occurred, lets display a message to the user.
				$error = "<div align='center' id='error'>username or password is incorrect.</div>";
			}else{
		
			    //Make some new information that will be used to update the existing user
				$i = new getIP();
				$d = new getDate();
				$s = new sessionID();
			
				$i->setIP();
				$d->setDate();
				$s->setID();

				$ip = $i->printIP();
				$date = $d->printDate();
				$id = sha1($s->printID());
			
      
			
				//Lets go ahead and update the table to hold the new information
				$mysqli->query("UPDATE $tbl_name SET session_id='$id' WHERE email='$login_username'")or die(mysql_error());
				$mysqli->query("UPDATE $tbl_name SET last_login='$date' WHERE email='$login_username'")or die(mysql_error());
				$mysqli->query("UPDATE $tbl_name SET last_ip='$ip' WHERE email='$login_username'")or die(mysql_error());

				//Set sessions
				$_SESSION['user'] = $name;
				$_SESSION['username'] = $login_username;
				$_SESSION['key'] = $id;
				
				header("location:adm/");
			}
		}
	}

?>

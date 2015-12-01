<?php
	$time = "A very long and sad and lonely emptiness void";
	
	if(!shell_exec('ps -A | grep java')){
		$status = "Running";
	}else{
		$status = "Not Running";
	}

	if(isset($_POST['delete'])){
		$location = $dir . $_POST['delete'];
		unlink($location);
	}

	if(isset($_POST['restart'])){
		//$pid = shell_exec('ps -A | grep sshd | awk "NR==1{print $1}"');
		if(!shell_exec('ps -A | grep java')){
//			shell_exec('pkill -f java');
//			shell_exec('export DISPLAY=:0.0');
//			putenv("DISPLAY=localhost:0");
			//shell_exec('cd ~/java-bulletin/');
//			shell_exec('java -jar ../../java-bulletin.jar $time');
//			shell_exec('bash /home/pi/java-bulletin/jstart');
//			shell_exec('screen -x pi/works -p 0 -X stuff "bash /home/pi/java-bulletin/jstart \015"');
//			shell_exec('shutdown -h now');

        $connection = ssh2_connect('127.0.0.1', 22);
        ssh2_auth_password($connection, 'pi', 'andrewclaytondonottouch');
	$stream = ssh2_exec($connection, 'killall java');
        $stream = ssh2_exec($connection, 'bash /home/pi/java-bulletin/jstart');

		}
	}
	
	if(isset($_POST['start'])){
		if(!shell_exec('ps -A | grep java')){
			$time = $_POST['time'];
//		shell_exec('export DISPLAY=:0.0');
//		putenv("DISPLAY=localhost:0");
		//shell_exec('~/java-bulletin/');
//		shell_exec('java -jar ../../java-bulletin.jar $time');
//		shell_exec('bash /home/pi/java-bulletin/jstart');
//		shell_exec('screen -x pi/works -p 0 -X stuff "bash /home/pi/java-bulletin/jstart \015"');

        $connection = ssh2_connect('127.0.0.1', 22);
        ssh2_auth_password($connection, 'pi', 'andrewclaytondonottouch');

        $stream = ssh2_exec($connection, 'bash /home/pi/java-bulletin/jstart');
		}
	}

	if(isset($_POST['submit'])){
		$target_file = $dir . basename($_FILES["fileToUpload"]["name"]);
		$uploadOk = 1;
		$imageFileType = pathinfo($target_file, PATHINFO_EXTENSION);
		
		// Check if image file is a actual image or fake image
		$check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
		if($check !== false) {
			$uploadOk = 1;
			if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
				//echo "The file ". basename( $_FILES["fileToUpload"]["name"]). " has been uploaded.";
			} else {
			}
		} else {
			$uploadOk = 0;
		}
	}


	$header = "<table id='fileTable' border='0' cellspacing='0' cellpadding='5'><tr>
						<td height='20' id='tblHeader'>File</td>
						<td height='20' id='tblHeader'>Action</td>
					</tr>";

	$handler = opendir($dir);

	while($file = readdir($handler)){
		$results[] = $file;
	}
	
	closedir($handler);
	
	foreach ($results as $f ){
		if($f == "." || $f == ".."){
			
		}else{
		
			$delete = "<button type='submit' name='delete' value='$f' border='0'><img src='./theme/images/delete.png' alt='SomeAlternateText'></button>";
	
			$files = $files. "<tr>
				<td height='20'>" .$f. "</td>
				<td height='20'>" .$delete. "</td></tr>";
		}
	}
	
	$files = $header . $files . "</table>";
?> 

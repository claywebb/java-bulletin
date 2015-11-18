<?php
	if(isset($_POST['delete'])){
		$location = $dir . $_POST['delete'];
		unlink($location);
	}

	if(isset($_POST['restart'])){
		$pid = shell_exec('ps -A | grep sshd | awk "NR==1{print $1}"');
		shell_exec('kill' . $pid);
		shell_exec('java -jar ../java-bulletin.jar 500');
	}
	
	if(isset($_POST['start'])){
		shell_exec('java -jar ../java-bulletin.jar 500');
	}

	if(isset($_POST['submit'])){
		$target_file = $dir . basename($_FILES["fileToUpload"]["name"]);
		$uploadOk = 1;
		$imageFileType = pathinfo($target_file, PATHINFO_EXTENSION);
		
		// Check if image file is a actual image or fake image
		$check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
		if($check !== false) {
			echo "File is an image - " . $check["mime"] . ".";
			$uploadOk = 1;
			if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
				//echo "The file ". basename( $_FILES["fileToUpload"]["name"]). " has been uploaded.";
			} else {
				echo "Sorry, there was an error uploading your file.";
			}
		} else {
			echo "File is not an image.";
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
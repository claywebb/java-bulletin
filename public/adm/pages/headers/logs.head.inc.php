<?php 
	
	if($_GET['c'] == "export"){
		$_SESSION['export'] = 1;
	}else{
		$_SESSION['export'] = 0;
	}
	
	//Connect to MySQl
	$mysqli = new mysqli($db['host'], $db['user'], $db['pass'], 'logs');
		if ($mysqli->connect_errno) {
			echo "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error;
		}
	
	if($_SESSION['export'] == 1){
			
		$result = mysqli_query($mysqli, "SELECT * FROM `logs` ORDER BY id ASC");
		
		header('Content-Type: text/csv; charset=utf-8');
		header("Content-Disposition: attachment; filename='logs.csv'");

		ob_end_clean();

		// create a file pointer connected to the output stream
		$output = fopen('php://output', 'w');
		
		fputcsv($output, array('ID', 'TYPE', 'USER NAME', 'LOG', 'DATE', 'TIME'));

			// fetch the data

		$rows = mysqli_query($mysqli, "SELECT id, type, userName, log, date, time FROM `logs`");
		
    

    
		$user = $_SESSION['user'];
		$log = "Exported Logs";
		$today = date("y-m-d");
		$time = date("G:i a"); 
		mysqli_query($mysqli, "INSERT INTO `logs` (id, type, userName, log, date, time) VALUES('', 'Export', '$user', '$log', '$today', '$time')")or die(mysql_error());
    
		

		// loop over the rows, outputting them
		while ($row = mysqli_fetch_assoc($rows)) fputcsv($output, $row);
		$message = "The information has been exported successfully!";
		exit;
	}	
	
	$name1 = "<table width='100%' height='30' id='header'><tr><td align='center'>
			  <img src='./theme/images/header-left.png' id='assetHeaderLeft' alt='' border='0' />
			  <span id='assetHeader'>Logs</span>
			  <img src='./theme/images/header-right.png' id='assetHeaderRight' alt='' border='0' /></td></tr></table>";
	
	$header1 = $name1. "<table width='100%' border='0' cellspacing='0' cellpadding='0'><tr>
						<td class='dGray' height='20'>ID</td>
						<td class='dGray' height='20'>TYPE</td>
						<td class='dGray' height='20'>USER NAME</td>
						<td class='dGray' height='20'>LOG</td>
						<td class='dGray' height='20'>DATE</td>
						<td class='dGray' height='20'>TIME</td>
						</tr>";
			
	//Grab information from the table and store them in variables
	$x = 0;
			
	$result = mysqli_query($mysqli, "SELECT * FROM `logs` ORDER BY id ASC");
			
	while($row = mysqli_fetch_array($result)){
		$id = $row['id'];
		$type = $row['type'];
		$userName = $row['userName'];
		$log = $row['log'];
		$date = $row['date'];
		$time = $row['time'];
							
						
		if($x&1) {
			$color = "dGray";
		} else {
			$color = "lGray";
		}
								
		$tblData1 = $tblData1. "<tr><td class='$color' height='20'>" .$id. "</td>
					<td class='$color' height='20'>" .$type. "</td>
					<td class='$color' height='20'>" .$userName. "</td>
					<td class='$color' height='20'>" .$log. "</td>
					<td class='$color' height='20'>" .$date. "</td>
					<td class='$color' height='20'>" .$time. "</td></tr>";

		$x++;
	}
			
	$tblData1 = $tblData1. "</table>";

?>
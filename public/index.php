<?php
	// Enable Errors
	ini_set('display_errors', 1);
	error_reporting(E_ALL);

	//Set default timezone
	date_default_timezone_set('America/New_York');

	// Start a session.
	session_start();


	// Include config file.
	if (file_exists('includes/config.inc.php'))
	{
		// Database file exists, require it.
		require_once('includes/config.inc.php');
	}
	else
	{
		// Database file does not exist, let's fail fast.
		die('Config file could not be found.');
	}

	// Include class file.
	if (file_exists('includes/class.inc.php'))
	{
		// Class file exists, require it.
		require_once('includes/class.inc.php');
	}
	else
	{
		// Class file does not exist, let's fail fast.
		die('Config file could not be found.');
	}

	
	
	// Check if a specific page is being requested.
	if (isset($_GET['p']))
	{
		// Page is being requested.
		$page = str_replace('.', '', str_replace('/', '', $_GET['p']));	
	}
	else
	{
		$page = $site['default'];
	}
	
	// Check if header page exists
	if (file_exists('theme/header.inc.php'))
	{
		require_once('theme/header.inc.php');
	}
	
	
	// Check if head page exists
	if (file_exists('pages/headers/'.$page.'.head.inc.php'))
	{
		require_once('pages/headers/'.$page.'.head.inc.php');
	}
	
	
	// Check if the page exists.
	if (file_exists('pages/'.$page.'.inc.php'))
	{
		require_once('pages/'.$page.'.inc.php');
	}
	else
	{
		// Page does not exists, let's 404, check if a 404 page exists.
		if (file_exists('pages/404.inc.php'))
		{
			// Custom 404 page exists, let's build our response.
		}
		else
		{
			//Custom 404 page does not exist.
		}
	}
	
	// Check if footer page exists
	if (file_exists('theme/footer.inc.php'))
	{
		require_once('theme/footer.inc.php');
	}
	
?>

<?php
	//This is the config file
	
	$site['default'] = "home";
	
	$db['host'] = "localhost";
	$db['user'] = "admin_andrew";
	$db['pass'] = "Meowmeowkittykat123";
	$db['name'] = "admin_andrew";

	$options = [
		'cost' => 15,
		'salt' => mcrypt_create_iv(22, MCRYPT_DEV_URANDOM),
	];

?>

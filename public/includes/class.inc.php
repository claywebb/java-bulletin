<?php

	//Get user's IP
	class getIP{
		private $ip;
		
		function setIP(){
			$this->ip = $_SERVER['REMOTE_ADDR'];
		}
		
		function printIP(){
			return($this->ip);
		}
	}
	
    
    //Genereate a user's session id
    class sessionID{
        private $sessID;
		private $max;
		private $charLst;
		private $i;
		
		function setID(){
			$this->max = 12;
			$this->charLst = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			$this->i = 0;

			while ($this->i < $this->max) {
				$this->sessID .= $this->charLst{mt_rand(0, (strlen($this->charLst) - 1))};
				$this->i++;
				return $this->sessID;
			}
		}
		
		function printID(){
			return($this->sessID);
			
		}
    }
    
    
    
    //Get Date
    class getDate{
        private $date;
		
		function setDate(){
			$this->date = date("Y-m-d H:i:s");
		}
		
		function printDate(){
			return($this->date);
		}
    }
?>
<?php
    $servername = "localhost";
   $username = "1044722";
   $password = "yusuke6635";
   $dbname = "1044722";
   
   $con = mysqli_connect($servername, $username,$password,$dbname);
   
   $email = $_POST["email"];
   $password = $_POST["password"];
   $name = $_POST["name"];
   $dob = $_POST["dob"];
   $age = $_POST["age"];
   $gender = $_POST["gender"];
   $height = $_POST["height"];
   $weight = $_POST["weight"];
   $doj = $_POST["doj"];
   $reward = $_POST["reward"];
   $image = $_POST['image'];
   
   
   $buffer = base64_decode($base);
   $buffer = mysqli_real_escape_string($buffer);
   
   
   $statement = mysqli_prepare($con,"INSERT INTO User_Profile (User_Email,Password,Name,Date_Of_Birth,Age,Gender,Initial_Weight,Height,Date_Of_Join,Reward_Point)Values(?,?,?,?,?,?,?,?,?,?)");
   mysqli_stmt_bind_param($statement,"ssssissssi",$email,$password,$name,$dob,$age,$gender,$height,$weight,$doj,$reward);
   mysqli_stmt_execute($statement);
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>
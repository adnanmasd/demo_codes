<?php

require_once './userRestHandler.php';

$userHandler = new UserRestHandler();

if (isset($_GET['action']) and $_GET['action'] === "login") {
    $data = json_decode(file_get_contents("php://input"), true);
    $email = $data['email'];
    $pass = $data['pass'];
    $ip = $data["ip"];
    $userHandler->loginUser($email, $pass, $ip);
} else if (isset($_GET['action']) and $_GET['action'] === "register") {
    $data = json_decode(file_get_contents("php://input"), true);
    $first = $data['firstName'];
    $last = $data['lastName'];
    $email = $data['email'];
    $pass = $data['password'];
    $mobile = $data['mobileNumber'];
    $userTypeID = $data['userTypeID'];
    $userHandler->registerUser($first, $last, $email, $pass, $mobile, $userTypeID);
}

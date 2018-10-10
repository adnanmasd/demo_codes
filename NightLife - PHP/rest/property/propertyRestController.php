<?php

require_once './propertyRestHandler.php';

$propertyHandler = new PropertyRestHandler ();
$city = "";
$venue = "";
$id = "";

if (isset($_GET ["id"])) {
    $id = $_GET ["id"];
    $propertyHandler->getProperty($id);
} else if (isset($_GET ['city'])) {
    $city = $_GET ["city"];
    $venue = (isset($_GET ['venue'])) ? $_GET ['venue'] : "All";
    $propertyHandler->getAllVenueInCity($city, $venue);
} else {
    
}

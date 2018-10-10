<?php

include_once '../../model/db/db.php';
include_once '../SimpleRest.php';
include 'emailSender.php';

class EmailHandler extends SimpleRest {

    function sendTestEmail($toEmail, $toName) {
        ini_set('max_execution_time', 300);
        $emailSender = new EmailSender();

        $rawData = [];
        $result = [];

        $validation = $this->validate_email($toEmail);

        if (array_key_exists("error", $validation)) {
            $result = $validation;
            $rawData = $result['error'];
            unset($rawData['statusCode']);
        } else {
            $result = $emailSender->sendTestEmail($toEmail, $toName);
            $rawData = $result;
            unset($rawData['statusCode']);
        }
        $statusCode = $result['statusCode'];
        $requestContentType = $_SERVER ['HTTP_ACCEPT'];
        $this->setHttpHeaders($requestContentType, $statusCode);
        $response = $this->encodeJson($rawData);
        echo $response;
    }

    function sendConfirmReservationEmail($memberUserId, $propertyId, $date, $time, $person) {
        ini_set('max_execution_time', 300);
        $emailSender = new EmailSender();
        $result = [];
        $db = new Db();
        $link = $db->connect();

        $query_user = "SELECT * FROM `users` WHERE `UserID` = '" . $memberUserId . "'";
        $query_property = "SELECT * FROM `property` WHERE `PropertyID` = '" . $propertyId . "'";

        $user_stmt = $link->query($query_user);
        $user = $user_stmt->fetch(PDO::FETCH_ASSOC);

        $property_stmt = $link->query($query_property);
        $property = $property_stmt->fetch(PDO::FETCH_ASSOC);

        if (empty($user) and empty($property)) {
            $result = ["error" => "UserID/VenueID is wrong.", 'statusCode' => 400];
        } else {
            $query_clubadmin = "SELECT * FROM `users` WHERE `UserID` = '" . $property['UserID'] . "'";
            $clubAdmin_stmt = $link->query($query_clubadmin);
            $clubAdmin = $clubAdmin_stmt->fetch(PDO::FETCH_ASSOC);
            $result = $emailSender->sendConfirmationEmail($property, $user, $clubAdmin, $date, $time, $person);
        }
        $statusCode = $result['statusCode'];
        $requestContentType = $_SERVER ['HTTP_ACCEPT'];
        $this->setHttpHeaders($requestContentType, $statusCode);
        $response = $this->encodeJson($result);
        echo $response;
    }

    function sendActivationEmail($userName, $userEmail, $hashKey) {
        ini_set('max_execution_time', 300);
        $emailSender = new EmailSender();
        $result = $emailSender->sendUserActivationEmail($userName, $userEmail, $hashKey);
        return $result;
    }

    public function validate_email($email) {
        $validation_errors = [];
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $validation_errors["Email"] = "Email is invalid ...";
        }
        return !empty($validation_errors) ? ["error" => $validation_errors, "statusCode" => 400] : ["success" => "Field Validation Successfull"];
    }

    public function encodeJson($responseData) {
        $jsonResponse = json_encode($responseData);
        return $jsonResponse;
    }

}

<?php

include 'emailHandler.php';

if ($_GET['action'] === "test") {
    $data = json_decode(file_get_contents("php://input"), true);

    $toEmail = isset($data['toEmail']) ? $data['toEmail'] : '';
    $toName = isset($data['toName']) ? $data['toName'] : '';

    $emailHandler = new EmailHandler();
    $emailHandler->sendTestEmail($toEmail, $toName);
}

if ($_GET['action'] === "sendConfirm") {
    $data = json_decode(file_get_contents("php://input"), true);

    $memberUserId = isset($data['memberUserId']) ? $data['memberUserId'] : '';
    $propertyId = isset($data['propertyId']) ? $data['propertyId'] : '';
    $checkinDate = isset($data['checkinDate']) ? $data['checkinDate'] : '';
    $checkinTime = isset($data['checkinTime']) ? $data['checkinTime'] : '';
    $person = isset($data['person']) ? $data['person'] : '';

    $emailHandler = new EmailHandler();
    $emailHandler->sendConfirmReservationEmail($memberUserId, $propertyId, $checkinDate, $checkinTime, $person);
}
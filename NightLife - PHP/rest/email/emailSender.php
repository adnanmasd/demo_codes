<?php

require 'phpMailer/PHPMailerAutoload.php';

class EmailSender {

    public $config;

    function __construct() {
        $this->config = parse_ini_file($_SERVER ['DOCUMENT_ROOT'] . '/NightLife/rest/email/email.ini');
    }

    public function sendTestEmail($toEmail, $toName) {
        $mail = new PHPMailer;
        $mail->SMTPDebug = 3;
        $mail->isSMTP();
        $mail->Host = $this->config['outgoing-server'];
        $mail->SMTPAuth = true;
        $mail->Username = $this->config['test-username'];
        $mail->Password = $this->config['test-password'];
        $mail->Port = $this->config['outgoing-port'];
        $mail->setFrom($this->config['test-username'], 'Test Night-Life');
        $mail->addAddress($toEmail, $toName);
        $mail->addReplyTo($this->config['test-username'], 'Test Night-Life');
        $mail->isHTML(true);
        $mail->Subject = 'Here is the subject';
        $mail->Body = 'This is the HTML message body <b>in bold!</b>';
        $mail->AltBody = 'This is the body in plain text for non-HTML mail clients';

        if (!$mail->send()) {
            return ['error' => $mail->ErrorInfo, 'statusCode' => 400];
        } else {
            return ['success' => "Email has been sent successfully", 'statusCode' => 200];
        }
    }

    public function sendConfirmationEmail($property, $memberUser, $clubAdmin, $date, $time, $person) {
        $result = [];
        $isSentToEndUser = $this->sendConfirmReservationToEndUser($property, $memberUser, $clubAdmin, $date, $time, $person);
        if (array_key_exists('error', $isSentToEndUser)) {
            $result["isSentToEndUser"] = false;
            $result["EndUserError"] = $isSentToEndUser['error'];
        } else {
            $result["isSentToEndUser"] = true;
        }
        $isSentToClubAdmin = $this->sendConfirmReservationToClubAdmin($property, $memberUser, $clubAdmin, $date, $time, $person);
        if (array_key_exists('error', $isSentToClubAdmin)) {
            $result["isSentToClubAdmin"] = false;
            $result["ClubAdminError"] = $isSentToClubAdmin['error'];
        } else {
            $result["isSentToClubAdmin"] = true;
        }

        if ($result['isSentToClubAdmin'] === true and $result['isSentToEndUser'] === true) {
            return ['statusCode' => 200, 'success' => 'Email sent successfully.', 'details' => $result];
        } else {
            return ['statusCode' => 400, 'error' => 'Some error happened while sending email', 'details' => $result];
        }
    }

    public function sendConfirmReservationToEndUser($property, $memberUser, $clubAdmin, $date, $time, $person) {
        $recipientEmail = $memberUser['Email'];
        $recipientFirstName = $memberUser['FirstName'];
        $recipientLastName = $memberUser['LastName'];

        $propertyName = $property['PropertyName'];
        $address = $property['StreetAddress'];

        $managerName = $clubAdmin['FirstName'] . " " . $clubAdmin['LastName'];
        $managerPhone = $clubAdmin['MobileNumber'];

        $mail = new PHPMailer;
        $mail->SMTPDebug = 0;
        $mail->isSMTP();
        $mail->Host = $this->config['outgoing-server'];
        $mail->SMTPAuth = true;
        $mail->Username = $this->config['username'];
        $mail->Password = $this->config['password'];
        $mail->Port = $this->config['outgoing-port'];
        $mail->setFrom($this->config['username'], 'Confirm Reservation Night-Life');
        $mail->addAddress($recipientEmail, $recipientFirstName . " " . $recipientLastName);
        $mail->addReplyTo($this->config['username'], 'Confirm Reservation Night-Life');
        $mail->isHTML(true);
        $mail->Subject = 'Confirmed Reservation at ' . $property['PropertyName'];
        $mail->Body = file_get_contents($this->config['contentPath'] . 'confirmEndUser.php?' . 'propertyName=' . urlencode($propertyName) . '&address=' . urlencode($address) .
                '&date=' . urlencode($date) . '&time=' . urlencode($time) . '&persons=' . urlencode($person) . '&manager=' . urlencode($managerName) . '&managerPhone=' . urlencode($managerPhone));

        if (!$mail->send()) {
            return ['error' => $mail->ErrorInfo];
        } else {
            return ['success' => "Email to end user has been sent successfully"];
        }
    }

    public function sendConfirmReservationToClubAdmin($property, $memberUser, $clubAdmin, $date, $time, $person) {
        $recipientEmail = $clubAdmin['Email'];
        $recipientName = $clubAdmin['FirstName'] . " " . $clubAdmin['LastName'];
        $propertyName = $property['PropertyName'];

        $userName = $memberUser['FirstName'] . " " . $memberUser['LastName'];
        $userEmail = $memberUser['Email'];
        $userNumber = $memberUser['MobileNumber'];

        $mail = new PHPMailer;
        $mail->SMTPDebug = 0;
        $mail->isSMTP();
        $mail->Host = $this->config['outgoing-server'];
        $mail->SMTPAuth = true;
        $mail->Username = $this->config['username'];
        $mail->Password = $this->config['password'];
        $mail->Port = $this->config['outgoing-port'];
        $mail->setFrom($this->config['username'], 'Confirmed Reservation Night-Life');
        $mail->addAddress($recipientEmail, $recipientName);
        $mail->addReplyTo($this->config['username'], 'Confirmed Reservation Night-Life');
        $mail->isHTML(true);
        $mail->Subject = 'Confirm Reservation at ' . $propertyName;
        $mail->Body = file_get_contents($this->config['contentPath'] . 'confirmClubAdmin.php?' .
                "propertyName=" . urlencode($propertyName) . "&name=" . urlencode($userName) . "&email=" . urlencode($userEmail) . "&number=" . urlencode($userNumber) .
                "&date=" . urlencode($date) . "&time=" . urlencode($time) . "&person=" . urlencode($person));

        if (!$mail->send()) {
            return ['error' => $mail->ErrorInfo];
        } else {
            return ['success' => "Email to club admin has been sent successfully"];
        }
    }

    public function sendUserActivationEmail($userName, $userEmail, $hash) {
        $mail = new PHPMailer;
        $mail->SMTPDebug = 0;
        $mail->isSMTP();
        $mail->Host = $this->config['outgoing-server'];
        $mail->SMTPAuth = true;
        $mail->Username = $this->config['username'];
        $mail->Password = $this->config['password'];
        $mail->Port = $this->config['outgoing-port'];
        $mail->setFrom($this->config['username'], 'Account Activation | Night-Life');
        $mail->addAddress($userEmail, $userName);
        $mail->addReplyTo($this->config['username'], 'Account Activation | Night-Life');
        $mail->isHTML(true);
        $mail->Subject = 'Account Activation for ' . $userName;
        $mail->Body = file_get_contents($this->config['contentPath'] . 'sendActivation.php?' .
                "userName=" . urlencode($userName) . "&hashKey=" . urlencode($hash));

        if (!$mail->send()) {
            return ['error' => $mail->ErrorInfo];
        } else {
            return ['success' => "Activation Email sent"];
        }
    }

}

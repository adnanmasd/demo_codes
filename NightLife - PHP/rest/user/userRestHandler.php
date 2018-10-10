<?php

require_once '../SimpleRest.php';
require_once '../../persistance/userActions.php';

class UserRestHandler extends SimpleRest {

    function loginUser($email, $pass, $ip) {
        $userActions = new UserActions();
        $rawData = [];
        $result = [];
        $validation = $this->validate_login_fields($email, $pass, $ip);
        if (array_key_exists('error', $validation)) {
            $result = $validation;
            $rawData = $result;
            unset($rawData['statusCode']);
        } else {
            $result = $userActions->loginUser($email, $pass, $ip);
            if (array_key_exists('error', $result)) {
                $rawData = $result;
                unset($rawData['statusCode']);
            } else {
                $rawData = $result['object'];
            }
        }
        $statusCode = $result['statusCode'];
        $requestContentType = $_SERVER ['HTTP_ACCEPT'];
        $this->setHttpHeaders($requestContentType, $statusCode);
        $response = $this->encodeJson($rawData);
        echo $response;
    }

    function registerUser($first, $last, $email, $pass, $mobile, $userTypeID) {
        $userActions = new UserActions();
        $rawData = [];
        $result = [];

        $validation = $this->validate_register_fields($first, $last, $email, $pass, $mobile, $userTypeID);

        if (array_key_exists("error", $validation)) {
            $result = $validation;
            $rawData = $result['error'];
            unset($rawData['statusCode']);
        } else {
            $result = $userActions->registerUser($first, $last, $email, $pass, $mobile, $userTypeID);
            $rawData = $result;
            unset($rawData['statusCode']);
        }
        $statusCode = $result['statusCode'];
        $requestContentType = $_SERVER ['HTTP_ACCEPT'];
        $this->setHttpHeaders($requestContentType, $statusCode);
        $response = $this->encodeJson($rawData);
        echo $response;
    }

    public function encodeJson($responseData) {
        $jsonResponse = json_encode($responseData);
        return $jsonResponse;
    }

    public function validate_register_fields($first, $last, $email, $pass, $mobile, $userTypeID) {
        $validation_errors = [];
        if (!filter_var($first, FILTER_VALIDATE_REGEXP, ["options" => ["regexp" => "/^[a-zA-Z\s]+/"]])) {
            $validation_errors["First Name"] = "Fisrt Name is invalid ...";
        }
        if (!filter_var($last, FILTER_VALIDATE_REGEXP, ["options" => ["regexp" => "/^[a-zA-Z\s]+/"]])) {
            $validation_errors["Last Name"] = "Last Name is invalid ...";
        }
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $validation_errors["Email"] = "Email is invalid ...";
        }
        if (!filter_var($pass, FILTER_VALIDATE_REGEXP, ["options" => ["regexp" => "/^[\w\d]{6,}/"]])) {
            $validation_errors["Password"] = "Password is invalid ...";
        }
        if (!filter_var($mobile, FILTER_VALIDATE_REGEXP, ["options" => ["regexp" => "/^\+[\d]{12,13}/"]])) {
            $validation_errors["Mobile Number"] = "Mobile Number is invalid/incomplete ...";
        }
        if (!filter_var($userTypeID, FILTER_VALIDATE_REGEXP, ["options" => ["regexp" => "/^[\d]/"]])) {
            $validation_errors["UserTypeID"] = "User Type ID is incorrect/not defined ...";
        }
        return !empty($validation_errors) ? ["error" => $validation_errors, "statusCode" => 400] : ["success" => "Fields Validation Successfull"];
    }

    public function validate_login_fields($email, $pass, $ip) {
        $validation_errors = [];
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $validation_errors["Email"] = "Email is invalid ...";
        }
        if (!filter_var($pass, FILTER_VALIDATE_REGEXP, ["options" => ["regexp" => "/^[\w\d]{6,}/"]])) {
            $validation_errors["Password"] = "Password is invalid ...";
        }
        if (!filter_var($ip, FILTER_VALIDATE_IP)) {
            $validation_errors["IP"] = "IP is invalid ...";
        }
        return !empty($validation_errors) ? ["error" => $validation_errors, "statusCode" => 400] : ["success" => "Fields Validation Successfull"];
    }

}

<?php

require_once '../SimpleRest.php';
require_once '../../persistance/propertyActions.php';

class PropertyRestHandler extends SimpleRest {

    function getAllPropertiesInCity($city) {
        $property = new PropertyActions ();
        $rawData = [];
        $result = [];

        $validation = $this->validate_city($city);
        if (array_key_exists('error', $validation)) {
            $result = $validation;
            $rawData = $result;
            unset($rawData['statusCode']);
        } else {
            $result = $property->findAllVenuesInCity($city, "All");
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

    function getAllVenueInCity($city, $venue) {
        $property = new PropertyActions ();
        $rawData = [];
        $result = [];

        $validation = $this->validate_city_venue($city, $venue);
        if (array_key_exists('error', $validation)) {
            $result = $validation;
            $rawData = $result;
            unset($rawData['statusCode']);
        } else {
            $result = $property->findAllVenuesInCity($city, $venue);
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

    public function getProperty($id) {
        $property = new PropertyActions ();
        $rawData = [];
        $result = [];

        $validation = $this->validate_id($id);
        if (array_key_exists('error', $validation)) {
            $result = $validation;
            $rawData = $result;
            unset($rawData['statusCode']);
        } else {
            $result = $property->findPropertyById($id);
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

    public function getAllImagesData($id) {
        $property = new PropertyActions();
        $rawData = [];
        $result = [];

        $validation = $this->validate_id($id);
        if (array_key_exists('error', $validation)) {
            $result = $validation;
            $rawData = $result;
            unset($rawData['statusCode']);
        } else {
            $result = $property->getAllImagesDetails($id);
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
        return $response;
    }

    public function encodeJson($responseData) {
        $jsonResponse = json_encode($responseData);
        return $jsonResponse;
    }

    public function validate_city($city) {
        $validation_errors = [];
        if (!filter_var($city, FILTER_VALIDATE_REGEXP, ["options" => ["regexp" => "/^[\w\-]+/"]])) {
            $validation_errors["city"] = "City name is not valid";
        }
        return !empty($validation_errors) ? ["error" => $validation_errors, "statusCode" => 400] : ["success" => "City Validation Successfull"];
    }

    public function validate_city_venue($city, $venue) {
        $validation_errors = [];
        if (!filter_var($city, FILTER_VALIDATE_REGEXP, ["options" => ["regexp" => "/^[a-zA-Z\-]+/"]])) {
            $validation_errors["city"] = "City name is not valid";
        }
        if (!filter_var($venue, FILTER_VALIDATE_REGEXP, ["options" => ["regexp" => "/^[a-zA-Z]+/"]])) {
            $validation_errors["venue"] = "Venue name is not valid";
        }
        return !empty($validation_errors) ? ["error" => $validation_errors, "statusCode" => 400] : ["success" => "City And Venue Validation Successfull"];
    }

    public function validate_id($id) {
        $validation_errors = [];
        if (!filter_var($id, FILTER_VALIDATE_REGEXP, ["options" => ["regexp" => "/^[\d]+/"]])) {
            $validation_errors["id"] = "Venue Id is not valid";
        }
        return !empty($validation_errors) ? ["error" => $validation_errors, "statusCode" => 400] : ["success" => "Venue Id Validation Successfull"];
    }

}

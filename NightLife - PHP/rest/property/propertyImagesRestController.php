<?php

require_once './propertyRestHandler.php';

$propertyHandler = new PropertyRestHandler ();
$id = $_GET['id'];
$images = [];

function readDirectoryForImages($id) {
    $images = [];
    $dir = "../../uploads/$id";
    $files = scandir($dir);
    if ($files !== false) {
        foreach ($files as $file) {
            if ($file !== '.' and $file !== '..') {
                array_push($images, readData("uploads/$id" . "/" . $file));
            }
        }
    }
    return $images;
}

function readData($path) {
    if ($path == null) {
        return false;
    } else {
        return [
            "name" => array_pop((array_slice(explode('/', $path), -1))),
            "size" => filesize($_SERVER["DOCUMENT_ROOT"] . "/NightLife/" . $path),
            "type" => image_type_to_mime_type(exif_imagetype("../../" . $path)),
            "file" => "../" . $path,
            "url" => "http://" . $_SERVER['SERVER_NAME'] . ':' . $_SERVER['SERVER_PORT'] . "/NightLife/" . $path
        ];
    }
}

$imagesData = readDirectoryForImages($id);
echo (json_encode($imagesData, JSON_UNESCAPED_SLASHES));

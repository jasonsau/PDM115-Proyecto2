<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json; charset=UTF-8");
    header("Access-Control-Allow-Methods: POST,GET");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

    Class ResponseDevice{
        public $status;
        public $message;
        public $id = 0;
    }

    $response = new ResponseDevice();
    $response->status = "success";
    $response->message = "";
    $response->id = 0;
    try{
        $conexion = new PDO('mysql:host=192.168.1.2:3001;dbname=camara', 'encuesta', 'encuesta');

        $_POST['Device'];
        $_POST['id'];
        if(!isset($_POST['Device'])){
            $response->status = "error";
            $response->message = "No se ha recibido el Device";
            $response->id = 0;
        }
        $id = null;

        if(isset($_POST['id'])){
            $id = $_POST['id'];
        }
        if($id == null) {
            $sqlInsert = "INSERT INTO Device (device_id) VALUES(:Device)";
            $prepareStament = $conexion->prepare($sqlInsert, array(PDO::ATTR_CURSOR => PDO::CURSOR_FWDONLY));

            $prepareStament->execute(array(':Device' => $_POST['Device']));

            $result = $prepareStament->fetchAll();
            $response->id = $conexion->lastInsertId();
        } else{
            $sqlUpdate = "UPDATE Device SET device_id = :Device WHERE id = :id";
            $prepareStament = $conexion->prepare($sqlUpdate, array(PDO::ATTR_CURSOR => PDO::CURSOR_FWDONLY));
            $prepareStament->execute(array(':Device' => $_POST['Device'], ':id' => $id));
            $response->status = "success";
            $response->message = "Se ha actualizado el Device";
            $response->id = $id;
        }


    }catch(PDOException $e){
        $response->status = "error";
        $response->message = $e->getMessage();
        $response->id = 0;
    }
    $myJson = json_encode($response);
    echo $myJson;
?>
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
        if(isset($_POST['ID']) && isset($_POST['TITULO']) && isset($_POST['MENSAJE'])) {
            $id = $_POST['ID'];
            $titulo = $_POST['TITULO'];
            $mensaje = $_POST['MENSAJE'];
            if($id == null || $titulo == null || $mensaje == null) {
                $response->status = "error";
                $response->message = "No se ha recibido el Device";
                $response->id = 0;
            } else {
                $sqlSelect = "SELECT device_id FROM Device WHERE id = :id";
                $prepareStament = $conexion->prepare($sqlSelect, array(PDO::ATTR_CURSOR => PDO::CURSOR_FWDONLY));
                $prepareStament->execute(array(':id' => $id));
                $result = $prepareStament->fetchAll(PDO::FETCH_ASSOC);
                $device = $result[0]['device_id'];

                $url = 'https://fcm.googleapis.com/fcm/send';
                $fields = array(
                    'to' => $device,
                    'notification' => array(
                        'title' => $titulo,
                        'body' => $mensaje,
                        'sound' => 'default',
                    ),
                    'direct_book_ok' => true,
                );
                $headers = array( "authorization: key=AAAAR3SnQkI:APA91bEVAtWJieMvi5WhgUSfFX_TR67Nk4mXnSVaYHW0YVup1tJcIasrcqV178HdTFWuj67AUYotq0t_bUUxWK5Yo9ophzwI6YNuOude1C-sU17l79_t7m8aCTDUx-SDMNoyDME0ebbq", "content-type: application/json");

                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_POST, true);
                curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers );
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
                $result = curl_exec($ch);
                echo $result;
                if($result === FALSE){
                    die('Curl failed: ' . curl_error($ch));
                }
                curl_close($ch);

            }

        }

    }catch(PDOException $e){
        $response->status = "error";
        $response->message = $e->getMessage();
        $response->id = 0;
    }
    $myJson = json_encode($response);
    echo $myJson;
?>
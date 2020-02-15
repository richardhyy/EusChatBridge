<?php
//=======================================================================================================
// UniBan connection password
//=======================================================================================================

$password = "UniBanSecret";

if (!empty($_POST["password"])) {
    if ($_POST["password"] != $password) {
        die("Access denied.");
    }
}
else {
  die("Empty password.");
}

//=======================================================================================================
// Create new webhook in your Discord channel settings and copy&paste URL
//=======================================================================================================

$webhookurl = "WebHookURL";

//=======================================================================================================
// Compose message. You can use Markdown
// Message Formatting -- https://discordapp.com/developers/docs/reference#message-formatting
// ex. Test **message** [https://krasin.space(<URL>)
//========================================================================================================

if (empty($_POST["message"])) {
    die("Empty message.");
}
else if (empty($_POST["playername"])) {
    die("Empty player name.");
}

$msg = "**[" . $_POST["playername"] . "]** " . $_POST["message"]; // Change the message format if necessary

$json_data = array ('content'=>"$msg");
$make_json = json_encode($json_data);

$ch = curl_init( $webhookurl );
curl_setopt( $ch, CURLOPT_HTTPHEADER, array('Content-type: application/json'));
curl_setopt( $ch, CURLOPT_POST, 1);
curl_setopt( $ch, CURLOPT_POSTFIELDS, $make_json);
curl_setopt( $ch, CURLOPT_FOLLOWLOCATION, 1);
curl_setopt( $ch, CURLOPT_HEADER, 0);
curl_setopt( $ch, CURLOPT_RETURNTRANSFER, 1);

$response = curl_exec( $ch );
//If you need to debug, or find out why you can't send message uncomment line below, and execute script.
//echo $response;

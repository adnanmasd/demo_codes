<?php
/* GET Parameter: 
 * 
 * userName = name of the user
 * hashKey = hashKey
 *
 */
$config = parse_ini_file($_SERVER ['DOCUMENT_ROOT'] . '/NightLife/rest/email/email.ini');

$link = $config['baseUrl'] . "/controller/userActivation.php?hash=" . $_GET['hashKey'];
?>

<!doctype html>
<html>
    <head>
        <title>Account Activation for <?php echo $_GET['userName'] ?></title>
    </head>
    <body>
        <h2>Please click on the link to activate the account</h2>
        <a href="<?php echo $link ?>"><?php echo $link ?></a>
    </body>
</html>

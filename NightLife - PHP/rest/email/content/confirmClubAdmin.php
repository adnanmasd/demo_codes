<?php
/* GET Parameter: 
 * 
 * propertyName = name of the property
 * name = user name
 * email = user email
 * number = user number
 * date = check in date
 * time = check in time
 * person = check in person
 * 
 */
?>

<!doctype html>
<html>
    <head>
        <title>Confirmed Reservation in <?php echo $_GET['propertyName'] ?></title>
    </head>
    <body>
        <h2>You have just received a reservation</h2>
        <table cellspacing="2" style="border: 1px dashed #a346a4; width: 650px; height: 200px;">
            <tr>
                <th>Client Name:</th><td><?php echo $_GET['name'] ?></td>
            </tr>
            <tr style="background-color: rgba(163, 70, 164, 0.4);">
                <th>Client Email:</th><td><?php echo $_GET['email'] ?></td>
            </tr>
            <tr>
                <th>Client Number:</th><td><?php echo $_GET['number'] ?></td>
            </tr>
            <tr style="background-color: rgba(163, 70, 164, 0.4);">
                <th>Date:</th><td><?php echo $_GET['date'] ?></td>
            </tr>
            <tr>
                <th>Time:</th><td><?php echo $_GET['time'] ?></td>
            </tr>
            <tr style="background-color: rgba(163, 70, 164, 0.4);">
                <th>No. Of persons:</th><td><?php echo $_GET['person'] ?></td>
            </tr>
        </table>
    </body>
</html>

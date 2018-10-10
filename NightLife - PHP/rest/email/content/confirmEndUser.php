<?php
/* GET Parameter: 
 * 
 * propertyName = name of the property
 * address = street address of the property
 * date = date of checkin
 * time = time of checkin
 * persons = number of persons
 * manager = veneu manager name
 * managerPhone = venue manager phone
 * 
 */
?>

<!doctype html>
<html>
    <head>
        <title>Confirmed Reservation in <?php echo $_GET['propertyName'] ?></title>
    </head>
    <body>
        <h2>Your reservation is sent to</h2>
        <table cellspacing="2" style="border: 1px dashed #a346a4; width: 650px; height: 200px;">
            <tr>
                <th>Name:</th><td><?php echo $_GET['propertyName'] ?></td>
            </tr>
            <tr style="background-color: rgba(163, 70, 164, 0.4);">
                <th>Street Address:</th><td><?php echo $_GET['address'] ?></td>
            </tr>
            <tr>
                <th>Date:</th><td><?php echo $_GET['date'] ?></td>
            </tr>
            <tr style="background-color: rgba(163, 70, 164, 0.4);">
                <th>Time:</th><td><?php echo $_GET['time'] ?></td>
            </tr>
            <tr>
                <th>No. of person(s):</th><td><?php echo $_GET['persons'] ?></td>
            </tr>
            <tr style="background-color: rgba(163, 70, 164, 0.4);">
                <th>Manager Name:</th><td><?php echo $_GET['manager'] ?></td>
            </tr>
            <tr>
                <th>Manager Number:</th><td><?php echo $_GET['managerPhone'] ?></td>
            </tr>
        </table>
    </body>
</html>

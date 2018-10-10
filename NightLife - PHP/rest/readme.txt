RESTFUL SERVICES USE:

All the RESTful services are located at /NightLife/rest/
All the RESTful services will return data in JSON format only
There is no proper error checking and validation currently


Services currently working :
    1. getting property in city
    2. getting specific property_type in city
    3. get a specific property by id
    4. getting user for login
    5. posting user for registration
    6. getting all images data for a property
    7. Sending a test email
    8. Sending Confirm Reservation Email

/**********************************************************************************************************************************************************************************************/

1. GETTING ALL PROPERTIES IN CITY : 
    You can get all the properties listed in the particular city by calling GET on URL below :

        http://localhost:8883/NightLife/rest/property/propertyRestController.php?city={{cityName}}

        BASE_URL      =   http://localhost:8883
        REST_PATH     =   /NightLife/rest 
        SERVICE       =   /property/propertyRestController.php
        Arguments     =>  [
                            GET : cityName --> name of the city to search in
                          ]
    This will return the JSON data of the properties in the {{cityName}}
    *****
    UPDATE 26/10 :
    This will return the JSON data of an array. The array will have :
            {
                "Property" : {{property}}
                "Features" : {{property_features}}
                "Images"   : {{property_images}}
            }
    UPDATE 3/11:
    This will return the JSON data of an array. The array will have :
            {
                "Property" : {{property}}
                "Features" : {{property_features}}
            }


/**********************************************************************************************************************************************************************************************/

2. GETTING SPECIFIC PROPERTY_TYPE IN CITY :
    You can get all the properties from a specific property_type (e.g. club, restaurant etc) in a particular city by calling GET on URL below :
        
        http://localhost:8883/NightLife/rest/property/propertyRestController.php?city={{cityName}}&venue={{propertyTypeName}}

        BASE_URL        =  http://localhost:8883
        REST_PATH       =  /NightLife/rest
        SERVICE         =  /property/propertyRestController.php
        Arguments       =>  [
                               GET : cityName -->  name of the city to search in
                               GET : venue    -->  name of the property type (e.g. club, restaurant)
                            ]
    This will return the JSON data of all the specific {{propertyTypeName}} in {{cityName}} 


/**********************************************************************************************************************************************************************************************/

3. GET A SPECIFIC PROPERTY BY ID : 
    You can get the specific property data by calling GET on the URL below with property_id provided:

        http://localhost:8883/NightLife/rest/property/propertyRestController.php?id={{propertyID}}

        BASE_URL        = http://localhost:8883
        REST_PATH       = /NightLife/rest
        SERVICE         = /property/propertyRestController.php
        Argument        => [
                                GET: propertyID  -->  ID of the property required to retrive
                           ]
    This will return the property data in JSON format.


/**********************************************************************************************************************************************************************************************/

4. GETTING USER FOR LOGIN :
    You can get the specific user from database by posting JSON and providing the username and password for login purpose.

        http://localhost:8883/NightLife/rest/user/userRestController.php?action=login
        
        BASE_URL        = http://localhost:8883
        REST_PATH       = /NightLife/rest
        SERVICE         = /user/userRestController.php?action=login
        ARGUMENTS       => [
                                JSON :  email       -->  email of the user
                                JSON :  password    -->  password of the user
                                JSON :  ip          -->  ip of the user
                           ]  
    This will update the last login info of the user after returning the user data in JSON format. 


/**********************************************************************************************************************************************************************************************/

5. POSTING USER FOR REGISTRATION :
    You can register the user by posting the JSON data to the following URL:

        http://localhost:8883/NightLife/rest/user/userRestController.php?action=register
        
        BASE_URL        = http://localhost:8883
        REST_PATH       = /NightLife/rest
        SERVICE         = /user/userRestController.php?action=register
        ARGUMENTS       => [
                                JSON :  firstName   -->  firstname of the user
                                JSON :  lastName    -->  lastname of the user
                                JSON :  email       -->  email of the user
                                JSON :  password    -->  password of the user
                                JSON :  mobileNumber-->  mobile number of the user
                                JSON :  userTypeID  -->  user type id (1 for memberuser, 2 for clubadmin)
                            ]
    This will store the userdata in user table.

    UPDATE : New Argument added : userTypeID
    
/**************************************************************************************************************************************************************************************************/

6. GETTING ALL IMAGES DATA FOR A PROPERTY:
    You can get the information about the property images by calling following url:
        
        http://localhost:8883/NightLife/rest/property/propertyImagesRestController.php?id={{propertyID}}
        BASE_URL        = http://localhost:8883
        REST_PATH       = /NightLife/rest
        SERVICE         = /property/propertyImagesRestController.php?id={{propertyID}}
        ARGUMENTS       => [
                                GET : id --> property ID
                           ]

        This will return the images information in following format:
                {
                    name => file name
                    size => file size
                    type => file mime type
                    file => path to file
                    url  => url of the file
                }

/**************************************************************************************************************************************************************************************************/
    
7. Sending a test email
You can send a test email via this web service by:
        
        http://localhost:8883/NightLife/rest/email/emailController.php?action=test
        BASE_URL        = http://localhost:8883
        REST_PATH       = /NightLife/rest
        SERVICE         = /email/emailController.php?action=test
        ARGUMENTS       => [
                                POST : toEmail  --> the email of the recipient
                                POST : toName   --> the name of the person
                           ]
    
/**************************************************************************************************************************************************************************************************/

8. Sending Confirm Reservation Email
You can send confirm reservation email by:
        
        http://localhost:8883/NightLife/rest/email/emailController.php?action=sendConfirm
        BASE_URL        = http://localhost:8883
        REST_PATH       = /NightLife/rest
        SERVICE         = /email/emailController.php?action=sendConfirm
        ARGUMENTS       => [
                                POST : memberUserId  --> the user id of the member
                                POST : propertyId    --> property id of the property
                                POST : checkinDate   --> checkin Date of the member
                                POST : checkinTime   --> checkin Time of the member
                                POST : person        --> Number of persons checking in
                           ]
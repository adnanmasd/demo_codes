AutoCopyProductsOfTheDay : There was a requirement where we store the featured products of the day on a calender.
                           But if some day is missing or empty, it automatically copies the featured products from the last day.
						   It was scheduled to run every midnight.


Deactivation : Uses the TimerService, if the registered user doesnot verify the email in specified time,
                the system automatically deletes the user from the db


Registration : During the saving of the product, the Images were saved either on Disk Or Amazon s3
               based on the configuration of the application 
			   (local development servers were using disk as image save/retieve and live server was using Amazon s3)


User : The Role based authenticaiton is implemented inside the code.
# food-delivery-application
Application designed specifically for the trial task

## Technologies used
* Java
* Spring framework
* H2 database
* Lombok - used for getters and setters, makes the code more clean and human readable
* Liquibase - used for comfortable use of database and manage inserted values for fee calculation
* mockito - used for mocking database and repository for testing

## Database
Stations database table store this information about a weather station:
* Name of the station
* WMO code of the station
* Air temperature
* Wind speed
* Weather phenomenon
* Timestamp of the observations
<br/>
Additional tables are implemented to store every type of fee and their values.


## CronJob for importing weather data
CronJob is used to retrieve data from https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php and the scheduled time is configurable in application.properties. <br/>
Requested data is stored in the stations database table.

## Delivery fee
Delivery fee is calculated according to task. Fee calculations are based on predefined parameters stored in respective database tables.

## REST interface
REST interface endpoints allow to get fees depending on city, vehicle and date(optional), and add new fee values in the database.
Every single requests is sent in a form http://localhost:8080/ + {endpointName}, where endpoint is the one specified in the mapping.
Endpoints:

1. Retrieve Delivery Fee by City and Vehicle
Method: GET <br/>
Endpoint: /{city}/{vehicle} <br/>
Description: Retrieves the fee for delivery based on the specified city and vehicle type.<br/>
Parameters:<br/>
* city (String): The city for delivery.
* vehicle (String): The type of vehicle used for delivery.
Returns: The total fee for delivery.<br/>
Throws: IOException if an invalid city or vehicle is entered.<br/>
Retrieve Delivery Fee by City, Vehicle, and Date<br/>

2. Retrieve Delivery Fee by City and Vehicle and Date
Method: GET<br/>
Endpoint: /{city}/{vehicle}/{date}<br/>
Description: Retrieves the fee for delivery based on the specified city, vehicle type, and delivery date.<br/>
Parameters:
* city (String): The city for delivery.
* vehicle (String): The type of vehicle used for delivery.
* date (Optional, LocalDateTime (format: yyyy-MM-dd-HH-mm-ss): The date and time of delivery.
Returns: The total fee for delivery.<br/>

3. Update RBF
Method: PUT<br/>
Endpoint: /rbf<br/>
Description: Updates the fees for regional base fees (RBF), allowing them to be used in fee calculations.<br/>
Request Body: RBFFeesDTO containing city, vehicle, and fee information.<br/>

4. Update ATEF
Method: PUT<br/>
Endpoint: /atef<br/>
Description: Updates the fees for air temperature fees (ATE), but these fees are not used directly in fee calculations without additional conditions in DeliveryService.<br/>
Request Body: ATEFFeesDTO containing vehicle, temperature, and fee information.<br/>

5. Update WSEF
Method: PUT<br/>
Endpoint: /wsef<br/>
Description: Updates the fees for WSEFFees, but these fees are not used directly in fee calculations without additional conditions in DeliveryService.<br/>
Request Body: WSEFFeesDTO containing vehicle, speed, and fee information.<br/>

6. Update WPEF
Method: PUT<br/>
Endpoint: /wpef<br/>
Description: Updates the fees for WPEFFees, allowing them to be used in fee calculations.<br/>
Request Body: WPEFFeesDTO containing vehicle, weather, and fee information.<br/>

## Bonuses
As a bonus excersise, you can get fees depending on city, vehicle and date(optional) using REST interface endpoint.
it is possible to change RBF without changing conditions in the method in Delivery Service, however the rest of the fees must be changed in the method to work with new conditions. 

## Tests
The application has tests for DeliveryService with 100% method and 77% line coverage, and UpdateDataFeeService with 100% method and 87% line coverage. 


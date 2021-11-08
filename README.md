# Tracking Mobile Sensors
This android application was built to monitor mobile sensors and upload the same in user databases in Firebase. Background services, power optmization, GPS satellite trackiong has also been implementation in addition to other conventional sensors.

# Sensor Implementation
Data collection for the following have been implemented. 
1. Geo Magnetic Rotation Sensor
2. Linear Acceleration Sensor
3. Gyroscope
4. Magnetic Field
5. Proximity Sensor
6. Ambient Light Sensor
7. Pressure Sensor
8. Relative humidity sensor
9. Gravity Sensor
10. Pedometer
11. GPS satellite tracking

# Firebase Connection
1. Login Page : The user gives permission set for logging into the application. An unique ID with password is created in firebase. 
2. Main Sensor data collection : After user gives permission for data collection a background service runs which collects data and uploads to Firebase.
3. Alternative storage options : The data can also be stored in a CSV file in the phone itself. Each date has a different CSV file in which data is entered and collected.

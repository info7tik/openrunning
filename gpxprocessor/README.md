# GpxProcessor
Parse GPX files to extract running statistics.

## Compilation
```
mvn clean package -DskipTests
```

## Run the application
* Read the GPX files in arguments (compute the total distance, the total time and the pace)
```
java -jar target/gpxprocessor-0.0.1-SNAPSHOT.jar activities/activity_1152705872.gpx activities/activity_1152706063.gpx activities/activity_1187540398.gpx activities/activity_1187540661.gpx
```
* Read the GPX files in arguments and generate statistics with the record module and the frequency module.
  * The available statistic modules are listed in the file [StatisticModuleName.java](src/main/java/fr/openrunning/gpxprocessor/statistics/StatisticModuleName.java)
  * The record module computes the best times for specific distances defined in the file
    [StatisticModuleManager.java](src/main/java/fr/openrunning/gpxprocessor/statistics/StatisticModuleManager.java)
  * The frequency module aggregates the runs per week, month and year.
```
java -jar target/gpxprocessor-0.0.1-SNAPSHOT.jar activities/activity_1152705872.gpx activities/activity_1152706063.gpx activities/activity_1187540398.gpx activities/activity_1187540661.gpx --stats=record --stats=frequency
```
* Read the GPX files in arguments, generate statistics and save the data to the database (the email `me@home.com` must
  exist in the table `users`)
```
java -jar target/gpxprocessor-0.0.1-SNAPSHOT.jar activities/activity_1152705872.gpx activities/activity_1152706063.gpx activities/activity_1187540398.gpx activities/activity_1187540661.gpx --stats=record --save --user=me@home.com
```

## Clear the generated data
Connect to the MariaDB server and remove all tables except the `users` table which contains the user accounts (email and passwords)

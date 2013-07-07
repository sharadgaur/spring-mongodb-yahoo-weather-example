spring-mongodb-yahoo-weather-example
====================================
There are 3 parts - A data collector, data processor and a web service.

Data Collector
Create an application/process that populates a data store[1] with all of the valid zip codes in Texas.  The zip codes in the data store[1] should then be used as an input for calling the old Yahoo weather API to retrieve weather data (low temp, high temp) for that zip code and the results are populated into another data store[2].
 
Data Processor
Create an application/process that processes the data store[2] to pull out the lowest temp, highest temp and average temp across all zip codes on a given day and the results are populated into another data store[3].  The processing should be done via map/reduce jobs.
 
Web Service
Create a simple web service that has a method which takes an input parameter of a zip code.  The method should call the old Yahoo weather API to retrieve the weather for that location, aggregate in the information from the data store [3] for the current day, and then finally a result is passed back to the caller.

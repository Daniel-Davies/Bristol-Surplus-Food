
# Draft JSON API

This is the mimumum required data for our current app concept 

# Objects and DataType

ID's for each object should be unique but only within their category. 

Eg. a distributor and beneficiary can both have an id of 0 but a no two distributors can have an id of 0

## Distributor

 - Int : ID
 - String : Name
 - Int : Preferred Transport (enum)
 - [Beneficiary] : Chosen beneficiaries of the day (list of ID's)
 - [Supplier] : Chosen suppliers of the day  (list of ID's)
 - (Float, Float) : Location 

## Beneficiary

 - Int : ID
 - String : Name
 - String : Time required for that day (formatted like "HH:MM")
 - (Float, Float) : Location 
 - [(String, Int)] : Food type and amount needed that day (measured in boxes/crates?)

## Supplier

 - Int : ID
 - String : Name
 - String : Time distributor should arrive (formatted like "HH:MM")
 - (Float, Float) : Location 
 - [(String, Int)] : Food type and amount available that day (measured in boxes/crates?)

# Example GET and POST requests with result

## GET getting objects


    GET example.com/distributers/1

Returns distributer with ID 1

    “{
        “id”:1,	
    	“name”:”Nick Wu”,
    	“transport”:3,
        
    	“location”:{
    		“longitude”:52,
    		“latitude”:14
        },
    	
    	“chosenCharities”:{
        	2, 5, 1
        },
    	
    	“chosenSuppliers”{
            1, 8, 2
        }
    }”

In the app, getting the greyed out charities ->

    GET example.com/distributers

Returns 8 for example

    For x in 8{
    
    	distributer a = GET example.com/distributers/x
    
    	greyOut(a.chosenCharities)
    
    }


Would be the same with every object

## POST creating objects

    POST example.com/distrubutors 

Data sent: JSON formatted the same as above

Should return an ID if valid. If not valid, should return -1 or some other not valid ID?

This should create a distributor on the database

    POST example.com/distributors <- "{ "chosenCharities":{1, 2, 5} }" 
    
should return -1 as it is not a valid (missing other fields) 

Once the ID is returned correctly:

    POST example.com/distributers/1 <- "(VALID JSON)" 

should update the database so that the distributor of ID 1 is changed. This is for updating the chosen charities etc.

This could return either 0 or 1 for invalid or valid

A server suggestion:

Should be able to POST example.com/distributors/1 <- "{ "chosenCharities":{1, 2, 5} }" as this would only update the relevant information instead of resending all the data that does not change. EG the name or ID. 

---------

## GET checking for database update

This is for making the app display changes to chosen charities in realtime. Eg, if another distributor has already chosen a charity then this functionality is needed to grey out the box in realtime.


insert id instead of 1

    GET example.com/distributors/1/update

should return either 0 or 1 if a change has been made on the database since the last request. 

Eg. On first login of that day this should return 1. It should then return 0 until anything on the database changes, then the app can get the distributor list and see if a charity has been updated.

The app will then make this get request every X seconds to keep the UI updated with the current state of the database.

This check should NOT be triggered when the location of a distributor changes. As this will be happening all the time and not required to update the UI of the app.




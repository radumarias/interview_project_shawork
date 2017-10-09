Using the data from Google places https://developers.google.com/places/web-service/search create a simple app to save places based on city and have the CRUD operations on them.

Implementation:
1. Have a list with places based on city, have a combo or something to select the city and show places only from that location
2. Populate the list by doing a search with let’s’ say 5km radius and keep the results in db and on a future search match the existing ones and override the data only if the user did not changed the data from your app, simple dirty flag will do. On first edit on a place you fetch the details using the details api.
3. Implement CRUD operations on each of  these places, also show images
4. Implement various filters on the list, choose the fields you think it would matter for you as a traveler :)

Bonus:
5. In list view show a map with all the places. I think this will help https://develpers.google.com/maps/documentation/javascript/tutorial if not google is your friend :)

Technologies to use:
PostgreSQL
Jetty
GWT
Guice
JPA

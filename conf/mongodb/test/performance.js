var noOfLatitude = noOfLongitude = 1000;
var noOfRecords = noOfRows * noOfLongitude;
var distanceDelta = 0.0001;

var centrePoint = { latitude: 0, longitude: 0 };

var coordinates = [centrePoint.latitude - distanceDelta * noOfLatitude, centrePoint.longitude - distanceDelta * noOfLongitude];

for (i = 0; i < noOfLatitude; i += 1) {
	for (j = 0; j < noOfLongitude; j += 1) {
		db.locations.insert({layerType: 'performanceTest', description: 'Performance i = ' + i + ' j = ' + j, coordinates: coordinates });
		coordinates[0] += distanceDelta;
		coordinates[1] += distanceDelta;
	}
}


db.locations.ensureIndex({coordinates: '2d', layerType: 1});

db.clients.ensureIndex({email: 1 }, {unique: true});

db.clients.ensureIndex({layerType: 1 });

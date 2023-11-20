/**
 *
 * @author Bella Hill
 * @author Steven Hockey
 **/



let map; // the map (L.map)
let crashMarkers = []; // the crash markers that are currently displayed, in a list for the purpose of easy removal.
let routingControl; // an object that handles the routing and directions (L.routing.control)
let areaCircle; // the circle representing the circular search area
let areaMarker; // the marker showing the centre of the circular search area
let heatCircle; // the circle representing the circular search area, but for the heatmap
let heatMarkers = []; // the heat markers that are currently displayed
var crashManager; // used for js -> java communication
var routePolygon; // the polygon that tightly wraps around the route, used to find crashes on the route.

const BUFFER_RADIUS = 0.05;

// for clustering points
let markerCluster = new L.markerClusterGroup({
    showCoverageOnHover: false,
    removeOutsideVisibleBounds: true, // for performance
    spiderLegPolylineOptions: { weight: 0, color: '#000', opacity: 0 }
});

// for the heatmap
let heatMap = L.heatLayer(heatMarkers, {minOpacity: 0.25, radius: 25, gradient: {.25:"lime",.5:"yellow",.75:"orange",1:"red"}});

// for the interesting bug where if there was only one marker in markerCluster it wouldn't display
let markerLayer = new L.layerGroup;

// icon for a crashMarker
let crashMarkerIcon = new L.icon({
    iconUrl: '../img/crash_warning.png',
    iconSize: [40, 40],
    iconAnchor: [0, 40]
});

// icon for an areaMarker
let areaMarkerIcon = new L.icon({
    iconUrl: '../img/start_icon.png',
    iconSize: [14, 14], // Set the icon size
    iconAnchor: [7, 7], // Set the icon anchor
});

// icon for the start and end of a route
let routeIcon = new L.icon({
    iconUrl: '../img/route_icon.png',
    iconSize: [40, 40], // Set the icon size
    iconAnchor: [21, 37], // Set the icon anchor
});

//for more complex things such as tooltips
let routeIconLayer = new L.layerGroup;

/**
 * This object can be returned to our java code, where we can call the functions we define inside it
 */
let jsConnector = {
    initMap: initMap,

    addCrashMarkers: addCrashMarkers,
    displayCrashMarkers: displayCrashMarkers,
    removeCrashMarkers: removeCrashMarkers,

    addHeatMarker: addHeatMarker,
    displayHeatMap: displayHeatMap,
    removeHeatMap: removeHeatMap,

    displayAreaCircle: displayAreaCircle,
    removeAreaCircle: removeAreaCircle,

    displayRoute: displayRoute,
    receiveCrashesInBounds: receiveCrashesInBounds,
    removeRoute: removeRoute,

    crashMarkerIcon: crashMarkerIcon
};

/**
 * Creates and initialises the map
 */
function initMap() {
    var mapOptions = {
        center: [-43.522480501006044, 172.58119632272948],
        zoom: 15,
        zoomControl: false,
        doubleClickZoom: false,
        minZoom: 5.3,
        maxBounds: [[-50, 150], [-30, 200]]
    };
    map = new L.map('map', mapOptions);
    new L.TileLayer('https://tile.csse.canterbury.ac.nz/hot/{z}/{x}/{y}.png', { // UCs tilemap server
        attribution: '© OpenStreetMap contributors<br>Served by University of Canterbury'
    }).addTo(map);
}

/**
 * Adds a single crash marker to the map, and handles the weird single crash bug
 * @param latlng the coordinate of the marker
 */
function addCrashMarker(latlng) {

    markerLayer.clearLayers();

    let crashMarker = L.marker(latlng, {
        icon: crashMarkerIcon
    });

    if (crashMarkers.length === 0) {
        markerLayer.addLayer(crashMarker);
    }

    markerCluster.addLayer(crashMarker);
    crashMarkers.push(crashMarker);
}

/**
 * Adds a list of crashes from java to the map
 * @param javaCrashes list of latitudes and longitudes of crashes
 */
function addCrashMarkers(javaCrashes) {
    markerLayer.clearLayers();

    var crashes = JSON.parse(javaCrashes);

    if(crashes.length===2) {
        let crashMarker = L.marker([crashes[0], crashes[1]], {
            icon: crashMarkerIcon
        });

        markerLayer.addLayer(crashMarker);

    }
    for (let i = 0; i < crashes.length; i=i+2) {
        let crashMarker = L.marker([crashes[i], crashes[i+1]], {
            icon: crashMarkerIcon
        });
        markerCluster.addLayer(crashMarker);
        crashMarkers.push(crashMarker);
    }
}

/**
 * Adds markerCluster to the map
 */
function displayCrashMarkers() {
    map.addLayer(markerCluster);
    map.addLayer(markerLayer);

}

/**
 * Removes all currently displayed crash markers from the map and removes markerCLuster from the map
 */
function removeCrashMarkers() {
    markerCluster.clearLayers();
    markerLayer.clearLayers();
    crashMarkers = []; // Empty the markers array
    map.removeLayer(markerCluster);
}

/**
 * Adds a heat marker for the given parameters.
 * @param title (unused)
 * @param lat Latitude
 * @param lng Longitude
 * @param severity The given severity of the crash
 */
function addHeatMarker(title, lat, lng, severity) {
    let gradient;
    if (severity==="Non-Injury Crash") {
        gradient = 0.25;
    } else if (severity==="Minor Crash") {
        gradient = 0.5;
    } else if (severity==="Serious Crash") {
        gradient = 0.75;
    } else if (severity==="Fatal Crash") {
        gradient = 1;
    }
    heatMarkers.push([lat, lng, gradient]);
}

/**
 * Displays the heat map by adding it to the map.
 * @param centre the centre of the circular area
 * @param radius the radius of the circular area
 */
function displayHeatMap(centre, radius) {
    heatMap.addTo(map);
    heatCircle = new L.circle([centre.lat, centre.lng],
        {
            radius: radius*1000
        });
    heatCircle.addTo(map);
    map.fitBounds(heatCircle.getBounds());
    heatCircle.remove();
    var heatCanvas = heatMap._canvas;
    heatCanvas.style.opacity = '0.7';
}

/**
 * Removes heatMap from the map
 */
function removeHeatMap() {
    if(heatMap) {
        heatMarkers.length = 0;
        heatMap.remove();
    }
}

/**
 * Converts a user-entered address into a possibly better looking one by converting the first letter of every necessary
 * word to uppercase.
 * @param address the address string
 * @returns {string} the converted address string
 */
function capitaliseAddresses(address) {
    const words = address.split(" ");
    const commonWordsToExclude = ["of", "a", "an", "the", "in", "on"];

    return words.map((word, index) => {
        const lowercaseWord = word.toLowerCase();

        // Check if the word is a common word to exclude or a numeric value.
        if (commonWordsToExclude.includes(lowercaseWord) || !isNaN(word)) {
            return word; // Leave common words and numeric values as they are.
        } else {
            // Capitalise the word and check if there is a letter following a number.
            const capitalisedWord = word[0].toUpperCase() + word.substring(1);
            if (index > 0 && !isNaN(words[index - 1])) {
                return capitalisedWord; // Capitalise if it follows a number.
            } else if (word.match(/^\d+[a-zA-Z]$/)) {
                return word.replace(/\d+([a-zA-Z])/, (match, letter) => {
                    return match.replace(letter, letter.toUpperCase());
                }); // Capitalise if it is a number followed by a single letter.
            } else {
                return capitalisedWord; // Capitalise normally.
            }
        }
    }).join(" ");
}

/**
 * Displays a red circular area and a blue central dot, indicate the area of a search
 * @param centre the centre of the search circle
 * @param radius the radius of the search circle
 * @param centreStr the centre address of the search circle
 */
function displayAreaCircle(centre, radius, centreStr) {
    const address = capitaliseAddresses(centreStr);

    areaCircle = new L.circle([centre.lat, centre.lng],
        {
            radius: radius*1000,
            color: "#022d91"
        });
    areaCircle.addTo(map);
    map.fitBounds(areaCircle.getBounds());

    areaMarker = L.marker([centre.lat, centre.lng], {
        icon: areaMarkerIcon
    }).bindTooltip(address, {
        className: "tooltip"
    });


    areaMarker.addTo(map);
}

/**
 * Removes the area circle (if it exists)
 */
function removeAreaCircle() {
    if (areaCircle) {
        areaCircle.remove();
    }
    if (areaMarker) {
        areaMarker.unbindTooltip();
        areaMarker.remove();
    }
}

/**
 * Displays a route on the map, then sets up a listener that upon a route being made, will get the bounding box of the
 * route and send it to java
 *
 * @param waypointsIn a string representation of an array of lat lng json objects [("lat": -42.0, "lng": 173.0), ...]
 * @param transportMode a string representation of the chosen transport mode.
 * @param startStr start address for route
 * @param endStr end address for route
 */
function displayRoute(waypointsIn, transportMode, startStr, endStr) {
    var waypointsArray = JSON.parse(waypointsIn);
    var waypoints = [];
    waypointsArray.forEach(element => waypoints.push(new L.latLng(element.lat, element.lng)));
    map.fitBounds(waypoints);

    const startAddress = capitaliseAddresses(startStr);
    const endAddress = capitaliseAddresses(endStr);
    routeIconLayer.clearLayers();
    routeIconLayer.unbindTooltip();

    let startMarker = L.marker(waypoints[0], {
        icon: routeIcon
    }).bindTooltip("Start: "+ startAddress);

    let endMarker = L.marker(waypoints[1], {
        icon: routeIcon
    }).bindTooltip("End: " + endAddress);


    routeIconLayer.addLayer(startMarker);
    routeIconLayer.addLayer(endMarker);
    routeIconLayer.addTo(map);

    routingControl = new L.Routing.control({
        waypoints: waypoints,
        draggableWaypoints: false,
        addWaypoints: false,
        lineOptions: {
            styles: [{color: "black"}]
        },
        router: L.Routing.graphHopper('eac80048-7fe7-433d-9875-f83b424b76e0', {
            urlParameters: {
                vehicle: transportMode,
                algorithm: "alternative_route",
                'alternative_route.max_paths': 3,
                'alternative_route.max_weight_factor': 2
            }
        }),
        createMarker: function() { return null; },
    });

    routingControl.addTo(map);

    routingControl.on("routeselected", function(routeSelectedEvent) {

        removeCrashMarkers();
        // get coordinates of polyline describing route, add swap lat and lng (for turf.js)
        let coords = routeSelectedEvent.route.coordinates;
        let swappedCoords = [];
        coords.forEach(coord => swappedCoords.push([coord.lng, coord.lat]));

        routePolygon = null;
        routePolygon = turf.buffer(turf.lineString(swappedCoords), BUFFER_RADIUS, {units: 'kilometres', steps: 1});

        let [minLat, maxLat, minLng, maxLng] = calculateBounds(coords);

        crashManager.getBoundsOfRouteFromJS(minLat, maxLat, minLng, maxLng);
    });

    displayCrashMarkers();
}

/**
 * This function is called by Java, and uses turf.js to find what crashes in the crashesIn list are contained within
 * the "route polygon"
 * @param crashesIn a list of crashes queried from the database using a bounding box.
 */
function receiveCrashesInBounds(crashesIn) {
    var crashes = JSON.parse(crashesIn);
    let crashIndexes = [];

    for (let i = 0; i < crashes.length; i=i+2) {
        // NOTE: turf.js takes coordinates as [lng, lat] as opposed to leaflet [lat, lng]
        let lat = crashes[i];
        let lng = crashes[i+1];
        if (turf.booleanPointInPolygon(turf.point([lng, lat]), routePolygon)) {
            crashIndexes.push(i/2);
            addCrashMarker([lat, lng]);
        }
    }

    displayCrashMarkers();
    
    crashManager.getCrashesOnRouteFromJS(crashIndexes);
}

/**
 * Removes the current route being displayed (will not do anything if there is no route currently displayed)
 */
function removeRoute() {
    if(routingControl) {
        routingControl.remove();
    }
    routeIconLayer.clearLayers();
    routeIconLayer.unbindTooltip();
}

/**
 * Calculates minimum lats and lngs from a list of coordinates.
 * @param coords
 * @returns {*[]}
 */
function calculateBounds(coords) {
    let minLat = coords[0].lat,
        maxLat = coords[0].lat,
        minLng = coords[0].lng,
        maxLng = coords[0].lng;

    for (const coord of coords) {
        minLat = Math.min(minLat, coord.lat);
        maxLat = Math.max(maxLat, coord.lat);
        minLng = Math.min(minLng, coord.lng);
        maxLng = Math.max(maxLng, coord.lng);
    }

    return [minLat, maxLat, minLng, maxLng];
}
#include <ArduinoJson.h>

enum IssueType { ROAD, LIGHTNING, GREEN_SPACES, PUBLIC_DOMAIN, PUBLIC_DISORDER,
          PUBLIC_TRANSPORT, BUILDINGS, TRAFFIC_ROAD_SIGNS, ANIMALS };

const String typeStrings[] = {
  "ROAD",
  "LIGHTNING",
  "GREEN_SPACES",
  "PUBLIC_DOMAIN",
  "PUBLIC_DISORDER",
  "PUBLIC_TRANSPORT",
  "BUILDINGS",
  "TRAFFIC_ROAD_SIGNS",
  "ANIMALS"
};

const double UPPER_LEFT_LAT = 47.09099;
const double UPPER_LEFT_LNG = 21.86040;
const double LOWER_RIGHT_LAT = 47.02749;
const double LOWER_RIGHT_LNG = 21.95793;


typedef struct {
  double lat;
  double lng;
} Address;


typedef struct {
  IssueType type;
  Address address;
  String state;
  int likesNumber;
  int dislikesNumber;
  bool hasLocation;

  void toJson(JsonObject& root) const {
    root["type"] = typeStrings[type];
    JsonObject addressJson = root.createNestedObject("address");
    addressJson["lat"] = address.lat;
    addressJson["lng"] = address.lng;
    root["state"] = state;
    root["likesNumber"] = likesNumber;
    root["dislikesNumber"] = dislikesNumber;
    root["hasLocation"] = hasLocation;
  }

  void fromJson(JsonObject& root) {
    String typeString = root["type"].as<String>();
    for (int i = 0; i < sizeof(typeStrings) / sizeof(typeStrings[0]); i++) {
      if (typeStrings[i] == typeString) {
        type = static_cast<IssueType>(i);
        break;
      }
    }
    JsonObject addressJson = root["address"];
    double lat = addressJson["lat"].as<double>();
    double lng = addressJson["lng"].as<double>();
    address = {lat, lng};
    state = root["state"].as<String>();
    likesNumber = root["likesNumber"].as<int>();
    dislikesNumber = root["dislikesNumber"].as<int>();
    hasLocation = root["hasLocation"].as<bool>();
  }
} Issue;

void setup() {
  Serial.begin(9600);
  randomSeed(millis());
  while (!Serial) {
    // Wait for serial port to connect. Needed for native USB port only
  }
}

void loop() {
  if (Serial) {
    Issue objects[2];

    for (int i = 0; i < 2; i++) {
      IssueType randomType = static_cast<IssueType>(random(0, 9));
      objects[i].type = randomType;
      double randomLat = random(LOWER_RIGHT_LAT * 100000, UPPER_LEFT_LAT * 100000) / 100000.0;
      double randomLng = random(UPPER_LEFT_LNG * 100000, LOWER_RIGHT_LNG * 100000) / 100000.0;
      objects[i].address = {randomLat, randomLng};
      objects[i].state = "REGISTERED";
      objects[i].likesNumber = 0;
      objects[i].dislikesNumber = 0;
      objects[i].hasLocation = true;
    }

    StaticJsonDocument<500> doc;
    JsonArray array = doc.to<JsonArray>();
    for (int i = 0; i < 2; i++) {
      JsonObject objJson = array.createNestedObject();
      objects[i].toJson(objJson);
    }
    String jsonStr;
    serializeJson(array, jsonStr);

    Serial.println(jsonStr);
  } else {
    Serial.println("Serial port is busy.");
  }

  delay(20000);
}

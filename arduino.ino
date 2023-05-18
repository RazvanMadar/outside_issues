#include <ArduinoJson.h>

typedef struct {
  double lat;
  double lng;
} Address;

typedef struct {
  String type;
  Address address;
  String state;
  int likesNumber;
  int dislikesNumber;
  bool hasLocation;
  int value;

  void toJson(JsonObject& root) const {
    root["type"] = type;
    JsonObject addressJson = root.createNestedObject("address");
    addressJson["lat"] = address.lat;
    addressJson["lng"] = address.lng;
    root["state"] = state;
    root["likesNumber"] = likesNumber;
    root["dislikesNumber"] = dislikesNumber;
    root["hasLocation"] = hasLocation;
    root["value"] = value;
  }
} Issue;

Issue issues[12];

void setup() {
  Serial.begin(9600);
  randomSeed(millis());
  Address addresses[12];
  addresses[0] = {47.08088004191627, 21.91583633422852}; // above Rogerius    - 12
  addresses[1] = {47.04417173680878, 21.942100524902347}; // above Dimitrie Cantemir - 11
  addresses[2] = {47.08263167570331, 21.875667572021488}; // 1NOU
  addresses[3] = {47.022398621893444, 21.898155212402347}; // 6
  addresses[4] = {47.098761780151115, 21.917552947998047}; // 2
  addresses[5] = {47.07117401928081, 21.863651275634766}; // 4
  addresses[6] = {47.04952921777288, 21.98587417602539}; // 9
  addresses[7] = {47.06895360359799, 21.976432800292972}; // 8
  addresses[8] = {47.096888511208654, 21.886653900146488}; // 3
  addresses[9] = {47.04753842906147, 21.854724884033207}; // 5
  addresses[10] = {47.02801474206072, 21.979351043701175}; // 7
  addresses[11] = {47.02226520048006, 21.93557739257813}; // 10
  
  for (int i = 0; i < 12; i++) {
    if (i < 4) {
      issues[i].type = "PUBLIC_DISORDER";
    } else if (i < 8) {
      issues[i].type = "LIGHTNING";
    } else {
      issues[i].type = "ROAD";
    }
    issues[i].state = "REGISTERED";
    issues[i].likesNumber = 0;
    issues[i].dislikesNumber = 0;
    issues[i].hasLocation = true;
    issues[i].address = addresses[i];
  }
  
  while (!Serial) {
    // Wait for serial port to connect. Needed for native USB port only
  }
}

void loop() {
  if (Serial) {
    StaticJsonDocument<2000> doc;
    JsonArray array = doc.to<JsonArray>();
    for (int i = 0; i < 12; i++) {
      int randomValue;
      if (i < 4) {
        randomValue = random(20);
      } else if (i < 8) {
        randomValue = random(50, 150);
      } else {
        randomValue = random(50, 150);
      }
      issues[i].value = randomValue;
      JsonObject objJson = array.createNestedObject();
      issues[i].toJson(objJson);
    }
    String jsonStr;
    serializeJson(array, jsonStr);

    Serial.println(jsonStr);
  } else {
    Serial.println("Serial port is busy.");
  }

  delay(100000);
}

#include <Wire.h>
#include "Adafruit_VL6180X.h"
#include <SoftwareSerial.h>
#include <PhysiBleSerial.h>
#include <BLDC_Motor.h>

#define DIST_SENSOR_SIZE   4
#define BT_RX_PIN         10
#define BT_TX_PIN         9

#define LEFT_M1_PW_PIN    22
#define LEFT_M1_DIR_PIN   23
#define LEFT_M1_VR_PIN    10
#define LEFT_M2_PW_PIN    24
#define LEFT_M2_DIR_PIN   25
#define LEFT_M2_VR_PIN    11

#define RIGHT_M1_PW_PIN   26
#define RIGHT_M1_DIR_PIN  27
#define RIGHT_M1_VR_PIN   12
#define RIGHT_M2_PW_PIN   28
#define RIGHT_M2_DIR_PIN  29
#define RIGHT_M2_VR_PIN   13

// Distance Unit - mm
#define ANGLE_DIST_ZERO        100
#define ANGLE_DIST_P_VARTICAL1   120  // 15도
#define ANGLE_DIST_P_VARTICAL2   140  // 30도
#define ANGLE_DIST_P_VARTICAL3   160  // 45도
#define ANGLE_DIST_M_VARTICAL1   80  // 15도
#define ANGLE_DIST_M_VARTICAL2   60  // 30도
#define ANGLE_DIST_M_VARTICAL3   40  // 45도

#define ANGLE_DIST_P_HORIZONTAL1   115  // 15도
#define ANGLE_DIST_P_HORIZONTAL2   130  // 30도
#define ANGLE_DIST_M_HORIZONTAL1   85  // 15도
#define ANGLE_DIST_M_HORIZONTAL2   70  // 30도


SoftwareSerial btSerial(BT_RX_PIN, BT_TX_PIN);
PhysiBleSerial physiSerial;
Adafruit_VL6180X distSensors[DIST_SENSOR_SIZE];
BLDC_Motor leftMotor1;
BLDC_Motor leftMotor2;
BLDC_Motor rightMotor1;
BLDC_Motor rightMotor2;


int left1_Measure_Dist, left2_Measure_Dist, right1_Measure_Dist, right2_Measure_Dist;
int left1_Standard_Dist, left2_Standard_Dist, right1_Standard_Dist, right2_Standard_Dist;

String controlData;


void setDistPosition(uint8_t bus)
{
  Wire.beginTransmission(0x70);  // TCA9548A address is 0x70
  Wire.write(1 << bus);          // send byte to select bus
  Wire.endTransmission();
}

void beginDistSensor() {
  for (int i = 0; i < DIST_SENSOR_SIZE; i++) {
    distSensors[i] = Adafruit_VL6180X();
    setDistPosition(i + 1);
    Serial.print(i);
    Serial.print(F(" Distance Sensor begin -> "));
    if (!distSensors[i].begin()) {
      Serial.println(F("Failed to find sensor!"));
      while (1);
    }
    Serial.println(F("Sensor found!"));
  }
}

uint8_t getDistance(int pos) {
  setDistPosition(pos + 1);
  uint8_t range = distSensors[pos].readRange();
  uint8_t status = distSensors[pos].readRangeStatus();
  if (status == VL6180X_ERROR_NONE) {
    return range;
  }
  return -1;
}


void bleMessageHandler(String msg) {
  if (msg.startsWith("$") && msg.endsWith("#")) {
    controlData = msg.substring(1, msg.length() - 2);
    Serial.print(F("Recv BT Msg : "));
    Serial.println(controlData);
    if (controlData.startsWith("ZS")|| controlData.equals("FN")) {
      left1_Standard_Dist = left2_Standard_Dist = right1_Standard_Dist = right2_Standard_Dist = ANGLE_DIST_ZERO;
    } else {
      setStandardDistance();
    }
  }
}

void setStandardDistance(){
  int sepIndex = controlData.indexOf("/");
  String leftControl = controlData.substring(0, sepIndex); 
  String rightControl = controlData.substring(sepIndex + 1, controlData.length()); 
}



void setup() {
  Serial.begin(115200);

  physiSerial.enable(&btSerial, 9600);
  Serial.println(physiSerial.getAddress());
  physiSerial.handleMessage = &bleMessageHandler;

  Wire.begin();
  beginDistSensor();

  leftMotor1.init(LEFT_M1_PW_PIN, LEFT_M1_DIR_PIN, LEFT_M1_VR_PIN);
  leftMotor2.init(LEFT_M2_PW_PIN, LEFT_M2_DIR_PIN, LEFT_M2_VR_PIN);
  rightMotor1.init(RIGHT_M1_PW_PIN, RIGHT_M1_DIR_PIN, RIGHT_M1_VR_PIN);
  rightMotor2.init(RIGHT_M2_PW_PIN, RIGHT_M2_DIR_PIN, RIGHT_M2_VR_PIN);
}

void loop() {
  left1_Measure_Dist = getDistance(0);
  left2_Measure_Dist = getDistance(1);
  right1_Measure_Dist = getDistance(2);
  right2_Measure_Dist = getDistance(3);


  physiSerial.messageListener();
  delay(75);
}

// # One Step
// Motor Angle = 1.8, Gear Rate = 0.0125
// 1 Degree = 80 Step

// # Angle - Move Pos
// Left Motor = Down(+), Up(-)
// Right Motor = Down(-), Up(+)

// # Control Protocal
//421,20,10/321,20,10


#include <AccelStepper.h>
#include <Wire.h>
#include <Adafruit_VL6180X.h>
#include <SoftwareSerial.h>

#define TCA9548A_ADDR   0x77

#define STEP_UNIT         80
#define ZERO_POS_DIST     229
#define TOLERANCE_FIELD   2
#define DIST_SCALING      2
#define RESET_SPEED       1000

#define DIR_UP        1
#define DIR_DOWN      2
#define DIR_LEFT      3
#define DIR_RIGHT     4
#define DIR_ZERO      5

#define BLE_RX_PIN    15
#define BLE_TX_PIN    14

#define LF_LM_STP_PIN    10
#define LF_LM_DIR_PIN    23
#define LF_RM_STP_PIN    11
#define LF_RM_DIR_PIN    26

#define RF_LM_STP_PIN    12
#define RF_LM_DIR_PIN    33
#define RF_RM_STP_PIN    13
#define RF_RM_DIR_PIN    36

Adafruit_VL6180X vl6180;
AccelStepper step_LL(AccelStepper::DRIVER, LF_LM_STP_PIN, LF_LM_DIR_PIN);
AccelStepper step_LR(AccelStepper::DRIVER, LF_RM_STP_PIN, LF_RM_DIR_PIN);
AccelStepper step_RL(AccelStepper::DRIVER, RF_LM_STP_PIN, RF_LM_DIR_PIN);
AccelStepper step_RR(AccelStepper::DRIVER, RF_RM_STP_PIN, RF_RM_DIR_PIN);
SoftwareSerial bleSerial(BLE_RX_PIN, BLE_TX_PIN);

bool llFinished, lrFinished, rlFinished, rrFinished;
bool isReset = false;
String conData = "";
String stateMsg = "";

void dataSplit(String originalStr, char separator, String *result)
{
  int sepIdx;
  int arrIdx = 0;
  while (true)
  {
    sepIdx = originalStr.indexOf(separator);
    if (-1 != sepIdx)
    {
      result[arrIdx++] = originalStr.substring(0, sepIdx);
      originalStr = originalStr.substring(sepIdx + 1);
    }
    else
    {
      result[arrIdx++] = originalStr;
      break;
    }
  }
}

int getPhaseSpeed(String phase) {
  switch (phase.toInt()) {
    case 1:
      return 1000;
    case 2:
      return 1500;
    default:
      return 2000;
  }
}

void setMultiflax(uint8_t bus)
{
  Wire.beginTransmission(TCA9548A_ADDR);  // TCA9548A address is 0x70
  Wire.write(1 << bus);          // send byte to select bus
  Wire.endTransmission();
}

void beginVL6180(uint8_t bus) {
  setMultiflax(bus);
  Serial.print(F("VL6180x "));
  Serial.print(bus);
  if (! vl6180.begin()) {
    Serial.println(" Failed to find sensor!");
    while (1);
  }
  Serial.println(" Sensor found!");
}

uint16_t measureDistance(uint8_t bus) {
  setMultiflax(bus);
  if (vl6180.readRangeStatus() != VL6180X_ERROR_NONE) {
    return -1;
  }
  return vl6180.readRange();
}

void startReset() {
  uint16_t distValue_LL = measureDistance(2);
  uint16_t distValue_LR = measureDistance(3);
  uint16_t distValue_RL = measureDistance(4);
  uint16_t distValue_RR = measureDistance(5);

  if (distValue_LL == -1 || distValue_LR == -1 || distValue_RL == -1 || distValue_RR == -1) {
    while (1);
  }

  Serial.print(F("Init Dist : "));
  Serial.print(distValue_LL);
  Serial.print(F(", "));
  Serial.print(distValue_LR);
  Serial.print(F(" / "));
  Serial.print(distValue_RL);
  Serial.print(F(", "));
  Serial.print(distValue_RR);

  float llDir = distValue_LL < ZERO_POS_DIST ? -RESET_SPEED : RESET_SPEED;
  float lrDir = distValue_LR < ZERO_POS_DIST ? RESET_SPEED : -RESET_SPEED;
  float rlDir = distValue_RL < ZERO_POS_DIST ? -RESET_SPEED : RESET_SPEED;
  float rrDir = distValue_RR < ZERO_POS_DIST ? RESET_SPEED : -RESET_SPEED;

  Serial.print(F("Set Dir (L, R) : "));
  Serial.print(llDir);
  Serial.print(F(", "));
  Serial.print(lrDir);
  Serial.print(F(" / "));
  Serial.print(rlDir);
  Serial.print(F(", "));
  Serial.print(rrDir);

  step_LL.setSpeed(llDir);
  step_LR.setSpeed(lrDir);
  step_RL.setSpeed(rlDir);
  step_RR.setSpeed(rrDir);

  isReset = llFinished = lrFinished = rlFinished = rrFinished = false;
}

bool checkZeroPos() {
  uint16_t distValue_LL = measureDistance(2);
  uint16_t distValue_LR = measureDistance(3);
  uint16_t distValue_RL = measureDistance(4);
  uint16_t distValue_RR = measureDistance(5);

  if (!llFinished && abs(distValue_LL - ZERO_POS_DIST) == TOLERANCE_FIELD) {
    Serial.print(F("Zero Dist (LL) : "));
    Serial.println(distValue_LL);
    step_LL.stop();
    step_LL.setCurrentPosition(0);
    step_LL.moveTo(0);
    llFinished = true;
  }

  if (!lrFinished && abs(distValue_LR - ZERO_POS_DIST) == TOLERANCE_FIELD) {
    Serial.print(F("Zero Dist (LR) : "));
    Serial.println(distValue_LR);
    step_LR.stop();
    step_LR.setCurrentPosition(0);
    step_LR.moveTo(0);
    lrFinished = true;
  }

  if (!rlFinished && abs(distValue_RL - ZERO_POS_DIST) == TOLERANCE_FIELD) {
    Serial.print(F("Zero Dist (RL) : "));
    Serial.println(distValue_RL);
    step_RL.stop();
    step_RL.setCurrentPosition(0);
    step_RL.moveTo(0);
    rlFinished = true;
  }

  if (!rrFinished && abs(distValue_RR - ZERO_POS_DIST) == TOLERANCE_FIELD) {
    Serial.print(F("Zero Dist (RR) : "));
    Serial.println(distValue_RR);
    step_RR.stop();
    step_RR.setCurrentPosition(0);
    step_RR.moveTo(0);
    rrFinished = true;
  }

  isReset = llFinished && lrFinished && rlFinished && rrFinished;
  if (isReset) {
    Serial.println("Finish zroe setting.");
  }
}

void setZeroPosConfig() {
  step_LR.setMaxSpeed(RESET_SPEED);
  step_LL.setMaxSpeed(RESET_SPEED);
  step_RL.setMaxSpeed(RESET_SPEED);
  step_RR.setMaxSpeed(RESET_SPEED);
  step_LR.moveTo(0);
  step_LL.moveTo(0);
  step_RL.moveTo(0);
  step_RR.moveTo(0);
}

void setReturnMotionConfig(AccelStepper leftStep, AccelStepper rigthStep, String phase) {
  leftStep.setMaxSpeed(getPhaseSpeed(phase));
  rigthStep.setMaxSpeed(getPhaseSpeed(phase));
  leftStep.moveTo(0);
  rigthStep.moveTo(0);
}


void setUpMotionConfig(AccelStepper leftStep, AccelStepper rigthStep, String angle, String phase) {
  long pos = angle.toInt() * STEP_UNIT;
  leftStep.setMaxSpeed(getPhaseSpeed(phase));
  rigthStep.setMaxSpeed(getPhaseSpeed(phase));
  leftStep.moveTo(-pos);
  rigthStep.moveTo(pos);
}


void setDownMotionConfig(AccelStepper leftStep, AccelStepper rigthStep, String angle, String phase) {
  long pos = angle.toInt() * STEP_UNIT;
  leftStep.setMaxSpeed(getPhaseSpeed(phase));
  rigthStep.setMaxSpeed(getPhaseSpeed(phase));
  leftStep.moveTo(pos);
  rigthStep.moveTo(-pos);
}


void setLeftMotionConfig(AccelStepper leftStep, AccelStepper rigthStep, String angle, String phase) {
  long pos = angle.toInt() * STEP_UNIT;
  leftStep.setMaxSpeed(getPhaseSpeed(phase));
  rigthStep.setMaxSpeed(getPhaseSpeed(phase));
  leftStep.moveTo(pos);
  rigthStep.moveTo(pos);
}

void setRightMotionConfig(AccelStepper leftStep, AccelStepper rigthStep, String angle, String phase) {
  long pos = angle.toInt() * STEP_UNIT;
  leftStep.setMaxSpeed(getPhaseSpeed(phase));
  rigthStep.setMaxSpeed(getPhaseSpeed(phase));
  leftStep.moveTo(-pos);
  rigthStep.moveTo(-pos);
}

void setMotion(String conData) {
  int sepPos = conData.indexOf('/');
  String leftConfigs[10];
  String rightConfigs[10];
  dataSplit(conData.substring(0, sepPos), ',', leftConfigs);
  dataSplit(conData.substring(sepPos + 1), ',', rightConfigs);

  switch (leftConfigs[0].toInt()) {
    case DIR_UP:
      setUpMotionConfig(step_LL, step_LR, leftConfigs[1], leftConfigs[2]);
      break;
    case DIR_DOWN:
      setDownMotionConfig(step_LL, step_LR, leftConfigs[1], leftConfigs[2]);
      break;
    case DIR_LEFT:
      setLeftMotionConfig(step_LL, step_LR, leftConfigs[1], leftConfigs[2]);
      break;
    case DIR_RIGHT:
      setRightMotionConfig(step_LL, step_LR, leftConfigs[1], leftConfigs[2]);
      break;
    case DIR_ZERO:
      setReturnMotionConfig(step_LL, step_LR, leftConfigs[2]);
      break;
  }

  switch (rightConfigs[0].toInt()) {
    case DIR_UP:
      setUpMotionConfig(step_RL, step_RR, rightConfigs[1], rightConfigs[2]);
      break;
    case DIR_DOWN:
      setDownMotionConfig(step_RL, step_RR, rightConfigs[1], rightConfigs[2]);
      break;
    case DIR_LEFT:
      setLeftMotionConfig(step_RL, step_RR, rightConfigs[1], rightConfigs[2]);
      break;
    case DIR_RIGHT:
      setRightMotionConfig(step_RL, step_RR, rightConfigs[1], rightConfigs[2]);
      break;
    case DIR_ZERO:
      setReturnMotionConfig(step_RL, step_RR, leftConfigs[2]);
      break;
  }
}

void bleListener() {
  while (Serial.available()) {
    char c = (char)Serial.read();
    conData += c;
    delay(1);
  }

  if (conData != "") {
    Serial.print(F("Recv Data : "));
    Serial.println(conData);
    if (conData.equals("ZS")) {
      stateMsg = "ZS";
      setZeroPosConfig();
    } else {
      stateMsg = "AM";
      setMotion(conData);
    }
    conData = "";
  }
}


void setup() {
  Serial.begin(115200);
  Wire.begin();
  Serial.println(F("Firmware setup.."));

  delay(500);
  while (!Serial) {
    delay(1);
  }

  beginVL6180(2);
  beginVL6180(3);
  beginVL6180(4);
  beginVL6180(5);

  step_LL.setAcceleration(2000.0);
  step_LL.setMaxSpeed(2000);
  step_LR.setAcceleration(2000.0);
  step_LR.setMaxSpeed(2000);
  step_RL.setAcceleration(2000.0);
  step_RL.setMaxSpeed(2000);
  step_RR.setAcceleration(2000.0);
  step_RR.setMaxSpeed(2000);
}

void loop() {
  bleListener();

  if (isReset) {
    if (step_LL.distanceToGo() == 0 && step_LR.distanceToGo() == 0 &&
        step_RL.distanceToGo() == 0 && step_RR.distanceToGo() == 0) {
      // send control finish.
      if(stateMsg != ""){
        bleSerial.print(stateMsg);
        stateMsg = "";
      }
    }
    step_LL.run();
    step_LR.run();
    step_RL.run();
    step_RR.run();
  } else {
    if (checkZeroPos())
      return;
    step_LL.runSpeed();
    step_LR.runSpeed();
    step_RL.runSpeed();
    step_RR.runSpeed();
  }
}

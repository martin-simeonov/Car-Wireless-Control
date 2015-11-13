#include <Bridge.h>
#include <YunServer.h>
#include <YunClient.h>
#include <Servo.h>

// Steering servo
Servo myservo;

// Motor 2
int forward = 11; // Forward mode
int back = 3; // Back mode
int leftHeadlight = 7;
int rightHeadlight = 6;
int leftStop = 5;
int rightStop = 4;
int speed = 50;

#define PORT 6666
YunServer server(PORT);

void setup() {
  myservo.attach(9);
  myservo.write(34);
  
  // Start our connection
  Serial.begin(115200);
  Bridge.begin();
  pinMode(forward,OUTPUT);
  pinMode(back,OUTPUT);
  pinMode(leftHeadlight, OUTPUT);
  pinMode(rightHeadlight, OUTPUT);
  pinMode(leftStop, OUTPUT);
  pinMode(rightStop, OUTPUT);
  
  // Disable for some connections:
  // Start listening for connections  
  
  server.noListenOnLocalhost();
  server.begin();
  pinMode(13, OUTPUT);
  digitalWrite(13, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(1000);
}

void loop() {  
  // Listen for clients
  YunClient client = server.accept();
  String command;
  while(client.connected()) {
    if(client.available()) {
      char cmd = client.read();
      if(cmd == '\n'){
        process(command);
        command = "";
      } else {
        command += cmd;
      }
    }
  }
  client.stop();
  delay(50);
}

void process(String command) {
  Serial.println("command: " + command);
  
  if (command == "forward") {
    analogWrite(forward,speed);
    digitalWrite(leftStop, LOW);
    digitalWrite(rightStop, LOW);
  }

  if (command == "left") {
    myservo.write(65);
  }

  if (command == "right") {
    myservo.write(1);
  }
  
  if (command == "back") {
    analogWrite(back,speed);
    digitalWrite(leftStop, LOW);
    digitalWrite(rightStop, LOW);
  }
  
  if (command == "stop") {
    digitalWrite(forward,LOW);
    digitalWrite(back,LOW);
    digitalWrite(leftStop, HIGH);
    digitalWrite(rightStop, HIGH);
  }

  if (command == "straight") {
    myservo.write(34);
  }

  if (command.startsWith("speed")) {
    speed = command.substring(6).toInt();
  }

  if (command.startsWith("steerByAngle")) {
    myservo.write(command.substring(13).toInt());
  }

  if (command.startsWith("headlights")) {
    String power = command.substring(11);
    if (power == "on") {
      digitalWrite(leftHeadlight, HIGH);
      digitalWrite(rightHeadlight, HIGH);
    } else if (power == "off") {
      digitalWrite(leftHeadlight, LOW);
      digitalWrite(rightHeadlight, LOW);
    }
  }
}

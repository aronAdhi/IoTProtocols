#include <SPI.h>
#include <RH_RF95.h>
#include <Wire.h>
#include <RTClib.h>

// Singleton instance of the radio driver
RH_RF95 rf95;

// Create an instance of the real time clock object
RTC_DS1307 rtc;

void setup() 
{
  Serial.begin(9600);

  // Initialize the RTC object
  rtc.begin();

  // Initialize the LoRa module
  if (!rf95.init())
    Serial.println("init failed");
  rf95.setFrequency(895.0);
}

void loop()
{
  // Get the current time
  DateTime now = rtc.now();
  
  // Prompt the user to enter the destination
  Serial.println("Enter destination:");
  Serial.println("1. Lanka");
  Serial.println("2. HYD Gate");

  Serial.println("Enter the number of people:");
  while (!Serial.available()) {
    // Wait for user input
  }
  int destination = Serial.parseInt();
  int num_people = Serial.parseInt();
 
  // Create the message to send
  String message = String(now.unixtime()) + "," + String(destination) + "," + String(num_people);
  char message_buffer[message.length() + 1];
  message.toCharArray(message_buffer, sizeof(message_buffer));

  // Send the message
  rf95.send((uint8_t*)message_buffer, sizeof(message_buffer));
  rf95.waitPacketSent();

  // Print a confirmation message
  Serial.println("Message sent:");
  Serial.println(message);
  
  // Wait before sending the next message
  delay(1000);
}

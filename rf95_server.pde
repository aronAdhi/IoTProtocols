// rf95_server.pde
// -*- mode: C++ -*-
// Example sketch showing how to create a simple messageing server
// with the RH_RF95 class. RH_RF95 class does not provide for addressing or
// reliability, so you should only use RH_RF95  if you do not need the higher
// level messaging abilities.
// It is designed to work with the other example rf95_client
// Tested with Anarduino MiniWirelessLoRa


#include <SPI.h>
#include <RH_RF95.h>
#include <MySQL_Connection.h>
#include <MySQL_Cursor.h>

RH_RF95 rf95;

void setup() 
{    
  Serial.begin(9600);
  if (!rf95.init())
    Serial.println("init failed");  
  rf95.setFrequency(895.0);
}

void loop()
{
  if (rf95.available())
  { 
    uint8_t buf[RH_RF95_MAX_MESSAGE_LEN];
    uint8_t len = sizeof(buf);
    if (rf95.recv(buf, &len))
    {
      Serial.print("got request: ");
      Serial.println((char*)buf);
      
      uint8_t data[] = "And hello back to you";
      rf95.send(data, sizeof(data));
      rf95.waitPacketSent();
      Serial.println("Sent a reply");
    }
    else
    {
      Serial.println("recv failed");
    }
  }
}



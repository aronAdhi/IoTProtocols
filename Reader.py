import mysql.connector
import serial
import datetime

# Open serial port to read data from Arduino
ser = serial.Serial('COM5', 9600)

# Connect to the MySQL database
mydb = mysql.connector.connect(
  host="localhost",
  user="root",
  password="",
  database="protocols"
)

def insert(input_string):
    # Parse the input string
    values = input_string.strip().split(',')

    if(len(values)<3):
        return

    # Extract the values
    timestamp = values[0]
    destination = values[1]
    num_of_people = values[2]

    timestamp = int(''.join(filter(str.isdigit, timestamp)))
    print(timestamp)

    now = datetime.datetime.now()
    timestamp = int(now.timestamp())
    timestamp = datetime.datetime.fromtimestamp(timestamp)

    if(destination == "0"):
        print("duplicate eliminated")
        return

    # Insert the values into the database
    mycursor = mydb.cursor()
    sql = "INSERT INTO request ( Time, Destination, Num_of_people) VALUES ( %s, %s, %s)"
    val = ( timestamp, destination, num_of_people)
    mycursor.execute(sql, val)
    mydb.commit()

    print(mycursor.rowcount, "record inserted.")


while True:
    # Read a line of data from the serial port
    print("we are in")
    data = ser.readline().decode().strip()
    print("inserting data ",data)
    insert(data)

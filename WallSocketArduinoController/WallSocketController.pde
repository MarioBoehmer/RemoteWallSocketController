byte in;

void setup()
{
  pinMode(10, OUTPUT); //On
  pinMode(12, OUTPUT); //Off
  pinMode(13, OUTPUT); //LED
  Serial.begin(9600);
}

void loop()
{
  if (Serial.available() > 0) {
    // get incoming byte:
    in = Serial.read();
    if(in == 1) {
      digitalWrite(13, HIGH);
      digitalWrite(10, HIGH);
      delay(250);
      digitalWrite(10, LOW);
    } 
    else if(in == 0){
      digitalWrite(13, LOW);
      digitalWrite(12, HIGH);
      delay(250);
      digitalWrite(12, LOW);
    }
  }
}


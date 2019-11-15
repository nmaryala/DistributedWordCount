rm *.txt
rm temp/*.txt
rm temp/immediate/*.txt

javac WordCount.java
javac Client.java
javac ClientHandler.java
javac MasterServer.java
javac Heartbeat.java

java WordCount
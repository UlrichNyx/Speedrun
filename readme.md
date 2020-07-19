To compile:
javac -cp .:../lib/controlsfx-11.0.1.jar:../lib/javax.mail.jar:../lib/javax.activation-1.2.0.jar --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.graphics,javafx.swing -d ../out *.java
To run client:
java -cp .:../lib/controlsfx-11.0.1.jar:../lib/javax.mail.jar:../lib/javax.activation-1.2.0.jar --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.graphics,javafx.swing  Client 4323
To run Server:
java -cp .:../lib/controlsfx-11.0.1.jar:../lib/javax.mail.jar:../lib/javax.activation-1.2.0.jar Server
To export path:
export PATH_TO_FX=/Users/mako/Desktop/Speedrun/lib/javafx-sdk-13.0.2/lib
Alternatively, you can use the bash scripts: compile.sh, server.sh, client.sh in that order.

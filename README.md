Surreal-Travel
==============

Travel Agency System

## To run Maven Server:
(assuming ```$PWD``` in the project root directory)
```bash
# Compile if needed
$ mvn compile install

$ cd SurrealTravelWeb
$ mvn tomcat7:run
```

## To run CLI application:
(again from the project rood directory)
```bash
$ cd SurrealTravelCLI
$ mvn exec:java -Dexec.args="[ARGUMENTS]"

# or use included Perl script
$ ./run [ARGUMENTS]
```

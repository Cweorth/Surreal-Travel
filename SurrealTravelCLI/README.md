Surreal Travel Command Line Interface (CLI)
===========================================

Contents:

 1. How to run
 2. Usage
 3. Examples
 4. Errors

## How to run the application
assuming ```$PWD``` is ```<project_core>/SurrealTravelCLI```
```bash
$ mvn exec:java -Dexec.args="[ARGUMENTS]"
```

The tomcat server can be run from ```SurrealTravelWeb``` by
```bash
$ mvn tomcat7:run
```

If you have *Perl* installed, you can use the included script, that convenientely
    handles the arguments (also does escaping of arguments with spaces).

For instance, the following command
```bash
$ ./run --url http://localhost:8084/pa165/rest trips-add -f 2014/07/21 -t 2015/01/10\
        -d "The Council of Elrond" -c 20 -p 500 -e "1 2 3 4"
```

will call this
```bash
$ mvn --quiet exec:java -Dexec.args=" --url http://localhost:8084/pa165/rest \
        trips-add -f 2014/07/21 -t 2015/01/10 -d 'The Council of Elrond'\
        -c 20 -p 500 -e '1 2 3 4'"
```

## Usage (full)

```bash
usage: [--debug] [--url url] [-? (--command-help) {command|LIST}] [-h (--help)] [-v (--verbose)] COMMAND
use -? [--command-help] to show help for each subcommand

 COMMAND                                : main command                             
 --debug                                : enables debug messaged
 --url url                              : specify base URL
 -? (--command-help) {command|LIST}     : show command help or lists commands
                                          if "LIST" specified
 -h (--help)                            : show program help
 -v (--verbose)                         : verbose mode
```

### Commands

Available commands:
(use ```--command-help``` command to show help)
```
excursions-list
excursions-get
excursions-add
excursions-edit
excursions-delete
trips-list
trips-get
trips-add
trips-edit
trips-delete
```

### Excursion command options

#### ```excursions-list```

Lists all excursions.

#### ```excursions-get```

List excursions with the given ```id```.

```bash
command usage: --id id

--id id : specify the excursion id
```
#### ```excursions-add```

Create new excursion.

```bash
command usage: --description description --destination destination [--duration duration] --excursionDate date --price price

--description description : specify the excursion description [string]
--destination destination : specify the excursion destination [string]
--duration duration       : specify the duration of excursion [integer]
--excursionDate date      : specify the excursion date [yyyy/MM/dd]
--price price             : specify the excursion price [integer]
```

#### ```excursions-edit```

Edit existing excursion.

```bash
command usage: [--description description] [--destination destination] [--duration duration] [--excursionDate date] --id id [--price price]

--description description : specify the excursion description [string]
--destination destination : specify the excursion destination [string]
--duration duration       : specify the duration of excursion [integer]
--excursionDate date      : specify the excursion date [yyyy/MM/dd]
--id id                   : specify the excursion id
--price price             : specify the excursion price [integer]
```

#### ```excursions-delete```

Delete existing excursion.

```bash
command usage: --id id

--id id : specify the excursion id
```
### Trip command options

#### ```trips-list```

Lists all trips.

```bash
command usage:  [-e (--list-excursions)]

-e (--list-excursions) : lists excursions in the trip
```

### ```trips-get```

Lists trips with the given ```id```.

```bash
command usage:  --id id [-e (--list-excursions)]

--id id                : the trip id
-e (--list-excursions) : lists excursions in the trip
```
### ```trips-add```

Adds a new trip.

```bash
command usage:  --basePrice (-p) price --capacity (-c) capacity --destination (-d) destination [--excursions (-e) ids...] --from (-f) date --to (-t) date

--basePrice (-p) price         : trip price
--capacity (-c) capacity       : trip capacity
--destination (-d) destination : trip destination
--excursions (-e) ids...       : indices of excursions for this trip
--from (-f) date               : trip start [yyyy/MM/dd]
--to (-t) date                 : trip end [yyyy/MM/dd]
```
### ```trips-edit```

Edits the trip with the given ```id```.

```bash
command usage:  [--basePrice (-p) price] [--capacity (-c) capacity] [--destination (-d) destination] [--excursions (-e) ids...] [--from (-f) date] --id id [--no-excursions] [--to (-t) date]

--basePrice (-p) price         : trip price
--capacity (-c) capacity       : trip capacity
--destination (-d) destination : trip destination
--excursions (-e) ids...       : indices of excursions for this trip
--from (-f) date               : trip start [yyyy/MM/dd]
--id id                        : the ID of the trip to edit
--no-excursions                : remove all excursions
--to (-t) date                 : trip end [yyyy/MM/dd]
```
### ```trips-delete```

Deletes the trip with the given ```id```.

```bash
command usage:  --id id

--id id : the ID of the trip to delete
```

## Examples

### Main commands

Show program help:
```bash
$ ./run --help
```

Show all available commands:
```bash
$ ./run -? LIST
```

Show help about command ```trips-list```
```bash
$ ./run -? trips-list
```

Enables maven output and ```WARN``` log entries
```bash
$ ./run --verbose ...
```

Enable maven output and ```TRACE``` log entries
```bash
$ ./run --debug ...
```

### Excursion examples

Show all excursions:
```bash
$ ./run excursions-list
```

Show an excursion with ID ```5```:
```bash
$ ./run excursions-get --id 5
```

Add a new excursion to ```Rivendell```:
```bash
$ ./run excursions-add --description "Council of Elrond" --destination "Rivendell" --excursionDate 3018/11/25 --duration 1 --price 150
```

Change the price of the excursion with ID ```3``` to ```300```:
```bash
$ ./run excursions-edit --id 3 --price 300
```

Delete the excursion with ID ```5```:
```bash
$ ./run excursions-delete --id 5
```

### Trip examples

Show all trips and excursions included
```bash
$ ./run trips-list --list-excursions
```

Show the trip with ID ```5```:
```bash
$ ./run trips-get --id 5
```

Add a new trip to ```Moria``` with excursions that have IDs ```1```, ```2``` and ```3```:
```bash
$ ./run trips-add --from 3018/11/24 --to 3019/04/25 --destination "Moria" --capacity 15 --basePrice 500 --excursions 1 2 3
```

Remove all excursions from the trip with ID ```3``` and set its price to ```700```:
```bash
$ ./run trips-edit --id 3 --no-excursions --basePrice 700
```

Remove the trip with ID ```9```:
```bash
$ ./run trips-delete --id 9
```

## Errors

> ```The <entity> with id <id> was not found.```

The meaning is obvious.

> ```The <entity> is not valid.```

The validation of the entity has failed, check the server log.

> ```A constraint prevented this trip from modification.```

The excursion added to the trip is outside of the date range when the trip takes place

> ```The <entity> cannot be deleted because of integrity constraints.```

The entity is referenced by some other entity (for instance, a trip may be referenced by some reservation); it may not be deleted

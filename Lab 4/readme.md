# Laboratory work 4
## The assignment can be viewed in the file Laboratorium 4.pdf
The application is a shell over the H2 database. It allows you to manage the contents of the database in text mode using the console.

## Instalation (Linux)

To run the aplication you need to slart H2 database.
You can get in from here:
[Official page](https://www.h2database.com/html/download.html "H2 database")

To run the database server, go to h2/bin dirrectory and run the following commands:
```bash
$ chmod +x h2.sh
$ ./h2.sh
```

Then use password "root" to run database server

Now aplication is ready to launch

Tested on 19, 21 versions of jdk.

## Usage

The application allows you to manage objects of Mage, Tower classes.
The following operations are available:
* Adding new records
* Deleting existing records
* Output all records for a class
* Executing queries from a list with user-defined parameters
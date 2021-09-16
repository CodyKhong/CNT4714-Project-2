# CNT4714-Project-2

Description: In this assignment you will develop a Java-based GUI front-end (client-side)
application that will connect to your MySQL server via JDBC.
You are to develop a Java application that will allow any client (the end-user) to execute commands
against the database. You will create a Java GUI-based application front-end that will accept any
MySQL DDL or DML command, pass this through a JDBC connection to the MySQL database
server, execute the statement and return the results to the client. Note that while technically your
application must be able to handle any DDL or DML command, we won’t actually use all of the
commands available in these sublanguages. For one thing, it would be quite rare to allow a client to
create a database or a table within a database. Note too, that the only DML command that uses the
executeQuery() method of JDBC is the Select command, all other DML and DDL commands
utilize executeUpdate(). Some screen shots of what your Java GUI front-end should look like
are shown below. Basically, this GUI is an extension of the GUI that was developed in the lecture
notes and is available on WebCourses as DisplayQueryResults.java. Your Java application must give
the user the ability to execute any SQL DDL or DML command for which the user has the correct
permissions. Note also, that if the user wishes to change databases in the middle of a session, they
must reconnect to the new database. Their user information can remain in the proper window, but you
must click the reconnect button to establish a connection to the new database. You will be able to
start multiple instances of your Java application and allow different clients to connect simultaneously
to the MySQL server, since the default number of connections is set at 151 (see your Workbench
options file under the networking tab). In addition, a transaction logging operation will occur which
keeps a running total of the number of queries and the number of updates that have occurred via the
user application. This is a separate database (i.e., completely different database than any that the user
can connect to), that the application will connect, with root user privileges, and update after each user
operation completes. See below for more details on this feature of your application.
Once you’ve created your application, you will execute a sequence of DML and DDL commands and
illustrate the output from each in your GUI for two different users. For this project you will create,
in addition to the root user, a client user with limited permissions on the database (see below). The
root user is assumed to have all permissions on the database, any command they issue will be
executed. The client user will be far more restricted.

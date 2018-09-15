# Voting-app

Java voting app with client and server sides.

Features:
- Multi threaded server to handle large amount of clients. 
- Socket Connection between client and Server encrypted using java JSSE library.
- Serialization of java objects to pass them between server and client. (Poll and Person)
- Data stored in SQL database.
- Database connection detected and schema built by server.
- Client has simple UI using gridbaglayout. 
- Client UI in seperate thread from client program and updated as needed.
- Error handling throughout program
- Automatic client restart options when vote completed or error detected. 
- Database functionalty localized to DatabaseManager class. 
- Server features to: 
    disable/enable database access
    Toggle debug messages
    view total votes from all polls
    stop server and close all connections
    View SSL Debug information
    
JSSE/SSL information:
- In order to get the SSL encryption working properly you must create a key, keystore, certificate and Trust store. 
- The key can be stored in the key store and the certificate stored in the Trust store. 
- the files (myKey.cert, myKeyStore.jks, myTrustStore.jts) and command line commands (javaJSSEcommands.txt) are included in this repository for information only. It is recommended users of this program generate their own. 
- The server uses the RSA key stored in the keystore while the client validates it using the certification stored in the Truststore. 
- Sample Code can be found in SocketHandler and ClientAcceptor.
- Any other questons I'm happy to answer. 

Database Information: 
- Uses SQL database tested on windows machine using local XAMPP hosting.

Running Instructions: 
- Start server.java with optional boolean command line argument: Java Server.java [true/false]
- Command line argument is for SSL debug information
- Start any number of clients and walk through interaction: Java Client.java


Future Iterations: 
- Handle multiple polls at one time
- Encrypt connection to database
- More/better Error handling
- UI improvements
- Pattern matching of candates votes based on ID rather than name String.
- Better field validation and handling during user registration. 
- Ability to show vote counts in client using graphics or other visual data representations.
- More things as I come up with them

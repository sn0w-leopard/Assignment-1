TO COMPILE/RUN USING MAKEFILE
*****************************

1. Be in root of project folder in terminal
2. type 'make' into terminal, class files are compiled into the bin directory as is convention 
3. type 'make run' into terminal, MyServer is run and the server starts
4. open a new terminal window, navigate to the bin directory of the project, and type 'java MyClient', a client will start (rn connects automatically to default values, but functionality being added to accept command line arguments or ability to choose display name)
5. in a  new terminal window, repeat step 4 for any additional client you would like to connect
6. after making changes and saving them type 'make clean' in terminal back in the project root and go back to step 1

CURRENT FUNCTIONALITY
*********************
server auto assigns id to every new client that connects, chat functionality (not sure if  working 100% yet?)
broadcast was working before, not sure if still is
exit option closes the connection with the client
file query works 100%, just changed to work with makefile and committed

FUTURE FUNCTIONALITY
********************
upload/download? can be moved into current functionality when done
function to probe server for list of other connected clients? can be moved into current functionality when done
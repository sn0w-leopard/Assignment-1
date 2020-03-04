#Makefile for assignment 1 CSC3002F
compiler = javac
interpreter = java
options = -g -cp bin -d bin
srcdir = src
bindir = bin
docdir = doc

compile: src/MyServer.java src/MyClient.java
		$(compiler) $(options) $(srcdir)/*.java #compiles all the java files

default: compile

run:
		cd $(bindir) && $(interpreter) MyServer #starts the server

clean:
		rm -f $(bindir)/*.class #cleans the bin dir

docs:
		javadoc -html5 -d $(docdir)/ $(srcdir)/*.java

cleandocs:
		rm -f $(docdir)/*.html $(docdir)/*.js $(docdir)/element-list $(docdir)/*.css $(docdir)/*.zip #cleans the bin dir
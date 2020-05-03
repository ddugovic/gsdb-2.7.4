CC= javac -O -d ../.. -classpath '../..:'$$CLASSPATH

OBJS=	ParseArgs.class dbe.class NetInterface.class
PREFIX= src

all: $(OBJS)

clean:
	rm *.class ; rm src/*~ *~

.SUFFIXES:      .class .java $(SUFFIXES)
.FAILED:

ParseArgs.class: src/ParseArgs.java
dbe.class: src/dbe.java ../Record.class ParseArgs.class
NetInterface.class: src/NetInterface.java ../Record.class ../Index.class \
	../Comparator.class ../Database.class

$(OBJS):
	$(CC) $(PREFIX)/$*.java

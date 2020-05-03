
COPT=
#CC=guavac
CC=javac

all : Const.class Client.class chooseFile.class Comment.class \
      Data.class Decode.class Game.class GameSelect.class \
      Gen.class Gsdb.class Help.class MainGsdb.class Move.class \
      Search.class StreamListener.class MyDialog.class \
      StreamAnalyser.class varCanvas.class varFrame.class \
      varChoice.class GameType.class gsdb.zip \
      Server.class Connection.class ListRecords.class Copy.class \
      DelFirst.class Merge.class ListGames.class ChangeField.class \
      ExportRecords.class

Const.class : Const.java
	$(CC) $(COPT) Const.java 

Client.class : Client.java 
	$(CC) $(COPT) Client.java 

chooseFile.class : chooseFile.java 
	$(CC) $(COPT) chooseFile.java 

Comment.class : Comment.java 
	$(CC) $(COPT) Comment.java 

Data.class : Data.java 
	$(CC) $(COPT) Data.java 

Decode.class : Decode.java 
	$(CC) $(COPT) Decode.java 

Game.class : Game.java 
	$(CC) $(COPT) Game.java 

GameSelect.class : GameSelect.java 
	$(CC) $(COPT) GameSelect.java 

GameType.class : GameType.java 
	$(CC) $(COPT) GameType.java 

Gen.class : Gen.java 
	$(CC) $(COPT) Gen.java 

Help.class : Help.java 
	$(CC) $(COPT) Help.java 

Move.class : Move.java 
	$(CC) $(COPT) Move.java 

MyDialog.class : MyDialog.java 
	$(CC) $(COPT) MyDialog.java 

Search.class : Search.java 
	$(CC) $(COPT) Search.java 

StreamListener.class : StreamListener.java 
	$(CC) $(COPT) StreamListener.java 

Gsdb.class : Gsdb.java 
	$(CC) $(COPT) Gsdb.java 

MainGsdb.class : MainGsdb.java 
	$(CC) $(COPT) MainGsdb.java 

StreamAnalyser.class : StreamAnalyser.java 
	$(CC) $(COPT) StreamAnalyser.java

varCanvas.class : varCanvas.java 
	$(CC) $(COPT) varCanvas.java

varChoice.class : varChoice.java 
	$(CC) $(COPT) varChoice.java

varFrame.class : varFrame.java 
	$(CC) $(COPT) varFrame.java

Server.class : Server.java
	$(CC) $(COPT) Server.java 

ExportRecords.class : ExportRecords.java
	$(CC) $(OPT) ExportRecords.java

ListRecords.class : ListRecords.java
	$(CC) $(OPT) ListRecords.java

ListGames.class : ListGames.java
	$(CC) $(OPT) ListGames.java

Copy.class : Copy.java
	$(CC) $(COPT) Copy.java 

DelFirst.class : DelFirst.java
	$(CC) $(COPT) DelFirst.java 

Merge.class : Merge.java
	$(CC) $(COPT) Merge.java 

ChangeField.class : ChangeField.java
	$(CC) $(COPT) ChangeField.java 

gsdb.zip : Const.class Client.class chooseFile.class Comment.class Data.class Decode.class Game.class GameSelect.class GameType.class Gen.class Gsdb.class Help.class MainGsdb.class Move.class Search.class StreamListener.class MyDialog.class StreamAnalyser.class varCanvas.class varFrame.class varChoice.class
	zip -ruq gsdb.zip Const.class Client.class chooseFile.class Comment.class Data.class Decode.class Game.class GameSelect.class GameType.class Gen.class Gsdb.class Help.class MainGsdb.class Move.class Search.class StreamListener.class MyDialog.class StreamAnalyser.class varCanvas.class varFrame.class varChoice.class

clean :
	rm *.class



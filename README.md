# Clone Reddit

Features include  
    - Adding topics  
	- Upvote topic multiple times  
	- Downvote topic Multiple times  
	- Topic sorted in descending order of votes  
	- Unit tests  

Built using:  
	- Android Studio  
	- Kotlin  
	- MVVM  
	- Android Lifeycle components  
	- RxJava, RXBinding  
	- JUnit  
	- Gradle  

Build Instructions :  

Clone the source repository  

On the command line, enter:  
	git clone https://rogue_anji@bitbucket.org/rogue_anji/clone-reddit.git  

Android Studio
	- Import the project cloned above
	- Run the project
	- For testing, go to the test directory com/test/clonereddit and right click to run "Tests in clonereddit"

Command Line - Go to the directory where you cloned the repo
	- cd clone-reddit (Make sure you see "gradlew" in this directory)
	- add the android sdk location by adding "local.properties" file at this location
	- run the project from the terminal "./gradlew installDebug".
	- test the project "./gradlew test".

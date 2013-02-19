1. Download and install the Java Development Kit if you don't have it already from here:
http://www.oracle.com/technetwork/java/javase/downloads/java-se-jdk-7-download-432154.html

2. Download Eclipse IDE for Java EE Developers here:
http://www.eclipse.org/downloads/

3. Unzip eclipse, it will create an eclipse folder.  Click eclipse.exe to run

4. Close the welcome window

5. Go to Help -> Eclipse Marketplace

6. Search for 	"Maven Integration"

7. CLick the install button under the "Maven Integration for Eclipse WTP" plugin

8. CLick next, accept the license agreement, select finish.  Select yes when it asks you to restart.

Maven is a build tool which simplifies building java projects. In a nutshell, a maven project hass a pom.xml file which defines metadata about your project (such as any dependencies).  Maven then downloads the dependencies for you and includes them in your project.  See this link for a 5-minute maven tutorial:

http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html

Here is a more detailed tutorial:

http://maven.apache.org/guides/getting-started/index.html

8b. Repeat this process, but search for egit in the marketplace and install it as well.  Restart eclipse.

9. If you get a dialog saying "HOME is not set" select "Do not show again" then click ok

10. Close the welcome screen if you get it

11. Window->Open perspective -> Git repository exploring, click Ok

12. Select the "Clone a Git repository" link
URI: https://github.com/kharyam/SensorVisualizer.git

13. Click next

14. Make sure master is selected and click Next, Click Finish

15. SensorVisualizer will appear as a git repository in the left pane

16. Right click "SensorVisualizer" and select "Import Projects..."

23. Select "Import as general project", select next, then finish

24. In the upper right, select the "Java EE" button.  If you don't see it, do Window->open perspective->other->Java EE and select ok

The project will be in the project explorer on the left, you can expand it to see the source code.  The java packages and code will be under the src/main/java folder

25 To run it in eclipse, Right click on Main.java (under sensorvisualizer.controller) and select Run As->Java Application.  The login window will pop up and The console output should appear at the bottom of the screen after you log in.

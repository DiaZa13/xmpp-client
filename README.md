<!-- ABOUT THE PROJECT -->
## XMMP-CLIENT

XMPP is a open protocol based on XML. Originally was developed for instant messaging, even though nowdays is used on a varietity of communication areas, since direct messages to voice calls. The XMPP-CLIENT project is a basic chat client that uses XMPP as a protocol and java as a programming language.

![image](https://user-images.githubusercontent.com/54074539/184997585-2616bf74-8fd2-45bc-8038-13c46dcb4904.png)

This XMPP client was developed to understand some basic and general concepts about networks and asynchronous programming. 

<!-- FEATURES -->
### Features

- [x] Account
    - [x] Register new account
    - [x] Log in to an existing account
    - [x] Delete account
 - [x] Users
    - [x] Show users and its presence
    - [x] Send a subscription request
    - [x] Accept/decline a subscription request
    - [x] Delete a user from your contacts
    - [x] Get detail information of a contact
 - [x] Messaging
    - [x] Send a direct message
    - [x] Send a group message
    - [x] Join a group
    - [x] Send/receive files (XEP-0047 via XEP-0095)
- [x] Edit presence message (status)
- [x] Receive/send notifications


### Built With 🛠️

[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?theme=calm&username=openjdk&repo=jdk)](https://www.oracle.com/java/technologies/)
[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?theme=calm&username=apache&repo=maven-plugin-tools)](https://github.com/apache/maven-plugin-tools)
[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?theme=calm&username=JetBrains&repo=intellij-community)](https://github.com/JetBrains/intellij-community)
[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?theme=calm&username=igniterealtime&repo=Smack)](https://github.com/igniterealtime/Smack)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Getting Started 🚀
Here are the basic steps to get a running copy of zclient on your local machine

### Prerequisites 📋

Firs of all, be sure to have install the next dependencies.

* java SDK 18 
You can install it via [Java SE 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)
* Add JAVA_HOME to yout enviroment variables
Here is an example of how to do it [Set JAVS_HOME on windows](https://stackoverflow.com/questions/2619584/how-to-set-java-home-on-windows-7/17142065#17142065)

Also, it is necessary to enable ansi codes on your terminal. If you don't have it already, you can do it, by running the next command.

  * cmd
  ```sh
    reg add HKCU\Console /v VirtualTerminalLevel /t REG_DWORD /d 1
  ```
  * powershell
  ```sh
    Set-ItemProperty HKCU:\Console VirtualTerminalLevel -Type DWORD 1
  ```
 For the changes to take effect, you will need to open a new terminal.

### Installation 🔧

1. Clone the repo
   ```sh
   git clone https://github.com/DiaZa13/xmpp-client.git
   ```
2. Go to the already clone project directory and change to client directory
   ```sh
   cd client
   ```
3. Compile the program
   ```sh
   mvnw clean compile
   ```
4. Make an executable java jar
   ```sh
   mvnw package
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->
## Usage ⚙️
 5. Execute the xmpp-client (make sure you still on client directory)
   ```sh
   java -jar .\target\xmmp-client-1.0.jar
   ```
   When you execute the project it will print you out a menu where you can log in into an exisiting account or create a new account on the XMPP server. Once you are logged, you can type **-help** to request the option menu. One of the most importante features develop was to sent and receive files, to test this feaure you will find o the main directory a folder named *files/send* on wich you have to save the files you want to send, and also you will find *files/received* folder where you can find the files somebody else sent you. 
   
   ![image](https://user-images.githubusercontent.com/54074539/184999361-12d9f0fe-e19d-404a-8b07-ea03d4455bc4.png)

I hope you enojoy testing this amazing xmmp-client!

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTACT -->
## Contact ✒️

Diana Zaray Corado - zaraycorado@gmail.com

Project Link: [https://github.com/DiaZa13/xmpp-client)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

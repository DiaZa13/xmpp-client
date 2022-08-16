<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](https://example.com)

There are many great README templates available on GitHub; however, I didn't find one that really suited my needs so I created this enhanced one. I want to create a README template so amazing that it'll be the last one you ever need -- I think this is it.

Here's why:
* Your time should be focused on creating something amazing. A project that solves a problem and helps others
* You shouldn't be doing the same tasks over and over like creating a README from scratch
* You should implement DRY principles to the rest of your life :smile:

Of course, no one template will serve all projects since your needs may be different. So I'll be adding more in the near future. You may also suggest changes by forking this repo and creating a pull request or opening an issue. Thanks to all the people have contributed to expanding this template!

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

[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?theme=calm&username=michalsnik&repo=aos)](https://www.oracle.com/java/technologies/)
[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?theme=calm&username=michalsnik&repo=aos)](https://github.com/igniterealtime/Smack)
[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?theme=calm&username=michalsnik&repo=aos)](https://gitbox.apache.org/repos/asf/maven-sources.git)
[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?theme=calm&username=michalsnik&repo=aos)](https://github.com/JetBrains/intellij-community)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started
Here are the basic steps to get a running copy of zclient on your local machine

### Prerequisites

Firs of all, be sure to have install the next dependencies.

* java SDK 18 
You can install it via [Java SE 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)

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

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/DiaZa13/xmpp-client.git
   ```
2. Change directory
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
 5. Execute the xmpp-client (make sure you still on client directory)
   ```sh
   java -jar .\target\xmmp-client-1.0.jar
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage



<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

Diana Zaray Corado - zaraycorado@gmail.com

Project Link: [https://github.com/DiaZa13/xmpp-client)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/othneildrew/Best-README-Template/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/othneildrew/Best-README-Template.svg?style=for-the-badge
[forks-url]: https://github.com/othneildrew/Best-README-Template/network/members
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/othneildrew/Best-README-Template/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/othneildrew/Best-README-Template/issues
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://github.com/othneildrew/Best-README-Template/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/othneildrew
[product-screenshot]: images/screenshot.png
[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Vue.js]: https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D
[Vue-url]: https://vuejs.org/
[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.io/
[Svelte.dev]: https://img.shields.io/badge/Svelte-4A4A55?style=for-the-badge&logo=svelte&logoColor=FF3E00
[Svelte-url]: https://svelte.dev/
[Laravel.com]: https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white
[Laravel-url]: https://laravel.com
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white
[JQuery-url]: https://jquery.com 

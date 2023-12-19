Telegram Tree Management Bot
This project is a Telegram bot developed using Spring Boot, PostgreSQL, and the TelegramBots library. The bot allows users to create, view, and delete category trees.

Requirements
Java 17 or higher
PostgreSQL
Docker (optional, for Dockerization)
Telegram API Token

Getting Started

1. Clone the Repository:
git clone https://github.com/BackenderDev/telegram-tree-management-bot.git
cd telegram-tree-bot
2. Set Up PostgreSQL Database:
Create a PostgreSQL database and update the application.properties file with the database configuration.
3. Configure Telegram API Token:
Get your Telegram API Token from the BotFather on Telegram.
Update the application.properties file with your Telegram API Token.
4. Build and Run the Application:
./mvnw clean install
java -jar target/telegram-tree-bot.jar
5. Start Chatting with the Bot:
Search for your bot on Telegram and start chatting by sending commands like /viewTree, /addElement, /removeElement, /help, etc.

Functionalities
/viewTree
Displays the category tree in a structured format.

/addElement <elementName>
Adds a new root element with the specified name.

/addElement <parentElement> <childElement>
Adds a new child element to an existing parent element. If the parent element does not exist, a corresponding message is displayed.

/removeElement <elementName>
Removes the specified element. If it is a root element, all child elements are also removed. Displays a message if the element is not found.

/help
Displays a list of all available commands and their brief descriptions.

/download
Downloads an Excel document with the category tree. The document format is at the developer's discretion.

/upload
Accepts an Excel document with the category tree and saves all elements in the database.

Unit Tests
The project includes unit tests to ensure the reliability of the code. Run the tests using the following command:
./mvnw test

Dockerization
To run the application using Docker, follow these steps:
1. Build Docker Image:
docker build -t telegram-tree-bot .
2. Run Docker Container:
docker run -p 8080:8080 telegram-tree-bot


Now, the Telegram Tree Management Bot is running inside a Docker container. Access it through Telegram and start managing your category trees!

Feel free to contribute, report issues, or suggest improvements. Happy coding!

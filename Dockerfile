FROM openjdk:17-alpine
WORKDIR /app
COPY chatbot/tg-bot.jar tg-bot.jar
CMD ["java", "-jar", "tg-bot.jar"]
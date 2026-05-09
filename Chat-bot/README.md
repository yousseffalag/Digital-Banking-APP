# Chat-bot

This module runs a Telegram chatbot using Spring AI and Mistral.

## Setup

1. Copy `Chat-bot/.env.example` to `Chat-bot/.env`.
2. Set your own keys and username:
   - `MISTRAL_API_KEY`
   - `TELEGRAM_API_KEY`
   - `TELEGRAM_BOT_USERNAME`
   - `BACKEND_API_BASE_URL` (for example `http://localhost:8085`)
3. Run the service from the `Chat-bot` folder:
   - Windows: `./mvnw.cmd spring-boot:run`
   - Unix/macOS: `./mvnw spring-boot:run`

## Chat commands

Use these commands in Telegram to query your banking backend:

- `/accounts` — list your accounts
- `/account <id>` — show account details
- `/history <id>` — show recent operations
- `/customers` — list all customers
- `/help` — show command help

You can also ask natural language questions like:
- "what's the balance for hassan"

## Verifying startup

- Look for Spring Boot startup logs showing `Started ChatBotApplication`.
- The bot should register successfully; if it fails, you will see a Telegram API exception in the console.
- If the bot startup is successful, open Telegram and send a message to your bot.

## Notes

- The `.env` file is ignored by Git and is only for local use.
- If you prefer, you can export the environment variables instead of using `.env`.
- The app now reads the API key values and bot username from environment variables and no longer stores your friend's keys in source control.

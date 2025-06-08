# ProteinPulseAI

ProteinPulseAI is a Spring Boot application that leverages Generative AI to calculate protein content in food items using the CIQUAL (French Food Composition Database) as its data source. This tool helps users accurately determine protein amounts in their diet, making it particularly useful for individuals following high-protein diets or tracking their protein intake.

## Features

- AI-powered protein content calculation
- Integration with CIQUAL database for accurate food composition data (through their xml files)
- Natural language processing for food item recognition
- Detailed protein breakdown and nutritional information
- Support for multiple food items in a single query
- User-friendly interface for protein calculations

## Prerequisites

- Java 17 or higher
- Maven 3.6.x or higher
- Spring Boot 3.x
- CIQUAL database access
- OpenAI API key

## Getting Started

1. Clone the repository:
```bash
git clone <repository-url>
cd protein-pulse-ai
```

2. Configure your environment variables:
```bash
# Create a .env file with the following variables
SPRING_AI_OPENAI_API_KEY=your_openai_api_key
```

3. Build the project:
```bash
./mvnw clean install
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── io/
│   │       └── hellorin/
│   │           └── proteinpulseai/
│   │               ├── component/
│   │               │   └── CiqualDataLoader.java       # Loads and initializes CIQUAL database data
│   │               ├── controller/
│   │               │   └── ProteinCalculationController.java  # Handles API endpoints
│   │               ├── model/
│   │               │   ├── CompositionTable.java      # XML mapping for food compositions
│   │               │   └── FoodItemList.java          # XML mapping for food items list
│   │               ├── repository/
│   │               │   └── NutritionRepository.java   # Manages food and nutrition data
│   │               ├── service/
│   │               │   ├── AiOrchestrationService.java    # Manages AI interactions
│   │               │   ├── FoodService.java              # Handles food-related operations
│   │               │   ├── MathService.java              # Provides mathematical calculations
│   │               │   └── ProteinService.java           # Manages protein calculations
│   │               └── ProteinPulseAiDemoApplication.java  # Main application class
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── io/
            └── hellorin/
                └── proteinpulseai/
```

Each component serves a specific purpose:
- **component**: Contains application components like data loaders
- **controller**: REST API endpoints for protein calculations
- **model**: Data models and XML mappings for CIQUAL database
- **repository**: Data access layer for nutrition information
- **service**: Business logic and AI integration services

## Configuration

The application can be configured through `application.properties` or environment variables:

```properties
# OpenAI Configuration
spring.ai.openai.api-key=${SPRING_AI_OPENAI_API_KEY}
```

## API Usage

The API accepts natural language input to calculate protein content in food items. Here's an example:

```bash
curl --location --request POST 'localhost:8080/api/protein/amount' \
--header 'Content-Type: text/plain' \
--data-raw 'How much protein did I consume if I had:
- 150 grams of '\''Chicken, meat, raw'\''
- an egg'
```

The API will return a detailed response including:
- Protein content for each food item
- Total protein amount consumed
- Breakdown of how each food contributed to the total
- Reminders about water intake and supplements

Example response:
```
Protein amount calculation completed. Final result: You consumed a total of **42.70 grams** of protein from the food items you listed.

Here's the breakdown of the contributions:

- **Chicken**: 30 grams of protein
- **Egg**: 12.7 grams of protein

Together, these two food items provided you with a significant source of protein, which is essential for muscle repair, growth, and overall health. 

These protein values are sourced from the CIQUAL database (https://ciqual.anses.fr/).

Additionally, remember to stay well-hydrated by drinking enough water, and consider taking Vitamin C/B supplements along with essential minerals to support your overall nutrition.
```

The API is flexible with natural language input. You can phrase your questions in various ways, such as:
- "What's the protein content of 150g chicken and an egg?"
- "Calculate protein for 150g raw chicken meat and one egg"
- "How much protein in 150g chicken and an egg?"

## How It Works

1. User inputs food items through the API using natural language
2. The application uses AI to understand and process the food items
3. CIQUAL database is queried for accurate nutritional information
4. Protein calculations are performed based on the database values
5. Results are returned with detailed protein content information

## Project Goals

This project serves as a practical exploration of:
- Spring AI capabilities and features
- Cursor IDE's development experience
- Integration of AI technologies in Spring Boot applications

The primary goal is to experiment with and understand the tools and frameworks provided by the Spring AI team while building a useful protein calculation application.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Spring Boot team
- OpenAI for providing the AI capabilities
- CIQUAL database for nutritional information
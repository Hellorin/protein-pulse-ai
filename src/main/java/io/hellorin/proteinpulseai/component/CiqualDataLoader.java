package io.hellorin.proteinpulseai.component;

import io.hellorin.proteinpulseai.model.Composition;
import io.hellorin.proteinpulseai.model.CompositionTable;
import io.hellorin.proteinpulseai.model.FoodItem;
import io.hellorin.proteinpulseai.model.FoodItemList;
import io.hellorin.proteinpulseai.repository.NutritionRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Component responsible for loading and initializing the CIQUAL food database data.
 * This class implements CommandLineRunner to ensure data is loaded during application startup.
 * It loads two main XML files:
 * 1. Food items database (alim_2020_07_07.xml)
 * 2. Food compositions database (compo_2020_07_07.xml)
 */
@Component
public class CiqualDataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CiqualDataLoader.class);
    private final NutritionRepository nutritionRepository;

    public CiqualDataLoader(NutritionRepository nutritionRepository) {
        this.nutritionRepository = nutritionRepository;
    }

    /**
     * Executes the data loading process when the application starts.
     * Loads both food items and their compositions into the database.
     *
     * @param args Command line arguments (not used)
     * @throws Exception if any error occurs during the loading process
     */
    @Override
    public void run(String... args) throws Exception {
        nutritionRepository.loadFood(loadFoodDatabase());
        nutritionRepository.loadCompositions(loadCompositionTable());
    }

    /**
     * Loads the food composition data from the XML file.
     * Uses JAXB to unmarshal the XML data into Composition objects.
     *
     * @return List of Composition objects containing nutritional information
     * @throws JAXBException if there's an error during XML unmarshalling
     * @throws IOException if there's an error reading the XML file
     */
    List<Composition> loadCompositionTable() throws JAXBException, IOException {
        long startTime = System.currentTimeMillis();

        JAXBContext context = JAXBContext.newInstance(CompositionTable.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        try (InputStream is = new ClassPathResource("compo_2020_07_07.xml").getInputStream()) {
            CompositionTable compositionTable = (CompositionTable) unmarshaller.unmarshal(is);

            long endTime = System.currentTimeMillis();
            LOGGER.info("Loaded {} compo items with const_code 25000 in {} ms", compositionTable.getCompositions().size(), (endTime - startTime));

            return compositionTable.getCompositions();
        }
    }

    /**
     * Loads the food items data from the XML file.
     * Uses JAXB to unmarshal the XML data into FoodItem objects.
     *
     * @return List of FoodItem objects containing food information
     * @throws JAXBException if there's an error during XML unmarshalling
     * @throws IOException if there's an error reading the XML file
     */
    List<FoodItem> loadFoodDatabase() throws JAXBException, IOException {
        long startTime = System.currentTimeMillis();
        
        JAXBContext context = JAXBContext.newInstance(FoodItemList.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        
        try (InputStream is = new ClassPathResource("alim_2020_07_07.xml").getInputStream()) {
            FoodItemList foodItemList = (FoodItemList) unmarshaller.unmarshal(is);
            List<FoodItem> items = foodItemList.getFoodItems();
            
            long endTime = System.currentTimeMillis();
            LOGGER.info("Loaded {} food items in {} ms", items.size(), (endTime - startTime));
            
            return items;
        }
    }
}

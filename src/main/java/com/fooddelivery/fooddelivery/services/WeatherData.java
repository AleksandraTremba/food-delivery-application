package com.fooddelivery.fooddelivery.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
/**
 * Component for retrieving weather data from an external XML source and updating the database.
 */
@Component
@RequiredArgsConstructor
public class WeatherData {

    private static final String URL_STRING = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private static final String TALLINN = "Tallinn-Harku";
    private static final String TARTU = "Tartu-Tõravere";
    private static final String PARNU = "Pärnu";
    private static final int FIRST_INDEX = 0;

    private static final String STATION_TAG = "station";
    private static final String NAME_TAG = "name";
    private static final String WMO_CODE_TAG = "wmocode";
    private static final String AIR_TEMPERATURE_TAG = "airtemperature";
    private static final String WIND_SPEED_TAG = "windspeed";
    private static final String PHENOMENON_TAG = "phenomenon";

    private final DeliveryService deliveryService;

    /**
     * Scheduled method to retrieve weather data periodically and update the database.
     *
     * @throws IOException If there is an error processing the XML document or updating the database.
     */
    @Scheduled(cron = "${configurable.cron}")
    public void getWeather() throws IOException {
        try {
            URL url = new URL(URL_STRING);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setExpandEntityReferences(false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(url.openStream());
            doc.getDocumentElement().normalize();

            long unixTimestamp = Long.parseLong(doc.getDocumentElement().getAttributeNode("timestamp").getValue());
            Instant instant = Instant.ofEpochSecond(unixTimestamp);
            ZoneId zone = ZoneId.of("Europe/Tallinn");
            LocalDateTime timestamp = LocalDateTime.ofInstant(instant, zone);

            NodeList nList = doc.getElementsByTagName(STATION_TAG);
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getElementsByTagName(NAME_TAG).item(FIRST_INDEX).getTextContent();
                    if (name.equals(TALLINN) || name.equals(TARTU) || name.equals(PARNU)) {
                        Integer wmo = Integer.valueOf(eElement.getElementsByTagName(WMO_CODE_TAG).item(FIRST_INDEX).getTextContent());
                        double temperature = Double.parseDouble(eElement.getElementsByTagName(AIR_TEMPERATURE_TAG).item(FIRST_INDEX).getTextContent());
                        double windSpeed = Double.parseDouble(eElement.getElementsByTagName(WIND_SPEED_TAG).item(FIRST_INDEX).getTextContent());
                        String phenomenon = eElement.getElementsByTagName(PHENOMENON_TAG).item(FIRST_INDEX).getTextContent();
                        deliveryService.updatingDatabase(name, wmo, temperature, windSpeed, phenomenon, timestamp);
                    }
                }
            }
        } catch (NumberFormatException | ParserConfigurationException | SAXException | IOException e) {
            throw new IOException("Error processing XML document", e);
        }
    }
}
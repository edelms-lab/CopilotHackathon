package com.microsoft.hackathon.copilotdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStream;

/* 
* Create a GET operation to return the value of a key passed as query parameter. 
* If the key is not passed, return "key not passed".
* If the key is passed, return "hello <key>".
* 
*/
@RestController
public class DemoController {

    @GetMapping("/hello")
    public String demo(@RequestParam(value = "key", required = false) String key) {
        if (key == null) {
            return "key not passed";
        } else {
            return "hello " + key;
        }
    }

    //create a New operation under /diffdates that calculates the difference between two dates. The operation should receive two dates as parameter in format dd-MM-yyyy and return the difference in days
    @GetMapping("/diffdates")
    public String diffDates(@RequestParam(value = "date1") String date1, @RequestParam(value = "date2") String date2) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
        java.time.LocalDate d1 = java.time.LocalDate.parse(date1, formatter);
        java.time.LocalDate d2 = java.time.LocalDate.parse(date2, formatter);
        long diff = java.time.temporal.ChronoUnit.DAYS.between(d1, d2);
        return "Difference between " + date1 + " and " + date2 + " is " + diff + " days";
    }

    // Validate the format of a spanish phone number (+34 prefix, then 9 digits, starting with 6, 7 or 9). The operation should receive a phone number as parameter and return true if the format is correct, false otherwise.
    @GetMapping("/validatephone")
    public boolean validatephone(@RequestParam(name = "phone", required = false) String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        String regex = "^\\+34[679]\\d{8}$";
        return phone.matches(regex);
    }


    // Validate the format of a spanish DNI (8 digits and 1 letter). The operation should receive a DNI as parameter and return true if the format is correct, false otherwise.
    @GetMapping("/validatedni")
    public boolean validatedni(@RequestParam(name = "dni", required = false) String dni) {
        if (dni == null || dni.isEmpty()) {
            return false;
        }
        String regex = "^\\d{8}[A-Za-z]$";
        return dni.matches(regex);
    }


    //Based on existing colors.json file under resources, given the name of the color as path parameter, return the hexadecimal code. If the color is not found, return 404
    @GetMapping("/color/{name}")
    public ResponseEntity<String> color(@PathVariable("name") String name) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("colors.json");
        ObjectMapper objectMapper = new ObjectMapper();
        // create JsonNode from mapper
        JsonNode rootNode = objectMapper.readTree(inputStream);
        for (JsonNode color : rootNode) {
            // if color name is found, return the hex code
            if (color.get("color").asText().equals(name)) {
                return new ResponseEntity<String>(color.get("code").get("hex").asText(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<String>("Color not found", HttpStatus.NOT_FOUND);
    }


    // new operation that call the API https://api.chucknorris.io/jokes/random and return the joke
    @GetMapping("/joke")
    public String joke() throws IOException {
        java.net.URL url = new java.net.URL("https://api.chucknorris.io/jokes/random");
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        if (status == 200) {
            InputStream inputStream = con.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(inputStream);
            return rootNode.get("value").asText();
        } else {
            return "Error calling joke API";
        }
    }



    //write a code for Given a url as query parameter, parse it and return the protocol, host, port, path and query parameters. The response should be in Json format
    @GetMapping("/parseurl")
    public ResponseEntity<String> parseurl(@RequestParam(name = "url", required = false) String url) throws IOException {
        if (url == null || url.isEmpty()) {
            return new ResponseEntity<String>("URL not passed", HttpStatus.BAD_REQUEST);
        }
        java.net.URL urlObj = new java.net.URL(url);
        String protocol = urlObj.getProtocol();
        String host = urlObj.getHost();
        int port = urlObj.getPort();
        String path = urlObj.getPath();
        String query = urlObj.getQuery();
        ObjectMapper objectMapper = new ObjectMapper();
        com.fasterxml.jackson.databind.node.ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("protocol", protocol);
        rootNode.put("host", host);
        rootNode.put("port", port);
        rootNode.put("path", path);
        
        // Parse query parameters into an object
        com.fasterxml.jackson.databind.node.ObjectNode queryNode = objectMapper.createObjectNode();
        if (query != null && !query.isEmpty()) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    queryNode.put(keyValue[0], keyValue[1]);
                }
            }
        }
        rootNode.set("query", queryNode);
        
        return new ResponseEntity<String>(objectMapper.writeValueAsString(rootNode), HttpStatus.OK);
    }


    //write a code for Given the path of a file and count the number of occurrences of a provided word. The path and the word should be query parameters. The response should be in Json format
    @GetMapping("/countword")
    public ResponseEntity<String> countword(@RequestParam(name = "path", required = false) String path, @RequestParam(name = "word", required = false) String word) throws IOException {
        if (path == null || path.isEmpty() || word == null || word.isEmpty()) {
            return new ResponseEntity<String>("Path or word not passed", HttpStatus.BAD_REQUEST);
        }
        java.nio.file.Path filePath = java.nio.file.Paths.get(path);
        if (!java.nio.file.Files.exists(filePath)) {
            return new ResponseEntity<String>("File not found", HttpStatus.NOT_FOUND);
        }
        String content = new String(java.nio.file.Files.readAllBytes(filePath));
        int count = content.split(word, -1).length - 1;
        ObjectMapper objectMapper = new ObjectMapper();
        com.fasterxml.jackson.databind.node.ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("count", count);
        return new ResponseEntity<String>(objectMapper.writeValueAsString(rootNode), HttpStatus.OK);
    }

}

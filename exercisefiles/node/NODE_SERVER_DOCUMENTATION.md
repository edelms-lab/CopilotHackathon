# Node.js Server Documentation

A comprehensive Node.js HTTP server with multiple endpoints for various operations including validation, API integration, file processing, and system information.

## Server Details

- **Port:** 3000
- **Base URL:** http://localhost:3000
- **Default Response:** 405 Method Not Supported (for unsupported methods)

## Dependencies

- `http` - Node.js built-in HTTP module
- `fs` - Node.js file system module
- `axios` - HTTP client for external API calls
- `readline` - Node.js line-by-line file reading

## Endpoints

### 1. `/get` - Simple Key-Value Greeting
**Method:** GET  
**Query Parameters:**
- `key` (string) - The key to append to the greeting

**Responses:**
- `key not passed` - When no key parameter is provided
- `hello {key}` - When a key is provided

**Example:**
```
GET http://localhost:3000/get?key=world
Response: hello world
```

---

### 2. `/DaysBetweenDates` - Calculate Days Between Two Dates
**Method:** GET  
**Query Parameters:**
- `date1` (string) - First date (ISO 8601 format)
- `date2` (string) - Second date (ISO 8601 format)

**Responses:**
- `Days between {date1} and {date2}: {count}` - Success
- `date1 and date2 parameters are required` - Missing parameters
- `Invalid date format` - Invalid date format

**Example:**
```
GET http://localhost:3000/DaysBetweenDates?date1=2026-01-01&date2=2026-12-31
Response: Days between 2026-01-01 and 2026-12-31: 364
```

---

### 3. `/Validatephonenumber` - Spanish Phone Number Validation
**Method:** GET  
**Query Parameters:**
- `phoneNumber` (string) - Phone number to validate (Spanish format)

**Validation Rules:**
- Optional country code: +34, 0034, or 34
- Mobile numbers starting with 6 or 7
- 9 digits after country code
- Allows spaces and dashes between numbers

**Responses:**
- `valid` - Phone number matches Spanish format
- `invalid` - Phone number doesn't match Spanish format

**Examples:**
```
GET http://localhost:3000/Validatephonenumber?phoneNumber=34666666666
Response: valid

GET http://localhost:3000/Validatephonenumber?phoneNumber=+34666666666
Response: valid

GET http://localhost:3000/Validatephonenumber?phoneNumber=666-666-666
Response: valid

GET http://localhost:3000/Validatephonenumber?phoneNumber=123456789
Response: invalid
```

---

### 4. `/ValidateSpanishDNI` - Spanish DNI Validation
**Method:** GET  
**Query Parameters:**
- `dni` (string) - Spanish DNI to validate (format: 8 digits + 1 letter)

**Validation Logic:**
- Calculates the correct DNI letter using modulo 23 against the letter table: "TRWAGMYFPDXBNJZSQVHLCKE"
- Compares the provided letter with the calculated letter

**Responses:**
- `valid` - DNI letter matches calculated value
- `invalid` - DNI letter doesn't match calculated value

**Example:**
```
GET http://localhost:3000/ValidateSpanishDNI?dni=12345678Z
Response: valid or invalid (depends on correct calculation)
```

---

### 5. `/ReturnColorCode` - Get Color Hex Code
**Method:** GET  
**Query Parameters:**
- `color` (string) - Color name to look up

**Requirements:**
- Must have `colors.json` file in the same directory
- File should contain an array of color objects with `color` and `code.hex` fields

**Responses:**
- `{hex_code}` - Hex code of the color (e.g., #FF0000)
- `not found` - Color not found in colors.json

**Example:**
```
GET http://localhost:3000/ReturnColorCode?color=red
Response: #FF0000
```

---

### 6. `/TellMeAJoke` - Random Joke Generator
**Method:** GET  
**External API:** https://official-joke-api.appspot.com/random_joke

**Responses:**
- `{setup} {punchline}` - Random joke (setup + punchline)
- `Error fetching joke` - API call failed

**Example:**
```
GET http://localhost:3000/TellMeAJoke
Response: Why did the programmer quit his job? Because he didn't get arrays
```

---

### 7. `/MoviesByDirector` - Search Movies by Director
**Method:** GET  
**Query Parameters:**
- `director` (string) - Director name to search for

**External API:** OMDB API (http://www.omdbapi.com)  
**API Key:** Required (configure in code - currently set to 'XXXXXXX')

**Responses:**
- `{movie1}, {movie2}, ...` - Comma-separated list of movies
- `Error fetching movies` - API call failed or director not found

**Note:** You must obtain an API key from https://www.omdbapi.com/apikey.aspx and replace 'XXXXXXX' in the code.

**Example:**
```
GET http://localhost:3000/MoviesByDirector?director=Steven%20Spielberg
Response: Jaws, E.T. the Extra-Terrestrial, Jurassic Park, ...
```

---

### 8. `/ParseUrl` - Parse URL Components
**Method:** GET  
**Query Parameters:**
- `someurl` (string) - Complete URL to parse

**Parsed Components:**
- Protocol (e.g., https://)
- Host (domain with port)
- Port (if specified)
- Pathname (path after domain)
- Search (query string)
- Hash (fragment identifier)

**Response:**
- `host: {host}` - The parsed host component
- `Invalid URL` - URL parsing failed

**Example:**
```
GET http://localhost:3000/ParseUrl?someurl=https://www.example.com:8080/path?query=value#hash
Response: host: www.example.com:8080
```

---

### 9. `/GetFullTextFile` - Read File and Filter Lines
**Method:** GET  
**File:** Reads `sample.txt` from the same directory

**Functionality:**
- Reads entire file into memory
- Splits by carriage return (\r)
- Filters lines containing "Fusce"

**Responses:**
- `{line1}, {line2}, ...` - Comma-separated matching lines
- `Error reading file` - File read failed

**Example:**
```
GET http://localhost:3000/GetFullTextFile
Response: Fusce ornare lacinia lorem, Fusce vitae mauris nec...
```

**Note:** This loads the entire file into memory. Use `/GetLineByLinefromtTextFile` for large files.

---

### 10. `/GetLineByLinefromtTextFile` - Read File Line-by-Line (Streaming)
**Method:** GET  
**File:** Reads `sample.txt` from the same directory

**Functionality:**
- Uses Node.js readline interface for efficient streaming
- Implements Promise-based async processing
- Filters lines containing "Fusce"

**Responses:**
- `{line1},{line2},...` - Comma-separated matching lines
- `Error reading file` - File read failed

**Example:**
```
GET http://localhost:3000/GetLineByLinefromtTextFile
Response: Fusce ornare lacinia lorem,Fusce vitae mauris nec...
```

**Advantages:** Memory-efficient for large files, processes one line at a time

---

### 11. `/CalculateMemoryConsumption` - Get Memory Usage
**Method:** GET  
**No Query Parameters**

**Functionality:**
- Returns the heap memory currently used by the Node.js process
- Converts bytes to GB
- Rounds to 2 decimal places

**Response:**
- `{value} GB` - Memory consumption (e.g., 0.05 GB)

**Example:**
```
GET http://localhost:3000/CalculateMemoryConsumption
Response: 0.05 GB
```

---

### 12. `/RandomEuropeanCountry` - Get Random European Country
**Method:** GET  
**No Query Parameters**

**Functionality:**
- Returns a random European country from a predefined array of 50+ countries
- Includes ISO country code

**Response:**
- `{country} {iso_code}` - Country name and 2-letter ISO code

**Supported Countries:** Italy, France, Spain, Germany, UK, Greece, Portugal, Romania, Bulgaria, Croatia, Czech Republic, Denmark, Estonia, Finland, Hungary, Ireland, Latvia, Lithuania, Luxembourg, Malta, Netherlands, Poland, Slovakia, Slovenia, Sweden, Belgium, Austria, Switzerland, Cyprus, Iceland, Norway, Albania, Andorra, Armenia, Azerbaijan, Belarus, Bosnia and Herzegovina, Georgia, Kazakhstan, Kosovo, Liechtenstein, Macedonia, Moldova, Monaco, Montenegro, Russia, San Marino, Serbia, Turkey, Ukraine, Vatican City

**Example:**
```
GET http://localhost:3000/RandomEuropeanCountry
Response: France FR
```

---

## Setup and Running

### Installation
```bash
npm install axios
```

### Running the Server
```bash
node nodeserver.js
```

### Output
```
server is listening on port 3000
```

---

## Error Handling

All endpoints return appropriate HTTP status codes:
- **200:** Success
- **400:** Bad request (invalid parameters or URL)
- **405:** Method not supported (non-GET requests)
- **500:** Server error (file read, API call failures)

---

## Notes

- **OMDB API Key:** Replace 'XXXXXXX' in the MoviesByDirector endpoint with your actual API key
- **File Dependencies:** `/ReturnColorCode`, `/GetFullTextFile`, and `/GetLineByLinefromtTextFile` require `colors.json` and `sample.txt` files in the working directory
- **Async Operations:** Joke and Movie endpoints use axios promises for async HTTP calls
- **Memory:** The memory consumption endpoint returns heap usage only (not total process memory)
- **URL Encoding:** Query parameters with spaces should be URL-encoded (use %20)

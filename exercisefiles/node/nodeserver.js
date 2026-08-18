// write a nodejs server that will expose a method call "get" that will return the value of the key passed in the query string
// example: http://localhost:3000/get?key=hello
// if the key is not passed, return "key not passed"
// if the key is passed, return "hello" + key
// if the url has other methods, return "method not supported"
// when server is listening, log "server is listening on port 3000"

const http = require('http');
const fs = require('fs');
const axios = require('axios');
const readline = require('readline');

const server = http.createServer((req, res) => {
    const url = new URL(req.url, `http://${req.headers.host}`);
    const pathname = url.pathname;
    const searchParams = url.searchParams;

    if (req.method === 'GET' && pathname === '/get') {
        const key = searchParams.get('key');
        if (!key) {
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end('key not passed');
        } else {
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end(`hello ${key}`);
        }

    } else if (req.method === 'GET' && pathname === '/DaysBetweenDates') {
        const date1 = searchParams.get('date1');
        const date2 = searchParams.get('date2');
        
        if (!date1 || !date2) {
            res.writeHead(400, { 'Content-Type': 'text/plain' });
            res.end('date1 and date2 parameters are required');
        } else {
            try {
                // calculate days between two dates
                const d1 = new Date(date1);
                const d2 = new Date(date2);
                const timeDiff = Math.abs(d2 - d1);
                const daysDiff = Math.ceil(timeDiff / (1000 * 60 * 60 * 24));
                
                res.writeHead(200, { 'Content-Type': 'text/plain' });
                res.end(`Days between ${date1} and ${date2}: ${daysDiff}`);
            } catch (e) {
                res.writeHead(400, { 'Content-Type': 'text/plain' });
                res.end('Invalid date format');
            }
        }

    } else if (req.method === 'GET' && pathname === '/Validatephonenumber') {
        // Get phoneNumber from querystring
        const phoneNumber = searchParams.get('phoneNumber');
        
        // Validate phoneNumber with Spanish format
        const regex = /^(\+34|0034|34)?[ -]*(6|7)[ -]*([0-9][ -]*){8}$/;
        
        // If phoneNumber is valid return "valid"
        if (regex.test(phoneNumber)) {
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end('valid');
        }
        // If phoneNumber is not valid return "invalid"
        else {
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end('invalid');
        }

    } else if (req.method === 'GET' && pathname === '/ValidateSpanishDNI') {
        // Get dni from querystring
        const dni = searchParams.get('dni');
        
        // Calculate DNI letter
        const dniLetter = dni.charAt(dni.length - 1);
        const dniNumber = dni.substring(0, dni.length - 1);
        const dniLetterCalc = "TRWAGMYFPDXBNJZSQVHLCKE".charAt(dniNumber % 23);
        
        // If DNI is valid return "valid"
        if (dniLetter === dniLetterCalc) {
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end('valid');
        }
        // If DNI is not valid return "invalid"
        else {
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end('invalid');
        }

    } else if (req.method === 'GET' && pathname === '/ReturnColorCode') {
        // Read colors.json file and return the hex code
        const colors = fs.readFileSync('colors.json', 'utf-8');
        const colorsObj = JSON.parse(colors);
        
        // Get color var from querystring
        const color = searchParams.get('color');
        let colorFound = 'not found';
        
        // For each color in colors.json
        for (let i = 0; i < colorsObj.length; i++) {
            // If color is found return the color code hex
            if (colorsObj[i].color === color) {
                colorFound = colorsObj[i].code.hex;
                break;
            }
        }
        
        res.writeHead(200, { 'Content-Type': 'text/plain' });
        res.end(colorFound);

    } else if (req.method === 'GET' && pathname === '/TellMeAJoke') {
        // Make a call to the joke API and return a random joke using axios
        axios.get('https://official-joke-api.appspot.com/random_joke')
            .then(function (response) {
                // Handle success
                res.writeHead(200, { 'Content-Type': 'text/plain' });
                res.end(response.data.setup + " " + response.data.punchline);
            })
            .catch(function (error) {
                // Handle error
                console.log(error);
                res.writeHead(500, { 'Content-Type': 'text/plain' });
                res.end('Error fetching joke');
            });

    } else if (req.method === 'GET' && pathname === '/MoviesByDirector') {
        // Get director name from querystring
        const director = searchParams.get('director');
        
        // Make a call to the OMDB API and return a list of movies using axios
        axios.get('http://www.omdbapi.com/?apikey=XXXXXXX&s=' + director)
            .then(function (response) {
                // Return the full list of movies
                let movies = '';
                if (response.data.Search) {
                    for (let i = 0; i < response.data.Search.length; i++) {
                        movies = movies + response.data.Search[i].Title + ', ';
                    }
                }
                res.writeHead(200, { 'Content-Type': 'text/plain' });
                res.end(movies);
            })
            .catch(function (error) {
                // Handle error
                console.log(error);
                res.writeHead(500, { 'Content-Type': 'text/plain' });
                res.end('Error fetching movies');
            });

    } else if (req.method === 'GET' && pathname === '/ParseUrl') {
        // Retrieves a parameter from querystring called someurl
        const someUrl = searchParams.get('someurl');
        
        // Parse the URL and extract protocol, host, port, path, querystring and hash
        try {
            const urlObj = new URL(someUrl);
            const protocol = urlObj.protocol;
            const host = urlObj.host;
            const port = urlObj.port;
            const path = urlObj.pathname;
            const querystring = urlObj.search;
            const hash = urlObj.hash;
            
            // Return the parsed host
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end('host: ' + host);
        } catch (error) {
            res.writeHead(400, { 'Content-Type': 'text/plain' });
            res.end('Invalid URL');
        }

    } else if (req.method === 'GET' && pathname === '/GetFullTextFile') {
        // Read sample.txt and return lines that contain the word "Fusce"
        try {
            const text = fs.readFileSync('sample.txt', 'utf-8');
            const lines = text.split('\r');
            let linesFound = '';
            
            for (let i = 0; i < lines.length; i++) {
                if (lines[i].includes('Fusce')) {
                    linesFound = linesFound + lines[i] + ', ';
                }
            }
            
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end(linesFound);
        } catch (error) {
            res.writeHead(500, { 'Content-Type': 'text/plain' });
            res.end('Error reading file');
        }

    } else if (req.method === 'GET' && pathname === '/GetLineByLinefromtTextFile') {
        // Read sample.txt line by line
        const lineReader = readline.createInterface({
            input: fs.createReadStream('sample.txt')
        });
        
        // Create a promise to read the file line by line and return lines containing "Fusce"
        const promise = new Promise(function (resolve, reject) {
            const lines = [];
            lineReader.on('line', function (line) {
                if (line.includes('Fusce')) {
                    lines.push(line);
                }
            });
            lineReader.on('close', function () {
                resolve(lines);
            });
            lineReader.on('error', function (error) {
                reject(error);
            });
        });
        
        // Return the list of lines
        promise.then(function (lines) {
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end(lines.toString());
        }).catch(function (error) {
            res.writeHead(500, { 'Content-Type': 'text/plain' });
            res.end('Error reading file');
        });

    } else if (req.method === 'GET' && pathname === '/CalculateMemoryConsumption') {
        // Return the memory consumption of the process in GB, rounded to 2 decimals
        const memory = process.memoryUsage().heapUsed / 1024 / 1024 / 1024;
        res.writeHead(200, { 'Content-Type': 'text/plain' });
        res.end(memory.toFixed(2) + ' GB');

    } else if (req.method === 'GET' && pathname === '/RandomEuropeanCountry') {
        // Make an array of european countries and its iso codes
        const countries = [
            { country: "Italy", iso: "IT" },
            { country: "France", iso: "FR" },
            { country: "Spain", iso: "ES" },
            { country: "Germany", iso: "DE" },
            { country: "United Kingdom", iso: "GB" },
            { country: "Greece", iso: "GR" },
            { country: "Portugal", iso: "PT" },
            { country: "Romania", iso: "RO" },
            { country: "Bulgaria", iso: "BG" },
            { country: "Croatia", iso: "HR" },
            { country: "Czech Republic", iso: "CZ" },
            { country: "Denmark", iso: "DK" },
            { country: "Estonia", iso: "EE" },
            { country: "Finland", iso: "FI" },
            { country: "Hungary", iso: "HU" },
            { country: "Ireland", iso: "IE" },
            { country: "Latvia", iso: "LV" },
            { country: "Lithuania", iso: "LT" },
            { country: "Luxembourg", iso: "LU" },
            { country: "Malta", iso: "MT" },
            { country: "Netherlands", iso: "NL" },
            { country: "Poland", iso: "PL" },
            { country: "Slovakia", iso: "SK" },
            { country: "Slovenia", iso: "SI" },
            { country: "Sweden", iso: "SE" },
            { country: "Belgium", iso: "BE" },
            { country: "Austria", iso: "AT" },
            { country: "Switzerland", iso: "CH" },
            { country: "Cyprus", iso: "CY" },
            { country: "Iceland", iso: "IS" },
            { country: "Norway", iso: "NO" },
            { country: "Albania", iso: "AL" },
            { country: "Andorra", iso: "AD" },
            { country: "Armenia", iso: "AM" },
            { country: "Azerbaijan", iso: "AZ" },
            { country: "Belarus", iso: "BY" },
            { country: "Bosnia and Herzegovina", iso: "BA" },
            { country: "Georgia", iso: "GE" },
            { country: "Kazakhstan", iso: "KZ" },
            { country: "Kosovo", iso: "XK" },
            { country: "Liechtenstein", iso: "LI" },
            { country: "Macedonia", iso: "MK" },
            { country: "Moldova", iso: "MD" },
            { country: "Monaco", iso: "MC" },
            { country: "Montenegro", iso: "ME" },
            { country: "Russia", iso: "RU" },
            { country: "San Marino", iso: "SM" },
            { country: "Serbia", iso: "RS" },
            { country: "Turkey", iso: "TR" },
            { country: "Ukraine", iso: "UA" },
            { country: "Vatican City", iso: "VA" }
        ];
        
        // Return a random country from the array
        const randomCountry = countries[Math.floor(Math.random() * countries.length)];
        
        // Return the country and its iso code
        res.writeHead(200, { 'Content-Type': 'text/plain' });
        res.end(randomCountry.country + " " + randomCountry.iso);

    } else {
        res.writeHead(405, { 'Content-Type': 'text/plain' });
        res.end('method not supported');
    }
});

server.listen(3000, () => {
    console.log('server is listening on port 3000');
});
package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
)

// Book ...
type Book struct {
	id     int
	title  string
	author string
	read   bool
}

const (
	htmlTemplate = `<!DOCTYPE html>
    <html>
    <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
    
        <title>lit's reading journey</title>
    
        <style>
            .read {
                border: solid 2px green;
                border-bottom: 0;
                margin: 1em 0;
            }
            .noread {
                border: solid 2px read;
                border-bottom: 0;
                margin: 1em 0;
            }
        </style>
    </head>
    
    <body>
        <header>
            <h1>lit's reading journey</h1>
        </header>
    
            <h2>Read: @read@</h2>
            
            <h2>Full list</h2>
                    
            <ul>@list@</ul>    
    
    </body></html>
    `
)

func (b *Book) String() string {
	if b.read {
		return fmt.Sprintf("(%d) \"%s\" by %s (read)", b.id, b.title, b.author)
	}
	return fmt.Sprintf("(%d) \"%s\" by %s", b.id, b.title, b.author)
}

// HTML ...
func (b *Book) HTML() string {
	if b.read {
		return fmt.Sprintf("<li class=\"read\">(%d) \"%s\" by %s</li>", b.id, b.title, b.author)
	}
	return fmt.Sprintf("<li class=\"noread\">(%d) \"%s\" by %s</li>", b.id, b.title, b.author)
}

func numberOfReadBooks(books *[]Book) int {
	read := 0
	for _, b := range *books {
		if b.read {
			read++
		}
	}
	return read
}

func readInputFile(inputFile string) ([]Book, error) {
	var books []Book

	file, err := os.Open(inputFile)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		text := scanner.Text()
		fields := strings.Split(text, ",")
		id, err := strconv.ParseInt(fields[0], 10, 64)
		if err != nil {
			panic(err)
		}
		read := false
		if len(fields) > 3 {
			read = true
		}
		if err != nil {
			panic(err)
		}
		book := Book{
			id:     int(id),
			title:  fields[1],
			author: fields[2],
			read:   read,
		}
		books = append(books, book)
	}

	if err != nil {
		return nil, err
	}

	return books, nil
}

func main() {

	if len(os.Args) != 2 {
		log.Fatal("wrong arguments")
	}

	file := os.Args[1]

	books, err := readInputFile(file)
	if err != nil {
		panic(err)
	}

	output := strings.ReplaceAll(htmlTemplate, "@read@", fmt.Sprintf("%d", numberOfReadBooks(&books)))

	var buf strings.Builder
	for _, b := range books {
		buf.WriteString(b.HTML())
	}

	output = strings.ReplaceAll(output, "@list@", buf.String())

	fmt.Println(output)

}

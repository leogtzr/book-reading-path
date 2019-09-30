package main

import (
	"testing"
)

func TestBookString(t *testing.T) {
	b := Book{
		id:     1,
		read:   true,
		title:  "a",
		author: "b",
	}

	expectedBookString := "(1) \"a\" by b (read)"
	if b.String() != expectedBookString {
		t.Errorf("expecting: %s, got: %s", expectedBookString, b.String())
	}

}

func TestBookHTML(t *testing.T) {
	b := Book{
		id:     1,
		read:   true,
		title:  "a",
		author: "b",
	}
	expectedBookHTML := "<li class=\"read\">(1) \"a\" by <i>b</i></li>"
	if b.HTML() != expectedBookHTML {
		t.Errorf("expecting: %s, got: %s", expectedBookHTML, b.HTML())
	}

	b.read = false

	expectedBookHTML = "<li class=\"noread\">(1) \"a\" by <i>b</i></li>"
	if b.HTML() != expectedBookHTML {
		t.Errorf("expecting: %s, got: %s", expectedBookHTML, b.HTML())
	}
}

func TestNumberOfRecords(t *testing.T) {
	books := []Book{
		Book{read: true},
		Book{read: false},
		Book{read: true},
	}

	expectedNumberOfRecords := 2
	numberOfBooksRead := numberOfReadBooks(&books)

	if expectedNumberOfRecords != numberOfBooksRead {
		t.Errorf("expected: %d, got: %d", expectedNumberOfRecords, numberOfBooksRead)
	}

}

func TestExtractBookFromRead(t *testing.T) {
	b := Book{
		id:     1,
		title:  "Moby Dick",
		author: "Melville",
		read:   true,
	}

	text := "1,Moby Dick,Melville,x"
	bookFromText, err := extractBookFromRecord(text)
	if err != nil {
		t.Error(err)
	}

	if b != bookFromText {
		t.Errorf("expected: %s, got: %s", b.String(), bookFromText.String())
	}
}

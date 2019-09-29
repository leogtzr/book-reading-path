package com.reading.journey.batch;

import com.reading.journey.domain.Book;
import com.reading.journey.util.BookUtils;
import org.springframework.batch.item.ItemWriter;

import java.io.PrintWriter;
import java.util.List;

public class StringBuilderItemWriter implements ItemWriter<Book> {

    private static final String HTML_TEMPLATE = "<!DOCTYPE html>\n"+
            "    <html>\n"+
            "    <head>\n"+
            "    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n"+
            "        <meta charset=\"utf-8\">\n"+
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"+
            "    \n"+
            "        <title>lit's reading journey</title>\n"+
            "    \n"+
            "        <style>\n"+
            "            .read {\n"+
            "                border: solid 2px red;\n"+
            "                border-bottom: 0;\n"+
            "                margin: 1em 0;\n"+
            "            }\n"+
            "            .noread {\n"+
            "                border: solid 2px green;\n"+
            "                border-bottom: 0;\n"+
            "                margin: 1em 0;\n"+
            "            }\n"+
            "        </style>\n"+
            "    </head>\n"+
            "    \n"+
            "    <body>\n"+
            "        <header>\n"+
            "            <h1>lit's reading journey</h1>\n"+
            "        </header>\n"+
            "    \n"+
            "            <h2>Read: @read@</h2>\n"+
            "            \n"+
            "            <h2>Full list</h2>\n"+
            "                    \n"+
            "            <ul>@list@</ul>    \n"+
            "    \n"+
            "    </body></html>";

    private final String outputFilePath;

    public StringBuilderItemWriter(final String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    @Override
    public void write(final List<? extends Book> books) throws Exception {
        final StringBuilder sb = new StringBuilder();

        try (final PrintWriter pw = new PrintWriter(outputFilePath)) {
            books.forEach(book -> sb.append(book.toHTML()));
            String output = HTML_TEMPLATE.replaceAll("@list@", sb.toString());
            output = output.replaceAll("@read@", BookUtils.numberOfRead(books) + "");
            pw.write(output);
        }
    }

}

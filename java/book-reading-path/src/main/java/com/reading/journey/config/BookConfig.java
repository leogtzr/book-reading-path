package com.reading.journey.config;

import com.reading.journey.batch.BookReadingProcessor;
import com.reading.journey.domain.Book;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BookConfig {

    @Value("${csvFilePath}")
    private String csvFilePath;

    @Value("${htmlOutputFile}")
    private String htmlOutputFile;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public String htmlOutputFile() {
        return htmlOutputFile;
    }

    @Bean
    public String csvFilePath() {
        return csvFilePath;
    }

    @Bean
    public FlatFileItemReader<Book> csvBookReader() {
        final FlatFileItemReader <Book> reader = new FlatFileItemReader<> ();
        reader.setResource(new FileSystemResource(csvFilePath()));
        reader.setLineMapper(new DefaultLineMapper<>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[]{
                                "id",
                                "title",
                                "author"
                        });
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Book>() {
                    {
                        setTargetType(Book.class);
                    }
                });
            }
        });
        return reader;
    }


    @Bean
    public ItemProcessor<Book, Book> csvBookProcessor() {
        return new BookReadingProcessor();
    }

    @Bean
    public Step csvFileTransformStep() throws Exception {
        return stepBuilderFactory.get("csvFileTransformStep")
                .<Book, Book>chunk(1)
                .reader(csvBookReader())
                .processor(csvBookProcessor())
                .writer(bookItemWriter())
                .build();
    }

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("job")
                .start(csvFileTransformStep())
                .build()
                ;
    }

    @Bean
    public FlatFileItemWriter<Book> bookItemWriter() throws Exception {
        final FlatFileItemWriter<Book> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setLineAggregator(new PassThroughLineAggregator<>());
        // String customerOutputPath = File.createTempFile("customerOutput", ".out").getAbsolutePath();
        // System.out.println(">> Output Path: " + customerOutputPath);
        itemWriter.setResource(new FileSystemResource(htmlOutputFile()));
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

}

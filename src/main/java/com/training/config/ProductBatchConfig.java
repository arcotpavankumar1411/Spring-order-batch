package com.training.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.training.model.Product;
import com.training.repository.ProductRepository;

@Configuration
@EnableBatchProcessing
public class ProductBatchConfig {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private StepBuilderFactory stepfact;

	@Autowired
	private JobBuilderFactory jobFactory;


	@Bean
	public FlatFileItemReader<Product> itemReader() {

		FlatFileItemReader<Product> fileReader = new FlatFileItemReader<>();

		fileReader.setResource(new FileSystemResource("src/main/resources/product.csv"));
		fileReader.setName("Product-CSV");
		fileReader.setLinesToSkip(1);
		fileReader.setLineMapper(lineMapper());

		return fileReader;

	}

	private LineMapper<Product> lineMapper() {

		DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("p_Id", "productName", "price");

		BeanWrapperFieldSetMapper<Product> setMapper = new BeanWrapperFieldSetMapper<>();
		setMapper.setTargetType(Product.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(setMapper);

		return lineMapper;
	}

	@Bean
	public ProductProcess productprocess() {

		return new ProductProcess();
	}

	@Bean
	public RepositoryItemWriter<Product> itemWriter() {

		RepositoryItemWriter<Product> itermwriter = new RepositoryItemWriter<>();
		itermwriter.setRepository(productRepo);
		itermwriter.setMethodName("save");

		return itermwriter;
	}

	@Bean
	public Step step() {

		return stepfact.get("Step-1").<Product, Product>chunk(10).reader(itemReader())
				.processor(productprocess()).writer(itemWriter()).taskExecutor(taskExecutor()).build();

	}

	@Bean
	public Job job() {
		  
		
		return jobFactory.get("product-Job").flow(step()).end().build();
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(10);
		return taskExecutor;
	}

}

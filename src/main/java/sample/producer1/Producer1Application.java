package sample.producer1;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;


import com.example.Sensor;
import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.schema.registry.client.ConfluentSchemaRegistryClient;
//import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;
//import org.springframework.cloud.schema.registry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
//@EnableSchemaRegistryClient
@RestController
@RequiredArgsConstructor

public class Producer1Application {

	private Random random = new Random();
	
	@Autowired
	private KafkaTemplate<String,Object> template;

	BlockingQueue<Sensor> unbounded = new LinkedBlockingQueue<>();
	
	@Autowired
	  private ProducerService producer; 

	public static void main(String[] args) {
		SpringApplication.run(Producer1Application.class, args);
	}
	// injected from application.properties
	     @Value("${topic.name}")  
		  private String topicName;

	     @Value("${topic.partitions-num}")
		  private int numPartitions;

	     @Value("${topic.replication-factor}")
		  private int replicas;

		  @Bean
		  NewTopic moviesTopic() {
		    return new NewTopic(topicName, numPartitions, (short) replicas);
		  }

	/*private Sensor randomSensor() {
		Sensor sensor = new Sensor();
		sensor.setId(UUID.randomUUID().toString() + "-v1");
		sensor.setAcceleration(random.nextFloat() * 10);
		sensor.setVelocity(random.nextFloat() * 100);
		sensor.setTemperature(random.nextFloat() * 50);
		return sensor;
	}*/

	/*@RequestMapping(value = "/messages", method = RequestMethod.POST)
	public String sendMessage() {
		//unbounded.offer(randomSensor());
		Sensor sensor = new Sensor();
		sensor.setId("Mona25");
		sensor.setAcceleration(56.8f);
		sensor.setVelocity(89.8f);
		sensor.setTemperature(89.7f);
		template.send(topicName, new Sensor());

		
		return "ok, have fun with v1 payload!";
	}
	
	/*@GetMapping("/data/{id}/{acceleration}/{velocity}/{temperature}")
	public String sendMessage(@PathVariable String id, @PathVariable float acceleration, @PathVariable float velocity,
			 @PathVariable float temperature) {
		Sensor sense=new Sensor(id,acceleration,velocity,temperature);
		template.send(topicName,sense);
        return "Sent employee details to consumer";
	}
	
	
	 @RequestMapping(method=RequestMethod.GET,value="/publish/{name}")
	   public String getMessage(@PathVariable("name") final String name) {
		 template.send(topicName, "Hi"+" "+name); 
		  return "Data Published Successfully";
		  }
	 */
	 @GetMapping("/data/{id}/{acceleration}/{velocity}/{temperature}")
	    public String producerAvroMessage(@PathVariable String id, @PathVariable float acceleration, @PathVariable float velocity,
				 @PathVariable float temperature) {
		this.producer.sendMessage(new Sensor(id,acceleration,velocity,temperature));
	        return "Sent sensor details to consumer";
	    }
	 
	/*
	 * @Bean public Supplier<Sensor> supplier() { return () -> unbounded.poll(); }
	 */

	/*@Configuration
	static class ConfluentSchemaRegistryConfiguration {
		@Bean
		public SchemaRegistryClient schemaRegistryClient(@Value("${spring.cloud.stream.kafka.binder.producer-properties.schema.registry.url}") String endpoint){
			ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
			client.setEndpoint(endpoint);
			return client;
		}
	}*/
}




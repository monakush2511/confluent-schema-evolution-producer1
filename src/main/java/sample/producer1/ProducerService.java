package sample.producer1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.example.Sensor;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog(topic = "Producer Logger")
@RequiredArgsConstructor
public class ProducerService {

	 @Value("${topic.name}")
  private String TOPIC;

  @Autowired
  private KafkaTemplate<String, Sensor> kafkaTemplate;

public void sendMessage(Sensor sense) {
	{ 
		this.kafkaTemplate.send(this.TOPIC,sense);
	  System.out.println("Produced sensor -> %s" +sense);
	  
	  }
}
}
package ms.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableJpaAuditing
@Slf4j
@SpringBootApplication
@EnableJpaRepositories(basePackages = "ms.order.service.infrastructure.persistence")
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);

		var resultado = sumar(1,2);
		log.info("resultado: {}", resultado );

		var result = restar(10,6);
		log.info("result:{}",result);
	}

	public static int sumar(int numero1, int numero2){
		var resultado = numero1 + numero2;
		return resultado;

	}

	public static int restar(int number1, int number2){
		var result = number1 - number2;
		return result;
	}






}

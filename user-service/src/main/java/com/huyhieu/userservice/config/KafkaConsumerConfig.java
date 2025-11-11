package com.huyhieu.userservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {
  public static final String DEAD_LETTER_TOPIC = "keycloak_admin_events-dlt";

  /**
   * Tạo một Factory riêng cho DLT Consumer. Factory này sẽ ghi đè global config và SỬ DỤNG
   * StringDeserializer.
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> dltListenerContainerFactory(
      KafkaProperties kafkaProperties) {

    Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    ConsumerFactory<String, String> dltConsumerFactory = new DefaultKafkaConsumerFactory<>(props);

    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(dltConsumerFactory);
    factory.setCommonErrorHandler(new DefaultErrorHandler());
    return factory;
  }

  @Bean
  public CommonErrorHandler errorHandler(KafkaOperations<String, Object> kafkaOperations) {

    // 2. Tạo Recoverer (bộ gửi DLT)
    // Dùng constructor NÀY. Nó nhận vào 2 thứ:
    // (1) Kafka template (để gửi)
    // (2) Một hàm (BiFunction) để quyết định gửi đến topic nào
    // QUAN TRỌNG: Constructor này SẼ THÊM các DLT header (như ORIGINAL_TOPIC)
    DeadLetterPublishingRecoverer recoverer =
        new DeadLetterPublishingRecoverer(
            kafkaOperations,
            (record, exception) -> {
              // (record) là message hỏng
              // (exception) là lỗi
              log.warn(
                  "Message hỏng (offset: {}) sẽ được gửi đến DLT: {}",
                  record.offset(),
                  exception.getMessage());
              // Luôn trả về topic DLT cố định của chúng ta
              return new TopicPartition(DEAD_LETTER_TOPIC, record.partition());
            });

    // 3. Cấu hình ErrorHandler (Giống hệt như cũ)
    DefaultErrorHandler errorHandler =
        new DefaultErrorHandler(
            recoverer, new FixedBackOff(500L, 3L) // 500ms, tối đa 3 lần thử
            );
    errorHandler.addNotRetryableExceptions(
        com.fasterxml.jackson.core.JsonProcessingException.class,
        org.springframework.messaging.converter.MessageConversionException.class);

    return errorHandler;
  }
}

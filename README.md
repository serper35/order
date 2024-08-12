# Логирование с использованием Spring AOP
Этот проект демонстрирует реализацию логирования с использованием Spring AOP в системе обработки заказов на основе Spring. Функциональность логирования перехватывает вызовы методов и обработку исключений, не изменяя при этом основную бизнес-логику.

## Структура проекта
- Aspect: Содержит аспект логирования, который перехватывает вызовы методов и исключения для их логирования.
- Controller: REST-контроллеры, обрабатывающие HTTP-запросы.
- DTO (Data Transfer Object): Объекты, используемые для передачи данных между слоями приложения.
- Exception: Пользовательские классы исключений, используемые в проекте.
- Mapper: Классы, отвечающие за преобразование между моделями и DTO.
- Model: Классы, представляющие бизнес-сущности.
- Repository: Интерфейсы для доступа к данным.
- Service: Классы, содержащие бизнес-логику.
- Tests: Тестовые классы, проверяющие логику сервиса и функциональность логирования.

## Реализация логирования

### Конфигурация Аспекта
Аспект логирования перехватывает вызовы методов в сервисном слое и логирует имя метода, аргументы и любые выбрасываемые исключения.
### Примеры логов
Пример логов, которые могут быть сгенерированы при вызове метода getOrder в сервисном слое:
- `2024-08-13T00:51:08.794+03:00  INFO 8888 --- [Order] [nio-8080-exec-2] ru.t1.Order.aspect.LoggingAspect         : Calling method getOrder with args [3]`
- `2024-08-13T00:51:08.901+03:00  INFO 8888 --- [Order] [nio-8080-exec-2] ru.t1.Order.aspect.LoggingAspect         : Returning from method getOrder with result OrderDTO{id=3, description='Заказ 1', status='PENDING', userId=4}`

## Начало работы
### Предварительные требования
- IntelliJ IDEA
- Java 21
- SpringBoot
- Spring Boot Test
- Spring Boot AOP
- Spring Boot Data JPA
- Spring Web
- PostgreSQL
- Log4j2
- Lombok

## Сборка и запуск приложения

### Соберите проект:
mvn clean install

### Запустите приложение:
mvn spring-boot:run

### Доступ к API:

API будет доступен по адресу http://localhost:8080.

## Тестирование
### Тесты логирования
Следующий тест проверяет, что аспект логирования правильно логирует вызов метода deleteOrder при негативном сценарии:  

`@ExtendWith(OutputCaptureExtension.class)`  
`@SpringBootTest`  
`public class OrderServiceImplTest {`  

    @Autowired
    private OrderServiceImpl orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Mapper mapper;
    void testDeleteOrderNotFoundLogging(CapturedOutput output) {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1));

        // Проверка логов
        assertThat(output).contains("Calling method deleteOrder with args")
                .contains("Exception thrown in method deleteOrder");
    }
    }
  c результатом: 
- `2024-08-13T01:04:10.465+03:00  INFO 3820 --- [Order] [           main] ru.t1.Order.aspect.LoggingAspect         : Calling method deleteOrder with args [1]`
- `2024-08-13T01:04:10.466+03:00 ERROR 3820 --- [Order] [           main] ru.t1.Order.aspect.LoggingAspect         : Exception thrown in method deleteOrder: Order not found exception`

## Заключение
Этот проект демонстрирует, как можно эффективно использовать Spring AOP для отделения логики логирования от бизнес-логики, что делает код более модульным и легким в обслуживании.
  


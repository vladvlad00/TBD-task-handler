package ro.uaic.info.spring.microservices.forex.forexservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForexController
{
    @Autowired
    private ExchangeValueRepository repository;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to)
    {
        return repository.findByFromAndTo(from, to);
    }

    @GetMapping("/")
    public String homePage()
    {
        return "Salut";
    }
}

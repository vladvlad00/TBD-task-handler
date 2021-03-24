package ro.uaic.info.spring.microservices.forex.forexservice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class ExchangeValue
{
    @Id
    private Long id;

    @Column(name="currency_from")
    private String from;

    @Column(name="currency_to")
    private String to;

    private BigDecimal conversionMultiple;

    public ExchangeValue()
    {
    }

    public ExchangeValue(Long id, String from, String to, BigDecimal conversionMultiple)
    {
        this.id = id;
        this.from = from;
        this.to = to;
        this.conversionMultiple = conversionMultiple;
    }

    public Long getId()
    {
        return id;
    }

    public String getFrom()
    {
        return from;
    }

    public String getTo()
    {
        return to;
    }

    public BigDecimal getConversionMultiple()
    {
        return conversionMultiple;
    }
}

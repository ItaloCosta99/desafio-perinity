package com.perinity.grc.application.ports.output;

import com.perinity.grc.application.domain.model.Sale;
import java.util.List;

public interface SaleRepositoryPort {
    Sale save(Sale sale);
    List<Sale> findAllSales();
}
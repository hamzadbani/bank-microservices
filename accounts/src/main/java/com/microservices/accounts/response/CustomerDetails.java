package com.microservices.accounts.response;

import com.microservices.accounts.entity.Accounts;
import com.microservices.accounts.entityDTO.Cards;
import com.microservices.accounts.entityDTO.Loans;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetails implements Serializable {
    private Accounts accounts;
    private List<Loans> loans;
    private List<Cards> cards;
}

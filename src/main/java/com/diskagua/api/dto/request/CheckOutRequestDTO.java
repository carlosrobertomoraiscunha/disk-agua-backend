package com.diskagua.api.dto.request;

import com.diskagua.api.models.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutRequestDTO {

    @NotEmpty
    @JsonProperty("pagamento")
    private PaymentMethod paymentMethod;
}

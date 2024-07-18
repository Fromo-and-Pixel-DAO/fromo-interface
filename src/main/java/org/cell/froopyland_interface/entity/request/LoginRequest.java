package org.cell.froopyland_interface.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author yozora
 * Description login request
 **/
@Data
@NoArgsConstructor
public class LoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 276662605350167601L;

    /**
     * address
     */
    @NotBlank(message = "address can't be null!")
    private String address;

    /**
     * signature
     */
    @NotBlank(message = "signature can't be null!")
    private String signature;

    /**
     * timestamp
     */
    @NotBlank(message = "timestamp can't be null!")
    private String timestamp;


    /**
     * access token ttl
     */
    private Long ttl;

    /**
     * chain ID
     */
    private String chainId;

}

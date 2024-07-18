package org.cell.froopyland_interface.support;

import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

/**
 * description custom gas provider
 * program Web3App
 *
 * @author yozora
 **/
public class CustomGasProvider extends StaticGasProvider {


    // DefaultGasProvider
    // GAS_LIMIT = BigInteger.valueOf(9_000_000);
    // GAS_PRICE = BigInteger.valueOf(4_100_000_000L));

    /**
     * description: custom gasPrice gasLimit
     *
     * @param gasPrice This is the amount you are prepared in Ether per unit of gas.
     * @param gasLimit This is the total amount of gas you are happy to spend on the transaction execution.
     *                 here is an upper limit of how large a single transaction can be in an Ethereum block which restricts this value typically to less then 6,700,000.
     * @author yozora
     */
    public CustomGasProvider(BigInteger gasPrice, BigInteger gasLimit) {
        super(gasPrice, gasLimit);
    }

}

package org.cell.froopyland_interface.support;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.cell.froopyland_interface.contract.BidFroopyLand;
import org.cell.froopyland_interface.entity.constants.BlockchainConst;
import org.cell.froopyland_interface.utils.Web3Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yozora
 * description FroopyLandProvider
 * @version 1.0
 */
@Log4j2
@Configuration
public class FroopyLandProvider {

    @Value("${fromo.key}")
    private String key;

    @Value("${eth.sepolia.rpc}")
    private String sepoliaRpc;

    public BidFroopyLand bidFroopyLand;

    @PostConstruct
    public void init() {
        try {
            Web3j web3j = Web3Utils.initWebClient(sepoliaRpc);
            bidFroopyLand = BidFroopyLand.load(BlockchainConst.BID_CONTRACT_ADDRESS, web3j, Credentials.create(key), new CustomGasProvider(BigInteger.valueOf(1_000_000_000), BigInteger.valueOf(500_000)));
            log.info("fromo: {}", bidFroopyLand.getContractAddress());
        } catch (Exception e) {
            log.error("init game info error", e);
        }
    }

    @Bean(name = "defaultFroopyLand")
    public BidFroopyLand getFroopyLand() {
        return bidFroopyLand;
    }
}

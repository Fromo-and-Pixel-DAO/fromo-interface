package org.cell.froopyland_interface.utils;

import lombok.extern.log4j.Log4j2;
import org.bouncycastle.util.encoders.Hex;
import org.cell.froopyland_interface.entity.constants.BlockchainConst;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author yozora
 * @description
 **/
@Log4j2
public class Web3Utils {

    private Web3Utils() {
        throw new IllegalStateException("Utility class.");
    }

    private static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    public static BigInteger getTokenAmount(String endPoint, String contractAddress, String userAddress) throws IOException {
        Web3j web3j = Web3j.build(new HttpService(endPoint));
        Function function = new Function("balanceOf", Collections.singletonList(new Address(userAddress)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        String data = FunctionEncoder.encode(function);
        EthCall ethCall = web3j.ethCall(Transaction.createFunctionCallTransaction(userAddress, null, null, null, contractAddress, data), DefaultBlockParameterName.LATEST).send();

        if (ethCall.hasError()) {
            throw new RuntimeException("Error: " + ethCall.getError().getMessage());
        } else {
            List<Type> types = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            return ((Uint256) types.get(0)).getValue();
        }
    }

    /**
     * description: get event filter
     *
     * @param web3j           web3j instance
     * @param fromBlock       fromBlock
     * @param contractAddress contractAddress
     * @param params          params
     * @return java.math.BigInteger
     * @author yozora
     **/
    private static BigInteger getFilterIdByEthWithFilter(Web3j web3j, BigInteger fromBlock, String contractAddress, String... params) throws IOException {
        DefaultBlockParameter blockParameter = DefaultBlockParameter.valueOf(fromBlock);
        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(blockParameter, DefaultBlockParameterName.LATEST, contractAddress);
        // 事件字段
        List<TypeReference<?>> parameters = Arrays.asList(new TypeReference<Uint256>() {
        }, new TypeReference<Address>() {
        });
        Event event = new Event(params[0], parameters);
        if (params.length > 1) {
            filter.addSingleTopic(EventEncoder.encode(event));
            filter.addNullTopic();
            filter.addSingleTopic("0x" + TypeEncoder.encode(new Address((params[2]))));
        } else {
            filter.addSingleTopic(EventEncoder.encode(event));
        }
        Request<?, EthFilter> ethFilterRequest = web3j.ethNewFilter(filter);
        return ethFilterRequest.send().getFilterId();
    }

    /**
     * description: generate params keccak256
     *
     * @param param1  param1
     * @param address address
     * @param param2  param2
     * @return java.lang.String
     * @author yozora
     **/
    public static String generateSha3Code(Uint256 param1, Address address, Uint256 param2) {
        List<Type> inputParameters = Arrays.asList(param1, address, param2);
        return Hash.sha3(FunctionEncoder.encodeConstructor(inputParameters));
    }

    /**
     * description: generate params keccak256
     *
     * @param chainName chainName
     * @param chainId   chainId
     * @param address   address
     * @param tokenId   tokenId
     * @return java.lang.String
     * @author yozora
     **/
    public static String generateSha3Code(Utf8String chainName, Uint256 chainId, Address address, Uint256 tokenId) {
        List<Type> inputParameters = Arrays.asList(chainName, chainId, address, tokenId);
        return Hash.sha3(FunctionEncoder.encodeConstructor(inputParameters));
    }

    /**
     * description: verify signature
     *
     * @param templateOrigin sign message template
     * @param userAddress    userAddress
     * @param signature      signature
     * @return java.lang.String
     * @author yozora
     **/
    public static String verifyMessage(String templateOrigin, String userAddress, String signature) {

        String[] resultStr = templateOrigin.split("\\\\n");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < resultStr.length; i++) {
            if (i == resultStr.length - 1) {
                result.append(resultStr[i]);
            } else {
                result.append(resultStr[i]).append("\n");
            }
        }
        String template = result.toString();
        log.info("sign template:{}", template);
        String message = template.replace("{address}", userAddress);
        return Web3Utils.recoverAddressFromSignature(signature, userAddress, message);
    }

    private static String recoverAddressFromSignature(String signature, String address, String message) {

        String prefix = Web3Utils.MESSAGE_PREFIX + message.length();
        byte[] msgHash = Hash.sha3((prefix + message).getBytes());

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        Sign.SignatureData sd =
                new Sign.SignatureData(
                        v,
                        Arrays.copyOfRange(signatureBytes, 0, 32),
                        Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered = "";
        boolean match = false;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey =
                    Sign.recoverFromSignature(
                            (byte) i,
                            new ECDSASignature(
                                    new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                            msgHash);

            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress(publicKey);
                if (addressRecovered.equals(address)) {
                    break;
                }
            }
        }
        return addressRecovered;
    }

    /**
     * description: get signature data
     *
     * @param signature signature
     * @return org.web3j.crypto.Sign.SignatureData
     * @author yozora
     **/
    private static Sign.SignatureData getSignatureData(String signature) {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        return new Sign.SignatureData(
                v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));
    }

    /**
     * 功能描述: init client
     *
     * @return org.web3j.protocol.Web3j
     * @author yozora
     */
    public static Web3j initWebClient(String endPoint) {
        return Web3j.build(new HttpService(endPoint));
    }

}

package org.cell.froopyland_interface.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.cell.froopyland_interface.contract.BidFroopyLand;
import org.cell.froopyland_interface.dao.NonFungibleTokenDao;
import org.cell.froopyland_interface.entity.constants.BlockchainConst;
import org.cell.froopyland_interface.entity.constants.GameConstant;
import org.cell.froopyland_interface.entity.interfaces.OpenSeaNft;
import org.cell.froopyland_interface.entity.nonFungibleToken.LatestNftCodeInfoPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenCodePojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenDesPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.dto.AmountDto;
import org.cell.froopyland_interface.entity.nonFungibleToken.dto.SettleBidDto;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.*;
import org.cell.froopyland_interface.entity.request.BaseNonFungibleTokenRequest;
import org.cell.froopyland_interface.entity.request.DesNonFungibleTokenRequest;
import org.cell.froopyland_interface.entity.request.UserNftRequest;
import org.cell.froopyland_interface.entity.request.UserNonFungibleTokenRequest;
import org.cell.froopyland_interface.entity.response.*;
import org.cell.froopyland_interface.entity.user.dto.UserJoinLogDto;
import org.cell.froopyland_interface.entity.user.vo.MyHistoricalDividendsVo;
import org.cell.froopyland_interface.entity.user.vo.MyProfitVo;
import org.cell.froopyland_interface.service.NonFungibleTokenService;
import org.cell.froopyland_interface.service.UserService;
import org.cell.froopyland_interface.utils.OkHttpUtil;
import org.cell.froopyland_interface.utils.PageUtils;
import org.cell.froopyland_interface.utils.Web3Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yozora
 * @description NonFungibleTokenServiceImpl
 **/
@Log4j2
@Service
public class NonFungibleTokenServiceImpl implements NonFungibleTokenService {

    @Value("${fromo.omo.price}")
    private String omoPrice;

    @Value("${fromo.eth.price}")
    private String ethPrice;

    @Value("${eth.sepolia.rpc}")
    private String sepoliaRpc;

    @Autowired
    private NonFungibleTokenDao nonFungibleTokenDao;

    @Autowired
    private UserService userService;

    @Resource(name = "defaultFroopyLand")
    private BidFroopyLand bidFroopyLand;

    private final long CYCLELENGTH = 900;

    private final long TIMERATIOBASEDONCYCLE = 600;

    private final long BIDSTARTPOINT = 1719298800;

    @Override
    public NonFungibleTokenCodePojo getNonFungibleTokenCodeInfo(BaseNonFungibleTokenRequest baseNonFungibleTokenRequest) {
        return nonFungibleTokenDao.getNonFungibleTokenCodeInfo(baseNonFungibleTokenRequest);
    }

    @Override
    public List<NonFungibleTokenPojo> getUserNonFungibleTokenList(UserNonFungibleTokenRequest userNonFungibleTokenRequest) {
        processKeyWord(userNonFungibleTokenRequest);
        int total = nonFungibleTokenDao.getUserNonFungibleTokenCount(userNonFungibleTokenRequest);
        if (userNonFungibleTokenRequest.hasData(total)) {
            return nonFungibleTokenDao.getUserNonFungibleTokenList(userNonFungibleTokenRequest);
        }
        return new ArrayList<>();
    }

    @Override
    public List<NonFungibleTokenPojo> getLatestNonFungibleTokenList(UserNonFungibleTokenRequest userNonFungibleTokenRequest) {
        if (userNonFungibleTokenRequest.getTimeType() == 0) {
            userNonFungibleTokenRequest.setStartTime(DateUtils.addDays(new Date(), -30));
        }
        if (userNonFungibleTokenRequest.getTimeType() == 1) {
            userNonFungibleTokenRequest.setStartTime(DateUtils.addDays(new Date(), -7));
        }
        if (userNonFungibleTokenRequest.getTimeType() == 2) {
            userNonFungibleTokenRequest.setStartTime(DateUtils.addDays(new Date(), -3));
        }
        ArrayList<LatestNftCodeInfoPojo> nftCodes = nonFungibleTokenDao.getLatestLoginCode(userNonFungibleTokenRequest);
        List<String> nftCodeList = new LinkedList<>();
        nftCodes.forEach(nftCode -> nftCodeList.add(nftCode.getNonFungibleTokenCode()));
        //        latestNonFungibleTokenList.forEach(latestNonFungibleToken -> {
//            latestNonFungibleToken.setLoginCount(nftCodes.get(0).getLoginCount());
//        });
        return nonFungibleTokenDao.getLatestNonFungibleTokenList(nftCodeList);
    }

    @Override
    public NonFungibleTokenPojo getUserNonFungibleTokenInfo(BaseNonFungibleTokenRequest baseNonFungibleTokenRequest) {
        return nonFungibleTokenDao.getUserNonFungibleTokenInfo(baseNonFungibleTokenRequest);
    }

    @Override
    public ArrayList<DesNonFungibleTokenVo> getAllNonFungibleTokenDescription() {
        return nonFungibleTokenDao.getAllNonFungibleTokenDescription();
    }

    @Override
    public DesNonFungibleTokenVo getNonFungibleTokenDescription(String nftAddress) {
        return nonFungibleTokenDao.getNonFungibleTokenDescription(nftAddress);
    }

    @Override
    public List<DesNonFungibleTokenVo> getUserNonFungibleTokenDescription(DesNonFungibleTokenRequest desNonFungibleTokenRequest) {
        return nonFungibleTokenDao.getUserNonFungibleTokenDescription(desNonFungibleTokenRequest);
    }

    @Override
    public AuctionInfoVo getAuctionInfo() throws Exception {

        AuctionInfoVo auctionInfoVo = new AuctionInfoVo();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        BigInteger n = BigInteger.ZERO;
        try {
            n = bidFroopyLand.getTimeBasedCurrentBidId().send();
        } catch (Exception e) {
            log.error("getAuctionInfo error", e);
        }
        Long maxBidId = n.longValue();
        log.info("maxBidId: {}", maxBidId);
        auctionInfoVo.setBidId(maxBidId);
        SettleBidDto settleBidDto = nonFungibleTokenDao.getSettleBid(maxBidId);
        List<BidVo> bidderForm = nonFungibleTokenDao.getBidderForm(maxBidId);
        if (!bidderForm.isEmpty()) {
            auctionInfoVo.setHighestBid(Convert.fromWei(bidderForm.get(0).getAmount(), Convert.Unit.ETHER).toString());
            auctionInfoVo.setBiddersCount(bidderForm.size());
        } else {
            auctionInfoVo.setHighestBid("0");
            auctionInfoVo.setBiddersCount(0);
        }
        if (new Date(BIDSTARTPOINT * 1000).getTime() > System.currentTimeMillis()) {
            auctionInfoVo.setStatus(GameConstant.UPCOMING);
            auctionInfoVo.setStartTimestamp(BIDSTARTPOINT * 1000);
        } else {
            long currentTimeMillis = System.currentTimeMillis() / 1000;
            long dTime = currentTimeMillis - BIDSTARTPOINT;
            long remindTime = dTime % CYCLELENGTH;
            if (remindTime > TIMERATIOBASEDONCYCLE) {
                auctionInfoVo.setStatus(GameConstant.STAKING);
                auctionInfoVo.setStartTimestamp((BIDSTARTPOINT + n.longValue() * CYCLELENGTH) * 1000);
                auctionInfoVo.setBidWinnerAddress(!bidderForm.isEmpty() ? bidderForm.get(0).getUserAddress() : "");
            } else {
                auctionInfoVo.setStatus(GameConstant.BIDING);
                auctionInfoVo.setStartTimestamp((BIDSTARTPOINT + n.longValue() * CYCLELENGTH + TIMERATIOBASEDONCYCLE) * 1000);
            }
        }


//        // 如果在晚上8点到中午12点之间，则状态为进行中
//        calendar.set(Calendar.HOUR_OF_DAY, 20);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        if (hour >= 20 || hour < 12) {
//            // 当前时间在0点前,则为今天晚上8点,当前时间在0点后,则为昨天晚上8点
//            auctionInfoVo.setStartTimestamp(hour >= 20 ? calendar.getTime().getTime() : DateUtils.addDays(calendar.getTime(), -1).getTime());
//            auctionInfoVo.setStatus(GameConstant.BIDING);
//        } else {
//            // 如果在中午12点到晚上8点之间，则状态为待玩家质押/即将开始
//            calendar.set(Calendar.HOUR_OF_DAY, 12);
//            auctionInfoVo.setStartTimestamp(calendar.getTime().getTime());
//            auctionInfoVo.setStatus(GameConstant.STAKING);
//            auctionInfoVo.setBidWinnerAddress(!bidderForm.isEmpty() ? bidderForm.get(0).getUserAddress() : "");
//        }
        // 如果存在结算信息，则状态为已结束
        if (settleBidDto != null) {
            auctionInfoVo.setStatus(GameConstant.NOT_STARTED);
            auctionInfoVo.setBidWinnerAddress(settleBidDto.getUserAddress());
        }

        return auctionInfoVo;
    }

    @Override
    public GameNftAmountVo getGameDetail(String userAddress, Long gameId) {
        GameNftAmountVo gameNftAmountVo = new GameNftAmountVo();
        List<AmountDto> bonusAmountList = userService.getClaimKeyBonus(userAddress, gameId);
        BigDecimal bonusAmount = BigDecimal.ZERO;
        for (AmountDto amountDto : bonusAmountList) {
            bonusAmount = bonusAmount.add(amountDto.getAmount());
        }
        String string = Convert.fromWei(bonusAmount, Convert.Unit.ETHER).multiply(new BigDecimal(ethPrice)).toString();
        gameNftAmountVo.setKeyDividends(string);

        NonFungibleTokenPojo nftAuctionsByGameId = nonFungibleTokenDao.getNftAuctionsByGameId(gameId);
        if (!nftAuctionsByGameId.getStatus().equals(GameConstant.UPCOMING)) {
            List<String> players = nonFungibleTokenDao.getGamePlayers(gameId);
            gameNftAmountVo.setBiddersCount(players.isEmpty() ? 0 : players.size());
        }
        gameNftAmountVo.setImageUrl(nftAuctionsByGameId.getImageUrl());
        return gameNftAmountVo;
    }


    @Override
    public NftListVo getNftAuctions(Integer status, Integer pageNum) {
        int total = nonFungibleTokenDao.getNftAuctionsCount(status);
        List<NonFungibleTokenVo> nftList = new ArrayList<>();
        int pageSize = Math.min(total, 20);
        if (PageUtils.hasNext(total, pageNum, pageSize)) {
            int startIndex = (pageNum - 1) * 20;
            int endIndex = pageNum * 20;
            List<NonFungibleTokenPojo> nonFungibleTokenPojoList = new ArrayList<>();
            if (status == null) {
                List<NonFungibleTokenPojo> nftAuctions = nonFungibleTokenDao.getNftAuctions(GameConstant.UPCOMING, startIndex, endIndex);
                List<NonFungibleTokenPojo> nftBiding = nonFungibleTokenDao.getNftAuctions(GameConstant.ONGOING, startIndex, endIndex);
                List<NonFungibleTokenPojo> nftFinished = nonFungibleTokenDao.getNftAuctions(GameConstant.FINISHED, startIndex, endIndex);

                nonFungibleTokenPojoList.addAll(nftAuctions);
                nonFungibleTokenPojoList.addAll(nftBiding);
                nonFungibleTokenPojoList.addAll(nftFinished);
            } else {
                nonFungibleTokenPojoList = nonFungibleTokenDao.getNftAuctions(status, startIndex, endIndex);
            }
            if (!nonFungibleTokenPojoList.isEmpty()) {
                for (NonFungibleTokenPojo nonFungibleTokenPojo : nonFungibleTokenPojoList) {
                    NonFungibleTokenVo nonFungibleTokenVo = new NonFungibleTokenVo();
                    BeanUtils.copyProperties(nonFungibleTokenPojo, nonFungibleTokenVo);

                    if (nonFungibleTokenPojo.getStartTime() != null) {
                        nonFungibleTokenVo.setStartTimestamp(nonFungibleTokenPojo.getStartTime().getTime());
                    }
                    if (nonFungibleTokenPojo.getEndTime() != null) {
                        nonFungibleTokenVo.setEndTimestamp(nonFungibleTokenPojo.getEndTime().getTime());
                    }
//                    if (StringUtils.isNoneBlank(nonFungibleTokenPojo.getKeyPrice())) {
//                        nonFungibleTokenVo.setFinalPrice(nonFungibleTokenPojo.getKeyPrice());
//                    }
                    if (StringUtils.isNoneBlank(nonFungibleTokenPojo.getSalesAmount())) {
                        nonFungibleTokenVo.setTotalMintFee(Convert.fromWei(nonFungibleTokenPojo.getSalesAmount(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                        nonFungibleTokenVo.setFinalPrice((new BigDecimal(nonFungibleTokenPojo.getSalesAmount()).multiply(new BigDecimal("2").divide(new BigDecimal("10"))).setScale(0, RoundingMode.HALF_UP)).toString());
                    }

                    nonFungibleTokenVo.setBiddersCount(0);
                    if (!nonFungibleTokenPojo.getStatus().equals(GameConstant.UPCOMING)) {
                        List<String> players = nonFungibleTokenDao.getGamePlayers(nonFungibleTokenPojo.getGameId());
//                        List<BidVo> bidderForm = nonFungibleTokenDao.getBidderForm(nonFungibleTokenPojo.getGameId());
                        nonFungibleTokenVo.setBiddersCount(players.isEmpty() ? 0 : players.size());
                    }

                    // 更新游戏状态
                    if (nonFungibleTokenVo.getStatus().equals(GameConstant.UPCOMING) && nonFungibleTokenPojo.getStartTime() != null && nonFungibleTokenPojo.getStartTime().getTime() < System.currentTimeMillis()) {
                        nonFungibleTokenDao.updateGameStatus(nonFungibleTokenVo.getGameId(), GameConstant.ONGOING);
                        nonFungibleTokenVo.setStatus(GameConstant.ONGOING);
                    } else if (nonFungibleTokenVo.getEndTime() != null && nonFungibleTokenVo.getEndTime().getTime() < System.currentTimeMillis() && !nonFungibleTokenVo.getStatus().equals(GameConstant.FINISHED)) {
                        nonFungibleTokenDao.updateGameStatus(nonFungibleTokenVo.getGameId(), GameConstant.FINISHED);
                        nonFungibleTokenVo.setStatus(GameConstant.FINISHED);
                    }

                    nftList.add(nonFungibleTokenVo);
                }
            }
        }
        return new NftListVo(total, nftList);
    }

    @Override
    public NftListVo getNftPoolList(int status, Integer pageNum) {
        int startIndex = (pageNum - 1) * 200;
        int endIndex = pageNum * 200;
        List<NonFungibleTokenVo> nftList = new ArrayList<>();
        List<NonFungibleTokenPojo> nonFungibleTokenPojoList = nonFungibleTokenDao.getNftAuctions(status, startIndex, endIndex);
        List<GameNftVo> nftRetrievedList = userService.getNftRetrievedList(null, startIndex, endIndex);

        if (!nonFungibleTokenPojoList.isEmpty()) {
            if (!nftRetrievedList.isEmpty()) {
                Iterator<NonFungibleTokenPojo> iterator = nonFungibleTokenPojoList.iterator();
                while (iterator.hasNext()) {
                    NonFungibleTokenPojo nonFungibleTokenPojo = iterator.next();
                    for (GameNftVo gameNftVo : nftRetrievedList) {
                        if (nonFungibleTokenPojo.getGameId().equals(gameNftVo.getGameId())) {
                            iterator.remove();
                        }
                    }
                }
            }
        }

        if (!nonFungibleTokenPojoList.isEmpty()) {
            for (NonFungibleTokenPojo nonFungibleTokenPojo : nonFungibleTokenPojoList) {
                NonFungibleTokenVo nonFungibleTokenVo = new NonFungibleTokenVo();
                BeanUtils.copyProperties(nonFungibleTokenPojo, nonFungibleTokenVo);

                if (nonFungibleTokenPojo.getStartTime() != null) {
                    nonFungibleTokenVo.setStartTimestamp(nonFungibleTokenPojo.getStartTime().getTime());
                }
                if (nonFungibleTokenPojo.getEndTime() != null) {
                    nonFungibleTokenVo.setEndTimestamp(nonFungibleTokenPojo.getEndTime().getTime());
                }
//                if (StringUtils.isNoneBlank(nonFungibleTokenPojo.getKeyPrice())) {
//                    nonFungibleTokenVo.setFinalPrice(Convert.fromWei(nonFungibleTokenPojo.getKeyPrice(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
//                }
                if (StringUtils.isNoneBlank(nonFungibleTokenPojo.getSalesAmount())) {
                    nonFungibleTokenVo.setTotalMintFee(Convert.fromWei(nonFungibleTokenPojo.getSalesAmount(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                    nonFungibleTokenVo.setFinalPrice(Convert.fromWei((new BigDecimal(nonFungibleTokenPojo.getSalesAmount()).multiply(new BigDecimal("2")).divide(new BigDecimal("10")).setScale(0, RoundingMode.HALF_UP)), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                }

                nonFungibleTokenVo.setBiddersCount(0);
                List<String> players = nonFungibleTokenDao.getGamePlayers(nonFungibleTokenPojo.getGameId());
                nonFungibleTokenVo.setBiddersCount(players.isEmpty() ? 0 : players.size());
                nftList.add(nonFungibleTokenVo);
            }
        }
        return new NftListVo(nonFungibleTokenPojoList.size(), nftList);
    }

    @Override
    public Integer getStakeNotices(String userAddress) {
        BigInteger n = BigInteger.ZERO;
        try {
            n = bidFroopyLand.getTimeBasedCurrentBidId().send();
        } catch (Exception e) {
            log.error("getAuctionInfo error", e);
        }
        long maxBidId = n.longValue();
        long lastBidId = maxBidId - 1L;
        List<BidVo> bidderForm = nonFungibleTokenDao.getBidderForm(lastBidId);
        if (!bidderForm.isEmpty() && bidderForm.get(0).getUserAddress().equals(userAddress)) {
            SettleBidDto settleBid = nonFungibleTokenDao.getSettleBid(lastBidId);
            if (settleBid != null && settleBid.getUserAddress().equals(userAddress)) {
                return 0;
            } else {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public List<NonFungibleTokenPojo> getJoinLog(String startBlock, String endBlock) throws IOException {
        EthFilter ethFilter = new EthFilter(DefaultBlockParameter.valueOf(new BigInteger(startBlock)), DefaultBlockParameter.valueOf(new BigInteger(endBlock)), BlockchainConst.BID_CONTRACT_ADDRESS);
        ethFilter.addSingleTopic("0x48bba6a72d4a9fae48e2469ce99518c38afec785fa4f3df38a1a76b03a979537");
        Web3j web3j = Web3Utils.initWebClient(sepoliaRpc);
        List<EthLog.LogResult> logs = web3j.ethGetLogs(ethFilter).send().getLogs();
        if (logs != null && !logs.isEmpty()) {
            for (EthLog.LogResult logResult : logs) {
                if (logResult != null) {
                    BidFroopyLand.GameJoinedEventResponse gameJoinedEventFromLog = BidFroopyLand.getGameJoinedEventFromLog((Log) logResult);
                    String userAddress = gameJoinedEventFromLog.Player;
                    BigInteger gameId = gameJoinedEventFromLog.GameId;
                    BigInteger keyAmount = gameJoinedEventFromLog._keyAmount;
                    String transactionHash = ((Log) logResult).getTransactionHash();

                    log.info("game joined userAddress:{} gameId:{} keyAmount:{}", userAddress, gameId, keyAmount);
                    String address = nonFungibleTokenDao.getUserJoinLog(userAddress, gameId.longValue(), transactionHash);
                    if (StringUtils.isBlank(address)) {
                        UserJoinLogDto userJoinLogDto = new UserJoinLogDto(gameId.longValue(), userAddress, keyAmount, transactionHash);
                        nonFungibleTokenDao.addUserJoinLog(userJoinLogDto);
                        log.info("add game joined");
                    }
                }
            }
        }
        return null;
    }

    @Override
    public UserNftListVo getUserNftList(String userAddress, String next, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageSize > 200) {
            pageSize = 200;
        }
        List<NonFungibleTokenVo> nftList = new ArrayList<>();

//        String userAddress = JWTUtils.getUserAddress();
        String res = OkHttpUtil.getOpenSeaNonFungibleTokenByUserAddress(userAddress, next, pageSize, BlockchainConst.SEPOLIA_NAME);
        // 解析数据入库
        JSONObject jsonObject = JSON.parseObject(res);
        if (jsonObject == null) {
            UserNftListVo nftListResponse = new UserNftListVo();
            nftListResponse.setNftList(nftList);
            return nftListResponse;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("nfts");
        if (jsonArray == null || jsonArray.isEmpty()) {
            UserNftListVo nftListResponse = new UserNftListVo();
            nftListResponse.setNftList(nftList);
            return nftListResponse;
        }
        next = jsonObject.getString("next");
        List<OpenSeaNft> assets = jsonArray.toList(OpenSeaNft.class);
        if (!assets.isEmpty()) {
            for (OpenSeaNft asset : assets) {
                NonFungibleTokenVo nonFungibleTokenVo = new NonFungibleTokenVo();
                nonFungibleTokenVo.setChainName(BlockchainConst.SEPOLIA_NAME);
                nonFungibleTokenVo.setNftAddress(asset.getContract());
                nonFungibleTokenVo.setUserAddress(userAddress);
                nonFungibleTokenVo.setImageUrl(asset.getImageUrl());
                nonFungibleTokenVo.setTokenId(asset.getIdentifier());
                nonFungibleTokenVo.setName(asset.getName());
                if (StringUtils.isNoneBlank(asset.getMetadataUrl()) && asset.getMetadataUrl().length() > 500) {
                    nonFungibleTokenVo.setTokenMetadataUrl("metadata url too long");
                } else {
                    nonFungibleTokenVo.setTokenMetadataUrl(asset.getMetadataUrl());
                }
                nonFungibleTokenVo.setImageUrl(asset.getImageUrl());
                nonFungibleTokenVo.setOpenSeaUrl(asset.getOpenseaUrl());

                // 查询是否已经在进行中
                List<NonFungibleTokenPojo> nonFungibleTokenPojo = nonFungibleTokenDao.getNftAuctionsByNftAddressAndTokenId(nonFungibleTokenVo.getNftAddress(), nonFungibleTokenVo.getTokenId());
                int auctionsCount = 0;
                if (!nonFungibleTokenPojo.isEmpty()) {
                    for (NonFungibleTokenPojo fungibleTokenPojo : nonFungibleTokenPojo) {
                        if (!fungibleTokenPojo.getStatus().equals(GameConstant.FINISHED) && fungibleTokenPojo.getEndTime() != null && fungibleTokenPojo.getEndTime().getTime() < System.currentTimeMillis()) {
                            nonFungibleTokenDao.updateGameStatus(fungibleTokenPojo.getGameId(), GameConstant.FINISHED);
                        } else {
                            if (fungibleTokenPojo.getStatus().equals(GameConstant.ONGOING)) {
                                nonFungibleTokenVo.setStatus(GameConstant.ONGOING);
                                break;
                            }
                            if (fungibleTokenPojo.getStatus().equals(GameConstant.FINISHED)) {
                                auctionsCount++;
                            }
                        }
                    }
                    nonFungibleTokenVo.setAuctionsCount(auctionsCount);
                }
                nftList.add(nonFungibleTokenVo);
            }
        }
        return new UserNftListVo(next, nftList);
    }

    @Override
    public Long getMaxGameId() {
        return nonFungibleTokenDao.getMaxGameId();
    }

    @Override
    public GameBriefVo getGameBrief() {
        GameBriefVo gameBriefVo = new GameBriefVo();
        try {
            Long maxGameId = getMaxGameId();
            if (maxGameId == null) {
                gameBriefVo.setTotalGames(0L);
            } else {
                gameBriefVo.setTotalGames(maxGameId + 1);
            }
            int total = nonFungibleTokenDao.getNftAuctionsCount(null);
            List<NonFungibleTokenPojo> nftAuctions = nonFungibleTokenDao.getNftAuctions(null, 0, total);

            int totalKey = 0;
            BigDecimal totalMintFee = BigDecimal.ZERO;
            for (NonFungibleTokenPojo nftAuction : nftAuctions) {
                if (nftAuction.getEndTime() != null && nftAuction.getEndTime().getTime() < System.currentTimeMillis() && !nftAuction.getStatus().equals(GameConstant.FINISHED)) {
                    nonFungibleTokenDao.updateGameStatus(nftAuction.getGameId(), GameConstant.FINISHED);
                    nftAuction.setStatus(GameConstant.FINISHED);
                }
                if (nftAuction.getStatus().equals(GameConstant.FINISHED)) {
                    totalKey = totalKey + Integer.parseInt(nftAuction.getTotalKeyMinted());
                    totalMintFee = totalMintFee.add(Convert.fromWei(nftAuction.getKeyPrice(), Convert.Unit.ETHER).multiply(new BigDecimal(nftAuction.getTotalKeyMinted())));
                }
            }

            List<AmountDto> bonusAmountList = userService.getClaimKeyBonus(null, null);
            List<AmountDto> prizeAmountList = userService.getWithdrawLastPlayerPrize(null);
            List<AmountDto> saleAmountList = userService.getWithdrawSaleRevenue(null);
            BigDecimal bonusAmount = BigDecimal.ZERO;
            for (AmountDto amountDto : bonusAmountList) {
                bonusAmount = bonusAmount.add(amountDto.getAmount());
            }
            BigDecimal prizeAmount = BigDecimal.ZERO;
            for (AmountDto amountDto : prizeAmountList) {
//                prizeAmount = prizeAmount.multiply(new BigDecimal("1.5")).add(amountDto.getAmount());
                prizeAmount = prizeAmount.add(amountDto.getAmount());
            }
            prizeAmount = prizeAmount.multiply(new BigDecimal("1.5"));
            BigDecimal saleAmount = BigDecimal.ZERO;
            for (AmountDto amountDto : saleAmountList) {
                saleAmount = saleAmount.add(amountDto.getAmount());
            }
            gameBriefVo.setTokenPrice(omoPrice);
            gameBriefVo.setTotalKeyMinted(Integer.toString(totalKey));
            gameBriefVo.setTotalMintFee(totalMintFee.setScale(4, RoundingMode.HALF_UP).toString());
            gameBriefVo.setTotalProfits(bonusAmount.add(prizeAmount).add(saleAmount).setScale(0, RoundingMode.HALF_UP).toString());
            gameBriefVo.setTotalPrize(Convert.fromWei(gameBriefVo.getTotalProfits(), Convert.Unit.ETHER).multiply(new BigDecimal(omoPrice)).setScale(4, RoundingMode.HALF_UP).toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return gameBriefVo;
    }

    @Override
    public List<BidVo> getBidderForm(Long bidId) throws Exception {

        BigInteger n = BigInteger.ZERO;
        try {
            n = bidFroopyLand.getTimeBasedCurrentBidId().send();
        } catch (Exception e) {
            log.error("getBidderForm error", e);
        }
        long maxBidId = n.longValue();
        if (bidId == null) {
            bidId = maxBidId;
        }
        List<BidVo> bidderForm = nonFungibleTokenDao.getBidderForm(bidId);
        for (BidVo bidVo : bidderForm) {
            bidVo.setAmount(Convert.fromWei(bidVo.getAmount(), Convert.Unit.ETHER).toString());
        }
        return bidderForm;
    }

    @Override
    public List<DesNonFungibleTokenVo> getNonFungibleTokenItems(UserNonFungibleTokenRequest userNonFungibleTokenRequest) {
        return nonFungibleTokenDao.getNonFungibleTokenItems(userNonFungibleTokenRequest);
    }

    @Override
    public NonFungibleTokenCodePojo getNonFungibleTokenCode(String nftCode) {
        return nonFungibleTokenDao.getNonFungibleTokenCode(nftCode);
    }

    @Override
    public NonFungibleTokenPojo getNonFungibleTokenInfo(String contractAddress, String tokenId, String chainName) {
        return nonFungibleTokenDao.getNonFungibleTokenInfo(contractAddress, tokenId, chainName);
    }

    @Override
    public List<NonFungibleTokenDesPojo> getNonNonFungibleTokenDescription(DesNonFungibleTokenRequest desNonFungibleTokenRequest) {
        if (desNonFungibleTokenRequest.hasData(nonFungibleTokenDao.getNonFungibleTokenDescriptionCount(desNonFungibleTokenRequest))) {
            return nonFungibleTokenDao.getNonNonFungibleTokenDescription(desNonFungibleTokenRequest);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserNonFungibleTokenInfo(BaseNonFungibleTokenRequest baseNonFungibleTokenRequest) {
        nonFungibleTokenDao.updateUserNonFungibleTokenInfo(baseNonFungibleTokenRequest);
    }

    @Override
    public int getDescriptionCount(String chainId) {
        return nonFungibleTokenDao.getNonFungibleTokenDescriptionCount(null);
    }

    @Override
    public MyProfitVo getMyProfit(String userAddress) {

        MyProfitVo myProfitVo = new MyProfitVo();

        List<AmountDto> bonusAmountList = userService.getClaimKeyBonus(userAddress, null);
        List<AmountDto> prizeAmountList = userService.getWithdrawLastPlayerPrize(userAddress);
        List<AmountDto> saleAmountList = userService.getWithdrawSaleRevenue(userAddress);

        List<Long> saleIds = saleAmountList.stream().map(AmountDto::getGameId).distinct().collect(Collectors.toList());

        try {
            Tuple4<BigInteger, BigInteger, BigInteger, BigInteger> send = bidFroopyLand.getBidderInfoOf(userAddress).send();
            myProfitVo.setFlTokens(send.component2().toString());
            myProfitVo.setLockedFlTokens(send.component2().subtract(send.component4()).toString());
            myProfitVo.setWithdrawalAmountTokens(send.component4().toString());
        } catch (Exception e) {
            log.error("getMyProfit error", e);
            myProfitVo.setFlTokens("0");
            myProfitVo.setWithdrawalAmountTokens("0");
        }
        List<NonFungibleTokenPojo> userJoinedGameIds = userService.getUserJoinedGameIds(userAddress);
        List<NonFungibleTokenPojo> userJoinedGameInfos = userService.getUserJoinedGameInfo(userAddress);
        userJoinedGameIds.stream().filter(userJoinedGameId -> userJoinedGameId.getCreator().equals(userAddress.toLowerCase())).map(NonFungibleTokenPojo::getGameId).collect(Collectors.toSet());

        List<AmountDto> keysConvertedList = userService.selectUserKeysConvertedLog(userAddress);

        myProfitVo.setFlPrice(omoPrice);
        BigDecimal joinedKeys = BigDecimal.ZERO;
        BigDecimal lockedKeys = BigDecimal.ZERO;
        Set<Long> unconvertedGameIds = new HashSet<>();
        List<Long> convertedGameIds = new ArrayList<>();
        for (NonFungibleTokenPojo userJoinedGameInfo : userJoinedGameInfos) {
            if (StringUtils.isNoneBlank(userJoinedGameInfo.getJoinedAmount())) {
                joinedKeys = joinedKeys.add(new BigDecimal(userJoinedGameInfo.getJoinedAmount()));
                if (userJoinedGameInfo.getEndTime() != null && userJoinedGameInfo.getEndTime().getTime() < System.currentTimeMillis() && !userJoinedGameInfo.getStatus().equals(GameConstant.FINISHED)) {
                    nonFungibleTokenDao.updateGameStatus(userJoinedGameInfo.getGameId(), GameConstant.FINISHED);
                    userJoinedGameInfo.setStatus(GameConstant.FINISHED);
                }
                if (!userJoinedGameInfo.getStatus().equals(GameConstant.FINISHED)) {
                    lockedKeys = lockedKeys.add(new BigDecimal(userJoinedGameInfo.getJoinedAmount()));
                } else {
                    unconvertedGameIds.add(userJoinedGameInfo.getGameId());
                }
            }
        }

        // 我创建的游戏 +1key
        if (!userJoinedGameIds.isEmpty()) {
            for (NonFungibleTokenPojo userJoinedGameId : userJoinedGameIds) {
                if (userJoinedGameId.getCreator().equals(userAddress.toLowerCase())) {
                    joinedKeys = joinedKeys.add(BigDecimal.ONE);
                    if (!userJoinedGameId.getStatus().equals(GameConstant.FINISHED)) {
                        lockedKeys = lockedKeys.add(new BigDecimal(userJoinedGameId.getJoinedAmount()));
                    }
                }
            }
        }

        // 是否有转换过的key
        if (!keysConvertedList.isEmpty()) {
            for (AmountDto amountDto : keysConvertedList) {
                if (amountDto.getAmount() != null) {
                    BigDecimal converted = Convert.fromWei(amountDto.getAmount(), Convert.Unit.ETHER).setScale(0, RoundingMode.HALF_UP);
                    if (converted.toString().length() > 18) {
                        continue;
                    }
                    joinedKeys = joinedKeys.subtract(Convert.fromWei(amountDto.getAmount(), Convert.Unit.ETHER)).setScale(4, RoundingMode.HALF_UP);
                    convertedGameIds.add(amountDto.getGameId());
                }
            }
        }
        convertedGameIds.iterator().forEachRemaining(unconvertedGameIds::remove);


        myProfitVo.setKeys(joinedKeys.toString());
        myProfitVo.setLockedKeys(lockedKeys.toString());
        if (joinedKeys.subtract(lockedKeys).compareTo(BigDecimal.ZERO) > 0) {
            myProfitVo.setCanConvert(1);
        }
        myProfitVo.setConvertedGameIds(convertedGameIds);
        myProfitVo.setUnconvertedGameIds(unconvertedGameIds);
        // get max game id
        unconvertedGameIds.stream().max(Long::compareTo).ifPresent(gameId -> {
            // 查询是否游戏已结束
            bidFroopyLand.getGameStateOfGameIds(List.of(BigInteger.valueOf(gameId))).sendAsync().thenAccept(tuple -> {
                if (tuple.get(0).equals(BigInteger.valueOf(GameConstant.FINISHED))) {
                    myProfitVo.setCanConvert(1);
                } else {
                    myProfitVo.setCanConvert(0);
                }
            });
        });

        processUnclaimedGameInfo(myProfitVo, userAddress, saleIds, userJoinedGameIds);
        processKeyDividends(myProfitVo, userAddress, bonusAmountList);
        processFinalWinPrice(myProfitVo, userAddress, prizeAmountList);
        processNftDividends(myProfitVo, userAddress, saleAmountList);

        return myProfitVo;
    }

    @Override
    public UserDividendsVo getHistoricalDividendsAndPrize(String userAddress, Integer pageNum, Integer status) throws
            Exception {

        pageNum = pageNum + 1;
        UserDividendsVo userDividendsVo = new UserDividendsVo();
        List<MyHistoricalDividendsVo> historicalDividendsList = new ArrayList<>();
        // claim
        if (status.equals(GameConstant.BIDING)) {
            List<Integer> allClaimNftInfoCount = userService.getAllClaimNftInfoCount(userAddress);
            if (allClaimNftInfoCount.isEmpty()) {
                return new UserDividendsVo();
            }
            int total = allClaimNftInfoCount.stream().mapToInt(Integer::intValue).sum();
            userDividendsVo.setTotal(total);
            int pageSize = Math.min(total, 5);
            if (PageUtils.hasNext(total, pageNum, pageSize)) {
                int startIndex = (pageNum - 1) * pageSize;
                int endIndex = pageNum * pageSize;
                List<AmountDto> gameIds = userService.getAllClaimNftGameIds(userAddress, startIndex, endIndex);
                if (!gameIds.isEmpty()) {
                    for (AmountDto gameId : gameIds) {
                        MyHistoricalDividendsVo myHistoricalDividendsVo = new MyHistoricalDividendsVo();
                        myHistoricalDividendsVo.setAmount(Convert.fromWei(gameId.getAmount(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                        myHistoricalDividendsVo.setType(gameId.getType());
                        myHistoricalDividendsVo.setStatus(String.valueOf(GameConstant.BIDING));
                        NonFungibleTokenPojo nonFungibleTokenPojo = nonFungibleTokenDao.getNftAuctionsByGameId(gameId.getGameId());
                        GameNftVo gameNft = new GameNftVo();
                        BeanUtils.copyProperties(nonFungibleTokenPojo, gameNft);
                        gameNft.setTx(gameId.getTx());

                        myHistoricalDividendsVo.setGameNft(gameNft);
                        historicalDividendsList.add(myHistoricalDividendsVo);
                    }
                }
            }
            userDividendsVo.setHistoricalDividendsList(historicalDividendsList);

            // unclaimed
        } else {
            MyProfitVo myProfitVo = new MyProfitVo();

            List<AmountDto> bonusAmountList = userService.getClaimKeyBonus(userAddress, null);
            List<AmountDto> prizeAmountList = userService.getWithdrawLastPlayerPrize(userAddress);
            List<AmountDto> saleAmountList = userService.getWithdrawSaleRevenue(userAddress);

            List<Long> saleIds = saleAmountList.stream().map(AmountDto::getGameId).distinct().collect(Collectors.toList());

            List<NonFungibleTokenPojo> userJoinedGameIds = userService.getUserJoinedGameIds(userAddress);
            processUnclaimedGameInfo(myProfitVo, userAddress, saleIds, userJoinedGameIds);
            processKeyDividends(myProfitVo, userAddress, bonusAmountList);
            getFinalWinPriceGameIds(myProfitVo, userAddress, prizeAmountList);

            int total = myProfitVo.getUnclaimedFinalWinnerGameIds().size();
            if (!myProfitVo.getUnclaimedKeyDividends().equals("0.0000")) {
                total += myProfitVo.getUnclaimedKeyGameIds().size();
            }
            if (!myProfitVo.getUnclaimedNftDividends().equals("0.0000")) {
                total += myProfitVo.getUnclaimedNftGameIds().size();
            }
            userDividendsVo.setTotal(total);
            int pageSize = Math.min(total, 5);
            if (PageUtils.hasNext(total, pageNum, pageSize)) {
                int startIndex = (pageNum - 1) * pageSize;
                int endIndex = pageNum * pageSize;

                List<AmountDto> gameIds = new ArrayList<>();
                if (!myProfitVo.getUnclaimedKeyDividends().equals("0.0000")) {
                    for (Long unclaimedKeyGameId : myProfitVo.getUnclaimedKeyGameIds()) {
                        AmountDto amountDto = new AmountDto();
                        amountDto.setGameId(unclaimedKeyGameId);
                        amountDto.setType(String.valueOf(GameConstant.CLAIM_KEY));
                        gameIds.add(amountDto);
                    }
                }
                for (Long unclaimedFinalWinnerGameId : myProfitVo.getUnclaimedFinalWinnerGameIds()) {
                    AmountDto amountDto = new AmountDto();
                    amountDto.setGameId(unclaimedFinalWinnerGameId);
                    amountDto.setType(String.valueOf(GameConstant.LAST_CLAIM));
                    gameIds.add(amountDto);
                }
                if (!myProfitVo.getUnclaimedNftDividends().equals("0.0000")) {
                    for (Long unclaimedNftGameId : myProfitVo.getUnclaimedNftGameIds()) {
                        AmountDto amountDto = new AmountDto();
                        amountDto.setGameId(unclaimedNftGameId);
                        amountDto.setType(String.valueOf(GameConstant.NFT_SALE));
                        gameIds.add(amountDto);
                    }
                }

                if (!gameIds.isEmpty()) {
                    for (int i = 0; i < gameIds.size(); i++) {
                        if (i >= startIndex && i < endIndex) {
                            MyHistoricalDividendsVo myHistoricalDividendsVo = new MyHistoricalDividendsVo();
                            if (gameIds.get(i).getType().equals(String.valueOf(GameConstant.CLAIM_KEY))) {
                                Tuple2<List<BigInteger>, List<BigInteger>> send = bidFroopyLand.getPlayerStateOfGameIds(userAddress, List.of(new BigInteger(String.valueOf(gameIds.get(i).getGameId())))).send();
                                BigDecimal unclaimedKeyDividends = send.component1().stream().reduce(BigDecimal.ZERO, (a, b) -> a.add(new BigDecimal(b)), BigDecimal::add);
                                myHistoricalDividendsVo.setAmount(Convert.fromWei(unclaimedKeyDividends, Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                            }
                            if (gameIds.get(i).getType().equals(String.valueOf(GameConstant.LAST_CLAIM))) {
                                NonFungibleTokenPojo nftAuctionsByGameId = nonFungibleTokenDao.getNftAuctionsByGameId(gameIds.get(i).getGameId());
                                BigDecimal unclaimedFinalWinPrice = new BigDecimal(nftAuctionsByGameId.getSalesAmount()).divide(new BigDecimal("5"));
                                myHistoricalDividendsVo.setAmount(Convert.fromWei(unclaimedFinalWinPrice, Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                            }
                            if (gameIds.get(i).getType().equals(String.valueOf(GameConstant.NFT_SALE))) {
                                NonFungibleTokenPojo nftAuctionsByGameId = nonFungibleTokenDao.getNftAuctionsByGameId(gameIds.get(i).getGameId());
                                if (nftAuctionsByGameId == null || !StringUtils.isNoneBlank(nftAuctionsByGameId.getSalesAmount())) {
                                    continue;
                                }
                                BigDecimal nftDividends = new BigDecimal(nftAuctionsByGameId.getSalesAmount()).divide(new BigDecimal("2").setScale(0, RoundingMode.HALF_UP));
                                myHistoricalDividendsVo.setAmount(Convert.fromWei(nftDividends, Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                            }
                            myHistoricalDividendsVo.setType(gameIds.get(i).getType());
                            myHistoricalDividendsVo.setStatus(String.valueOf(GameConstant.NOT_STARTED));
                            NonFungibleTokenPojo nonFungibleTokenPojo = nonFungibleTokenDao.getNftAuctionsByGameId(gameIds.get(i).getGameId());
                            GameNftVo gameNft = new GameNftVo();
                            BeanUtils.copyProperties(nonFungibleTokenPojo, gameNft);

                            myHistoricalDividendsVo.setGameNft(gameNft);
                            historicalDividendsList.add(myHistoricalDividendsVo);
                        }
                    }
                }
            }

            userDividendsVo.setHistoricalDividendsList(historicalDividendsList);
        }

        return userDividendsVo;
    }

    @Override
    public UserRetrievedVo getMyPurchasedNfts(String userAddress, Integer pageNum, int pageSize) {
        UserRetrievedVo userRetrievedVo = new UserRetrievedVo();
        pageNum = pageNum + 1;
        int total = userService.getNftRetrievedCount(userAddress);
        List<GameNftVo> gameNftList = new ArrayList<>();
        pageSize = Math.min(total, 5);
        if (PageUtils.hasNext(total, pageNum, pageSize)) {
            int statIndex = (pageNum - 1) * pageSize;
            int endIndex = pageNum * pageSize;
            gameNftList = userService.getNftRetrievedList(userAddress, statIndex, endIndex);
        }
        userRetrievedVo.setTotal(total);
        userRetrievedVo.setGameNftList(gameNftList);
        return userRetrievedVo;
    }

    @Override
    public NftListVo getMyParticipationGames(String userAddress, Integer status) {
        NftListVo nftListVo = new NftListVo();
        List<NonFungibleTokenVo> nftList = userService.getMyParticipationGames(userAddress, status);
        nftListVo.setTotal(nftList.size());
        if (!nftList.isEmpty()) {
            for (NonFungibleTokenVo nonFungibleTokenVo : nftList) {
                List<String> players = nonFungibleTokenDao.getGamePlayers(nonFungibleTokenVo.getGameId());
                nonFungibleTokenVo.setBiddersCount(players.isEmpty() ? 0 : players.size());
                if (nonFungibleTokenVo.getStartTime() != null) {
                    nonFungibleTokenVo.setStartTimestamp(nonFungibleTokenVo.getStartTime().getTime());
                    nonFungibleTokenVo.setStartTime(null);
                }
                if (nonFungibleTokenVo.getEndTime() != null) {
                    nonFungibleTokenVo.setEndTimestamp(nonFungibleTokenVo.getEndTime().getTime());
                    nonFungibleTokenVo.setEndTime(null);
                }
//                if (StringUtils.isNoneBlank(nonFungibleTokenVo.getKeyPrice())) {
//                    nonFungibleTokenVo.setFinalPrice(Convert.fromWei(nonFungibleTokenVo.getKeyPrice(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
//                }
                if (StringUtils.isNoneBlank(nonFungibleTokenVo.getSalesAmount())) {
                    nonFungibleTokenVo.setTotalMintFee(Convert.fromWei(nonFungibleTokenVo.getSalesAmount(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                    nonFungibleTokenVo.setFinalPrice(Convert.fromWei((new BigDecimal(nonFungibleTokenVo.getSalesAmount()).multiply(new BigDecimal("2")).divide(new BigDecimal("10")).setScale(0, RoundingMode.HALF_UP)), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                }

                // 更新游戏状态
                if (nonFungibleTokenVo.getStatus().equals(GameConstant.UPCOMING) && nonFungibleTokenVo.getStartTime() != null && nonFungibleTokenVo.getStartTime().getTime() < System.currentTimeMillis()) {
                    nonFungibleTokenDao.updateGameStatus(nonFungibleTokenVo.getGameId(), GameConstant.ONGOING);
                    nonFungibleTokenVo.setStatus(GameConstant.ONGOING);
                } else if (nonFungibleTokenVo.getEndTime() != null && nonFungibleTokenVo.getEndTime().getTime() < System.currentTimeMillis() && !nonFungibleTokenVo.getStatus().equals(GameConstant.FINISHED)) {
                    nonFungibleTokenDao.updateGameStatus(nonFungibleTokenVo.getGameId(), GameConstant.FINISHED);
                    nonFungibleTokenVo.setStatus(GameConstant.FINISHED);
                }
            }
        }
        nftListVo.setNftList(nftList);
        return nftListVo;
    }

    @Override
    public NftListVo getMyAuctions(String userAddress, Integer status) {
        NftListVo nftListVo = new NftListVo();
        List<NonFungibleTokenVo> nftList = userService.getMyAuctions(userAddress, status);
        nftListVo.setTotal(nftList.size());
        if (!nftList.isEmpty()) {
            for (NonFungibleTokenVo nonFungibleTokenVo : nftList) {
                List<String> players = nonFungibleTokenDao.getGamePlayers(nonFungibleTokenVo.getGameId());
                nonFungibleTokenVo.setBiddersCount(players.isEmpty() ? 0 : players.size());
                if (nonFungibleTokenVo.getStartTime() != null) {
                    nonFungibleTokenVo.setStartTimestamp(nonFungibleTokenVo.getStartTime().getTime());
                    nonFungibleTokenVo.setStartTime(null);
                }
                if (nonFungibleTokenVo.getEndTime() != null) {
                    nonFungibleTokenVo.setEndTimestamp(nonFungibleTokenVo.getEndTime().getTime());
                    nonFungibleTokenVo.setEndTime(null);
                }
//                if (StringUtils.isNoneBlank(nonFungibleTokenVo.getKeyPrice())) {
//                    nonFungibleTokenVo.setFinalPrice(Convert.fromWei(nonFungibleTokenVo.getKeyPrice(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
//                }
                if (StringUtils.isNoneBlank(nonFungibleTokenVo.getSalesAmount())) {
                    nonFungibleTokenVo.setTotalMintFee(Convert.fromWei(nonFungibleTokenVo.getSalesAmount(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                    nonFungibleTokenVo.setFinalPrice(Convert.fromWei((new BigDecimal(nonFungibleTokenVo.getSalesAmount()).multiply(new BigDecimal("2")).divide(new BigDecimal("10")).setScale(0, RoundingMode.HALF_UP)), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
                }

                // 更新游戏状态
                if (nonFungibleTokenVo.getStatus().equals(GameConstant.UPCOMING) && nonFungibleTokenVo.getStartTime() != null && nonFungibleTokenVo.getStartTime().getTime() < System.currentTimeMillis()) {
                    nonFungibleTokenDao.updateGameStatus(nonFungibleTokenVo.getGameId(), GameConstant.ONGOING);
                    nonFungibleTokenVo.setStatus(GameConstant.ONGOING);
                } else if (nonFungibleTokenVo.getEndTime() != null && nonFungibleTokenVo.getEndTime().getTime() < System.currentTimeMillis() && !nonFungibleTokenVo.getStatus().equals(GameConstant.FINISHED)) {
                    nonFungibleTokenDao.updateGameStatus(nonFungibleTokenVo.getGameId(), GameConstant.FINISHED);
                    nonFungibleTokenVo.setStatus(GameConstant.FINISHED);
                }
            }
        }
        nftListVo.setNftList(nftList);
        return nftListVo;
    }


    /**
     * description: process unclaimed game info
     *
     * @param myProfitVo        myProfitVo
     * @param userAddress       userAddress
     * @param saleIds           saleIds
     * @param userJoinedGameIds userJoinedGameIds
     * @author yozora
     * @date 17:15 2024/4/14
     **/
    private void processUnclaimedGameInfo(MyProfitVo myProfitVo, String
            userAddress, List<Long> saleIds, List<NonFungibleTokenPojo> userJoinedGameIds) {
        List<Long> unclaimedKeyGameIds = new ArrayList<>();
        List<Long> unclaimedFinalWinnerGameIds = new ArrayList<>();
        List<Long> unclaimedNftGameIds = new ArrayList<>();

        BigDecimal unclaimedNftDividends = BigDecimal.ZERO;
        BigDecimal lockedNftDividends = BigDecimal.ZERO;
        if (!userJoinedGameIds.isEmpty()) {
            for (NonFungibleTokenPojo userJoinedGameInfo : userJoinedGameIds) {
                if (userJoinedGameInfo.getEndTime() != null && userJoinedGameInfo.getEndTime().getTime() < System.currentTimeMillis() && !userJoinedGameInfo.getStatus().equals(GameConstant.FINISHED)) {
                    // 更新游戏状态
                    nonFungibleTokenDao.updateGameStatus(userJoinedGameInfo.getGameId(), GameConstant.FINISHED);
                    userJoinedGameInfo.setStatus(GameConstant.FINISHED);
                }
                if (userJoinedGameInfo.getStatus().equals(GameConstant.UPCOMING) && userJoinedGameInfo.getStartTime() != null && userJoinedGameInfo.getStartTime().getTime() < System.currentTimeMillis()) {
                    nonFungibleTokenDao.updateGameStatus(userJoinedGameInfo.getGameId(), GameConstant.ONGOING);
                }
                unclaimedKeyGameIds.add(userJoinedGameInfo.getGameId());
                if (!saleIds.contains(userJoinedGameInfo.getGameId()) && userJoinedGameInfo.getUserAddress().equals(userAddress)) {
                    if (userJoinedGameInfo.getStatus().equals(GameConstant.FINISHED)) {
                        unclaimedNftGameIds.add(userJoinedGameInfo.getGameId());
                        if (userJoinedGameInfo.getSalesAmount() != null && userJoinedGameInfo.getSalesAmount().compareTo("0") > 0) {
                            unclaimedNftDividends = unclaimedNftDividends.add(new BigDecimal(userJoinedGameInfo.getSalesAmount()).divide(new BigDecimal("2")));
                        }
                    } else {
                        lockedNftDividends = lockedNftDividends.add(new BigDecimal(userJoinedGameInfo.getSalesAmount()).divide(new BigDecimal("2")));
                    }
                }
            }
        }

//        myProfitVo.setUnclaimedNftDividends(Convert.fromWei(unclaimedNftDividends, Convert.Unit.ETHER).multiply(new BigDecimal(ethPrice)).setScale(4, RoundingMode.HALF_UP).toString());
//        myProfitVo.setLockedNftDividends(Convert.fromWei(lockedNftDividends, Convert.Unit.ETHER).multiply(new BigDecimal(ethPrice)).setScale(4, RoundingMode.HALF_UP).toString());
        myProfitVo.setUnclaimedNftDividends(Convert.fromWei(unclaimedNftDividends, Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
        myProfitVo.setLockedNftDividends(Convert.fromWei(lockedNftDividends, Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
        myProfitVo.setUnclaimedKeyGameIds(unclaimedKeyGameIds);
        myProfitVo.setUnclaimedFinalWinnerGameIds(unclaimedFinalWinnerGameIds);
        myProfitVo.setUnclaimedNftGameIds(unclaimedNftGameIds);
    }

    /**
     * description: process key dividends
     *
     * @param myProfitVo      myProfitVo
     * @param userAddress     userAddress
     * @param bonusAmountList bonusAmountList
     * @author yozora
     * @date 17:15 2024/4/14
     **/
    private void processKeyDividends(MyProfitVo myProfitVo, String userAddress, List<AmountDto> bonusAmountList) {
        BigDecimal bonusAmount = BigDecimal.ZERO;
        for (AmountDto amountDto : bonusAmountList) {
            bonusAmount = bonusAmount.add(amountDto.getAmount());
        }
        myProfitVo.setKeyDividends(Convert.fromWei(bonusAmount.toString(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());

        List<Long> unclaimedKeyGameIds = myProfitVo.getUnclaimedKeyGameIds();
        List<Long> removedGameIds = new ArrayList<>();
        BigDecimal unclaimedKeyDividends = BigDecimal.ZERO;
        if (!unclaimedKeyGameIds.isEmpty()) {
            List<BigInteger> gameIds = unclaimedKeyGameIds.stream().map(BigInteger::valueOf).collect(Collectors.toList());
            try {
                Tuple2<List<BigInteger>, List<BigInteger>> send = bidFroopyLand.getPlayerStateOfGameIds(userAddress, gameIds).send();
                for (int i = 0; i < send.component1().size(); i++) {
                    if (send.component1().get(i).compareTo(BigInteger.ZERO) > 0) {
                        unclaimedKeyDividends = unclaimedKeyDividends.add(new BigDecimal(send.component1().get(i)));
                    } else {
                        removedGameIds.add(unclaimedKeyGameIds.get(i));
                    }
                }
                unclaimedKeyGameIds.removeAll(removedGameIds);
//                unclaimedKeyDividends = send.component1().stream().reduce(BigDecimal.ZERO, (a, b) -> a.add(new BigDecimal(b)), BigDecimal::add);
            } catch (Exception e) {
                log.error("get player state of game ids error", e);
            }
        }
        myProfitVo.setUnclaimedKeyDividends(Convert.fromWei(unclaimedKeyDividends.toString(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
    }

    private void processFinalWinPrice(MyProfitVo myProfitVo, String userAddress, List<AmountDto> prizeAmountList) {

        BigDecimal prizeAmount = BigDecimal.ZERO;
        for (AmountDto amountDto : prizeAmountList) {
//            prizeAmount = prizeAmount.multiply(new BigDecimal("1.5")).add(amountDto.getAmount());
            prizeAmount = prizeAmount.add(amountDto.getAmount());
        }
        UserNftRequest userNftRequest = new UserNftRequest();
        userNftRequest.setLastAddress(userAddress);
        userNftRequest.setStatus(GameConstant.FINISHED);
        List<NonFungibleTokenPojo> nonFungibleTokenPojoList = nonFungibleTokenDao.getUserNftAuctions(userNftRequest);
        BigDecimal unclaimedFinalWinPrice = BigDecimal.ZERO;
        if (!nonFungibleTokenPojoList.isEmpty()) {
            List<Long> unclaimedFinalWinnerGameIds = myProfitVo.getUnclaimedFinalWinnerGameIds();
            for (NonFungibleTokenPojo nonFungibleTokenPojo : nonFungibleTokenPojoList) {
                if (!prizeAmountList.isEmpty()) {
                    boolean isExist = false;
                    for (AmountDto amountDto : prizeAmountList) {
                        if (nonFungibleTokenPojo.getGameId().equals(amountDto.getGameId())) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        unclaimedFinalWinnerGameIds.add(nonFungibleTokenPojo.getGameId());
                        unclaimedFinalWinPrice = unclaimedFinalWinPrice.add(new BigDecimal(nonFungibleTokenPojo.getSalesAmount()).divide(new BigDecimal("5")));
                    }
                } else {
                    unclaimedFinalWinnerGameIds.add(nonFungibleTokenPojo.getGameId());
                    unclaimedFinalWinPrice = unclaimedFinalWinPrice.add(new BigDecimal(nonFungibleTokenPojo.getSalesAmount()).divide(new BigDecimal("5")));
                }
            }
            myProfitVo.setUnclaimedFinalWinnerGameIds(unclaimedFinalWinnerGameIds);
        }
        myProfitVo.setFinalWinPrice(Convert.fromWei(prizeAmount.toString(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
        myProfitVo.setUnclaimedFinalWinPrice(Convert.fromWei(unclaimedFinalWinPrice.toString(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
    }

    private void processNftDividends(MyProfitVo myProfitVo, String userAddress, List<AmountDto> saleAmountList) {
        BigDecimal saleAmount = BigDecimal.ZERO;
        for (AmountDto amountDto : saleAmountList) {
            saleAmount = saleAmount.add(amountDto.getAmount());
        }
        myProfitVo.setNftDividends(Convert.fromWei(saleAmount.toString(), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_UP).toString());
    }

    private void getFinalWinPriceGameIds(MyProfitVo myProfitVo, String
            userAddress, List<AmountDto> prizeAmountList) {
        UserNftRequest userNftRequest = new UserNftRequest();
        userNftRequest.setLastAddress(userAddress);
        userNftRequest.setStatus(GameConstant.FINISHED);
        List<NonFungibleTokenPojo> nonFungibleTokenPojoList = nonFungibleTokenDao.getUserNftAuctions(userNftRequest);
        if (!nonFungibleTokenPojoList.isEmpty()) {
            List<Long> unclaimedFinalWinnerGameIds = myProfitVo.getUnclaimedFinalWinnerGameIds();
            for (NonFungibleTokenPojo nonFungibleTokenPojo : nonFungibleTokenPojoList) {
                if (!prizeAmountList.isEmpty()) {
//                    for (AmountDto amountDto : prizeAmountList) {
//                        if (!nonFungibleTokenPojo.getGameId().equals(amountDto.getGameId())) {
//                            unclaimedFinalWinnerGameIds.add(nonFungibleTokenPojo.getGameId());
//                        }
//                    }
                    boolean isExist = false;
                    for (AmountDto amountDto : prizeAmountList) {
                        if (nonFungibleTokenPojo.getGameId().equals(amountDto.getGameId())) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        unclaimedFinalWinnerGameIds.add(nonFungibleTokenPojo.getGameId());
                    }
                } else {
                    unclaimedFinalWinnerGameIds.add(nonFungibleTokenPojo.getGameId());
                }
            }
            myProfitVo.setUnclaimedFinalWinnerGameIds(unclaimedFinalWinnerGameIds);
        }
    }

    /**
     * description: process key word
     *
     * @param userNonFungibleTokenRequest userNonFungibleTokenRequest
     * @author yozora
     * @date 17:15 2022/9/8
     **/
    private void processKeyWord(UserNonFungibleTokenRequest userNonFungibleTokenRequest) {
        // 判断关键词类型
        if (StringUtils.isNoneBlank(userNonFungibleTokenRequest.getKeyWord())) {
            // 是否合约地址
            if (userNonFungibleTokenRequest.getKeyWord().toUpperCase().startsWith("0X")) {
                userNonFungibleTokenRequest.setQueryAddress(userNonFungibleTokenRequest.getKeyWord());
            }
            // 是否token id
            else if (StringUtils.isNumeric(userNonFungibleTokenRequest.getKeyWord())) {
                userNonFungibleTokenRequest.setQueryToken(userNonFungibleTokenRequest.getKeyWord());
            }
            // 是否名称或描述
            else {
                userNonFungibleTokenRequest.setQueryNameOrDescription(userNonFungibleTokenRequest.getKeyWord());
            }
        }
    }

    public static void main(String[] args) {

        String data = "[{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0xa1601ac589b452fcdf800dc289a0e60c678f38fd64b6f1fdede9766f8c694e59\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"84040246936393764467923118662432238154602107327521087684622416135024432103658\",\"imageUrl\":\"https://openseauserdata.com/files/c2ec8d1c999f6453593a944b916fb88a.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/84040246936393764467923118662432238154602107327521087684622416135024432103658\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/84040246936393764467923118662432238154602107327521087684622416135024432103658\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x508ad24bb82781168ba22f18e76e19f8cc12ff8c13610f8bdac1e3dd9169d2c3\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"96792717509473452076117560004520133918722242644910912130652440786146948615750\",\"imageUrl\":\"https://openseauserdata.com/files/1099977e188cfe877925b0202922b0f8.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/96792717509473452076117560004520133918722242644910912130652440786146948615750\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/96792717509473452076117560004520133918722242644910912130652440786146948615750\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x9557356ea21d5e8f2e8ce4a43fba3d95f578b1b69387bf7a443f987a02d8bf3b\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"64978624665862057239118128298045790319407043925704802465350826201655533025062\",\"imageUrl\":\"https://openseauserdata.com/files/f5800602331d9e343fc267df65b4c35e.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/64978624665862057239118128298045790319407043925704802465350826201655533025062\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/64978624665862057239118128298045790319407043925704802465350826201655533025062\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x82983f29f4eff5780487f9dc9b99fb024574409b45388beccc1304114d754d12\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"96810651816748936710085696360098485959711948874775739317838988639110266181670\",\"imageUrl\":\"https://openseauserdata.com/files/c224a6335b336d63fc1a0d6d077bed67.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/96810651816748936710085696360098485959711948874775739317838988639110266181670\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/96810651816748936710085696360098485959711948874775739317838988639110266181670\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x60d081a5d553e5248819e2496aabfe828e30f8e6243bdff585ca2ad3207b34e1\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"61497170234060338203081569551039759548276709194544858674258353001568839289879\",\"imageUrl\":\"https://openseauserdata.com/files/c7f07fec49cd2f993de4c380c363a3d6.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/61497170234060338203081569551039759548276709194544858674258353001568839289879\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/61497170234060338203081569551039759548276709194544858674258353001568839289879\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0xd0acd6277782bb7a5dcfaf29f5985870bf600501334783492554e568dc55e46b\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"21387032184476340822982576335659098976563764158411304110983965673678321127182\",\"imageUrl\":\"https://openseauserdata.com/files/3bec6fae2b7b475e5f2e9be7a841f383.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/21387032184476340822982576335659098976563764158411304110983965673678321127182\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/21387032184476340822982576335659098976563764158411304110983965673678321127182\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x19d2fc50940f2c8c1d72ba048e791bd61c4b7544ee084c0eaf48200118e770af\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"65209319447912589979893868180577373036142886297811043679566839727672564767244\",\"imageUrl\":\"https://openseauserdata.com/files/0c89856cdc6c0276ca597f5d72dc91e5.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/65209319447912589979893868180577373036142886297811043679566839727672564767244\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/65209319447912589979893868180577373036142886297811043679566839727672564767244\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0xa2a1ecdb5314e213dcdc87f869430723d0373f1efce456d046e6e919fa688ba9\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"20305636771132142883834396116944568206942730196249271578352625073310818544170\",\"imageUrl\":\"https://openseauserdata.com/files/9ec75bbb666d6a3d0d08958345c7aeb9.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/20305636771132142883834396116944568206942730196249271578352625073310818544170\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/20305636771132142883834396116944568206942730196249271578352625073310818544170\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x459552fb7bbd27ee92c0f64d2d62ca47ba6af9f51394d05cf706404b32d08f2b\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"67281601917529964073965953781666644470274933613764007139960734739700596987918\",\"imageUrl\":\"https://openseauserdata.com/files/2a4faa64a2d2f41362f45bef3b7b009c.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/67281601917529964073965953781666644470274933613764007139960734739700596987918\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/67281601917529964073965953781666644470274933613764007139960734739700596987918\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x838b83b0f36a316df48a4ec0f9890f047b2fae5213e98746051db65ce26d968b\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"97972337751907398299910980369093335165868734283634712139916770960557295934164\",\"imageUrl\":\"https://openseauserdata.com/files/16017e1560ca7516dbe48fad4bf40c3d.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/97972337751907398299910980369093335165868734283634712139916770960557295934164\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/97972337751907398299910980369093335165868734283634712139916770960557295934164\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0xf5076a9c565086c83cd02315a7b55287fd3978fc051662bf66a3a00c88793e8c\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"34837072133707575489334008444544515137780832802217679446361228405527127648119\",\"imageUrl\":\"https://openseauserdata.com/files/97dc434be34979351740868afc109426.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/34837072133707575489334008444544515137780832802217679446361228405527127648119\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/34837072133707575489334008444544515137780832802217679446361228405527127648119\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0xc0e2882e2162b6c4736964fa31ac202ba1503f40023f37b86b9d2b99b59330f4\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"6358742994827633732907515357298978751866626045303729571420170078263157117465\",\"imageUrl\":\"https://openseauserdata.com/files/f5941e472b4f5487ae48d5ffedda0dc0.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/6358742994827633732907515357298978751866626045303729571420170078263157117465\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/6358742994827633732907515357298978751866626045303729571420170078263157117465\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x61c7064aa4ead85eaafa63c3bbd242a53e8d47b536a498984a534b7218c7b695\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"68145685958238067145340355572370835940669061786914761941801470223009450049196\",\"imageUrl\":\"https://openseauserdata.com/files/3fa1cb232923ed592988b7118b589f6a.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/68145685958238067145340355572370835940669061786914761941801470223009450049196\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/68145685958238067145340355572370835940669061786914761941801470223009450049196\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x8a527584f2a73a42eafed936fcca09d718aaa408c9ec5b58720602ab1b5f10a2\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"64507199290579595409461325786508541721432688609350493165832594746467144026643\",\"imageUrl\":\"https://openseauserdata.com/files/79f519f9652832933dae0b8a5249e567.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/64507199290579595409461325786508541721432688609350493165832594746467144026643\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/64507199290579595409461325786508541721432688609350493165832594746467144026643\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x859facaf9e01dadb7bdad178f29cc4c973bdc1b8afb9d67d8fc733bbda9a3ce1\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"60520793887601912010129782882269156711365489342550158168930442626820560199295\",\"imageUrl\":\"https://openseauserdata.com/files/1bb68d615cb5860b91c00efa2d931e5e.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/60520793887601912010129782882269156711365489342550158168930442626820560199295\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/60520793887601912010129782882269156711365489342550158168930442626820560199295\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x1a7411775026d8de46a976c252af46aa842c17e4173b1c0a26dff008c2c24d48\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"34373168648907797583810578921448678802362076761593172699829316811374532753742\",\"imageUrl\":\"https://openseauserdata.com/files/9294340be82b3a712a54d71bfbd6baef.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/34373168648907797583810578921448678802362076761593172699829316811374532753742\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/34373168648907797583810578921448678802362076761593172699829316811374532753742\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x778c5f2eb6dbbbc4ff5145f80631ac5e7f139f32c458d621b94fa4157a2b1244\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"96803030078106255872292391499646082127877937540096859330531859292217943211643\",\"imageUrl\":\"https://openseauserdata.com/files/719b76e220d0ec83dab558cf401d58a1.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/96803030078106255872292391499646082127877937540096859330531859292217943211643\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/96803030078106255872292391499646082127877937540096859330531859292217943211643\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0xb2d9cb223cec0b1c04f822d1b202c262e515d9fd42b2f17c0caa5f7d43daa214\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"52159454377903591008937142989005745920618172775984464099599917192897294885334\",\"imageUrl\":\"https://openseauserdata.com/files/65d4708c8935d38d3c173ece44934107.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/52159454377903591008937142989005745920618172775984464099599917192897294885334\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/52159454377903591008937142989005745920618172775984464099599917192897294885334\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x4afe3986fea4311278d922c872e6fe2226a634766e861b7808822381bc64bc12\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"980318471900211262500440328167551480097521583029258032619616850914686565317\",\"imageUrl\":\"https://openseauserdata.com/files/0ba73d3ad392fa11a9c4cb574dd33ed3.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/980318471900211262500440328167551480097521583029258032619616850914686565317\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/980318471900211262500440328167551480097521583029258032619616850914686565317\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x66a6ae3f51f104922ea9be6fb327ca28c2db11735d17dcbed89f750817c4c82d\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"20304112346557813730867754626827617779066572849974302264465712579502759687122\",\"imageUrl\":\"https://openseauserdata.com/files/4adaee3f3ff8292aaa79fe770b41abe0.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/20304112346557813730867754626827617779066572849974302264465712579502759687122\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/20304112346557813730867754626827617779066572849974302264465712579502759687122\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x99cad3ed2a58df8efeb5947d050c842fe3f3ec79ffae2a624f5fb3c81a775d1d\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"9459712118678943308072911890840976487149033035517440124182296819086500825343\",\"imageUrl\":\"https://openseauserdata.com/files/885734e38d093d50f2c0a5b2aef70ad1.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/9459712118678943308072911890840976487149033035517440124182296819086500825343\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/9459712118678943308072911890840976487149033035517440124182296819086500825343\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0xd22fa4ce63b9935045ddc75668f3d752d5f4f2f11d8da65701994c6ec3468f59\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"98196312676739334612345268270827077987530052291260772358874167111084458260117\",\"imageUrl\":\"https://openseauserdata.com/files/2c32cba21ef03be6bd01670d4474552e.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/98196312676739334612345268270827077987530052291260772358874167111084458260117\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/98196312676739334612345268270827077987530052291260772358874167111084458260117\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x3b8a90523867f9e623fd918bf4645a79b98dc454251dc2407aea2aaa442ffebb\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"30808173911862466915627113736361970940240396370050769545874041710853967612912\",\"imageUrl\":\"https://openseauserdata.com/files/4702e3b57faa06da71c46c5b59111847.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/30808173911862466915627113736361970940240396370050769545874041710853967612912\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/30808173911862466915627113736361970940240396370050769545874041710853967612912\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0x7649aaef7955ac301538a6485bc28867dbd28adcd3f76aaacc80f9a252c95410\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"76045176019033313044869004612418057643018027267651857790727843806528817288932\",\"imageUrl\":\"https://openseauserdata.com/files/97ad850e6ac16c9c62980f71be4f77e7.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/76045176019033313044869004612418057643018027267651857790727843806528817288932\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/76045176019033313044869004612418057643018027267651857790727843806528817288932\",\"bio\":null,\"loginCount\":null,\"platformList\":null},{\"name\":\"ENS\",\"symbol\":\"ENS\",\"chainName\":\"Ethereum\",\"chainId\":\"1\",\"nftCode\":\"0xe9b19dbb70208a166dd14e337b6a0a27c65af341ddd8f610e938260feda624d4\",\"userAddress\":null,\"nftAddress\":\"0x57f1887a8BF19b14fC0dF6Fd9B2acc9Af147eA85\",\"tokenId\":\"22248793100052210207313397859332869631586162104091620001005983271698388640988\",\"imageUrl\":\"https://openseauserdata.com/files/fda6acb0d3f7914fa11019c694a64350.svg\",\"animationUrl\":null,\"fromPlatform\":\"0\",\"tokenMetadataUrl\":\"https://metadata.ens.domains/mainnet/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/22248793100052210207313397859332869631586162104091620001005983271698388640988\",\"permalink\":\"https://opensea.io/assets/ethereum/0x57f1887a8bf19b14fc0df6fd9b2acc9af147ea85/22248793100052210207313397859332869631586162104091620001005983271698388640988\",\"bio\":null,\"loginCount\":null,\"platformList\":null}]";
        List<NonFungibleTokenPojo> userNonFungibleTokenList = JSON.parseArray(data, NonFungibleTokenPojo.class);

        System.out.println(userNonFungibleTokenList);
        // 暂存查询排名
        Set<String> nftNames = userNonFungibleTokenList.stream().collect(Collectors.groupingBy(NonFungibleTokenPojo::getName)).keySet();

        nftNames.forEach(System.out::println);
    }
}

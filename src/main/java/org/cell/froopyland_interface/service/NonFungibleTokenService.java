package org.cell.froopyland_interface.service;


import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenCodePojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenDesPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.AuctionInfoVo;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.BidVo;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.GameBriefVo;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.GameNftAmountVo;
import org.cell.froopyland_interface.entity.request.BaseNonFungibleTokenRequest;
import org.cell.froopyland_interface.entity.request.DesNonFungibleTokenRequest;
import org.cell.froopyland_interface.entity.request.UserNonFungibleTokenRequest;
import org.cell.froopyland_interface.entity.response.*;
import org.cell.froopyland_interface.entity.user.vo.MyProfitVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yozora
 * @description NonFungibleTokenService
 **/
public interface NonFungibleTokenService {

    /**
     * description: get user NFT info
     *
     * @param userNonFungibleTokenRequest userNonFungibleTokenRequest {userAddress,NFTAddress?}
     * @return java.util.List<com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenPojo>
     * @author yozora
     **/
    List<NonFungibleTokenPojo> getUserNonFungibleTokenList(UserNonFungibleTokenRequest userNonFungibleTokenRequest);

    /**
     * description: get user NFT info
     *
     * @param userNonFungibleTokenRequest userNonFungibleTokenRequest {userAddress,timeType}
     * @return java.util.List<com.metaverse.nfgate.entity.nonFungibleToken.NonFungibleTokenPojo>
     * @author yozora
     **/
    List<NonFungibleTokenPojo> getLatestNonFungibleTokenList(UserNonFungibleTokenRequest userNonFungibleTokenRequest);

    /**
     * description: get user NFT info
     *
     * @param baseNonFungibleTokenRequest nonFungibleTokenRequest  {userAddress, NFTAddress, NFTId}
     * @return com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenCodePojo
     * @author yozora
     **/
    NonFungibleTokenPojo getUserNonFungibleTokenInfo(BaseNonFungibleTokenRequest baseNonFungibleTokenRequest);

    /**
     * description: get NFT code info
     *
     * @param baseNonFungibleTokenRequest nonFungibleTokenRequest {NFTCode}
     * @return com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenCodePojo
     * @author yozora
     **/
    NonFungibleTokenCodePojo getNonFungibleTokenCodeInfo(BaseNonFungibleTokenRequest baseNonFungibleTokenRequest);

    /**
     * description: update NFT owner
     *
     * @param baseNonFungibleTokenRequest nonFungibleTokenRequest {userAddress, NFTCode}
     * @author yozora
     **/
    void updateUserNonFungibleTokenInfo(BaseNonFungibleTokenRequest baseNonFungibleTokenRequest);

    /**
     * description: get NFT description info
     *
     * @param nftAddress NFTAddress
     * @return com.metaverse.trust.entity.request.response.DesNonFungibleTokenResponse
     * @author yozora
     **/
    DesNonFungibleTokenVo getNonFungibleTokenDescription(String nftAddress);

    /**
     * description: get all NFT description info
     *
     * @return java.util.ArrayList<com.metaverse.trust.entity.request.response.DesNonFungibleTokenResponse>
     * @author yozora
     **/
    ArrayList<DesNonFungibleTokenVo> getAllNonFungibleTokenDescription();

    /**
     * description: get NFT items
     *
     * @param userNonFungibleTokenRequest userNonFungibleTokenRequest {networkName,chainId}
     * @return java.util.List<com.metaverse.trust.entity.response.DesNonFungibleTokenResponse>
     * @author yozora
     **/
    List<DesNonFungibleTokenVo> getNonFungibleTokenItems(UserNonFungibleTokenRequest userNonFungibleTokenRequest);

    /**
     * description: get NFT code info
     *
     * @param nftCode nftCode
     * @return com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenCodePojo
     * @author yozora
     **/
    NonFungibleTokenCodePojo getNonFungibleTokenCode(String nftCode);

    /**
     * description: get NFT info
     *
     * @param contractAddress contract address
     * @param tokenId         token id
     * @param chainName       network name
     * @return com.metaverse.nfgate.entity.nonFungibleToken.NonFungibleTokenPojo
     * @author yozora
     **/
    NonFungibleTokenPojo getNonFungibleTokenInfo(String contractAddress, String tokenId, String chainName);

    /**
     * description: get NFT description info
     *
     * @param desNonFungibleTokenRequest desNonFungibleTokenRequest {pageNum,pageSize?}
     * @return java.util.List<com.metaverse.nfgate.entity.nonFungibleToken.NonFungibleTokenDesPojo>
     * @author yozora
     **/
    List<NonFungibleTokenDesPojo> getNonNonFungibleTokenDescription(DesNonFungibleTokenRequest desNonFungibleTokenRequest);

    /**
     * description: get NFT description count
     *
     * @param chainId chain id
     * @return int
     * @author yozora
     * @date 18:48 2022/7/12
     **/
    int getDescriptionCount(String chainId);

    /**
     * description: get user NFT description info
     *
     * @param desNonFungibleTokenRequest desNonFungibleTokenRequest {pageNum,pageSize?}
     * @return java.util.List<com.metaverse.nfgate.entity.response.DesNonFungibleTokenResponse>
     * @author yozora
     * @date 17:07 2022/7/22
     **/
    List<DesNonFungibleTokenVo> getUserNonFungibleTokenDescription(DesNonFungibleTokenRequest desNonFungibleTokenRequest);

    NftListVo getNftAuctions(Integer status, Integer pageNum);

    UserNftListVo getUserNftList(String userAddress, String next, Integer pageSize);

    Long getMaxGameId();

    GameBriefVo getGameBrief();

    List<BidVo> getBidderForm(Long bidId) throws Exception;

    MyProfitVo getMyProfit(String userAddress) throws Exception;

    UserDividendsVo getHistoricalDividendsAndPrize(String userAddress, Integer pageNum, Integer status) throws Exception;

    UserRetrievedVo getMyPurchasedNfts(String userAddress, Integer pageNum, int pageSize);

    NftListVo getMyParticipationGames(String userAddress, Integer status);

    AuctionInfoVo getAuctionInfo() throws Exception;

    GameNftAmountVo getGameDetail(String userAddress, Long gameId);

    NftListVo getMyAuctions(String userAddress, Integer status);

    NftListVo getNftPoolList(int status, Integer pageNum);

    Integer getStakeNotices(String userAddress);


    List<NonFungibleTokenPojo> getJoinLog(String startBlock, String endBlock) throws IOException;

}

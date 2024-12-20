package org.cell.froopyland_interface.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cell.froopyland_interface.entity.nonFungibleToken.LatestNftCodeInfoPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenCodePojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenDesPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.dto.SettleBidDto;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.BidVo;
import org.cell.froopyland_interface.entity.request.*;
import org.cell.froopyland_interface.entity.response.DesNonFungibleTokenVo;
import org.cell.froopyland_interface.entity.user.dto.UserJoinLogDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yozora
 * @description NonFungibleTokenDao
 **/
@Mapper
public interface NonFungibleTokenDao {

    /**
     * description: get user NFT info
     *
     * @param userNonFungibleTokenRequest userNonFungibleTokenRequest {userAddress,NFTAddress}
     * @return java.util.List<com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenPojo>
     * @author yozora
     **/
    List<NonFungibleTokenPojo> getUserNonFungibleTokenList(UserNonFungibleTokenRequest userNonFungibleTokenRequest);

    /**
     * description: get NFT code info
     *
     * @param baseNonFungibleTokenRequest nonFungibleTokenRequest {NFTCode}
     * @return com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenCodePojo
     * @author yozora
     **/
    NonFungibleTokenCodePojo getNonFungibleTokenCodeInfo(BaseNonFungibleTokenRequest baseNonFungibleTokenRequest);

    /**
     * description: get user NFT info
     *
     * @param baseNonFungibleTokenRequest nonFungibleTokenRequest {userAddress, NFTAddress, NFTId}
     * @return com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenCodePojo
     * @author yozora
     **/
    NonFungibleTokenPojo getUserNonFungibleTokenInfo(BaseNonFungibleTokenRequest baseNonFungibleTokenRequest);

    /**
     * description:  update NFT owner
     *
     * @param baseNonFungibleTokenRequest nonFungibleTokenRequest {userAddress, NFTCode}
     * @author yozora
     **/
    void updateUserNonFungibleTokenInfo(BaseNonFungibleTokenRequest baseNonFungibleTokenRequest);

    /**
     * description: get NFT description info
     *
     * @param nftAddress nonFungibleTokenAddress
     * @return com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenDesPojo
     * @author yozora
     **/
    DesNonFungibleTokenVo getNonFungibleTokenDescription(@Param("nftAddress") String nftAddress);

    /**
     * description: get user NFT count
     *
     * @param userNonFungibleTokenRequest userNonFungibleTokenRequest {userAddress, NFTAddress}
     * @return int
     * @author yozora
     **/
    int getUserNonFungibleTokenCount(UserNonFungibleTokenRequest userNonFungibleTokenRequest);

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
     * description:  get NFT code info
     *
     * @param nftCode nftCode
     * @return com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenCodePojo
     * @author yozora
     **/
    NonFungibleTokenCodePojo getNonFungibleTokenCode(@Param("nftCode") String nftCode);

    /**
     * description: get NFT info
     *
     * @param tokenId tokenId
     * @return com.metaverse.nfgate.entity.nonFungibleToken.NonFungibleTokenPojo
     * @author yozora
     **/
    NonFungibleTokenPojo getNonFungibleTokenInfo(@Param("nftAddress") String nftAddress, @Param("tokenId") String tokenId, @Param("chainName") String chainName);

    /**
     * description: get NFT info
     *
     * @param nftCodeList nftCodeList
     * @return java.util.List<com.metaverse.nfgate.entity.nonFungibleToken.NonFungibleTokenPojo>
     * @author yozora
     **/
    List<NonFungibleTokenPojo> getLatestNonFungibleTokenList(@Param("nftCodeList") List<String> nftCodeList);

    /**
     * description: get latest login NFT info
     *
     * @param userNonFungibleTokenRequest userNonFungibleTokenRequest {userAddress, startTime}
     * @return java.util.ArrayList<java.lang.String>
     * @author yozora
     **/
    ArrayList<LatestNftCodeInfoPojo> getLatestLoginCode(UserNonFungibleTokenRequest userNonFungibleTokenRequest);

    /**
     * description: get exist NFT code
     *
     * @param codeNonFungibleTokenRequest codeNonFungibleTokenRequest {nonFungibleTokenId,nonFungibleTokenAddress}
     * @return com.metaverse.nfgate.entity.nonFungibleToken.NonFungibleTokenCodePojo
     * @author yozora
     **/
    NonFungibleTokenCodePojo getExistNonFungibleTokenCode(CodeNonFungibleTokenRequest codeNonFungibleTokenRequest);

    /**
     * description: get NFT description info
     *
     * @param desNonFungibleTokenRequest desNonFungibleTokenRequest {pageNum, pageSize?}
     * @return java.util.List<com.metaverse.nfgate.entity.nonFungibleToken.NonFungibleTokenDesPojo>
     * @author yozora
     **/
    List<NonFungibleTokenDesPojo> getNonNonFungibleTokenDescription(DesNonFungibleTokenRequest desNonFungibleTokenRequest);

    /**
     * description: get NFT description count
     *
     * @param desNonFungibleTokenRequest desNonFungibleTokenRequest {pageNum, pageSize?}
     * @return int
     * @author yozora
     **/
    int getNonFungibleTokenDescriptionCount(DesNonFungibleTokenRequest desNonFungibleTokenRequest);

    /**
     * description: get user NFT description info
     *
     * @param desNonFungibleTokenRequest desNonFungibleTokenRequest
     * @return java.util.List<com.metaverse.nfgate.entity.response.DesNonFungibleTokenResponse>
     * @author yozora
     **/
    List<DesNonFungibleTokenVo> getUserNonFungibleTokenDescription(DesNonFungibleTokenRequest desNonFungibleTokenRequest);

    int getNftAuctionsCount(Integer status);

    NonFungibleTokenPojo getNftAuctionsByGameId(@Param("gameId") Long gameId);

    List<NonFungibleTokenPojo> getNftAuctions(@Param("status") Integer status, @Param("startIndex") Integer startIndex, @Param("endIndex") int endIndex);

    int getUserNftListCount(String userAddress);

    List<NonFungibleTokenPojo> getUserNftList(@Param("address") String userAddress, @Param("startIndex") int startIndex, @Param("endIndex") int endIndex);

    Long getMaxGameId();

    Long getMaxBidId();

    List<BidVo> getBidderForm(Long bidId);

    List<NonFungibleTokenPojo> getUserNftAuctions(UserNftRequest userNftRequest);

    void updateGameStatus(@Param("gameId") Long gameId, @Param("status") int status);

    SettleBidDto getSettleBid(Long bidId);

    List<String> getGamePlayers(Long gameId);

    List<NonFungibleTokenPojo> getNftAuctionsByNftAddressAndTokenId(@Param("nftAddress") String nftAddress, @Param("tokenId") String tokenId);

    void addUserJoinLog(UserJoinLogDto userJoinLogDto);

    String getUserJoinLog(@Param("userAddress") String userAddress, @Param("gameId") Long gameId, @Param("transactionHash") String transactionHash);


}

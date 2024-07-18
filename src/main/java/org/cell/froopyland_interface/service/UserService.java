package org.cell.froopyland_interface.service;


import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.dto.AmountDto;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.GameNftVo;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.NonFungibleTokenVo;
import org.cell.froopyland_interface.entity.request.BaseNonFungibleTokenRequest;
import org.cell.froopyland_interface.entity.request.LoginRequest;
import org.cell.froopyland_interface.entity.user.UserInfoPojo;
import org.cell.froopyland_interface.entity.user.UserLoginInfoDto;

import java.util.List;

/**
 * @author yozora
 * @description
 **/
public interface UserService {

    /**
     * description: query user info
     *
     * @param loginRequest login request {userAddress}
     * @return com.metaverse.trust.entity.user.UserInfoPojo
     * @author yozora
     **/
    UserInfoPojo queryUserInfoByAddress(LoginRequest loginRequest);

    /**
     * description: query user non-fungible token
     *
     * @param loginRequest login request {NFTCode}
     * @return com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenPojo
     * @author yozora
     **/
    NonFungibleTokenPojo queryNonFungibleTokenByAddress(BaseNonFungibleTokenRequest loginRequest);

    List<AmountDto> getClaimKeyBonus(String userAddress, Long gameId);

    List<AmountDto> getWithdrawLastPlayerPrize(String userAddress);

    List<AmountDto> getWithdrawSaleRevenue(String userAddress);

    List<NonFungibleTokenPojo> getUserJoinedGameIds(String userAddress);

    int getNftRetrievedCount(String userAddress);

    List<GameNftVo> getNftRetrievedList(String userAddress, int statIndex, int endIndex);

    List<NonFungibleTokenVo> getMyParticipationGames(String userAddress, Integer status);

    List<Integer> getAllClaimNftInfoCount(String userAddress);

    List<AmountDto> getAllClaimNftGameIds(String userAddress, int startIndex, int endIndex);

    List<NonFungibleTokenVo> getMyAuctions(String userAddress, Integer status);

    List<AmountDto> selectUserKeysConvertedLog(String userAddress);

    List<NonFungibleTokenPojo> getUserJoinedGameInfo(String userAddress);
}

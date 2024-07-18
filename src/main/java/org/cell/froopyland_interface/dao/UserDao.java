package org.cell.froopyland_interface.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.dto.AmountDto;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.GameNftVo;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.NonFungibleTokenVo;
import org.cell.froopyland_interface.entity.request.BaseNonFungibleTokenRequest;
import org.cell.froopyland_interface.entity.request.LoginRequest;
import org.cell.froopyland_interface.entity.user.UserInfoPojo;

import java.util.List;

/**
 * @author yozora
 * @description user dao
 **/
@Mapper
public interface UserDao {

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
     * @param loginRequest login request {NFTCode,userAddress}
     * @return com.metaverse.trust.entity.nonFungibleToken.NonFungibleTokenPojo
     * @author yozora
     **/
    NonFungibleTokenPojo queryNonFungibleTokenByAddress(BaseNonFungibleTokenRequest loginRequest);

    List<AmountDto> getClaimKeyBonus(@Param("userAddress") String userAddress, @Param("gameId") Long gameId);

    List<AmountDto> getWithdrawLastPlayerPrize(String userAddress);

    List<AmountDto> getWithdrawSaleRevenue(String userAddress);

    List<NonFungibleTokenPojo> getUserJoinedGameIds(String userAddress);

    int getNftRetrievedCount(String userAddress);

    List<GameNftVo> getNftRetrievedList(@Param("userAddress") String userAddress, @Param("statIndex") int statIndex, @Param("endIndex") int endIndex);

    List<NonFungibleTokenVo> getMyParticipationGames(@Param("userAddress") String userAddress, @Param("status") Integer status);

    List<NonFungibleTokenVo> getMyAuctions(@Param("userAddress") String userAddress, @Param("status") Integer status);

    List<Integer> getAllClaimNftInfoCount(String userAddress);

    List<AmountDto> getAllClaimNftGameIds(@Param("userAddress") String userAddress, @Param("startIndex") int startIndex, @Param("endIndex") int endIndex);

    List<AmountDto> selectUserKeysConvertedLog(String userAddress);

    List<NonFungibleTokenPojo> getUserJoinedGameInfo(String userAddress);
}

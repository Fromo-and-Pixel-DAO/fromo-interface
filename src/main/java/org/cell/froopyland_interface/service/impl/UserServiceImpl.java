package org.cell.froopyland_interface.service.impl;

import org.cell.froopyland_interface.dao.UserDao;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.dto.AmountDto;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.GameNftVo;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.NonFungibleTokenVo;
import org.cell.froopyland_interface.entity.request.BaseNonFungibleTokenRequest;
import org.cell.froopyland_interface.entity.request.LoginRequest;
import org.cell.froopyland_interface.entity.user.UserInfoPojo;
import org.cell.froopyland_interface.entity.user.UserLoginInfoDto;
import org.cell.froopyland_interface.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yozora
 * @description user service impl
 * @date 16:33 2022/4/29
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserInfoPojo queryUserInfoByAddress(LoginRequest loginRequest) {
        return userDao.queryUserInfoByAddress(loginRequest);
    }

    @Override
    public NonFungibleTokenPojo queryNonFungibleTokenByAddress(BaseNonFungibleTokenRequest loginRequest) {
        return userDao.queryNonFungibleTokenByAddress(loginRequest);
    }

    @Override
    public List<AmountDto> getClaimKeyBonus(String userAddress, Long gameId) {
        return userDao.getClaimKeyBonus(userAddress, gameId);
    }

    @Override
    public List<AmountDto> getWithdrawLastPlayerPrize(String userAddress) {
        return userDao.getWithdrawLastPlayerPrize(userAddress);
    }

    @Override
    public List<AmountDto> getWithdrawSaleRevenue(String userAddress) {
        return userDao.getWithdrawSaleRevenue(userAddress);
    }

    @Override
    public List<NonFungibleTokenPojo> getUserJoinedGameIds(String userAddress) {
        return userDao.getUserJoinedGameIds(userAddress);
    }

    @Override
    public int getNftRetrievedCount(String userAddress) {
        return userDao.getNftRetrievedCount(userAddress);
    }

    @Override
    public List<GameNftVo> getNftRetrievedList(String userAddress, int statIndex, int endIndex) {
        return userDao.getNftRetrievedList(userAddress, statIndex, endIndex);
    }

    @Override
    public List<NonFungibleTokenVo> getMyParticipationGames(String userAddress, Integer status) {
        return userDao.getMyParticipationGames(userAddress, status);
    }

    @Override
    public List<Integer> getAllClaimNftInfoCount(String userAddress) {
        return userDao.getAllClaimNftInfoCount(userAddress);
    }

    @Override
    public List<AmountDto> getAllClaimNftGameIds(String userAddress, int startIndex, int endIndex) {
        return userDao.getAllClaimNftGameIds(userAddress, startIndex, endIndex);
    }

    @Override
    public List<NonFungibleTokenVo> getMyAuctions(String userAddress, Integer status) {
        return userDao.getMyAuctions(userAddress, status);
    }

    @Override
    public List<AmountDto> selectUserKeysConvertedLog(String userAddress) {
        return userDao.selectUserKeysConvertedLog(userAddress);
    }

    @Override
    public List<NonFungibleTokenPojo> getUserJoinedGameInfo(String userAddress) {
        return userDao.getUserJoinedGameInfo(userAddress);
    }

}

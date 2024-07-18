package org.cell.froopyland_interface.controller;

import org.cell.froopyland_interface.entity.base.ApiResponse;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.GameNftAmountVo;
import org.cell.froopyland_interface.entity.request.DesNonFungibleTokenRequest;
import org.cell.froopyland_interface.entity.response.*;
import org.cell.froopyland_interface.entity.user.vo.MyProfitVo;
import org.cell.froopyland_interface.service.NonFungibleTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yozora
 * description user controller
 * @version 1.0
 */
@RestController
@RequestMapping("/fl")
public class UserController {

    @Autowired
    private NonFungibleTokenService nonFungibleTokenService;

    /**
     * description: get user nft list
     *
     * @param next     page number
     * @param pageSize page size
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.response.NftListResponse>
     * @author yozora
     **/
    @GetMapping({"/nft/getUserNftList/{userAddress}/{pageSize}/{next}", "/nft/getUserNftList/{userAddress}/{pageSize}"})
    public ApiResponse<UserNftListVo> getUserNftList(@PathVariable(value = "userAddress") String userAddress, @PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "next", required = false) String next) {
        userAddress = userAddress.toLowerCase();
        return new ApiResponse<>(nonFungibleTokenService.getUserNftList(userAddress, next, pageSize));
    }

    /**
     * description: get user nft description
     *
     * @param desNonFungibleTokenRequest desNonFungibleTokenRequest
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<java.util.List < org.cell.froopyland_interface.entity.response.DesNonFungibleTokenResponse>>
     * @author yozora
     **/
    @PostMapping("/nft/getUserDescription")
    public ApiResponse<List<DesNonFungibleTokenVo>> getNonFungibleTokenDescription(@RequestBody DesNonFungibleTokenRequest desNonFungibleTokenRequest) {
        desNonFungibleTokenRequest.setUserAddress(desNonFungibleTokenRequest.getUserAddress().toLowerCase());
        return new ApiResponse<>(nonFungibleTokenService.getUserNonFungibleTokenDescription(desNonFungibleTokenRequest));
    }

    @GetMapping("/user/getStakeNotices/{userAddress}")
    public ApiResponse<Integer> getStakeNotices(@PathVariable(value = "userAddress") String userAddress) {
        userAddress = userAddress.toLowerCase();
        return new ApiResponse<>(nonFungibleTokenService.getStakeNotices(userAddress));
    }

    /**
     * description: get game detail
     *
     * @param userAddress user address
     * @param gameId      game id
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<java.lang.String>
     * @author yozora
     **/
    @GetMapping("/user/gameDetail/{userAddress}/{gameId}")
    public ApiResponse<GameNftAmountVo> getGameDetail(@PathVariable(value = "userAddress") String userAddress, @PathVariable(value = "gameId") Long gameId) {
        userAddress = userAddress.toLowerCase();
        return new ApiResponse<>(nonFungibleTokenService.getGameDetail(userAddress, gameId));
    }

    /**
     * description: get user profit
     *
     * @param userAddress user address
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.user.vo.MyProfitVo>
     * @author yozora
     **/
    @GetMapping("/user/myProfit/{userAddress}")
    public ApiResponse<MyProfitVo> getMyProfit(@PathVariable(value = "userAddress") String userAddress) throws Exception {
        userAddress = userAddress.toLowerCase();
        return new ApiResponse<>(nonFungibleTokenService.getMyProfit(userAddress));
    }

    /**
     * description: get user historical dividends and prize
     * +
     *
     * @param userAddress user address
     * @param pageNum     page number
     * @param status      status
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.response.UserDividendsVo>
     * @author yozora
     **/
    @GetMapping("/user/historicalDividendsAndPrize/{userAddress}/{pageNum}/{status}")
    public ApiResponse<UserDividendsVo> getHistoricalDividendsAndPrize(@PathVariable(value = "userAddress") String userAddress, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "status") Integer status) throws Exception {
        userAddress = userAddress.toLowerCase();
        return new ApiResponse<>(nonFungibleTokenService.getHistoricalDividendsAndPrize(userAddress, pageNum, status));
    }

    /**
     * description: get user purchased nft
     *
     * @param userAddress user address
     * @param pageNum     page number
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.response.UserRetrievedVo>
     * @author yozora
     **/
    @GetMapping("/user/myPurchasedNfts/{userAddress}/{pageNum}")
    public ApiResponse<UserRetrievedVo> getMyPurchasedNfts(@PathVariable(value = "userAddress") String userAddress, @PathVariable(value = "pageNum") Integer pageNum) {
        userAddress = userAddress.toLowerCase();
        return new ApiResponse<>(nonFungibleTokenService.getMyPurchasedNfts(userAddress, pageNum, 5));
    }

    /**
     * description: get user participation games
     *
     * @param userAddress user address
     * @param status      status
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.response.NftListVo>
     * @author yozora
     **/
    @GetMapping({"/user/myParticipationGames/{userAddress}/{status}", "/user/myParticipationGames/{userAddress}"})
    public ApiResponse<NftListVo> getMyParticipationGames(@PathVariable(value = "userAddress") String userAddress, @PathVariable(value = "status", required = false) Integer status) {
        userAddress = userAddress.toLowerCase();
        return new ApiResponse<>(nonFungibleTokenService.getMyParticipationGames(userAddress, status));
    }

    /**
     * description: get user auctions
     *
     * @param userAddress user address
     * @param status      0 upcoming 1 ongoing 2 finished 3 all
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.response.NftListVo>
     * @author yozora
     **/
    @GetMapping({"/user/myAuctions/{userAddress}/{status}", "/user/myAuctions/{userAddress}"})
    public ApiResponse<NftListVo> getMyAuctions(@PathVariable(value = "userAddress") String userAddress, @PathVariable(value = "status", required = false) Integer status) {
        userAddress = userAddress.toLowerCase();
        return new ApiResponse<>(nonFungibleTokenService.getMyAuctions(userAddress, status));
    }
}

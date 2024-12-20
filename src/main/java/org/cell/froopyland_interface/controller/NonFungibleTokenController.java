package org.cell.froopyland_interface.controller;

import org.cell.froopyland_interface.entity.base.ApiResponse;
import org.cell.froopyland_interface.entity.constants.GameConstant;
import org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenPojo;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.AuctionInfoVo;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.BidVo;
import org.cell.froopyland_interface.entity.nonFungibleToken.vo.GameBriefVo;
import org.cell.froopyland_interface.entity.response.DesNonFungibleTokenVo;
import org.cell.froopyland_interface.entity.response.NftListVo;
import org.cell.froopyland_interface.service.NonFungibleTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

/**
 * @author yozora
 * description non fungible token controller
 * @version 1.0
 */
@RestController
@RequestMapping("/fl")
public class NonFungibleTokenController {

    @Autowired
    private NonFungibleTokenService nonFungibleTokenService;

    /**
     * description: get game brief
     *
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.nonFungibleToken.vo.GameBriefVo>
     * @author yozora
     **/
    @GetMapping("/game/getSysBrief")
    public ApiResponse<GameBriefVo> getGameBrief() {
        return new ApiResponse<>(nonFungibleTokenService.getGameBrief());
    }

    /**
     * description: get auction info
     *
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.nonFungibleToken.vo.AuctionInfoVo>
     * @author yozora
     **/
    @GetMapping("/game/getAuctionInfo")
    public ApiResponse<AuctionInfoVo> getAuctionInfo() throws Exception {
        return new ApiResponse<>(nonFungibleTokenService.getAuctionInfo());
    }


    /**
     * description: get bidder form
     *
     * @param bidId bid id
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<java.util.List < org.cell.froopyland_interface.entity.nonFungibleToken.vo.BidVo>>
     * @author yozora
     **/
    @GetMapping({"/game/getBidderForm/{bidId}", "/game/getBidderForm"})
    public ApiResponse<List<BidVo>> getBidderForm(@PathVariable(value = "bidId", required = false) Long bidId) throws Exception {
        return new ApiResponse<>(nonFungibleTokenService.getBidderForm(bidId));
    }


    /**
     * description: get nft list
     *
     * @param status  status
     * @param pageNum page number
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.response.NftListResponse>
     * @author yozora
     **/
    @GetMapping({"/nft/getNftAuctions/{pageNum}/{status}", "/nft/getNftAuctions/{pageNum}"})
    public ApiResponse<NftListVo> getNftAuctions(@PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "status", required = false) Integer status) {
        return new ApiResponse<>(nonFungibleTokenService.getNftAuctions(status, pageNum));
    }

    /**
     * description: get nft pool list
     *
     * @param pageNum page number
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.response.NftListResponse>
     * @author yozora
     **/
    @GetMapping("/nft/getNftPoolList/{pageNum}")
    public ApiResponse<NftListVo> getNftPoolList(@PathVariable(value = "pageNum") Integer pageNum) {
        return new ApiResponse<>(nonFungibleTokenService.getNftPoolList(GameConstant.FINISHED, pageNum));
    }

    /**
     * description: get nft description
     *
     * @param contractAddress contract address
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.response.DesNonFungibleTokenResponse>
     * @author yozora
     **/
    @GetMapping("/nft/getDescription/{contractAddress}")
    public ApiResponse<DesNonFungibleTokenVo> getNonFungibleTokenDescription(@PathVariable(value = "contractAddress") String contractAddress) {
        return new ApiResponse<>(nonFungibleTokenService.getNonFungibleTokenDescription(contractAddress));
    }

    /**
     * description: get nft info
     *
     * @param contractAddress contract address
     * @param tokenId         token id
     * @param chainName       chain name
     * @return org.cell.froopyland_interface.entity.base.ApiResponse<org.cell.froopyland_interface.entity.nonFungibleToken.NonFungibleTokenPojo>
     * @author yozora
     **/
    @GetMapping("/nft/getNftInfo/{contractAddress}/{tokenId}/{chainName}")
    public ApiResponse<NonFungibleTokenPojo> getNonFungibleTokenInfo(@PathVariable(value = "contractAddress") String contractAddress, @PathVariable(value = "tokenId") String tokenId, @PathVariable(value = "chainName") String chainName) {
        return new ApiResponse<>(nonFungibleTokenService.getNonFungibleTokenInfo(contractAddress, tokenId, chainName));
    }

    @GetMapping("/nft/getJoinLog/{startBlock}/{endBlock}")
    public ApiResponse<List<NonFungibleTokenPojo>> getJoinLog(@PathVariable(value = "startBlock") String startBlock, @PathVariable(value = "endBlock") String endBlock) throws IOException {
        return new ApiResponse<>(nonFungibleTokenService.getJoinLog(startBlock, endBlock));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            System.out.println(new BigDecimal(new Random().nextInt(995) + 5).divide(new BigDecimal(1000), 3, RoundingMode.HALF_UP));
        }
    }
}

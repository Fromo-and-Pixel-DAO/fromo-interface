package org.cell.froopyland_interface.entity.constants;

/**
 * @author yozora
 * @description OSeaConstant
 **/
public interface InterfaceApi {

    /**
     * OSeaConstant key
     */
    String K_O = "53ee05c4e2434ab9909e88f981c6b172";

    /**
     * NFTSearchConstant key
     */
    String K_N = "3PzNmstx";

    String OPEN_BASE_URL = "https://testnets-api.opensea.io/api/v2/";

    /**
     * assets url
     */
    String OPEN_SEA_ACCOUNT_URL = OPEN_BASE_URL + "chain/{chain}/account/{address}/nfts";

    /**
     * assets url
     */
    String OPEN_SEA_ASSETS_URL = OPEN_BASE_URL + "/chain/{chain}/contract/{address}/nfts";

    /**
     * assets detail url
     */
    String OPEN_SEA_ASSETS_DETAIL_URL = OPEN_BASE_URL + "/chain/{chain}/contract/{address}/nfts/{identifier}";

    /**
     * asset url
     */
    String OPEN_SEA_ASSET_URL = "https://api.opensea.io/api/v1/asset";

    /**
     * asset contract url
     */
    String OPEN_SEA_CONTRACT_URL = OPEN_BASE_URL + "/asset_contract";

    /**
     * collections url
     */
    String OPEN_SEA_COLLECTIONS_URL = OPEN_BASE_URL + "/collections";

    /**
     * contract url
     */
    String NFT_SCAN_CONTRACT_URL = OPEN_BASE_URL + "/collections";

    /**
     * asset url
     */
    String NFT_SCAN_ASSET_URL = "https://restapi.nftscan.com/api/v2/assets/";

    /**
     * telegram bot message url (abort)
     */
    String TELEGRAM_BOT_MESSAGE_URL = "https://api.telegram.org/bot5317172037:AAF4jv2CeKtAtPeU5xgCct8GK_HVdWbArgU/sendMessage";

    /**
     * token_metadata length
     */
    int METADATA_LENGTH = 3000;

    int INTERFACE_OPEN_SEA = 0;

    int INTERFACE_NFT_SEARCH = 1;
}

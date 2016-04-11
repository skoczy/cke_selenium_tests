package com.sfr.sitemaster.integration.impl

/**
 * Created by piotr on 14/12/15.
 */
class JDEResponse {
    Long EANCode
    String areaSalesManager
    String areaSalesManagerName
    Boolean chainConvenience
    String clusterName
    String compName
    String companyName
    String concept_Category
    String country
    String dealer_Stationman
    String dealer_StationmanName
    String district
    String mailingName
    String orgNumber
    String owner
    String salesZone
    Long siteID
    String siteName
    String siteType
    String status

    JDEAddressResponse siteMasterAddressDetail
    String[] siteMasterEmail
    Map<String, JDEPhoneResponse> siteMasterPhone

    static class JDEAddressResponse {
        String address
        String city_place
        String countyName
        String zipCode
    }

    static class JDEPhoneResponse {
        String phoneNumber
        String phoneNumberType
        String phonePrefix
    }
}

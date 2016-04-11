package com.sfr.sitemaster.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.sfr.sitemaster.enums.Owner
import com.sun.istack.NotNull

import javax.validation.Valid

/**
 * Created by piotr on 16/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteDTO {
    def meta
    @SMProperty(owner = Owner.SITEMASTER, path = 'id', label = 'ID', target = Long.class)
    Long id
    @SMProperty(owner = Owner.JDE, path = 'siteID', label = 'Station ID', target = Long.class)
    Long siteId
    @SMProperty(owner = Owner.JDE, path = 'siteName', label = 'Station name')
    def stationName
    @SMProperty(owner = Owner.JDE, path = 'dealer_StationmanName', label = 'Station Manager')
    def stationMan
    @SMProperty(owner = Owner.JDE, path = 'concept_Category', label = 'Category')
    def category
    @SMProperty(owner = Owner.JDE, path = 'owner', label = 'Station type')
    def stationType
    @SMProperty(owner = Owner.JDE, path = 'district', label = 'District')
    def district
    @SMProperty(owner = Owner.JDE, path = 'siteType', label = 'Station format')
    def stationFormat
    @SMProperty(owner = Owner.JDE, path = 'country', label = 'Country')
    String country
    @SMProperty(owner = Owner.JDE, path = 'salesZone', label = 'Sales zone')
    def salesZone
    @SMProperty(owner = Owner.JDE, path = 'companyName', label = 'Company name')
    def compName
    @SMProperty(owner = Owner.JDE, path = 'orgNumber', label = 'Organisation number')
    def orgNumber
    @SMProperty(owner = Owner.JDE, path = 'clusterName', label = 'Cluster name')
    def clusterName
    @Deprecated
    @SMProperty(owner = Owner.JDE, path = 'dealer_StationmanName', label = 'Dealer Station Man')
    def dealerStationManName
    @SMProperty(owner = Owner.JDE, path = 'areaSalesManagerName', label = 'Area Sales Manager')
    def areaSalesManagerName
    @SMProperty(owner = Owner.JDE, path = 'status', label = 'Status')
    def status
    @SMProperty(owner = Owner.JDE, path = 'EANCode', label = 'EAN Code')
    def eanCode
    @SMProperty(owner = Owner.JDE, path = 'siteMasterEmail', label = 'E-mail')
    def email
    @SMProperty(owner = Owner.JDE, path = 'siteMasterAddressDetail', target = AddressDTO.class, label = 'Address')
    @Valid
    AddressDTO address
    @SMProperty(owner = Owner.SITEMASTER, path = 'openingInfo', target = OpeningInfoDTO.class, label = 'Opening info')
    @Valid
    OpeningInfoDTO openingInfo
    @SMProperty(owner = Owner.JDE, path = 'siteMasterPhone', get = { it['WOR'] ? it['WOR'].phonePrefix + it['WOR']?.phoneNumber : null }, label = 'Phone')
    String phone
    @SMProperty(owner = Owner.JDE, path = 'siteMasterPhone', get = { it['FAX'] ? it['FAX'].phonePrefix + it['FAX']?.phoneNumber : null }, label = 'Fax')
    String fax
    @SMProperty(owner = Owner.SITEMASTER, path = 'xCoord', label = 'X Coordinates')
    @NotNull
    String xCoord
    @SMProperty(owner = Owner.SITEMASTER, path = 'yCoord', label = 'Y Coordinates')
    String yCoord
    @SMProperty(owner = Owner.JDE, path = 'chainConvenience', label = 'Chain convenience', target = Boolean.class)
    Boolean chainConvenience
    @SMProperty(owner = Owner.SITEMASTER, path = 'services', label = 'Services', target = List.class)
    List<ServiceDTO> services
    @SMProperty(owner = Owner.SITEMASTER, path = 'fuels', label = 'Fuels', target = List.class)
    List<ServiceDTO> fuels

}

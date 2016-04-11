package com.sfr.sitemaster.app

import com.sfr.apicore.commons.AbstractPropertyService

/**
 * Created by piotr on 06/11/15.
 */
class SitemasterPropertyService extends AbstractPropertyService {

    @Override
    protected List<String> getProjectPropertyFileNames() {
        ['sitemaster.properties']
    }
}

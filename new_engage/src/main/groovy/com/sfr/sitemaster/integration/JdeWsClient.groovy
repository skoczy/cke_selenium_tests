package com.sfr.sitemaster.integration

import com.sfr.sitemaster.integration.impl.JDEResponse
import rx.Observable

/**
 * Created by piotr on 20/10/15.
 */
interface JdeWsClient {
    def Observable<JDEResponse> getSiteById(final Long id)
}
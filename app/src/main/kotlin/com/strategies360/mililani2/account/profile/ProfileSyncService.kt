package com.strategies360.mililani2.account.profile

import itsmagic.present.simpleaccountmanager.sync.AccountSyncAdapter
import itsmagic.present.simpleaccountmanager.sync.AccountSyncService

/**
 *
 * A service to handle Account synchronization.
 */
class ProfileSyncService : AccountSyncService() {

    override fun initAccountSyncAdapter(): AccountSyncAdapter {
        // Return your sync adapter
        return ProfileSyncAdapter(this, true)
    }
}

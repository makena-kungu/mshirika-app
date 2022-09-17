package co.ke.mshirika.mshirika_app.ui_layer.ui.core.groups

import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.DetailedGroup
import co.ke.mshirika.mshirika_app.data_layer.repositories.GroupsRepo
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repo: GroupsRepo,
    private val store: PreferencesStoreRepository
) : MshirikaViewModel() {

    suspend fun detailedGroup(group: Int): DetailedGroup? {
        return repo.getGroupDetails(group)
    }

    val key: Flow<String?> = store.authKey
}
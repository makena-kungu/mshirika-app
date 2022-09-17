package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client.Cliente
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.group.Grupo
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount

@Dao
interface SearchDao {

    @Query("select * from clients_table where fullname like :consulta || memberNumber like :consulta || accountNo like :consulta")
    fun clients(consulta: String): PagingSource<Int, Cliente>

    @Query(
        """
        select * from loans_table
        where clientName like :consulta ||
        id like :consulta ||
        clientId like :consulta
        """
    )
    fun loans(consulta: String): PagingSource<Int, ConservativeLoanAccount>

    @Query("select * from groups_table where name like :consulta")
    fun groups(consulta:String):PagingSource<Int, Grupo>

}
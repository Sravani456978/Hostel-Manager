package uk.ac.tees.mad.hostelmanager.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.hostelmanager.data.repository.ComplaintRepositoryImpl
import uk.ac.tees.mad.hostelmanager.data.repository.MenuRepositoryImpl
import uk.ac.tees.mad.hostelmanager.domain.repository.ComplaintRepository
import uk.ac.tees.mad.hostelmanager.domain.repository.MenuRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMenuRepository(
        impl: MenuRepositoryImpl
    ): MenuRepository

    @Binds
    abstract fun bindComplaintRepository(
        impl: ComplaintRepositoryImpl
    ): ComplaintRepository
}

package uk.ac.tees.mad.hostelmanager.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.hostelmanager.data.local.room.AppDatabase
import uk.ac.tees.mad.hostelmanager.data.local.room.ComplaintDao
import uk.ac.tees.mad.hostelmanager.data.local.room.MealDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "app_db").fallbackToDestructiveMigration().build()

    @Provides
    fun provideMealDao(db: AppDatabase): MealDao = db.mealDao()

    @Provides
    fun provideComplaintDao(db: AppDatabase): ComplaintDao = db.complaintDao()
}
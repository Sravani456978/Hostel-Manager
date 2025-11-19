package uk.ac.tees.mad.hostelmanager.presentation.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.ac.tees.mad.hostelmanager.data.remote.firebase.MenuSeeder
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {

    init {

    }
}

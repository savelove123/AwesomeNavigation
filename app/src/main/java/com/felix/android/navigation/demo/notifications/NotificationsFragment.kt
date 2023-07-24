package com.felix.android.navigation.demo.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.felix.android.base.lazyNavViewModel
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.demo.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment(),Navigable {

    private var _binding: FragmentNotificationsBinding? = null
    private val notificationsViewModel :NotificationsViewModel by lazyNavViewModel{
        observeNavigation()
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val button: Button = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            button.text = it
        }
        button.setOnClickListener {
            notificationsViewModel.showUserInfo()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

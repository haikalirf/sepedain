package com.example.sepedain.main.ui.profile

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sepedain.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var stref: StorageReference
    private lateinit var auth: FirebaseAuth
    private lateinit var imageUri: Uri

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val dashboardViewModel =
//            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        val uid = auth.uid
        stref = FirebaseStorage.getInstance().getReference("profile-pictures/$uid")

        try {
            val localFile: File = File.createTempFile("Tempfile", ".jpg ")
            stref.getFile(localFile).addOnCompleteListener {
                if (it.isSuccessful) {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    binding.ivProfilepictureProfilefragment.setImageBitmap(bitmap)
                } else {
//                    Toast.makeText(requireActivity(), "Failed to retrieve image", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.ivProfilepictureProfilefragment.setOnClickListener(View.OnClickListener {
            selectImage()
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun selectImage() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            binding.ivProfilepictureProfilefragment.setImageURI(imageUri)
            val progressDialog = ProgressDialog(requireActivity())
            progressDialog.setMessage("Uploading File...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            auth = FirebaseAuth.getInstance()
            val uid = auth.uid
            stref = FirebaseStorage.getInstance().getReference("profile-pictures/$uid")
            stref.putFile(imageUri).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Successfully uploaded", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
                }
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        }
    }
}

package app.htheh.helpthehomeless.ui.addhomeless.savephoto

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.FragmentUploadHomelessPhotoBinding
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.ui.addhomeless.AddHomelessViewModel
import app.htheh.helpthehomeless.ui.addhomeless.selectlocation.SelectHomelessLocationFragmentArgs
import com.udacity.project4.locationreminders.savereminder.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import org.koin.android.ext.android.inject


private const val FILE_NAME = "homeless"
private const val CAM_PHOTO_REQUEST_CODE = 13
private const val LIB_PHOTO_REQUEST_CODE = 1000;
private const val PERMISSION_CODE = 1001;
private const val KEY_HOMELESS = "homeless"

/**
 * A simple [Fragment] subclass.
 * Use the [UploadHomelessPhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadHomelessPhotoFragment : Fragment() {

    companion object {
        internal const val ACTION_GEOFENCE_EVENT =
            "Homeless.addLocation.action.ACTION_GEOFENCE_EVENT"
    }

    val addHomelessViewModel: AddHomelessViewModel by inject()
    private lateinit var binding: FragmentUploadHomelessPhotoBinding
    private lateinit var homeLess: Homeless
    private lateinit var filePhoto: File
    private lateinit var currentPhotoPath: String
    private lateinit var photoUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeLess = SelectHomelessLocationFragmentArgs.fromBundle(arguments!!).homeless

        // Makes sure images are retrieved after configuration changes such as rotation
        if(savedInstanceState != null){
            val homeless = savedInstanceState.getParcelable(KEY_HOMELESS) as Homeless?
            addHomelessViewModel.photoURI.value = Uri.parse(homeless!!.imageUri)
            addHomelessViewModel.photoAbsolutePath.value = homeless!!.imagePath
        }

        // Inflate the layout for this fragment
        binding = FragmentUploadHomelessPhotoBinding.inflate(inflater, container, false)

        binding.btnTakePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.btnChoosePhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(this.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else{
                    chooseImageGallery();
                }
            }else{
                chooseImageGallery();
            }
        }

        if(addHomelessViewModel.photoAbsolutePath.value != null){
            val takenPhoto = BitmapFactory.decodeFile(addHomelessViewModel.photoAbsolutePath.value)
            binding.cameraImage.setImageBitmap(takenPhoto)
            binding.libraryImage.setImageResource(R.drawable.ic_photo_library_128)
        }
        if(addHomelessViewModel.photoURI.value != null) {
            binding.libraryImage.setImageURI(addHomelessViewModel.photoURI.value)
            binding.cameraImage.setImageResource(R.drawable.ic_photo_camera_128)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveProfile.setOnClickListener {

            //add imageUri and imagePath to homeLess object
            homeLess.imageUri = addHomelessViewModel.photoURI.value.toString()
            homeLess.imagePath = addHomelessViewModel.photoAbsolutePath.value

            // Navigate to review and save
            this.findNavController().navigate(UploadHomelessPhotoFragmentDirections.actionToReviewSave(homeLess))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val homeless = Homeless(
            homeLess.email!!, homeLess.firstName, homeLess.lastName,
            homeLess.phone, homeLess.needsHome, homeLess.approximateLocation,
            homeLess.latitude, homeLess.longitude, homeLess.walkScore, addHomelessViewModel.photoURI.value.toString(),
            addHomelessViewModel.photoAbsolutePath.value, homeLess.dateAdded
        )
        outState.putParcelable(KEY_HOMELESS, homeless)
    }

//    override fun onResume() {
//        super.onResume()
//        if(addHomelessViewModel.photoAbsolutePath.value != null){
//            val takenPhoto = BitmapFactory.decodeFile(addHomelessViewModel.photoAbsolutePath.value)
//            binding.cameraImage.setImageBitmap(takenPhoto)
//            binding.libraryImage.setImageResource(R.drawable.ic_photo_library_128)
//        }
//        if(addHomelessViewModel.photoURI.value != null) {
//            binding.libraryImage.setImageURI(addHomelessViewModel.photoURI.value)
//            binding.cameraImage.setImageResource(R.drawable.ic_photo_camera_128)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CAM_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            addHomelessViewModel.photoAbsolutePath.value = filePhoto?.absolutePath
            addHomelessViewModel.photoURI.value = null
            val takenPhoto = BitmapFactory.decodeFile(filePhoto?.absolutePath)
            binding.cameraImage.setImageBitmap(takenPhoto)
            binding.libraryImage.setImageResource(R.drawable.ic_photo_library_128)
        } else if(requestCode == LIB_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            addHomelessViewModel.photoURI.value = data?.data
            // Makes sure app still has access after relaunch
            this.requireActivity().contentResolver.takePersistableUriPermission(data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addHomelessViewModel.photoAbsolutePath.value = null
            binding.libraryImage.setImageURI(data?.data)
            binding.cameraImage.setImageResource(R.drawable.ic_photo_camera_128)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, LIB_PHOTO_REQUEST_CODE)
    }

    @Throws(IOException::class)
    private fun createImageFile(fileName: String): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val directoryStorage = this.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName + "_" + timeStamp, ".jpg", directoryStorage)
            .apply {
                addHomelessViewModel.photoAbsolutePath.value = absolutePath
            }
    }

    private fun dispatchTakePictureIntent() {
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.resolveActivity(this.requireContext().packageManager)
        filePhoto = createImageFile(FILE_NAME)
        val providerFile =
            FileProvider.getUriForFile(this.requireContext(),"app.htheh.helpthehomeless", filePhoto)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
        startActivityForResult(takePhotoIntent, CAM_PHOTO_REQUEST_CODE)
    }
}

package com.example.edistynytmobiiliohjelmointi2023

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentDetailBinding
import com.example.edistynytmobiiliohjelmointi2023.databinding.FragmentObjectDetectionBinding
import android.annotation.SuppressLint
import android.app.*
import android.content.Context.*
import android.content.ContextParams
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.*
import android.hardware.Camera.open
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.HandlerThread
import android.os.ParcelFileDescriptor.open
import android.provider.MediaStore.Files
import android.system.Os.open
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getMainExecutor
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.example.edistynytmobiiliohjelmointi2023.ml.SsdMobilenetV11Metadata1
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp


class ObjectDetectionFragment : Fragment() {
    private var _binding: FragmentObjectDetectionBinding? = null
    private val binding get() = _binding!!

    lateinit var labels: List<String>
    var colors = listOf<Int>(
        Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.GRAY, Color.BLACK,
        Color.DKGRAY, Color.MAGENTA, Color.YELLOW, Color.RED
    )
    val paint = Paint()
    lateinit var imageProcessor: ImageProcessor
    lateinit var bitmap: Bitmap
    lateinit var imageView: ImageView
    lateinit var cameraDevice: CameraDevice
    lateinit var handler: Handler
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    //lateinit var SsdMobilenetV11Metadata1
    //val model = SsdMobilenetV11Metadata1.newInstance(requireContext().applicationContext)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentObjectDetectionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        try {
            get_permission()

            //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
            //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ Jostain syystä en saanut luettua assets/labels.txt joten siirsin ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
            //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ sisällön tähän tiedostoon¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
            //¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤

            //val inputStream = ClassLoader::class.java.classLoader?.getResourceAsStream("assets/labels.txt")
            //labels = FileUtil.loadLabels(requireActivity().applicationContext ,inputStream  )        //(requireActivity().applicationContext.assets , "app\\src\\main\\assets\\labels.txt" )
            //labels = FileUtil.loadLabels(requireActivity().applicationContext, "assets\\labels.txt")

            labels =
                listOf(
                "person",
                    "bicycle",
                    "car",
                    "motorcycle",
                    "airplane",
                    "bus",
                    "train",
                    "truck",
                    "boat",
                    "traffic light",
                    "fire hydrant",
                    " ???",
                    "stop sign",
                    "parking meter",
                    "bench",
                    "bird",
                    "cat",
                    "dog",
                    "horse",
                    "sheep",
                    "cow",
                    "elephant",
                    "bear",
                    "zebra",
                    "giraffe",
                    "???",
                    "backpack",
                    "umbrella",
                    "???",
                    "???",
                    "handbag",
                    "tie",
                    "suitcase",
                    "frisbee",
                    "skis",
                    "snowboard",
                    "sports ball",
                    "kite",
                    "baseball bat",
                    "baseball glove",
                    "skateboard",
                    "surfboard",
                    "tennis racket",
                    "bottle",
                    "???",
                    "wine glass",
                    "cup",
                    "fork",
                    "knife",
                    "spoon",
                    "bowl",
                    "banana",
                    "apple",
                    "sandwich",
                    "orange",
                    "broccoli",
                    "carrot",
                    "hot dog",
                    "pizza",
                    "donut",
                    "cake",
                    "chair",
                    "couch",
                    "potted plant",
                    "bed",
                    "???",
                    "dining table",
                    "???",
                    "???",
                    "toilet",
                    "???",
                    "tv",
                    "laptop",
                    "mouse",
                    "remote",
                    "keyboard",
                    "cell phone",
                    "microwave",
                    "oven",
                    "toaster",
                    "sink",
                    "refrigerator",
                    "???",
                    "book",
                    "clock",
                    "vase",
                    "scissors",
                    "teddy bear",
                    "hair drier",
                    "toothbrush")


            imageProcessor =
                ImageProcessor.Builder().add(ResizeOp(300, 300, ResizeOp.ResizeMethod.BILINEAR))
                    .build()
            val model = SsdMobilenetV11Metadata1.newInstance(requireContext().applicationContext)
            val handlerThread = HandlerThread("videoThread")
            handlerThread.start()
            handler = Handler(handlerThread.looper)

            imageView = binding.imageView

            textureView = binding.textureView
            textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                    open_camera()
                }

                override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
                }

                override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                    return false
                }

                override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                    bitmap = textureView.bitmap!!
                    var image = TensorImage.fromBitmap(bitmap)
                    image = imageProcessor.process(image)

                    val outputs = model.process(image)
                    val locations = outputs.locationsAsTensorBuffer.floatArray
                    val classes = outputs.classesAsTensorBuffer.floatArray
                    val scores = outputs.scoresAsTensorBuffer.floatArray
                    val numberOfDetections = outputs.numberOfDetectionsAsTensorBuffer.floatArray

                    var mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    val canvas = Canvas(mutable)

                    val h = mutable.height
                    val w = mutable.width
                    paint.textSize = h / 15f
                    paint.strokeWidth = h / 85f
                    var x = 0
                    scores.forEachIndexed { index, fl ->
                        x = index
                        x *= 4
                        if (fl > 0.5) {
                            paint.setColor(colors.get(index))
                            paint.style = Paint.Style.STROKE
                            canvas.drawRect(
                                RectF(
                                    locations.get(x + 1) * w,
                                    locations.get(x) * h,
                                    locations.get(x + 3) * w,
                                    locations.get(x + 2) * h
                                ), paint
                            )
                            paint.style = Paint.Style.FILL
                            canvas.drawText(
                                labels.get(
                                    classes.get(index).toInt()
                                ) + " " + fl.toString(),
                                locations.get(x + 1) * w,
                                locations.get(x) * h,
                                paint
                            )
                        }
                    }

                    imageView.setImageBitmap(mutable)


                }
            }

//¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
//¤¤¤¤¤¤¤¤¤¤¤¤ Esimerkkikoodi on tarkoitettu käytettäksi activityssä joten seuraava ei ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
//¤¤¤¤¤¤¤¤¤¤¤¤ toiminut ilman muutosta ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
//¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤

            //    cameraManager = getSystemService(Context.CAMERA_SERVICE ) as CameraManager
            cameraManager = activity?.getSystemService(CAMERA_SERVICE) as CameraManager


        } catch (e: Exception) {
            Log.d("¤¤¤ERROR¤¤¤", "Error: $e");
        }


        return root
    }


    @SuppressLint("MissingPermission")
    fun open_camera() {
        cameraManager.openCamera(
            cameraManager.cameraIdList[0],
            object : CameraDevice.StateCallback() {
                override fun onOpened(p0: CameraDevice) {
                    cameraDevice = p0

                    var surfaceTexture = textureView.surfaceTexture
                    var surface = Surface(surfaceTexture)

                    var captureRequest =
                        cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    captureRequest.addTarget(surface)

                    cameraDevice.createCaptureSession(
                        listOf(surface),
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(p0: CameraCaptureSession) {
                                p0.setRepeatingRequest(captureRequest.build(), null, null)
                            }

                            override fun onConfigureFailed(p0: CameraCaptureSession) {
                            }
                        },
                        handler
                    )
                }

                override fun onDisconnected(p0: CameraDevice) {

                }

                override fun onError(p0: CameraDevice, p1: Int) {

                }
            },
            handler
        )
    }

    fun get_permission() {
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            get_permission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
       // model.close()
    }
}
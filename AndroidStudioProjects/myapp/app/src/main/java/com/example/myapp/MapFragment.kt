package com.example.myapp

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import com.example.myapp.databinding.FragmentMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import org.json.JSONArray
import java.io.IOException
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var binding : FragmentMapBinding

class MapFragment : Fragment(), OnMapReadyCallback {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var locationSource : FusedLocationSource
    private lateinit var naverMap: NaverMap
    private var marker: Marker? = null
    val g by lazy { android.location.Geocoder(requireContext(), Locale.KOREAN) } //geocoder
    var address: List<Address>? = null
    //장소검색 도로명 주소 받아오기
    private val getSearchResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val searchData = data.getStringExtra("data")
                binding.editSearchText .setText(searchData)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        locationSource = FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE)
    }

    //지오코더 위경도를 주소로 바꾸기
    fun Geocoder(address: String, callback: (lat:Double?, lng:Double?) -> Unit) {
        val gecoder = Geocoder(requireContext())
        Thread {
            try{
                val adrresses = gecoder.getFromLocationName(address,1)
                if (adrresses!!.isNotEmpty()) {
                    val location = adrresses[0]
                    val lat = location.latitude
                    val lng = location.longitude
                    callback(lat,lng)
                }else {
                    callback(null,null)
                }
            }catch (e: IOException) {
                e.printStackTrace()
                callback(null,null)
            }
        }.start()
    }

    //역지오코더 위경도를 주소로 바꾸기
    fun Rgeocoder(lat:Double,lng: Double, callback:(result:String)-> Unit){
        Thread {
            var result =""
            try{
                address = g.getFromLocation(lat,lng,10)
                if (address!!.size > 0) {
                    address?.get(0)?.let { Log.d("bb", it.getAddressLine(0))
                        result = it.getAddressLine(0)
                    }
                }
                callback(result)
            }
            catch (e : IOException){
                e.printStackTrace()
            }
        }.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater,container,false)
        binding.navermap.getMapAsync(this)
        //터치 막기
        binding.editSearchText.isFocusable = false
        binding.editSearchText.setOnClickListener{
            val intent = Intent(requireContext(),SearchActivity::class.java)
            getSearchResult.launch(intent)
        }
        // search text에 있는 주소를 버튼을 누르면 위도 경도값을 따와서 이제 이 위도경도를 가지고 지도위에 표시
        binding.button.setOnClickListener{//버튼을 누르면
            val address = binding.editSearchText.text.toString()//주소를 따옴
            Geocoder(address) { latitude, longitude -> // 지오코더를 통해 주소를 위도 경도로 바꿈
                if (latitude != null && longitude != null) {
                    Log.d("Geocoder", "위도: $latitude, 경도: $longitude")
                    requireActivity().runOnUiThread{ //ui 업데이트를 위해 메인스레드에서 실행
                        marker?.map = null // 기존에 마커를 삭제
                        marker = Marker() // 새로 마커 작성
                        marker?.position = LatLng(latitude, longitude)
                        marker?.icon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_blue)
                        marker?.width = Marker.SIZE_AUTO
                        marker?.height = Marker.SIZE_AUTO
                        marker?.isIconPerspectiveEnabled = true
                        marker?.map = naverMap
                        //카메라 이동
                        val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude,longitude))
                        naverMap.moveCamera(cameraUpdate)
                    }

                } else {
                    Log.d("Geocoder", "주소를 찾을 수 없습니다.")
                }
            }
        }



        //마커를 터치하면 장소 정보가 띄위게 해야함 우선 먼저 마커를 터치하면 뜨게만들어보자 그게 안된다면 마커를 터치하면 바로 그쪽 채팅하는 화면으로 가서 거기서 대중교통정보 보여주든가
        // 정보창을 커스텀마이징해서 하든가 해야함
        return binding.root
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.mapType = NaverMap.MapType.Basic //네이버 지도 유형
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, true) // 네이버 실시간 교통정보 그룹
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)// 네이버 대중교통 그룹
        naverMap.uiSettings.isLocationButtonEnabled = true //현재위치 버튼
        naverMap.locationSource = locationSource


        // 정류장 지하철역 마커 (추후 버스정류장도 할예정)
        val markerList = arrayListOf(
            LatLng(37.61717163458433, 127.07472872343648),//태릉입구역
            LatLng(37.620141667972206, 127.08382311192688),//화랑대역
            LatLng(37.61080277041527, 127.07770102477173),//먹골역 0
            LatLng(37.636752,127.067614),//하계역 0
            LatLng(37.625976,127.072819),//공릉역 0
            LatLng(37.615090979843025, 127.06705709598249),//석계역
        )

        for (m in markerList) {
            val marker = Marker()
            marker.position = m
            marker.icon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_yellow)
            marker.width = Marker.SIZE_AUTO
            marker.height = Marker.SIZE_AUTO
            marker.isIconPerspectiveEnabled = true
            marker.map = naverMap
            // 정보창 설정
            val infoWindow = InfoWindow()
            var stationName = ""
            Rgeocoder(m.latitude,m.longitude) {result ->
                Log.d("Rgecoder", result)
                val address = result.split(" ")
                Log.d("Rgecoder", address.toString())
                stationName = "${address.get(address.size - 1)}"
                Log.d("Rgecoder", stationName)
            }
            // 정보창에 각 지하철역의 이름을 띄움
            infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return stationName
                }
            }
            // 마커를 터치하면 정보창이 나타나게함
            val listner = Overlay.OnClickListener { overlay ->
                val marker = overlay as Marker
                if (marker.infoWindow == null) {
                    // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                    infoWindow.open(marker)
                } else {
                    // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                    infoWindow.close()
                }
                true
            }

            marker.onClickListener = listner
            // 이제 해야할거 대중교통 정보를 불러와야함
            // 그게 끝나면은 정보창을 커스텀해보기
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode,permissions,grantResults)) {
            if (!locationSource.isActivated) { //권한이 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
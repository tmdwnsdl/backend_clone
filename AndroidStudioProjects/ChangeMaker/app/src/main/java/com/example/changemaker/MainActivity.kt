package com.example.changemaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.changemaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        savedInstanceState?.run {//화면을 회전 후 다시 데이터 넣기, 키의 값이 없을 경우 0으로 설정
            binding.run {
                priceTextView.text = getString("priceValue", "0")
                textView20dollarCnt.text = getString("dcnt20", "0")
                textView10dollarCnt.text = getString("dcnt10", "0")
                textView5dollarCnt.text = getString("dcnt5", "0")
                textView1dollarCnt.text = getString("dcnt1", "0")
                textView25centCnt.text = getString("ccnt25", "0")
                textView10centCnt.text = getString("ccnt10", "0")
                textView5centCnt.text = getString("ccnt5", "0")
                textView1centCnt.text = getString("ccnt1", "0")
            }
        }
    }
    //계산기 숫자버튼
    fun touchButton(view:View){
        //가격표시하기
        val buttonName = resources.getResourceEntryName(view.id) //버튼 이름 가져오기
        val buttonId = buttonName.filter { it.isDigit() }.toInt() // 버튼 맨뒤에 있는 버튼 id가져오기
        val text = binding.priceTextView.text.toString() + buttonId.toString() // 기존 텍스트에 버튼 id 즉 숫자 넣기
        val current = binding.priceTextView.text
        //기존 text가 코인인경우
        if (current.length <= 2) {
            val price = text.toDouble() / 100
            if (price >= 1) {
                binding.priceTextView.text = String.format("%.2f", price)
            }else {
                binding.priceTextView.text = text
            }
        }else { //기존 text가 지폐인 경우
            val price = text.toDouble() * 10
            binding.priceTextView.text = String.format("%.2f", price)
        }
        //  잔돈의 금액 단위별을 리스트로 만들어 가져오기
        val list = calculate(binding.priceTextView.text.toString())
        binding.textView20dollarCnt.text = list.get(0).toString()
        binding.textView10dollarCnt.text = list.get(1).toString()
        binding.textView5dollarCnt.text = list.get(2).toString()
        binding.textView1dollarCnt.text = list.get(3).toString()
        binding.textView25centCnt.text = list.get(4).toString()
        binding.textView10centCnt.text = list.get(5).toString()
        binding.textView5centCnt.text = list.get(6).toString()
        binding.textView1centCnt.text = list.get(7).toString()
    }
    //clear 버튼을 클릭 시
    fun clear(view: View) {
        //숫자 삭제
        binding.priceTextView.text = ""
        //모든 잔돈 금액 0으로
        binding.textView20dollarCnt.text = "0"
        binding.textView10dollarCnt.text = "0"
        binding.textView5dollarCnt.text = "0"
        binding.textView1dollarCnt.text = "0"
        binding.textView25centCnt.text = "0"
        binding.textView10centCnt.text = "0"
        binding.textView5centCnt.text = "0"
        binding.textView1centCnt.text = "0"
    }

    // 가격 단위별 계산
    fun calculate(price: String): Array<Int> {
        var d_20 = 0
        var d_10= 0
        var d_5= 0
        var d_1= 0
        var c_25= 0
        var c_10= 0
        var c_5= 0
        var c_1= 0
        if (price.length <= 2) {
            val intial_price = price.toInt()
            c_25 = intial_price / 25
            c_10 = (intial_price % 25) / 10
            c_5 = (intial_price % 25) % 10 / 5
            c_1 = (intial_price % 25) % 10 % 5
        }else {
            val intial_price = (price.toDouble() * 100).toInt()
            d_20 = intial_price / 2000
            d_10 = (intial_price % 2000) / 1000
            d_5 = intial_price % 2000 % 1000 / 500
            d_1 = intial_price % 2000 % 1000 % 500 / 100
            c_25 = intial_price % 2000 % 1000 % 500 % 100 / 25
            c_10 = intial_price % 2000 % 1000 % 500 % 100 % 25 / 10
            c_5 = intial_price % 2000 % 1000 % 500 % 100 % 25 % 10 / 5
            c_1 =intial_price % 2000 % 1000 % 500 % 100 % 25 % 10 % 5
        }
        return arrayOf(d_20, d_10, d_5, d_1, c_25, c_10, c_5, c_1)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //화면 회전 이전 데이터 저장
        outState.run {
            putString("priceValue", binding.priceTextView.text.toString())
            putString("dcnt20", binding.textView20dollarCnt.text.toString())
            putString("dcnt10", binding.textView10dollarCnt.text.toString())
            putString("dcnt5", binding.textView5dollarCnt.text.toString())
            putString("dcnt1", binding.textView1dollarCnt.text.toString())
            putString("ccnt25", binding.textView25centCnt.text.toString())
            putString("ccnt10", binding.textView10centCnt.text.toString())
            putString("ccnt5", binding.textView5centCnt.text.toString())
            putString("ccnt1", binding.textView1centCnt.text.toString())
        }
    }
}